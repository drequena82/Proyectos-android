package org.example.asteroides.graphics;

import android.graphics.drawable.Drawable;
import android.view.View;

public class Nave extends Grafico
{

    public Nave(View view, Drawable drawable)
    {
	super(view, drawable);
    }

    public void incrementaPos(double factor,double aceleracion)
    {
	/* Hay que tener en cuenta el angulo del objeto para el movimiento */

	/*Cada 40 ms se moverá un incremento en cada eje, dado por su angulo*/
	incX = incX + (aceleracion * Math.cos(angulo) * factor);
	incY = incY + (aceleracion * Math.sin(angulo) * factor);
	
	posX += incX * factor;

	// Si salimos de la pantalla, corregimos posición

	if (posX < -ancho / 2)
	{
	    posX = view.getWidth() - ancho / 2;
	}

	if (posX > view.getWidth() - ancho / 2)
	{
	    posX = -ancho / 2;
	}

	posY += incY * factor;

	if (posY < -alto / 2)
	{
	    posY = view.getHeight() - alto / 2;
	}

	if (posY > view.getHeight() - alto / 2)
	{
	    posY = -alto / 2;
	}

	angulo += rotacion * factor; // Actualizamos ángulo

    }
}
