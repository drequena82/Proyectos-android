package org.example.asteroides;

import org.example.asteroides.views.VistaJuego;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;

public class Juego extends Activity
{

	private VistaJuego vista;
	public static final int REQUEST_CODE_DIALOG = 1234;
	public static final int REQUEST_CODE_RESUMEN = 1111;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{

		super.onCreate(savedInstanceState);

		setContentView(R.layout.juego);

		vista = (VistaJuego) findViewById(R.id.VistaJuego);
		vista.setActivityPadre(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.juego, menu);
		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		boolean isExit;
		switch (requestCode)
		{
		case Juego.REQUEST_CODE_DIALOG:
			if (resultCode == RESULT_OK)
			{
				isExit = data.getExtras().getBoolean("isExit");
				if (isExit)
				{
					finish();
				}
			}
			break;
		case Juego.REQUEST_CODE_RESUMEN:
			if (resultCode == RESULT_OK)
			{
				finish();
			}
			break;
		}
	}

	@Override
	protected void onStart()
	{
		super.onStart();
	}

	@Override
	public void onBackPressed()
	{
		// super.onBackPressed();
		Intent intent = new Intent(this, Abandonar.class);
		startActivityForResult(intent, Juego.REQUEST_CODE_DIALOG);
	}

	@Override
	protected void onPause()
	{
		super.onPause();
		vista.getThread().pausar();
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		vista.getThread().reanudar();
	}

	@Override
	protected void onDestroy()
	{
		vista.getThread().detener();
		super.onDestroy();
	}

	@Override
	protected void onStop()
	{
		vista.getThread().detener();

		/* Desactivamos el listener de los sensores */
		if (vista.isSensor())
		{
			vista.getmSensorManager().unregisterListener(vista);
		}

		super.onStop();
	}
}
