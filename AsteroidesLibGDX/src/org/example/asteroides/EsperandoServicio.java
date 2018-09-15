package org.example.asteroides;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.ProgressBar;

public class EsperandoServicio extends Activity
{

	private ProgressBar pbar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_esperando_servicio);
		
		pbar = (ProgressBar) findViewById(R.id.progressBarWaiting);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.esperando_servicio, menu);
		return true;
	}
	
	public void cargarPuntuaciones()
	{
		//TODO: 
		pbar.isEnabled();
	}

}
