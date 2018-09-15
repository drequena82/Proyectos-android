package org.example.asteroides.data;

import java.util.ArrayList;

import org.example.asteroides.beans.Puntuacion;
import org.example.asteroides.interfaces.AlmacenPuntuaciones;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AlmacenPuntuacionesBaseDatos extends SQLiteOpenHelper implements
		AlmacenPuntuaciones
{
	private int num_puntuaciones = 10;

	public AlmacenPuntuacionesBaseDatos(Context context)
	{
		super(context, "puntuaciones", null, 1);
		SharedPreferences pref = context.getSharedPreferences(
				"org.example.asteroides_preferences", Context.MODE_PRIVATE);
		num_puntuaciones = Integer.valueOf(pref.getString("numPuntuaciones",
				"10"));
	}

	@Override
	public void onCreate(SQLiteDatabase db)
	{
		db.execSQL("CREATE TABLE puntuaciones ("
				+ "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ "puntos INTEGER, nombre TEXT, fecha TEXT)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		/* En caso de una nueva versión habría que actualizar las tablas */
	}

	@Override
	public void guardarPuntuacion(int puntos, String nombre, String fecha)
	{
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL("INSERT INTO puntuaciones VALUES ( null, " + puntos + ", '"
				+ nombre + "', '" + fecha + "')");
	}

	public ArrayList<Puntuacion> listaPuntuaciones()
	{
		ArrayList<Puntuacion> result = new ArrayList<Puntuacion>();
		Puntuacion puntuacion = null;
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.rawQuery(
				"SELECT puntos, nombre,fecha FROM puntuaciones ORDER BY puntos DESC LIMIT "
						+ num_puntuaciones, null);
		while (cursor.moveToNext())
		{
			puntuacion = new Puntuacion();
			puntuacion.setPuntuacion(cursor.getInt(0));
			puntuacion.setNombre(cursor.getString(1));
			puntuacion.setFecha(cursor.getString(2));
			result.add(puntuacion);
		}
		cursor.close();

		return result;
	}
}
