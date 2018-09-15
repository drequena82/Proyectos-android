package org.example.asteroides.data;

import java.util.ArrayList;
import org.example.asteroides.beans.Puntuacion;
import org.example.asteroides.interfaces.AlmacenPuntuaciones;

import android.content.Context;
import android.content.SharedPreferences;

public class AlmacenPuntuacionesPreferencias implements AlmacenPuntuaciones
{
	private SharedPreferences sharedPrefs;
	private int num_puntuaciones = 10;
	public AlmacenPuntuacionesPreferencias(Context context)
	{
		sharedPrefs = context.getSharedPreferences("puntuaciones",
				Context.MODE_PRIVATE);
		
		SharedPreferences pref = context.getSharedPreferences(
				"org.example.asteroides_preferences", Context.MODE_PRIVATE);
		num_puntuaciones = Integer.valueOf(pref.getString("numPuntuaciones", "10"));
	}

	@Override
	public void guardarPuntuacion(int puntos, String nombre, String fecha)
	{
		SharedPreferences.Editor editor = sharedPrefs.edit();
		int posicion = 0;
		boolean isGuardado = false;
		ArrayList<Puntuacion> lista = this.listaPuntuaciones();
		
		/*Lista ordenada, podemos utilizar el algoritmo binario*/
		if(lista.isEmpty())
		{
			posicion = 0;
			editor.putString("puntuacion" + posicion, puntos + "-" + nombre + "-" + fecha);
		}else
		{
			for(Puntuacion puntuacion:lista)
			{
				if(puntuacion.getPuntuacion()<puntos && !isGuardado)
				{
					editor.putString("puntuacion" + (posicion++), puntos + "-" + nombre + "-" + fecha);
					
					isGuardado = true;
				}
				
				editor.putString("puntuacion" + (posicion++), puntuacion.getPuntuacion() + "-" + puntuacion.getNombre() + "-" + puntuacion.getFecha());
			}
			
			if(lista.size() < num_puntuaciones && !isGuardado)
			{
				editor.putString("puntuacion" + posicion, puntos + "-" + nombre + "-" + fecha);
			}
		}
		
		editor.commit();
	}

	@Override
	public ArrayList<Puntuacion> listaPuntuaciones()
	{
		ArrayList<Puntuacion> result = new ArrayList<Puntuacion>();
		Puntuacion puntuacion = null;
		String lineaPuntuacion = null;
		String[] campos = null;
		for (int counter = 0; counter < num_puntuaciones; counter++)
		{
			lineaPuntuacion = sharedPrefs.getString("puntuacion" + counter, "");
			if (lineaPuntuacion != "")
			{
				puntuacion = new Puntuacion();
				
				campos = lineaPuntuacion.split("-");
				puntuacion.setPuntuacion(Integer.valueOf(campos[0]));
				puntuacion.setNombre(campos[1]);
				puntuacion.setFecha(campos[2]);
				result.add(puntuacion);
			}
		}
		return result;
	}
}
