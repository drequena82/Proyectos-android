package com.libgdxexamples.android.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.main_layout);

    }

    public void test1(View view)
    {
	Intent i = new Intent(this, Test1.class);

	startActivity(i);
    }

    public void test2(View view)
    {
	Intent i = new Intent(this, Test2.class);

	startActivity(i);
    }

    public void test3(View view)
    {
	Intent i = new Intent(this, Test3.class);

	startActivity(i);
    }

    public void test4(View view)
    {
	Intent i = new Intent(this, Test4.class);

	startActivity(i);
    }

    public void test5(View view)
    {
	Intent i = new Intent(this, Test5.class);

	startActivity(i);
    }
    
    public void test6(View view)
    {
	Intent i = new Intent(this, Test6.class);

	startActivity(i);
    }
    
    public void test7(View view)
    {
	Intent i = new Intent(this, TestSimpleParticle.class);

	startActivity(i);
    }
    
    public void test8(View view)
    {
	Intent i = new Intent(this, TestParticle.class);

	startActivity(i);
    }
    
    public void test9(View view)
    {
	Intent i = new Intent(this, TestGame.class);

	startActivity(i);
    }
}
