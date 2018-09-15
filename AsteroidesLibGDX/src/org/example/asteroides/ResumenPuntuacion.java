package org.example.asteroides;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.example.asteroides.data.AlmacenPuntuacionesBaseDatos;
import org.example.asteroides.data.AlmacenPuntuacionesFicheroExterno;
import org.example.asteroides.data.AlmacenPuntuacionesFicheroInterno;
import org.example.asteroides.data.AlmacenPuntuacionesPreferencias;
import org.example.asteroides.data.AlmacenPuntuacionesSocket;
import org.example.asteroides.data.AlmacenPuntuacionesWeb;
import org.example.asteroides.interfaces.AlmacenPuntuaciones;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ResumenPuntuacion extends Activity
{

	private int tipoGuardado;
	private int puntuacion;
	private EditText nombre;
	private Date fecha;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		int puntuacion = 0;
		super.onCreate(savedInstanceState);

		setContentView(R.layout.resumen_puntuacion);
		TextView textScore = (TextView) findViewById(R.id.resumen_score);
		Button button_ok = (Button) findViewById(R.id.button_ok);
		
		nombre = (EditText) findViewById(R.id.editTextNombre);

		puntuacion = getIntent().getExtras().getInt("PUNTUACION");

		this.puntuacion = puntuacion;

		textScore.setText(String.valueOf(this.puntuacion));

		if(this.puntuacion == 0)
		{
			nombre.setEnabled(false);
			button_ok.setEnabled(false);
		}else
		{
			nombre.setEnabled(true);
			button_ok.setEnabled(true);
		}
		
		/* Obtenemos las preferencias para los tipos de guardado */
		/* Para recoger los valores de la pantalla de preferencias */
		SharedPreferences pref = getSharedPreferences(
				"org.example.asteroides_preferences", MODE_PRIVATE);
		tipoGuardado = Integer.valueOf(pref.getString("puntuaciones", "0"));
		
		
	}

	public void ok(View view)
	{
		/* Aqui habría que guardar la puntuación */
		Intent intent = new Intent();
		AlmacenPuntuaciones puntuaciones = null;
		String fecha;
		
		switch (this.tipoGuardado)
		{
		case 0:
			puntuaciones = new AlmacenPuntuacionesPreferencias(this);
			break;
		case 1:
			puntuaciones = new AlmacenPuntuacionesFicheroInterno(this);
			break;
		case 2:
			puntuaciones = new AlmacenPuntuacionesFicheroExterno(this);
			break;
		case 3:
			puntuaciones = new AlmacenPuntuacionesBaseDatos(this);
			break;
		case 4:
			puntuaciones = new AlmacenPuntuacionesWeb(this);
			break;
		case 5:
			puntuaciones = new AlmacenPuntuacionesSocket(this);
			break;
		case 6:
			break;
		case 7:
			break;
		}

		this.fecha = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy",
				Locale.ENGLISH);
		fecha = sdf.format(this.fecha);
		puntuaciones.guardarPuntuacion(this.puntuacion, this.nombre.getText()
				.toString(), fecha);

		setResult(RESULT_OK, intent);

		finish();
	}

	public void cancel(View view)
	{
		Intent intent = new Intent();
		setResult(RESULT_OK, intent);
		finish();
	}

	public boolean onTouchEvent(MotionEvent event)
	{
		if (event.getAction() == MotionEvent.ACTION_DOWN
				&& isOutOfBounds(this, event))
		{
			return true;
		}
		return super.onTouchEvent(event);
	}

	private boolean isOutOfBounds(Activity context, MotionEvent event)
	{
		final int x = (int) event.getX();
		final int y = (int) event.getY();
		final int slop = ViewConfiguration.get(context)
				.getScaledWindowTouchSlop();
		final View decorView = context.getWindow().getDecorView();
		return (x < -slop) || (y < -slop)
				|| (x > (decorView.getWidth() + slop))
				|| (y > (decorView.getHeight() + slop));
	}

}
