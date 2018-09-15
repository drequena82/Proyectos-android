package com.libgdxexamples.launchers;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.libgdxexamples.samples.ParticleTest;
import com.libgdxexamples.samples.Physics2;
import com.libgdxexamples.samples.Physics3;
import com.libgdxexamples.samples.Physics4;
import com.libgdxexamples.samples.Physics5;
import com.libgdxexamples.samples.Scene;
import com.libgdxexamples.samples.Scene2;
import com.libgdxexamples.samples.SceneDemo3;
import com.libgdxexamples.samples.SceneTest;
import com.libgdxexamples.samples.SceneTest2;
import com.libgdxexamples.samples.Test1;
import com.libgdxexamples.samples.Test2;

public class DesktopLauncher
{

    /**
     * @param args
     */
    public static void main(String[] args)
    {
	LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
	config.title = "Test1";
	config.width = 480;
	config.height = 800;
	switch (Integer.valueOf(args[0]))
	{
	case 0:
	    new LwjglApplication(new Test1(), config);
	    break;
	case 1:
	    new LwjglApplication(new Test2(), config);
	    break;
	case 2:
	    new LwjglApplication(new Physics2(), config);
	    break;
	case 3:
	    new LwjglApplication(new Physics3(), config);
	    break;
	case 4:
	    new LwjglApplication(new Physics4(), config);
	    break;
	case 5:
	    new LwjglApplication(new Physics5(), config);
	    break;
	case 6:
	    new LwjglApplication(new ParticleTest(), config);
	    break;
	case 7:
	    new LwjglApplication(new SceneTest2(), config);
	    break;
	case 8:
	    new LwjglApplication(new SceneTest(), config);
	    break;
	case 9:
	    new LwjglApplication(new SceneDemo3(), config);
	    break;
	case 10:
	    new LwjglApplication(new Scene(), config);
	    break;
	case 11:
	    new LwjglApplication(new Scene2(), config);
	    break;
	default:
	    new LwjglApplication(new Test1(), config);
	}

    }

}
