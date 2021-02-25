package com.example.photoalbumandroid67;

import java.util.ArrayList;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Gallery;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class SlideShow extends ActionBarActivity {
	
	public static final int REMOVE_PHOTO = 6;
	public static final int ADD_PTAG = 6;
	public static final int ADD_LTAG = 8;
	
	private ArrayList<Bitmap> myPhotos = new ArrayList<Bitmap>();
	private ArrayList<Photo> photoList = new ArrayList<Photo>();
	Gallery gallery;
	PicAdapter imgAdapter;
	
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.slideshowview);
    	openDB();
    	getArrayLists();
    	getBitmaps();
    	Bundle bundle = getIntent().getExtras();
    	int pos = bundle.getInt("currentpos");
    	gallery = (Gallery)findViewById(R.id.image_gallary);
    	imgAdapter = new PicAdapter(this, myPhotos);
    	gallery.setAdapter(imgAdapter);
    	gallery.setSelection(pos);
    	gallery.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
    		public void onItemSelected(AdapterView<?> parent, View v, int postion, long id) {
    		
    		TextView textview = (TextView)findViewById(R.id.photo_info);
    		Photo temp = photoList.get(postion);
    		String info = temp.displayTags();
    		textview.setText(info);
    		}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
    	});
    	//textview.setText(gallery.getId());
    }
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.slideshowoptions, menu);
		return true;
	}
    public boolean onOptionsItemSelected(MenuItem item) 
	{
    	int pos = gallery.getSelectedItemPosition();
    	Photo pic = photoList.get(pos);
    	TextView textview = (TextView)findViewById(R.id.photo_info);
    	Intent intent;
		switch(item.getItemId())
		{	
			case R.id.remove_persontag:
				pic.setPerson("null");
				PhotoAlbum.myDb.updateRow(pic.getID(),
										pic.getAlbumName(),
										pic.getPhotoName(),
										ViewPhotos.getBytes(pic.getPicture()),
										pic.getLocation(),
										"null");
				textview.setText(pic.displayTags());
				break;
			case R.id.remove_locationtag:
				pic.setLocation("null");
				PhotoAlbum.myDb.updateRow(pic.getID(),
										pic.getAlbumName(),
										pic.getPhotoName(),
										ViewPhotos.getBytes(pic.getPicture()),
										"null",
										pic.getPerson());
				textview.setText(pic.displayTags());
				break;
			case R.id.add_persontag:
				intent = new Intent(this, AddPTag.class);
				startActivityForResult(intent,ADD_PTAG);
				break;
			case R.id.add_locationtag:
				intent = new Intent(this, AddLTag.class);
				startActivityForResult(intent,ADD_LTAG);
				break;
		
		}
		return super.onContextItemSelected(item);
	}
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		//super.onActivityResult(requestCode, resultCode, intent);
		if(resultCode != RESULT_OK)
		{
			return;
		}
		Bundle bundle = intent.getExtras();
    	int pos = gallery.getSelectedItemPosition();
    	Photo pic = photoList.get(pos);
		if(bundle == null)
		{
			return;
		}
		if (requestCode == ADD_PTAG)
		{
			String tag = bundle.getString(AddPTag.PTAG_KEY);
			Toast.makeText(this, "new tag: " + tag, Toast.LENGTH_SHORT).show();
			pic.setPerson(tag);
			PhotoAlbum.myDb.updateRow(pic.getID(),
									pic.getAlbumName(),
									pic.getPhotoName(),
									ViewPhotos.getBytes(pic.getPicture()),
									pic.getLocation(),
									tag);
			TextView textview = (TextView)findViewById(R.id.photo_info);
			textview.setText(pic.displayTags());
		}
		if (requestCode == ADD_LTAG)
		{
			String tag = bundle.getString(AddLTag.LTAG_KEY);
			Toast.makeText(this, "new tag: " + tag, Toast.LENGTH_SHORT).show();
			pic.setLocation(tag);
			PhotoAlbum.myDb.updateRow(pic.getID(),
									pic.getAlbumName(),
									pic.getPhotoName(),
									ViewPhotos.getBytes(pic.getPicture()),
									tag,
									pic.getPerson());
			TextView textview = (TextView)findViewById(R.id.photo_info);
			textview.setText(pic.displayTags());
		}
	}
	public void getBitmaps()
	{
		Toast.makeText(this, "Length: " + photoList.size(), Toast.LENGTH_SHORT).show();
		for(int i = 0; i<photoList.size(); ++i)
		{
			Photo temp = photoList.get(i);
			Bitmap pic = temp.getPicture();
			myPhotos.add(pic);
		}
		
	}
	private String getAlbumName()
	{
    	Bundle bundle = getIntent().getExtras();
    	String temp = bundle.getString("albumName");
    	return temp;
	}
	protected void onDestroy() {
		super.onDestroy();	
		//closeDB();
	}
	public void getArrayLists()
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
				person = cursor.getString(4);
				location = cursor.getString(5);
				
				
				//Toast.makeText(this, "id: " + id +
				//					 "\nalbumName: " + albumName
				//					 + "\nphotoName: " + photoId
				//					 + "\nperson: " + person
				//					 + "\nlocation: " + location,
				//					 Toast.LENGTH_SHORT).show();
				Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
				if(albumName.compareTo(getAlbumName()) == 0 )
				{
					Photo temp = new Photo(id, albumName,photoId,bitmap,person,location);
					photoList.add(temp);
				}
									
			} while(cursor.moveToNext());
		}
		
	}
	public void RefreshArrayLists()
	{
		photoList.clear();
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
				person = cursor.getString(4);
				location = cursor.getString(5);
				
				
				//Toast.makeText(this, "id: " + id +
				//					 "\nalbumName: " + albumName
				//					 + "\nphotoName: " + photoId
				//					 + "\nperson: " + person
				//					 + "\nlocation: " + location,
				//					 Toast.LENGTH_SHORT).show();
				Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
				if(albumName.compareTo(getAlbumName()) == 0 )
				{
					Photo temp = new Photo(id, albumName,photoId,bitmap,person,location);
					photoList.add(temp);
				}
									
			} while(cursor.moveToNext());
		}
		
	}
	private void openDB() {
		PhotoAlbum.myDb = new DBAdapter(this);
		PhotoAlbum.myDb.open();
	}
	private void closeDB() {
		PhotoAlbum.myDb.close();
	}
}
