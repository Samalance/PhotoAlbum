package com.example.photoalbumandroid67;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.NoSuchElementException;

import android.content.Context;
import android.widget.Toast;

public class AlbumList {
	
	private static AlbumList albumList = null;
	private ArrayList<String> albumNames;
	private int maxId;
	private Context context;
	
	//FILENAME is name of albumfile
	private AlbumList()
	{
		albumNames = new ArrayList<String>();
		maxId = -1;
	}
	public static AlbumList getInstance(Context ctx) throws IOException
	{
		if (albumList == null)
		{
			albumList = new AlbumList();
			albumList.context = ctx;
			albumList.load();
		}
		return albumList;
	}
	public ArrayList<String> getAlbums()
	{
		return albumNames;
	}
	public String add(String albumname)
	{
		if(albumNames.size() == 0)
		{
			try 
			{
				albumNames.add(albumname);
				store();
			}
			catch(IOException e)
			{
				Toast.makeText(context, "Could not store song to file", 
							   Toast.LENGTH_LONG).show();
				
			}
		}
		else {
			int lo=0, hi=albumNames.size()-1, mid=-1, c=0;
			while (lo <= hi) {
				mid = (lo+hi)/2;
				c = albumname.compareTo(albumNames.get(mid));
				if (c == 0) {  // duplicate name
					break;
				}
				if (c < 0) {
					hi = mid-1;
				} else {
					lo = mid+1;
				}
			}
			int pos = c <= 0 ? mid : mid+1;
			albumNames.add(pos,albumname);
			try{
				store();
			}
			catch (IOException e)
			{
				Toast.makeText(context, "Could not store song to file", 
						   Toast.LENGTH_LONG).show();
			}
			return albumname;
		}
		return albumname;
	}
	public int getPos(String albumname)
	{
		if(albumNames.size() == 0)
		{
			return -1;
		}
		int lo=0, hi=albumNames.size()-1;
		
		while (lo <= hi) 
		{
			int mid = (lo+hi)/2;
			String name = albumNames.get(mid);
			int c = albumname.compareTo(name);
				if (c == 0) {
					return mid;
				}
				// check left
				int i=mid-1;
				while (i >= 0) {
					name = albumNames.get(i);
					if (albumname.compareTo(name) == 0) {
						return i;
					}
					i--;
				}
				// check right
				i = mid+1;
				while (i < albumNames.size()) {
					name = albumNames.get(i);
					if (albumname.compareTo(name) == 0) {
						return i;
					}
					i++;
				}
			if (c < 0) {
				hi = mid-1;
			} else {
				lo = mid+1;
			}
		}
		return -1;
	}
	public void renameAlbum(String albumName, String newAlbumName)
	{
		int pos = getPos(albumName);
		if(pos == -1)
		{
			throw new NoSuchElementException();
		}
		String prevName = albumNames.get(pos);
		albumNames.remove(pos);
		albumNames.add(newAlbumName);
		//albumNames.remove(pos);
		try
		{
			store();
		}
		catch (IOException e)
		{
			Toast.makeText(context, 
					"Could not store songs to file", 
					Toast.LENGTH_LONG).show();
		}
		
	}
	public ArrayList<String> remove(String albumname) throws NoSuchElementException
	{
		int pos = getPos(albumname);
		if (pos == -1)
		{
			throw new NoSuchElementException();
		}
		albumNames.remove(pos);
		try
		{
			store();
		}
		catch (IOException e)
		{
			Toast.makeText(context, 
					"Could not store songs to file", 
					Toast.LENGTH_LONG).show();
		}
		return albumNames;
	}
	public void setContext(Context ctx)
	{
		context = ctx;
	}
	public void store() throws IOException
	{
		FileOutputStream fos = 
			context.openFileOutput(PhotoAlbum.FILENAME, Context.MODE_PRIVATE);
		PrintWriter pw = new PrintWriter(fos);
		
		for (String albumName : albumNames)
		{
			pw.println(albumName);
		}
	}
	public void load() throws IOException
	{
		 int maxId = -1;
		 try
		 {
			 FileInputStream fis =
					 context.openFileInput(PhotoAlbum.FILENAME);
			 BufferedReader br = new BufferedReader(
					 					new InputStreamReader(fis));
			 String name;
			 while((name = br.readLine()) != null)
			 {
				 maxId++;
				 albumNames.add(name);
			 }
			 this.maxId = maxId;
			 fis.close();
				 
		 }
		 catch (FileNotFoundException e)
		 {
				Toast.makeText(context, "Could not load song from file", 
						   Toast.LENGTH_LONG).show();
		 }
	}
	public ArrayList<String> update(String name) throws NoSuchElementException
	{
		for(int i = 0; i < albumNames.size(); ++i)
		{
			if(albumNames.get(i) == name)
			{
				try {
					store();
				}
				catch (IOException e)
				{
					Toast.makeText(context, 
							"Could not store songs to file", 
							Toast.LENGTH_LONG).show();
				}
				return albumNames;
			}
		}
		throw new NoSuchElementException();
	}
	
}
