package org.example.asteroides;

import org.example.asteroides.game.AsteroidsGame;

import android.content.Intent;
import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

public class EspacioJuego extends AndroidApplication
{
    public static final int REQUEST_CODE_DIALOG = 1234;
    public static final int REQUEST_CODE_RESUMEN = 1111;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
	AsteroidsGame game = new AsteroidsGame(this);
	super.onCreate(savedInstanceState);
	AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
	initialize(game, config);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
	super.onActivityResult(requestCode, resultCode, data);
	boolean isExit;
	switch (requestCode)
	{
	case REQUEST_CODE_DIALOG:
	    if (resultCode == RESULT_OK)
	    {
		isExit = data.getExtras().getBoolean("isExit");
		if (isExit)
		{
		    finish();
		}
	    }
	    break;
	case REQUEST_CODE_RESUMEN:
	    if (resultCode == RESULT_OK)
	    {
		finish();
	    }
	    break;
	}
    }

    @Override
    public void onBackPressed()
    {
	Intent intent = new Intent(this, Abandonar.class);
	startActivityForResult(intent, REQUEST_CODE_DIALOG);
    }

    @Override
    protected void onPause()
    {
	// TODO Auto-generated method stub
	super.onPause();
    }

    @Override
    protected void onStop()
    {
	// TODO Auto-generated method stub
	super.onStop();
    }
}
