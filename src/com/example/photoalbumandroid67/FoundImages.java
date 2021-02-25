package com.example.photoalbumandroid67;

import java.util.ArrayList;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.GridView;

public class FoundImages extends Activity {

	private ArrayList<Photo> myPhotos = new ArrayList<Photo>();
	private ImageAdapter adapter;
	
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.viewphotos);
    	
    	Bundle bundle = this.getIntent().getExtras();
    	String search = bundle.getString(SearchTags.SEARCH_REQUEST);
    	int isPer = bundle.getInt("isPer");
    	GridView gridview = (GridView) findViewById(R.id.grid_view);
    	if(isPer == 0)
    		getArrayListsloc(search);
    	else
    		getArrayListsper(search);
    	adapter = new ImageAdapter(this, myPhotos);
    	gridview.setAdapter(adapter);
    	openDB();
    	

    	
    }	
	public void getArrayListsloc(String search)
	{
		Cursor cursor = PhotoAlbum.myDb.getAllRows();
		int id;
		String albumName;
		String photoId;
		Bitmap picture;
		byte[] image;
		String location;
		String person;
		
		if(cursor.moveToFirst())
		{
			do {
				id = cursor.getInt(0);
				albumName = cursor.getString(1);
				photoId = cursor.getString(2);
				image = cursor.getBlob(3);
				location = cursor.getString(4);
				person = cursor.getString(5);
				
				
				//Toast.makeText(this, "id: " + id +
				//					 "\nalbumName: " + albumName
				//					 + "\nphotoName: " + photoId
				//					 + "\nperson: " + person
				//					 + "\nlocation: " + location,
				//					 Toast.LENGTH_SHORT).show();
				Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
				if(location.contains(search))
				{
					Photo temp = new Photo(id, albumName,photoId,bitmap,location,person);
					myPhotos.add(temp);
				}
			} while(cursor.moveToNext());
		}
	}
	public void getArrayListsper(String search)
	{
		Cursor cursor = PhotoAlbum.myDb.getAllRows();
		int id;
		String albumName;
		String photoId;
		Bitmap picture;
		byte[] image;
		String location;
		String person;
		
		if(cursor.moveToFirst())
		{
			do {
				id = cursor.getInt(0);
				albumName = cursor.getString(1);
				photoId = cursor.getString(2);
				image = cursor.getBlob(3);
				location = cursor.getString(4);
				person = cursor.getString(5);
				
				
				//Toast.makeText(this, "id: " + id +
				//					 "\nalbumName: " + albumName
				//					 + "\nphotoName: " + photoId
				//					 + "\nperson: " + person
				//					 + "\nlocation: " + location,
				//					 Toast.LENGTH_SHORT).show();
				Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
				if(person.contains(search))
				{
					Photo temp = new Photo(id, albumName,photoId,bitmap,location,person);
					myPhotos.add(temp);
				}
			} while(cursor.moveToNext());
		}
	}
    protected void onDestroy() {
		super.onDestroy();	
		//closeDB();
	}
	private void openDB() {
		PhotoAlbum.myDb = new DBAdapter(this);
		PhotoAlbum.myDb.open();
	}
	private void closeDB() {
		PhotoAlbum.myDb.close();
	}
}
