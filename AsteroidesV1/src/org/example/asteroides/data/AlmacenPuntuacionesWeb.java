package org.example.asteroides.data;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.example.asteroides.beans.Puntuacion;
import org.example.asteroides.interfaces.AlmacenPuntuaciones;
import org.example.asteroides.xml.ManejadorXMLPuntuaciones;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xmlpull.v1.XmlSerializer;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.util.Xml;

public class AlmacenPuntuacionesWeb implements AlmacenPuntuaciones
{
	private ArrayList<Puntuacion> listaPuntuaciones;
	private Context contexto;
	private String FICHERO = "puntuacionesXML.txt";
	private int num_puntuaciones = 10;
	
	public AlmacenPuntuacionesWeb(Context context)
	{
		this.contexto = context;
		this.listaPuntuaciones = new ArrayList<Puntuacion>();
		
		SharedPreferences pref = context.getSharedPreferences(
				"org.example.asteroides_preferences", Context.MODE_PRIVATE);
		num_puntuaciones = Integer.valueOf(pref.getString("numPuntuaciones", "10"));
	}

	@Override
	public void guardarPuntuacion(int puntos, String nombre, String fecha)
	{
		Puntuacion nuevaPuntuacion;
		boolean isGuardado = false;
		ArrayList<Puntuacion> nuevaListaPuntuaciones = new ArrayList<Puntuacion>();
		try
		{
			/*Puntuación nueva*/
			nuevaPuntuacion = new Puntuacion();
			nuevaPuntuacion.setPuntuacion(puntos);
			nuevaPuntuacion.setNombre(nombre);
			nuevaPuntuacion.setFecha(fecha);

			/*leemos el archivo XML*/
			leerXML(contexto.openFileInput(FICHERO));
			
			/*Metemos la nueva puntuación en el array ordenado*/
			if(this.listaPuntuaciones.isEmpty())
			{
				nuevaListaPuntuaciones.add(nuevaPuntuacion);
			}else
			{
				for(Puntuacion puntuacion:listaPuntuaciones)
				{
					if(puntuacion.getPuntuacion() < nuevaPuntuacion.getPuntuacion() && !isGuardado)
					{
						nuevaListaPuntuaciones.add(nuevaPuntuacion);
						isGuardado = true;
					}
					
					nuevaListaPuntuaciones.add(puntuacion);
				}
				
				if(this.listaPuntuaciones.size() < num_puntuaciones && !isGuardado)
				{
					nuevaListaPuntuaciones.add(nuevaPuntuacion);
				}
			}
			
		} catch (FileNotFoundException e)
		{
			
		} catch (Exception e)
		{
			Log.e("Asteroides", e.getMessage(), e);
		}
		
		try
		{
			/*Volvemos a meter todas las puntuaciones en el archivo XML*/
			escribirXML(contexto.openFileOutput(FICHERO, Context.MODE_PRIVATE));
		} catch (Exception e)
		{
			Log.e("Asteroides", e.getMessage(), e);
		}
	}

	@Override
	public ArrayList<Puntuacion> listaPuntuaciones()
	{
		// TODO Auto-generated method stub
		return null;
	}

	public void escribirXML(OutputStream salida)
	{
		XmlSerializer serializador = Xml.newSerializer();

		try
		{
			serializador.setOutput(salida, "UTF-8");
			serializador.startDocument("UTF-8", true);
			serializador.startTag("", "lista_puntuaciones");
			for (Puntuacion puntuacion : listaPuntuaciones)
			{
				serializador.startTag("", "puntuacion");

				serializador.startTag("", "nombre");
				serializador.text(puntuacion.getNombre());
				serializador.endTag("", "nombre");

				serializador.startTag("", "puntos");
				serializador.text(String.valueOf(puntuacion.getPuntuacion()));
				serializador.endTag("", "puntos");

				serializador.startTag("", "fecha");
				serializador.text(String.valueOf(puntuacion.getFecha()));
				serializador.endTag("", "fecha");

				serializador.endTag("", "puntuacion");
			}
			serializador.endTag("", "lista_puntuaciones");
			serializador.endDocument();
		} catch (Exception e)
		{
			Log.e("Asteroides", e.getMessage(), e);
		}
	}

	public void leerXML(InputStream entrada) throws Exception
	{
		SAXParserFactory fabrica = SAXParserFactory.newInstance();
		SAXParser parser = fabrica.newSAXParser();
		XMLReader lector = parser.getXMLReader();
		ManejadorXMLPuntuaciones manejadorXML = new ManejadorXMLPuntuaciones();
		lector.setContentHandler(manejadorXML);
		lector.parse(new InputSource(entrada));
		
		this.listaPuntuaciones = manejadorXML.getListaPuntuaciones();
	}
}
