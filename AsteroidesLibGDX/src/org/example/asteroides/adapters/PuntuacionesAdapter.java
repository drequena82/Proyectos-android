package org.example.asteroides.adapters;

import java.util.Vector;

import org.example.asteroides.R;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class PuntuacionesAdapter extends BaseAdapter
{

	private Activity activity;
	private Vector<String> puntuaciones;

	public PuntuacionesAdapter(Activity activity, Vector<String> puntuaciones)
	{
		this.activity = activity;
		this.puntuaciones = puntuaciones;

	}

	@Override
	public int getCount()
	{
		int counter = 0;
		if (this.puntuaciones != null)
		{
			counter = this.puntuaciones.size();
		}
		return counter;
	}

	@Override
	public Object getItem(int position)
	{
		String value = null;
		if(this.puntuaciones != null)
		{
			value = this.puntuaciones.elementAt(position);
		}
		
		return value;
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{

		LayoutInflater mInflater = (LayoutInflater) activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		
		convertView = mInflater.inflate(R.layout.elemento_lista, null);
		TextView textView = (TextView) convertView.findViewById(R.id.titulo_elemento);
		textView.setText(puntuaciones.elementAt(position));
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
	
	@Override
	public boolean areAllItemsEnabled()
	{
		// TODO Auto-generated method stub
		return super.areAllItemsEnabled();
	}

}
