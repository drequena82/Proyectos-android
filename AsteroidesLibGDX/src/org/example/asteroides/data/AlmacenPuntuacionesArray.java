package org.example.asteroides.data;

import java.util.ArrayList;
import org.example.asteroides.beans.Puntuacion;
import org.example.asteroides.interfaces.AlmacenPuntuaciones;

public class AlmacenPuntuacionesArray implements AlmacenPuntuaciones
{

	private ArrayList<Puntuacion> puntuaciones;

	public AlmacenPuntuacionesArray()
	{

		puntuaciones = new ArrayList<Puntuacion>();
		Puntuacion puntuacion = new Puntuacion();
		puntuacion.setNombre("Pepito Domingez");
		puntuacion.setPuntuacion(10000);
		puntuacion.setFecha("20/05/2013");
		puntuaciones.add(puntuacion);

		puntuacion = new Puntuacion();
		puntuacion.setNombre("Pedro Martinez");
		puntuacion.setPuntuacion(120000);
		puntuacion.setFecha("20/05/2013");
		puntuaciones.add(puntuacion);

		puntuacion = new Puntuacion();
		puntuacion.setNombre("Paco Pérez");
		puntuacion.setPuntuacion(135555);
		puntuacion.setFecha("20/05/2013");
		puntuaciones.add(puntuacion);

	}

	@Override
	public void guardarPuntuacion(int puntos, String nombre, String fecha)
	{
		
		Puntuacion puntuacion = new Puntuacion();
		puntuacion.setNombre(nombre);
		puntuacion.setPuntuacion(puntos);
		puntuacion.setFecha(fecha);
		puntuaciones.add(puntuacion);
	}

	@Override
	public ArrayList<Puntuacion> listaPuntuaciones()
	{

		return puntuaciones;

	}

}
