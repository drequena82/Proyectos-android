package org.example.asteroides.interfaces;

import java.util.ArrayList;

import org.example.asteroides.beans.Puntuacion;

public interface AlmacenPuntuaciones
{
	public void guardarPuntuacion(int puntos, String nombre, String fecha);

	public ArrayList<Puntuacion> listaPuntuaciones();
}
