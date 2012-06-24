package com.mymusic;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.media.MediaPlayer;

public class MyMusicActivity extends ListActivity
{
	private List<String> files = new ArrayList<String>();
	private MediaPlayer mp = new MediaPlayer();
	File sdcard = Environment.getExternalStorageDirectory();
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.main);
		if(!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
			Toast.makeText(this, "Oops! SD Card is unmount.", Toast.LENGTH_SHORT).show();
		} else {
			File allFiles = new File(sdcard, "Music/");
			searchingFile(allFiles);
			setListAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,files));
		}
	}

	public void searchingFile(File path) {
		if(path.isDirectory()){
			String[] subNode = path.list();
			for(String filename : subNode) {
				searchingFile(new File(path, filename));
			}
		} else {
			files.add(path.getAbsoluteFile().toString());
		}
	}

	public void onListItemClick(ListView parent, View v, int position, long id)
	{
		try {
			FileDescriptor fd = null;
			String audioPath = files.get(position);
			FileInputStream fis = new FileInputStream(audioPath);
			fd = fis.getFD();
			mp.reset();
			mp.setDataSource(fd);
			mp.prepare();
			mp.start();
			Toast.makeText(this, "Now Playing " + files.get(position), Toast.LENGTH_SHORT).show();
		} catch (IOException e) {
			Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
		}
	}
}