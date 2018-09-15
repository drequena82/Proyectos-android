package org.example.asteroides.views;

import java.util.List;
import java.util.Vector;

import org.example.asteroides.Juego;
import org.example.asteroides.R;
import org.example.asteroides.ResumenPuntuacion;
import org.example.asteroides.graphics.Grafico;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.PathShape;
import android.graphics.drawable.shapes.RectShape;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

public class VistaJuego extends View implements SensorEventListener
{
    private int puntuacion = 0;

    private Activity activityPadre;
    private TextView sensorX;
    private TextView sensorY;
    private TextView sensorZ;

    private float posSensX;
    private float posSensY;
    private float posSensZ;

    private int anguloAnterior;
    
    // Incremento estándar de giro y aceleración
    private static final int PASO_GIRO_NAVE = 5;
    private static final float PASO_ACELERACION_NAVE = 0.5f;

    private static final int ASTEROIDE_GRANDE = 0;
    private static final int ASTEROIDE_MEDIO = 1;
    private static final int ASTEROIDE_PEQUEÑO = 2;

    // Atributos de la parametrización de la pantalla de preferencias
    private boolean isSensor;
    private int tipoGraficos;

    // //// ASTEROIDES //////
    private Vector<Grafico> asteroides; // Vector con los Asteroides
    private int numAsteroides; // Número inicial de asteroides
    private int numFragmentos; // Fragmentos en que se divide

    /* Drawable al dividir un asteroide */
    private Drawable drawableAsteroide[];

    // //// NAVE //////
    private Grafico nave;// Gráfico de la nave
    private int giroNave; // Incremento de dirección
    private float aceleracionNave; // aumento de velocidad

    // //// THREAD Y TIEMPO //////
    // Thread encargado de procesar el juego
    private ThreadJuego thread;

    // Cada cuanto queremos procesar cambios (ms)
    private static int PERIODO_PROCESO = 40;

    // Cuando se realizó el último proceso
    private long ultimoProceso = 0;

    /* Variables para el control de la pantalla táctil */
    // private float mX = 0, mY = 0;
    private boolean disparo = false;
    
    // //// MISIL //////
    private Grafico misil;
    private static int PASO_VELOCIDAD_MISIL = 40;
    private boolean misilActivo = false;
    private int tiempoMisil;

    private SensorManager mSensorManager;

    // //// MULTIMEDIA //////
    private SoundPool soundPool;

    private int idDisparo;
    private int idExplosion;

    /* Para la explosión de los asteroides, guardamos el alpha */
    private int alphaAsteroide = 255;

    private int alphaMisil = 255;

    private int alphaNave = 255;

    public VistaJuego(Context context, AttributeSet attrs)
    {

	super(context, attrs);

	Drawable drawableMisil = null;
	Drawable drawableNave = null;
	/* Para recoger los valores de la pantalla de preferencias */
	SharedPreferences pref = context.getSharedPreferences(
		"org.example.asteroides_preferences", Context.MODE_PRIVATE);

	isSensor = pref.getBoolean("sensor", true);
	tipoGraficos = Integer.valueOf(pref.getString("graficos", "0"));
	numFragmentos = Integer.valueOf(pref.getString("fragmentos", "3"));
	numAsteroides = 0;// Integer.valueOf(pref.getString("asteroides", "5"));
	/* -- */

	thread = new ThreadJuego();

	drawableNave = context.getResources().getDrawable(R.drawable.nave);
	restablecerPosicionNave(drawableNave);

	restablecerAsteroides(numAsteroides);

	drawableAsteroide = new Drawable[3];

	if (tipoGraficos == 1)
	{
	    /* Los tres tipos de asteroide que hay */
	    drawableAsteroide[VistaJuego.ASTEROIDE_GRANDE] = context
		    .getResources().getDrawable(R.drawable.asteroide1);

	    drawableAsteroide[VistaJuego.ASTEROIDE_MEDIO] = context
		    .getResources().getDrawable(R.drawable.asteroide2);

	    drawableAsteroide[VistaJuego.ASTEROIDE_PEQUEÑO] = context
		    .getResources().getDrawable(R.drawable.asteroide3);

	} else if (tipoGraficos == 0)
	{
	    for (int i = 0; i < numFragmentos; i++)
	    {
		ShapeDrawable dAsteroide = new ShapeDrawable(new PathShape(
			new Path(), 1, 1));
		dAsteroide.getPaint().setColor(Color.WHITE);
		dAsteroide.getPaint().setStyle(Style.STROKE);
		dAsteroide.setIntrinsicWidth(50 - i * 14);
		dAsteroide.setIntrinsicHeight(50 - i * 14);
		drawableAsteroide[i] = dAsteroide;
	    }

	}
	/* Definicion de sensores para el control por sensores */
	if (isSensor)
	{
	    mSensorManager = (SensorManager) context
		    .getSystemService(Context.SENSOR_SERVICE);
	    List<Sensor> listSensors = mSensorManager
		    .getSensorList(Sensor.TYPE_ORIENTATION);
	    if (!listSensors.isEmpty())
	    {
		Sensor orientationSensor = listSensors.get(0);
		mSensorManager.registerListener(this, orientationSensor,
			SensorManager.SENSOR_DELAY_GAME);
	    }
	}

	if (tipoGraficos == 1)
	{
	    /* Con bitMap sería igual que lo estabamos haciendo */
	    drawableMisil = context.getResources().getDrawable(
		    R.drawable.misil1);
	} else if (tipoGraficos == 0)
	{
	    /* Para crear un misil con graficos vectoriales */
	    ShapeDrawable dMisil = new ShapeDrawable(new RectShape());
	    dMisil.getPaint().setColor(Color.WHITE);
	    dMisil.getPaint().setStyle(Style.STROKE);
	    dMisil.setIntrinsicWidth(15);
	    dMisil.setIntrinsicHeight(3);
	    drawableMisil = dMisil;
	}

	misil = new Grafico(this, drawableMisil);

	/* Sonidos */
	soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
	idDisparo = soundPool.load(context, R.raw.disparo, 0);
	idExplosion = soundPool.load(context, R.raw.explosion, 0);
    }

    private void restablecerPosicionNave(Drawable drawableNave)
    {
	int posY = (int) (Math.random() * 4f - 2f);
	int posX = (int) (Math.random() * 4f - 2f);
	nave = new Grafico(this, drawableNave);
	Log.i("restablecerPosicionNave", "posIniX: " + (this.getWidth()/2) + "posIniY: "
		+ (this.getHeight()/2));
	giroNave = 0;
	aceleracionNave = 0.5f;

	nave.setPosX(0);
	nave.setIncX(0);

	nave.setPosY(0);
	nave.setIncY(0);
	
	nave.setAngulo(0);
    }

    private void restablecerAsteroides(int numAsteroides)
    {
	Drawable drawableAsteroide = null;

	if (tipoGraficos == 1)
	{
	    /* Aqui creamos los asteroides con graficos bitmap */
	    drawableAsteroide = getResources().getDrawable(
		    R.drawable.asteroide1);

	    asteroides = new Vector<Grafico>();

	    for (int i = 0; i < numAsteroides; i++)
	    {
		Grafico asteroide = new Grafico(this, drawableAsteroide);
		asteroide.setTipoAsteroide(VistaJuego.ASTEROIDE_GRANDE);
		asteroide.setIncY(Math.random() * 4 - 2);
		asteroide.setIncX(Math.random() * 4 - 2);
		asteroide.setAngulo((int) (Math.random() * 360));
		asteroide.setRotacion((int) (Math.random() * 8 - 4));
		asteroides.add(asteroide);
	    }
	} else if (tipoGraficos == 0)
	{
	    /* Aqui creamos los asteroides con gráficos vectoriales */
	}
    }

    private synchronized void destruyeAsteroide(int i)
    {
	int tam = -1;
	if (asteroides.get(i).getTipoAsteroide() != VistaJuego.ASTEROIDE_GRANDE)
	{
	    if (asteroides.get(i).getTipoAsteroide() == VistaJuego.ASTEROIDE_MEDIO)
	    {
		tam = VistaJuego.ASTEROIDE_PEQUEÑO;
	    }
	} else
	{
	    tam = VistaJuego.ASTEROIDE_MEDIO;
	}

	for (int n = 0; n < numFragmentos && tam != -1; n++)
	{
	    Grafico asteroide = new Grafico(this, drawableAsteroide[tam]);
	    asteroide.setTipoAsteroide(tam);
	    asteroide.setPosX(asteroides.get(i).getPosX());
	    asteroide.setPosY(asteroides.get(i).getPosY());
	    asteroide.setIncX(Math.random() * 7 - 2 - tam);
	    asteroide.setIncY(Math.random() * 7 - 2 - tam);
	    asteroide.setAngulo((int) (Math.random() * 360));
	    asteroide.setRotacion((int) (Math.random() * 8 - 4));
	    asteroides.add(asteroide);
	}

	if (tam == VistaJuego.ASTEROIDE_GRANDE)
	{
	    this.puntuacion += 10000;
	} else if (tam == VistaJuego.ASTEROIDE_MEDIO)
	{
	    this.puntuacion += 15000;
	} else if (tam == VistaJuego.ASTEROIDE_PEQUEÑO)
	{
	    this.puntuacion += 25000;
	}

	/* Podemos mostrar una explosion y luego hacer desaparecer el asteroide */
	Drawable explosion = getResources().getDrawable(R.drawable.explosion);
	asteroides.get(i).setDrawable(explosion);
	asteroides.get(i).setDestruido(true);
	misilActivo = false;

	/* Inicio del sonido de la explosion */
	soundPool.play(idExplosion, 1, 1, 0, 0, 1);
    }

    private synchronized void juegoTerminado()
    {
	Intent intent = new Intent(this.activityPadre, ResumenPuntuacion.class);
	intent.putExtra("PUNTUACION", this.puntuacion);

	this.thread.detener();

	this.activityPadre.startActivityForResult(intent,
		Juego.REQUEST_CODE_RESUMEN);
    }

    private synchronized void activaMisil()
    {
	if (!misilActivo)
	{
	    misil.setAngulo(nave.getAngulo());

	    // nave.getAncho()

	    misil.setPosX(nave.getPosX() + (nave.getAncho() / 2));
	    misil.setPosY(nave.getPosY() + (nave.getAlto() / 2));

	    /*
	     * misil.setPosX(nave.getPosX() + (nave.getAncho() / 2 -
	     * misil.getAncho() / 2)); misil.setPosY(nave.getPosY() +
	     * (nave.getAlto() / 2 - misil.getAlto() / 2));
	     */
	    misil.setIncX(Math.cos(Math.toRadians(misil.getAngulo()))
		    * PASO_VELOCIDAD_MISIL);

	    misil.setIncY(Math.sin(Math.toRadians(misil.getAngulo()))
		    * PASO_VELOCIDAD_MISIL);

	    tiempoMisil = (int) Math.min(
		    this.getWidth() / Math.abs(misil.getIncX()),
		    this.getHeight() / Math.abs(misil.getIncY())) - 2;
	    misilActivo = true;
	    soundPool.play(idDisparo, 1, 1, 1, 0, 1);
	}
    }

    private synchronized void moverNave(double retardo)
    {
	Log.d("moverNave", "Retardo:" + retardo);
	Log.d("moverNave", "Giro nave:" + giroNave);

	Log.d("moverNave", "Angulo nave:" + nave.getAngulo());

	// Aceleración nave multiplica el retardo para que haya una sensación de
	// velocidad mayor, de modo que con funciones trigonométricas
	// obtenemos el nuevo incremento mediante el calculo del coseno para el
	// eje x y el seno para el eje y.
	double nIncX = 0;
	double nIncY = 0;
	
	Log.d("moverNave", "Angulo: " + nave.getAngulo());
	Log.d("moverNave",
		"Valor cos(" + nave.getAngulo() + ")=" + Math.cos(nave.getAngulo()));
	Log.d("moverNave",
		"Valor sen(" + nave.getAngulo() + ")=" + Math.sin(nave.getAngulo()));

	aceleracionNave = 1;
	
	nIncX = nave.getIncX() + (aceleracionNave * Math.cos(Math.toRadians(nave.getAngulo())) * retardo);
	nIncY = nave.getIncY() + (aceleracionNave * Math.sin(Math.toRadians(nave.getAngulo())) * retardo);

	aceleracionNave = 0;//0.50f;

	Log.d("moverNave",
		"Aceleracion nave: " + aceleracionNave + " posX: "
			+ nave.getPosX() + " nIncX: " + nIncX + " posY: "
			+ nave.getPosY() + " nIncY: " + nIncY);
	Log.d("moverNave", "Retardo: " + retardo);
	Log.d("moverNave", "Angulo: " + nave.getAngulo());

	// Actualizamos si el módulo de la velocidad no excede el máximo
	if (Math.hypot(nIncX, nIncY) <= Grafico.MAX_VELOCIDAD)
	{
	    nave.setIncX(nIncX);
	    nave.setIncY(nIncY);
	}

	// Actualizamos posiciones X e Y
	nave.incrementaPos(retardo);
    }

    private void controlTactil(MotionEvent event)
    {
	final int historySize = event.getHistorySize();
	final int pointerCount = event.getPointerCount();

	float x = 0;
	float y = 0;

	switch (event.getAction())
	{
	case MotionEvent.ACTION_DOWN:
	    disparo = true;
	    break;
	case MotionEvent.ACTION_UP:
	    giroNave = 0;
	    aceleracionNave = .010f;

	    if (disparo)
	    {
		activaMisil();
	    }

	    break;
	}

	for (int h = 0; h < historySize; h++)
	{
	    Log.d("onTouchEvent",
		    "historical event time -> At time: "
			    + event.getHistoricalEventTime(h) + " milis");
	    if (pointerCount > 1)
	    {
		for (int p = 0; p < pointerCount; p++)
		{
		    Log.d("onTouchEvent",
			    "historicos - pointer " + event.getPointerId(p)
				    + ": (" + event.getHistoricalX(p, h) + ","
				    + event.getHistoricalY(p, h) + ")");
		    Log.d("onTouchEvent",
			    "actuales - pointer " + event.getPointerId(p) + ""
				    + ": (" + event.getX(p) + ","
				    + event.getY(p) + ")");

		    switch (event.getAction())
		    {

		    case MotionEvent.ACTION_MOVE:

			x = event.getX(p) - event.getHistoricalX(p, h);
			y = event.getY(p) - event.getHistoricalY(p, h);

			giroNave = 0;
			aceleracionNave = x / 100;

			break;
		    }
		}
	    } else
	    {
		switch (event.getAction())
		{
		case MotionEvent.ACTION_MOVE:

		    x = event.getX(0) - event.getHistoricalX(0, h);
		    y = event.getY(0) - event.getHistoricalY(0, h);

		    giroNave = (int) (-x + y);
		    aceleracionNave = .010f;
		    disparo = true;

		    break;
		}
	    }
	}
    }

    protected synchronized void actualizaFisica()
    {
	Drawable explosionMisil;
	Drawable explosionNave;

	long ahora = System.currentTimeMillis();

	// No hagas nada si el período de proceso no se ha cumplido.
	if (ultimoProceso + PERIODO_PROCESO > ahora)
	{
	    return;
	}

	// Para una ejecución en tiempo real calculamos retardo
	double retardo = (ahora - ultimoProceso) / PERIODO_PROCESO;

	ultimoProceso = System.currentTimeMillis();// ahora; // Para la próxima
						   // vez

	// Funcion que hace que la nave se mueva
	moverNave(retardo);

	for (Grafico asteroide : asteroides)
	{
	    asteroide.incrementaPos(retardo);
	}

	/*
	 * Comprobamos si la nave choca con alguno de los asteroides (si es asi
	 * se destruirá y terminará la partida)
	 */
	/* Verificamos la colisión con los asteroides */
	for (int i = 0; i < asteroides.size(); i++)
	{
	    if (nave.verificaColision(asteroides.elementAt(i)))
	    {
		nave.setDestruido(true);
		explosionNave = getResources()
			.getDrawable(R.drawable.explosion);
		nave.setDrawable(explosionNave);
		nave.setAncho(explosionNave.getIntrinsicWidth());
		nave.setAlto(explosionNave.getIntrinsicHeight());
		break;
	    }
	}

	// Actualizamos posición de misil
	if (misilActivo)
	{
	    misil.incrementaPos(retardo);
	    tiempoMisil -= retardo;
	    if (tiempoMisil < 0)
	    {
		misilActivo = false;
		misil.setDestruido(true);

		explosionMisil = getResources().getDrawable(
			R.drawable.explosion_misil);
		misil.setDrawable(explosionMisil);
		misil.setAncho(explosionMisil.getIntrinsicWidth());
		misil.setAlto(explosionMisil.getIntrinsicHeight());
	    } else
	    {
		/*
		 * Verificamos la colisión con la nave (si el misil colisiona
		 * con la nave, teminaremos la partida)
		 */

		if (misil.verificaColision(nave))
		{
		    // Explota el misil
		    misil.setDestruido(true);
		    explosionMisil = getResources().getDrawable(
			    R.drawable.explosion_misil);
		    misil.setDrawable(explosionMisil);
		    misil.setAncho(explosionMisil.getIntrinsicWidth());
		    misil.setAlto(explosionMisil.getIntrinsicHeight());

		    // Explota la nave
		    nave.setDestruido(true);
		    explosionNave = getResources().getDrawable(
			    R.drawable.explosion);
		    nave.setDrawable(explosionNave);
		    nave.setAncho(explosionNave.getIntrinsicWidth());
		    nave.setAlto(explosionNave.getIntrinsicHeight());

		    // Inicio del sonido de la explosion
		    soundPool.play(idExplosion, 1, 1, 0, 0, 1);

		}

		/* Verificamos la colisión con los asteroides */
		for (int i = 0; i < asteroides.size(); i++)
		{
		    if (misil.verificaColision(asteroides.elementAt(i)))
		    {
			misil.setDestruido(true);
			explosionMisil = getResources().getDrawable(
				R.drawable.explosion_misil);
			misil.setDrawable(explosionMisil);
			misil.setAncho(explosionMisil.getIntrinsicWidth());
			misil.setAlto(explosionMisil.getIntrinsicHeight());
			destruyeAsteroide(i);
			break;
		    }
		}
	    }
	}
    }

    @Override
    protected void onSizeChanged(int ancho, int alto, int ancho_anter,
	    int alto_anter)
    {

	super.onSizeChanged(ancho, alto, ancho_anter, alto_anter);

	// Una vez que conocemos nuestro ancho y alto.
	for (Grafico asteroide : asteroides)
	{
	    asteroide.setPosX(Math.random() * (ancho - asteroide.getAncho()));
	    asteroide.setPosY(Math.random() * (alto - asteroide.getAlto()));
	}

	ultimoProceso = System.currentTimeMillis();
	thread.execute();
    }

    @Override
    protected synchronized void onDraw(Canvas canvas)
    {
	Drawable dwbMisil;
	super.onDraw(canvas);

	/* Actualizamos la puntuacion en el label */
	TextView puntuacion = (TextView) this.activityPadre
		.findViewById(R.id.puntuacion_juego);

	sensorX = (TextView) this.activityPadre.findViewById(R.id.sensorX);

	sensorY = (TextView) this.activityPadre.findViewById(R.id.sensorY);

	sensorZ = (TextView) this.activityPadre.findViewById(R.id.sensorZ);

	sensorX.setText("X: " + this.posSensX);
	sensorY.setText("Y: " + this.posSensY);
	sensorZ.setText("Z: " + this.posSensZ);

	puntuacion.setText(String.valueOf(this.puntuacion));

	for (int i = 0; i < asteroides.size(); i++)
	{
	    Grafico asteroide = asteroides.get(i);
	    if (!asteroide.isDestruido())
	    {
		asteroide.dibujaGrafico(canvas);
	    } else
	    {

		if (alphaAsteroide <= 0)
		{
		    alphaAsteroide = 255;
		    asteroides.remove(i);
		} else
		{
		    asteroide.getDrawable().setAlpha(alphaAsteroide);
		    alphaAsteroide -= 15;
		    asteroide.dibujaGrafico(canvas);
		}
	    }
	}

	nave.dibujaGrafico(canvas);

	if (misil.isDestruido())
	{
	    if (alphaMisil <= 0)
	    {
		/* Reestablecemos los valores del misil */
		alphaMisil = 255;
		misilActivo = false;
		misil.setDestruido(false);
		dwbMisil = getResources().getDrawable(R.drawable.misil1);
		misil.setDrawable(dwbMisil);
		misil.setAncho(dwbMisil.getIntrinsicWidth());
		misil.setAlto(dwbMisil.getIntrinsicHeight());
	    } else
	    {
		misil.getDrawable().setAlpha(alphaMisil);
		alphaMisil -= 15;
		misil.dibujaGrafico(canvas);
	    }
	} else if (misilActivo)
	{
	    misil.dibujaGrafico(canvas);
	}

	/*
	 * Dibujamos la explosión y cuando termine de visualizarse se mostrará
	 * el activity que muestra la puntuación y guarda
	 */
	if (nave.isDestruido())
	{
	    if (alphaNave <= 0)
	    {
		/* La nave se ha destruido y saldremos del juego */
		alphaNave = 255;
		nave.setDestruido(false);

		juegoTerminado();
	    } else
	    {
		nave.getDrawable().setAlpha(alphaMisil);
		alphaNave -= 15;
		nave.dibujaGrafico(canvas);
	    }
	}

	/* Si no quedan asteroides terminamos el juego */
	// FIXME:
	/*
	 * if (asteroides.isEmpty()) { juegoTerminado(); }
	 */
    }

    @Override
    public boolean onKeyDown(int codigoTecla, KeyEvent evento)
    {
	super.onKeyDown(codigoTecla, evento);
	// Suponemos que vamos a procesar la pulsación
	boolean procesada = true;
	switch (codigoTecla)
	{
	case KeyEvent.KEYCODE_DPAD_UP:
	    aceleracionNave = +PASO_ACELERACION_NAVE;
	    break;
	case KeyEvent.KEYCODE_DPAD_LEFT:
	    giroNave = -PASO_GIRO_NAVE;
	    break;
	case KeyEvent.KEYCODE_DPAD_RIGHT:
	    giroNave = +PASO_GIRO_NAVE;
	    break;
	case KeyEvent.KEYCODE_DPAD_CENTER:
	case KeyEvent.KEYCODE_ENTER:
	    activaMisil();
	    break;
	default:
	    // Si estamos aquí, no hay pulsación que nos interese
	    procesada = false;
	    break;
	}
	return procesada;
    }

    @Override
    public boolean onKeyUp(int codigoTecla, KeyEvent evento)
    {
	super.onKeyUp(codigoTecla, evento);
	// Suponemos que vamos a procesar la pulsación
	boolean procesada = true;
	switch (codigoTecla)
	{
	case KeyEvent.KEYCODE_DPAD_UP:
	    aceleracionNave = 0;
	    break;
	case KeyEvent.KEYCODE_DPAD_LEFT:
	case KeyEvent.KEYCODE_DPAD_RIGHT:
	    giroNave = 0;
	    break;
	default:
	    // Si estamos aquí, no hay pulsación que nos interese
	    procesada = false;
	    break;
	}
	return procesada;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
	super.onTouchEvent(event);

	controlTactil(event);

	return true;
    }

    @Override
    public boolean performClick()
    {
	return super.performClick();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy)
    {

    }

    @Override
    public void onSensorChanged(SensorEvent event)
    {
	float valorX = event.values[0];
	float valorY = event.values[1];
	float valorZ = event.values[2];

	Log.d("onSensorChanged", "value: " + valorY);

	this.posSensX = valorX;
	this.posSensY = valorY;
	this.posSensZ = valorZ;
	
	nave.setAngulo(Math.round(valorY));
	
	anguloAnterior = (int)valorY;
	
    }

    public ThreadJuego getThread()
    {
	return thread;
    }

    public void setThread(ThreadJuego thread)
    {
	this.thread = thread;
    }

    public SensorManager getmSensorManager()
    {
	return mSensorManager;
    }

    public void setmSensorManager(SensorManager mSensorManager)
    {
	this.mSensorManager = mSensorManager;
    }

    public boolean isSensor()
    {
	return isSensor;
    }

    public void setSensor(boolean isSensor)
    {
	this.isSensor = isSensor;
    }

    public int getTipoGraficos()
    {
	return tipoGraficos;
    }

    public void setTipoGraficos(int tipoGraficos)
    {
	this.tipoGraficos = tipoGraficos;
    }

    public int getNumFragmentos()
    {
	return numFragmentos;
    }

    public void setNumFragmentos(int numFragmentos)
    {
	this.numFragmentos = numFragmentos;
    }

    public int getNumAsteroides()
    {
	return numAsteroides;
    }

    public void setNumAsteroides(int numAsteroides)
    {
	this.numAsteroides = numAsteroides;
    }

    public int getPuntuacion()
    {
	return puntuacion;
    }

    public void setPuntuacion(int puntuacion)
    {
	this.puntuacion = puntuacion;
    }

    public Activity getActivityPadre()
    {
	return activityPadre;
    }

    public void setActivityPadre(Activity activityPadre)
    {
	this.activityPadre = activityPadre;
    }

    /* Thread que hay que lanzar para actualizar los elementos de la pantalla */
    public class ThreadJuego extends AsyncTask<Void, Void, Void>
    {
	private boolean pausa, corriendo;

	public synchronized void pausar()
	{
	    pausa = true;
	}

	public synchronized void reanudar()
	{
	    pausa = false;
	    notify();
	}

	public void detener()
	{
	    corriendo = false;
	    if (pausa)
	    {
		reanudar();
	    }
	}

	@Override
	protected Void doInBackground(Void... params)
	{
	    corriendo = true;
	    while (corriendo)
	    {
		actualizaFisica();
		synchronized (this)
		{
		    while (pausa)
		    {
			try
			{
			    wait();
			} catch (Exception e)
			{
			    Log.e("ThreadJuego", "Error: " + e.getMessage());
			}
		    }
		}
	    }
	    return null;
	}

    }
}
