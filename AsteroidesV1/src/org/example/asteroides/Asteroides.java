package org.example.asteroides;

import org.example.asteroides.services.ServicioMusica;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class Asteroides extends Activity
{

	// private MediaPlayer mp;
	private boolean isMusicaFondo;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

	}

	@Override
	protected void onSaveInstanceState(Bundle outState)
	{
		super.onSaveInstanceState(outState);
		/*
		 * if (mp != null) { int pos = mp.getCurrentPosition();
		 * outState.putInt("musicPos", pos); }
		 */
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState)
	{
		super.onRestoreInstanceState(savedInstanceState);
		/*
		 * if (savedInstanceState != null && mp != null) { int pos =
		 * savedInstanceState.getInt("musicPos"); mp.seekTo(pos); }
		 */
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void lanzarJuego(View view)
	{
		Intent i = new Intent(this, Juego.class);

		startActivity(i);
	}

	public void lanzarAcercaDe(View view)
	{
		Intent i = new Intent(this, Acercade.class);

		startActivity(i);
	}

	public void lanzarConfig(View view)
	{
		Intent i = new Intent(this, Preferencias.class);

		startActivity(i);
	}

	public void lanzarPuntuaciones(View view)
	{
		Intent i = new Intent(this, Puntuaciones.class);

		startActivity(i);
	}

	public void salir(View view)
	{
		Builder builder = new Builder(this);

		AlertDialog alert = builder.create();
		alert.setCanceledOnTouchOutside(true);
		alert.setTitle(getString(R.string.app_name));
		alert.setMessage(getString(R.string.exit_label));
		alert.setButton(AlertDialog.BUTTON_POSITIVE,
				getString(android.R.string.yes),
				new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						finish();
					}
				});

		alert.setButton(AlertDialog.BUTTON_NEGATIVE,
				getString(android.R.string.no),
				new DialogInterface.OnClickListener()
				{

					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						dialog.cancel();
					}
				});
		alert.show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
		case R.id.about:
			lanzarAcercaDe(null);
			break;
		case R.id.config:
			lanzarConfig(null);
			break;
		}
		return true;
	}

	@Override
	public void onBackPressed()
	{
		salir(null);
	}

	@Override
	protected void onStart()
	{
		super.onStart();
		//Toast.makeText(this, "onStart", Toast.LENGTH_SHORT).show();
		
		/* Para recoger los valores de la pantalla de preferencias */
		SharedPreferences pref = getSharedPreferences(
				"org.example.asteroides_preferences", MODE_PRIVATE);
		isMusicaFondo = pref.getBoolean("musica", true);
		/* -- */

		// Toast.makeText(this, "onCreate", Toast.LENGTH_SHORT).show();
		// mp = MediaPlayer.create(this, R.raw.audio);
		// mp.start();
		if (isMusicaFondo)
		{
			startService(new Intent(Asteroides.this, ServicioMusica.class));
		}else
		{
			stopService(new Intent(Asteroides.this, ServicioMusica.class));
		}
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		// Toast.makeText(this, "onResume", Toast.LENGTH_SHORT).show();
	}

	@Override
	protected void onPause()
	{
		// Toast.makeText(this, "onPause", Toast.LENGTH_SHORT).show();
		super.onPause();
	}

	@Override
	protected void onStop()
	{
		// Toast.makeText(this, "onStop", Toast.LENGTH_SHORT).show();
		// mp.stop();
		super.onStop();
	}

	@Override
	protected void onRestart()
	{
		super.onRestart();
		// mp.stop();
		// Toast.makeText(this, "onRestart", Toast.LENGTH_SHORT).show();
	}

	@Override
	protected void onDestroy()
	{
		// Toast.makeText(this, "onDestroy", Toast.LENGTH_SHORT).show();
		if (isMusicaFondo)
		{
			stopService(new Intent(Asteroides.this, ServicioMusica.class));
		}
		super.onDestroy();
	}
}