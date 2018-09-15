package org.example.asteroides.utils;

public class GameUtils
{
    public static float getDegrees(float x, float y)
    {
	float result = 0;

	return result;
    }
    
    public static float getPosX(float width)
    {
	float result = 0;
	float sign = -1;
	sign = ((float)(Math.random() * 10)%2 == 0)?1:-1;
	
	result = (float)(Math.random() * 1000f)%width * sign;
	
	return result;
    }
    
    public static float getPosY(float heigth)
    {
	float result = 0;
	float sign = -1;
	sign = ((float)(Math.random() * 10)%2 == 0)?1:-1;
	
	result = (float)(Math.random() * 1000f)%heigth * sign;
	
	return result;
    }
    
    public static float getFactor()
    {
	float result = 0;
	switch ((int) (Math.random() * 10f) % 2)
	{
	case 0:
	    result = 1;
	    break;

	case 1:
	    result = -1;
	}
	return result;
    }
}
