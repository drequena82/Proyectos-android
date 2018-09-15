package org.example.asteroides.xml;

import java.util.ArrayList;

import org.example.asteroides.beans.Puntuacion;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class ManejadorXMLPuntuaciones extends DefaultHandler
{
	private StringBuilder cadena;
	private Puntuacion puntuacion;
	private ArrayList<Puntuacion> listaPuntuaciones;

	@Override
	public void startDocument() throws SAXException
	{
		listaPuntuaciones = new ArrayList<Puntuacion>();
		cadena = new StringBuilder();
	}

	@Override
	public void startElement(String uri, String nombreLocal,
			String nombreCualif, Attributes atr) throws SAXException
	{
		cadena.setLength(0);
		if (nombreLocal.equals("puntuacion"))
		{
			puntuacion = new Puntuacion();
		}
	}

	@Override
	public void characters(char ch[], int comienzo, int lon)
	{
		cadena.append(ch, comienzo, lon);
	}

	@Override
	public void endElement(String uri, String nombreLocal, String nombreCualif)
			throws SAXException
	{
		if (nombreLocal.equals("puntos"))
		{
			puntuacion.setPuntuacion(Integer.valueOf(cadena.toString()));
		} else if (nombreLocal.equals("nombre"))
		{
			puntuacion.setNombre(cadena.toString());
		} else if (nombreLocal.equals("fecha"))
		{
			puntuacion.setFecha(cadena.toString());
		} else if (nombreLocal.equals("puntuacion"))
		{
			listaPuntuaciones.add(puntuacion);
		}
	}

	@Override
	public void endDocument() throws SAXException
	{
	}

	public ArrayList<Puntuacion> getListaPuntuaciones()
	{
		return listaPuntuaciones;
	}

	public void setListaPuntuaciones(ArrayList<Puntuacion> listaPuntuaciones)
	{
		this.listaPuntuaciones = listaPuntuaciones;
	}
}
