package com.libgdxexamples.android.activities;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

public class Test6 extends AndroidApplication
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
	super.onCreate(savedInstanceState);
	AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
	initialize(new com.libgdxexamples.samples.GenerateItemOnTouch(), config);
    }
}
