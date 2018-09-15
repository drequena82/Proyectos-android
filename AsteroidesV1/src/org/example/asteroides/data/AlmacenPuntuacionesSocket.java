package org.example.asteroides.data;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import org.example.asteroides.beans.Puntuacion;
import org.example.asteroides.interfaces.AlmacenPuntuaciones;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

public class AlmacenPuntuacionesSocket implements AlmacenPuntuaciones
{
	private Context contexto;
	private int num_puntuaciones = 10;
	private String ip = "http://192.168.2.116";
	//private String ip = "http://192.168.2.104";
	private int puerto = 1234;

	public AlmacenPuntuacionesSocket(Context context)
	{
		this.contexto = context;

		SharedPreferences pref = context.getSharedPreferences(
				"org.example.asteroides_preferences", Context.MODE_PRIVATE);
		num_puntuaciones = Integer.valueOf(pref.getString("numPuntuaciones",
				"10"));
	}

	@Override
	public void guardarPuntuacion(int puntos, String nombre, String fecha)
	{
		GuardarThread hilo = new GuardarThread(puntos, nombre, fecha);
		hilo.run();
	}

	@Override
	public ArrayList<Puntuacion> listaPuntuaciones()
	{
		ObtenerThread hilo = new ObtenerThread();
		hilo.run();
		while(hilo.isAlive())
		{
		}
		return hilo.getPuntuaciones();
	}

	public class GuardarThread extends Thread
	{
		private int puntos;
		private String nombre;
		private String fecha;

		public GuardarThread(int puntos, String nombre, String fecha)
		{
			this.puntos = puntos;
			this.nombre = nombre;
			this.fecha = fecha;

		}

		public void run()
		{
			this.guardarPuntuacion(puntos, nombre, fecha);
		}

		public void guardarPuntuacion(int puntos, String nombre, String fecha)
		{
			try
			{
				Socket sk = new Socket(ip, puerto);
				BufferedReader entrada = new BufferedReader(
						new InputStreamReader(sk.getInputStream()));
				PrintWriter salida = new PrintWriter(new OutputStreamWriter(
						sk.getOutputStream()), true);
				salida.println(puntos + "-" + nombre + "-" + fecha);

				String respuesta = entrada.readLine();

				if (!"OK".equals(respuesta))
				{
					Log.e("Asteroides",
							"Error: respuesta de servidor incorrecta");
					Toast.makeText(contexto,
							"Error: respuesta de servidor incorrecta",
							Toast.LENGTH_LONG).show();
				}

				sk.close();
			} catch (Exception e)
			{
				Log.e("Asteroides", e.toString(), e);
				Toast.makeText(contexto, "Error:" + e.getMessage(),
						Toast.LENGTH_LONG).show();
			}
		}
	}

	public class ObtenerThread extends Thread
	{
		private ArrayList<Puntuacion> puntuaciones;
		
		public void run()
		{
			puntuaciones = this.listaPuntuaciones();
		}

		public ArrayList<Puntuacion> listaPuntuaciones()
		{
			ArrayList<Puntuacion> result = new ArrayList<Puntuacion>();
			Puntuacion puntuacion = null;
			String[] campos = null;
			int n = 0;
			String respuesta;
			try
			{
				Socket sk = new Socket(ip, puerto);
				BufferedReader entrada = new BufferedReader(
						new InputStreamReader(sk.getInputStream()));
				PrintWriter salida = new PrintWriter(new OutputStreamWriter(
						sk.getOutputStream()), true);

				salida.println("PUNTUACIONES");
				do
				{
					respuesta = entrada.readLine();
					if (respuesta != null)
					{
						campos = respuesta.split("-");
						puntuacion = new Puntuacion();
						puntuacion.setPuntuacion(Integer.valueOf(campos[0]));
						puntuacion.setNombre(campos[1]);
						puntuacion.setFecha(campos[2]);

						result.add(puntuacion);
						n++;
					}
				} while (n < num_puntuaciones && respuesta != null);
				sk.close();
			} catch (Exception e)
			{
				Log.e("Asteroides", e.toString(), e);
				Toast.makeText(contexto, "Error:" + e.getMessage(),
						Toast.LENGTH_LONG).show();
			}
			return result;
		}

		public ArrayList<Puntuacion> getPuntuaciones()
		{
			return puntuaciones;
		}

		public void setPuntuaciones(ArrayList<Puntuacion> puntuaciones)
		{
			this.puntuaciones = puntuaciones;
		}
		
	}
}
