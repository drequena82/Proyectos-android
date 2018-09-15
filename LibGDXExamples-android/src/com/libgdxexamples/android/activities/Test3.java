package com.libgdxexamples.android.activities;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.libgdxexamples.samples.GenerateBlocksOnTouch;

public class Test3 extends AndroidApplication
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
	super.onCreate(savedInstanceState);
	AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
	initialize(new GenerateBlocksOnTouch(), config);
    }
}
