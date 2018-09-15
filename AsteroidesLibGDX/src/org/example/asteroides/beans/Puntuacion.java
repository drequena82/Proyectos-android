package org.example.asteroides.beans;

import java.io.Serializable;

public class Puntuacion implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 360224529521644417L;

	private String nombre;

	private int puntuacion;

	private String fecha;

	public String getNombre()
	{
		return nombre;
	}

	public void setNombre(String nombre)
	{
		this.nombre = nombre;
	}

	public int getPuntuacion()
	{
		return puntuacion;
	}

	public void setPuntuacion(int puntuacion)
	{
		this.puntuacion = puntuacion;
	}

	public String getFecha()
	{
		return fecha;
	}

	public void setFecha(String fecha)
	{
		this.fecha = fecha;
	}
}
