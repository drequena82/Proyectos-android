package org.example.asteroides;

import java.util.ArrayList;

import org.example.asteroides.adapters.BeansAdapter;
import org.example.asteroides.beans.Puntuacion;
import org.example.asteroides.data.AlmacenPuntuacionesBaseDatos;
import org.example.asteroides.data.AlmacenPuntuacionesFicheroExterno;
import org.example.asteroides.data.AlmacenPuntuacionesFicheroInterno;
import org.example.asteroides.data.AlmacenPuntuacionesPreferencias;
import org.example.asteroides.data.AlmacenPuntuacionesSocket;
import org.example.asteroides.data.AlmacenPuntuacionesWeb;
import org.example.asteroides.interfaces.AlmacenPuntuaciones;

import android.app.ListActivity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

public class Puntuaciones extends ListActivity
{
	private AlmacenPuntuaciones puntuaciones;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		setContentView(R.layout.puntuaciones);

		SharedPreferences pref = getSharedPreferences(
				"org.example.asteroides_preferences", MODE_PRIVATE);
		int tipoGuardado = Integer.valueOf(pref.getString("puntuaciones", "0"));

		switch (tipoGuardado)
		{
		case 0:
			puntuaciones = new AlmacenPuntuacionesPreferencias(this);
			break;
		case 1:
			puntuaciones = new AlmacenPuntuacionesFicheroInterno(this);
			break;
		case 2:
			puntuaciones = new AlmacenPuntuacionesFicheroExterno(this);
			break;
		case 3:
			puntuaciones = new AlmacenPuntuacionesBaseDatos(this);
			break;
		case 4:
			puntuaciones = new AlmacenPuntuacionesWeb(this);
			break;
		case 5:
			puntuaciones = new AlmacenPuntuacionesSocket(this);
			break;
		case 6:
			break;
		case 7:
			break;
		}

		/* Asignamos a harcode las puntuaciones */
		ArrayList<Puntuacion> listaPuntuaciones = this.puntuaciones.listaPuntuaciones();

		/**/

		BeansAdapter adapter = new BeansAdapter(this, R.layout.elemento_lista,listaPuntuaciones);

		setListAdapter(adapter);
		/*
		 * setListAdapter(new PuntuacionesAdapter(this, (new
		 * AlmacenPuntuacionesArray()).listaPuntuaciones(10)));
		 * 
		 * getListView().setOnItemClickListener(new OnItemClickListener() {
		 * 
		 * @Override public void onItemClick(AdapterView<?> arg0, View arg1, int
		 * arg2, long arg3) { Log.d("Puntuaciones", "Ha hecho click en " +
		 * arg2);
		 * 
		 * } });
		 */
	}

	@Override
	protected void onListItemClick(ListView listView, View view, int position,
			long id)
	{
		super.onListItemClick(listView, view, position, id);
		/*
		 * Puntuacion p = (Puntuacion) getListAdapter().getItem(position);
		 * Toast.makeText(this, "Selección: " + p.getNombre() + " - " +
		 * p.getPuntuacion(), Toast.LENGTH_LONG).show();
		 */
	}
}
