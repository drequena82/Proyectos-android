package org.example.asteroides.data;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.example.asteroides.beans.Puntuacion;
import org.example.asteroides.interfaces.AlmacenPuntuaciones;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

public class AlmacenPuntuacionesFicheroInterno implements AlmacenPuntuaciones
{
	private Context context;
	private String fichero = "puntuaciones.txt";
	private int num_puntuaciones = 10;
	
	public AlmacenPuntuacionesFicheroInterno(Context context)
	{
		this.context = context;
		
		SharedPreferences pref = context.getSharedPreferences(
				"org.example.asteroides_preferences", Context.MODE_PRIVATE);
		num_puntuaciones = Integer.valueOf(pref.getString("numPuntuaciones", "10"));
	}

	@Override
	public void guardarPuntuacion(int puntos, String nombre, String fecha)
	{
		FileOutputStream fos;
		ArrayList<Puntuacion> lista;
		Puntuacion puntuacion;
		boolean isGuardado = false;
		try
		{
			lista = this.listaPuntuaciones();
			
			fos = context.openFileOutput(fichero, Context.MODE_APPEND);

			/* Escribimos la lista de puntuaciones ordenada */
			if(lista.isEmpty())
			{
				fos.write((puntos + "-" + nombre + "-" + fecha).getBytes());
			}else
			{
				for(int i=0;i<lista.size();i++)
				{
					puntuacion = lista.get(i);
					if(!isGuardado && puntuacion.getPuntuacion()<puntos)
					{
						fos.write((puntos + "-" + nombre + "-" + fecha).getBytes());
						isGuardado = true;
					}
					
					fos.write((puntuacion.getPuntuacion() + "-" + puntuacion.getNombre() + "-" + puntuacion.getFecha()).getBytes());
				}
				
				if(lista.size()<10 && !isGuardado)
				{
					fos.write((puntos + "-" + nombre + "-" + fecha).getBytes());
				}
			}

			fos.close();
		} catch (FileNotFoundException e)
		{
			Log.e("guardarPuntuacion - error: ", e.getMessage(), e);
			Toast.makeText(this.context, e.getMessage(), Toast.LENGTH_LONG)
			.show();
		} catch (IOException e)
		{
			Log.e("guardarPuntuacion - error: ", e.getMessage(), e);
			Toast.makeText(this.context, e.getMessage(), Toast.LENGTH_LONG)
			.show();
		}

	}

	@Override
	public ArrayList<Puntuacion> listaPuntuaciones()
	{
		ArrayList<Puntuacion> result = new ArrayList<Puntuacion>();
		String[] campos = null;
		try
		{
			FileInputStream fileIS = context.openFileInput(this.fichero);
			BufferedReader entrada = new BufferedReader(new InputStreamReader(
					fileIS));
			int n = 0;
			String linea;
			Puntuacion puntuacion = null;

			do
			{
				linea = entrada.readLine();
				if (linea != null)
				{
					campos = linea.split("-");

					puntuacion = new Puntuacion();

					puntuacion.setPuntuacion(Integer.valueOf(campos[0]));
					puntuacion.setNombre(campos[1]);
					puntuacion.setFecha(campos[2]);

					result.add(puntuacion);
					n++;
				}
			} while (n < num_puntuaciones && linea != null);
			fileIS.close();
		} catch (FileNotFoundException e)
		{
			Log.e("listaPuntuaciones - error: ", e.getMessage());
			Toast.makeText(this.context, e.getMessage(), Toast.LENGTH_LONG)
					.show();
		} catch (IOException iex)
		{
			Log.e("listaPuntuaciones - error: ", iex.getMessage());
			Toast.makeText(this.context, iex.getMessage(), Toast.LENGTH_LONG)
					.show();
		}
		return result;
	}

}
