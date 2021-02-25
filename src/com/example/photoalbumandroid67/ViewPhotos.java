package com.example.photoalbumandroid67;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.GridLayout;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;

public class ViewPhotos extends ActionBarActivity {
	
	public static final int SELECT_PHOTO = 5;
	public static final int MOVE_PHOTO = 7;
	private ArrayList<Bitmap> myPhotos = new ArrayList<Bitmap>();
	private ArrayList<Photo> photoList = new ArrayList<Photo>();
	private ArrayList<String> currentAlbum = new ArrayList<String>();
	private ImageAdapter adapter;
	
	
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.viewphotos);
    	GridView gridview = (GridView) findViewById(R.id.grid_view);
    	
    	openDB();
    	
    	Toast.makeText(this, "AlbumName: " + getAlbumName(), Toast.LENGTH_SHORT).show();
    	
    	currentAlbum = getCurrentAlbums();
    	getArrayLists();
    	getBitmaps();
    	adapter = new ImageAdapter(this, photoList);
    	gridview.setAdapter(adapter);
    	registerForContextMenu(gridview);
    	gridview.setOnItemClickListener(new OnItemClickListener() {
    		public void onItemClick(AdapterView<?> parent, View v, int postion, long id) {
    		
    		Intent intent = new Intent(getApplicationContext(), SlideShow.class);
    		Bundle bundle = new Bundle();
    		bundle.putInt("currentpos", postion);
    		bundle.putString("albumName", getAlbumName());
    		intent.putExtras(bundle);
    		startActivity(intent);
    			
    		}
    	});
    	
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu,v,menuInfo);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.view_album_menu, menu);  	
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
		GridView gridView = (GridView)findViewById(R.id.grid_view);
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
        Photo key = (Photo)gridView.getItemAtPosition(info.position);
        switch (item.getItemId()) 
        {
			case R.id.remove_photo:
				photoList.remove(info.position);
				PhotoAlbum.myDb.deleteRow(key.getID());
				adapter.notifyDataSetChanged();
				break;
			case R.id.move_photo:
				//photoList.remove(info.position);
				Intent intent = new Intent(this, MoveToAlbum.class);
				Bundle bundle = new Bundle();
				bundle.putStringArrayList("currentAlbums", currentAlbum);
				bundle.putInt("pos",info.position);
				intent.putExtras(bundle);
				startActivityForResult(intent, MOVE_PHOTO);
				adapter.notifyDataSetChanged();
        }
        return super.onContextItemSelected(item);
       }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) 
        {
        	return;
        }
        Bundle bundle = data.getExtras();
        	if (requestCode == MOVE_PHOTO) 
        	{
        		
        		String newAlbumName = bundle.getString(MoveToAlbum.NEWALBUM);
        		int pos = bundle.getInt(MoveToAlbum.CURRPOSITION);;
        		Photo pic = photoList.get(pos);
        		PhotoAlbum.myDb.updateRow(pic.getID(),
        					newAlbumName, 
        					pic.getPhotoName(),
        					getBytes(pic.getPicture()),
        					pic.getLocation(),
        					pic.getPerson());
        		photoList.remove(pos);
        		adapter.notifyDataSetChanged();
        	}
        	if (requestCode == SELECT_PHOTO) {
            	String temp = bundle.getString("albumName");
                Uri selectedImageUri = data.getData();
               // Toast.makeText(getApplicationContext(), "Found image\n" + selectedImageUri.toString(),
                //		Toast.LENGTH_SHORT).show();
                
                
				try 
				{
					InputStream imageStream;
					imageStream = getContentResolver().openInputStream(selectedImageUri);
					Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
					Photo pic = new Photo(1,getAlbumName(), 
											selectedImageUri.toString(),
											selectedImage,
											"null", "null");
											
					photoList.add(pic);						
					PhotoAlbum.myDb.insertRow(getAlbumName(), selectedImageUri.toString(),
							getBytes(selectedImage), "null", "null");
					//getArrayLists();
					//getBitmaps();
					adapter.notifyDataSetChanged();
					
				} 
				catch (FileNotFoundException e) 
				{
	                Toast.makeText(getApplicationContext(), "Error: File not Found",
	               		Toast.LENGTH_SHORT).show();
					e.printStackTrace();
				}
            }
    }
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
	// Inflate the menu; this adds items to the action bar if it is present.
    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.menu.albumview, menu);
	return true;
    }

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		//ListView listv = (ListView)findViewById(R.id.ListPhotos);
		//String key = listv.getItemAtPosition(info.position).toString();
		//Toast.makeText(getApplicationContext(), "Selected Album "+ key, Toast.LENGTH_SHORT).show();
		
		switch(item.getItemId())
		{
			case R.id.add_photo:
	        	Bundle bundle = new Bundle();
	        	String temp = bundle.getString("albumName");
	        	bundle.putString("albumName", temp);
				Intent intent = new Intent();
				intent.putExtras(bundle);
				intent.setType("image/*");
				//.ACTION_GET_CONTENT
				intent.setAction(Intent.ACTION_PICK);
				startActivityForResult(
						Intent.createChooser(intent, "Select Picture"), SELECT_PHOTO);
				return true;
			case R.id.remove_photo:
				return true;	
		}
		return true;
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
	private ArrayList<String> getCurrentAlbums()
	{
    	Bundle bundle = getIntent().getExtras();
    	ArrayList<String> temp = bundle.getStringArrayList("currentAlbums");
    	return temp;
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
				location = cursor.getString(4);
				person = cursor.getString(5);
				
				
				//Toast.makeText(this, "id: " + id +
				//					 "\nalbumName: " + albumName
				//					 + "\nphotoName: " + photoId
				//					 + "\nperson: " + person
				//					 + "\nlocation: " + location,
				//					 Toast.LENGTH_SHORT).show();
				Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
				if(albumName.compareTo(getAlbumName()) == 0 )
				{
					Photo temp = new Photo(id, albumName,photoId,bitmap,location,person);
					photoList.add(temp);
				}
									
			} while(cursor.moveToNext());
		}
		
	}
    public String getPath(Uri uri) {
        // just some safety built in 
        if( uri == null ) {
            return null;
        }
        // try to retrieve the image from the media store first
        // this will only work for images selected from gallery
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if( cursor != null ){
            int column_index = cursor
            .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        // this is our fallback here
        return uri.getPath();
    }
    public static byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }

    // convert from byte array to bitmap
    public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }
}

