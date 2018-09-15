package org.example.asteroides.adapters;

import java.util.List;

import org.example.asteroides.R;
import org.example.asteroides.beans.Puntuacion;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class BeansAdapter extends ArrayAdapter<Puntuacion>
{
	private Context context;

	public BeansAdapter(Context context, int resource, List<Puntuacion> objects)
	{
		super(context, resource, objects);
		this.context = context;
	}

	public View getView(int position, View convertView, ViewGroup parent)
	{
		LayoutInflater mInflater = (LayoutInflater) this.context
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		Puntuacion puntuacion = getItem(position);

		convertView = mInflater.inflate(R.layout.elemento_lista, null);

		TextView textView = (TextView) convertView.findViewById(R.id.titulo_elemento);
		textView.setText(puntuacion.getNombre());
		
		TextView textViewPuntuacion = (TextView) convertView
				.findViewById(R.id.subtitulo_elemento);
		textViewPuntuacion.setText(String.valueOf(puntuacion.getPuntuacion()));
		
		TextView textViewFecha = (TextView) convertView
				.findViewById(R.id.fecha_elemento);
		textViewFecha.setText(puntuacion.getFecha());

		ImageView imageView = (ImageView) convertView.findViewById(R.id.icono);
		switch (Math.round((float) Math.random() * 3))
		{
		case 0:
			imageView.setImageResource(R.drawable.asteroide1);
			break;
		case 1:
			imageView.setImageResource(R.drawable.asteroide2);
			break;
		default:
			imageView.setImageResource(R.drawable.asteroide3);
			break;
		}

		return convertView;
	}
}
