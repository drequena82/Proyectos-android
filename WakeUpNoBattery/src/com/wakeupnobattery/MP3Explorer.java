package com.wakeupnobattery;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MP3Explorer extends ListActivity
{

	private List<String> item = null;
	private List<String> path = null;
	private String root = "/sdcard";
	private TextView myPath;
	private File file;
	private static final String TYPE_FILE = "mp3";
	private Ringtone rtm;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.explorer_layout);
		myPath = (TextView) findViewById(R.id.path);
		/**/
		String state = Environment.getExternalStorageState();
		if (!Environment.MEDIA_MOUNTED.equals(state))
		{
			this.root = "/";
		}
		getDir(root);
	}

	private void getDir(String dirPath)
	{
		myPath.setText("Location: " + dirPath);
		item = new ArrayList<String>();
		path = new ArrayList<String>();

		File f = new File(dirPath);
		File[] files = f.listFiles();

		// this.checkFile(file.getName(), MP3Explorer.TYPE_FILE);

		if (!dirPath.equals(root))
		{

			item.add(root);
			path.add(root);
			item.add("../");
			path.add(f.getParent());

		}

		for (int i = 0; i < files.length; i++)
		{
			File file = files[i];
			if (file.isDirectory())
			{
				path.add(file.getPath());
				item.add(file.getName() + "/");
			} else
			{
				if (this.checkFile(file.getName(), MP3Explorer.TYPE_FILE))
				{
					path.add(file.getPath());
					item.add(file.getName());
				}
			}
		}

		ArrayAdapter<String> fileList = new ArrayAdapter<String>(this,
				R.layout.row, item);
		setListAdapter(fileList);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id)
	{

		file = new File(path.get(position));

		if (file.isDirectory())
		{
			if (file.canRead())
			{
				getDir(path.get(position));
			}
		} else
		{
			 new AlertDialog.Builder(this)
					.setIcon(R.drawable.icon)
					.setTitle(
							getString(R.string.label_explorerMessage) + " "
									+ file.getName() + " ?")
					.setPositiveButton(getString(R.string.ok_button),
							new DialogInterface.OnClickListener()
							{

								@Override
								public void onClick(DialogInterface dialog,
										int which)
								{
									Intent intent = new Intent();
									intent.putExtra(
											OptionsActivity.EXTRA_MP3FILE,
											file.getPath());
									setResult(OptionsActivity.LOADER_CODE,
											intent);
									finish();
								}
							})
					.setNegativeButton(getString(R.string.cancel_button),
							new DialogInterface.OnClickListener()
							{

								@Override
								public void onClick(DialogInterface dialog,
										int which)
								{
									// TODO Auto-generated method stub
									dialog.cancel();
								}
							})
					.setNeutralButton(getString(R.string.play_button),
							new DialogInterface.OnClickListener()
							{

								@Override
								public void onClick(DialogInterface dialog,
										int which)
								{
									if (rtm == null)
									{
										rtm = RingtoneManager.getRingtone(
												getApplicationContext(),
												Uri.fromFile(file));
									}
									if (rtm.isPlaying())
									{
										rtm.stop();
										rtm = null;

									} else
									{
										rtm.play();
									}
								}
							}).show();
		}
	}

	/**
	 * Método que comprueba si el fichero es un mp3
	 * 
	 * @param nameFile
	 * @param typeFile
	 * @return
	 */
	private boolean checkFile(String nameFile, String typeFile)
	{
		boolean isValid = false;
		String matching = "^.+\\.(" + typeFile + ")$";
		if (nameFile != null)
		{
			isValid = nameFile.toLowerCase().matches(matching);
		}
		return isValid;
	}

	@Override
	protected void onStop()
	{

		super.onStop();

	}

	@Override
	protected void onDestroy()
	{
		// TODO Auto-generated method stub
		super.onDestroy();
	}
}
