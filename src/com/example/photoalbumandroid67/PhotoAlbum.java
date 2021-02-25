package com.example.photoalbumandroid67;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import android.support.v7.app.ActionBarActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class PhotoAlbum extends ActionBarActivity {

	ListView listView;     // list view of albumnames
    public static final String FILENAME = "albumnames.txt";   // album names read from array in strings.xml
    public static final int ADD_ALBUM_CODE = 1;
    public static final int DELETE_ALBUM_CODE = 2;
    public static final int RENAME_ALBUM_CODE = 3;
    public static final int VIEW_ALBUM_CODE = 4;
    public static final int SEARCH_BY_LOC = 8;
    public static final int SEARCH_BY_PER = 9;
	Button addButton;
	EditText editText;   
	AlbumList myAlbums;
	
	public static DBAdapter myDb;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		try {
			myAlbums = myAlbums.getInstance(this);
		}
		catch (IOException e)
		{
			Toast.makeText(this, 
					"Could not load albums from file", 
					Toast.LENGTH_LONG).show();			
		}
		listView = (ListView)findViewById(R.id.ListAlbums);
		registerForContextMenu(listView);
		
		listView.setAdapter(
				new ArrayAdapter<String>(this, R.layout.album, myAlbums.getAlbums()));
		
		myDb = new DBAdapter(this);
		myDb.open();
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();	
		myDb.close();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.photo_album, menu);
		return true;
	}
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu,v,menuInfo);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.album_context_menu, menu);
	}
	@Override
	public boolean onContextItemSelected(MenuItem item)
	{
		ListView listv = (ListView)findViewById(R.id.ListAlbums);
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		String key = listv.getItemAtPosition(info.position).toString();
		//Toast.makeText(getApplicationContext(), "Selected Album "+ key, Toast.LENGTH_SHORT).show();
		
		Bundle bundle = new Bundle();
		switch(item.getItemId())
		{
			case R.id.rename_album:
				Intent intent = new Intent(this, RenameAlbum.class);
				ArrayList<String> tempList = myAlbums.getAlbums();
				bundle.putStringArrayList("currentAlbums", tempList);
				bundle.putString("albumName", key);
				intent.putExtras(bundle);
				startActivityForResult(intent,RENAME_ALBUM_CODE);
				return true;
			case R.id.view_album:
				Intent intent2 = new Intent(this, ViewPhotos.class);
				ArrayList<String> tempList2 = myAlbums.getAlbums();
				bundle.putStringArrayList("currentAlbums", tempList2);
				bundle.putString("albumName", key);
				intent2.putExtras(bundle);
				startActivityForResult(intent2,VIEW_ALBUM_CODE);
				return true;
		
		}
		return super.onContextItemSelected(item);
		
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		
		
		switch(id) {
		case R.id.add_album:
			
			Intent intent = new Intent(this,AddAlbum.class);
			Bundle bundle = new Bundle();
			ArrayList<String> tempList = myAlbums.getAlbums();
			bundle.putStringArrayList("currentAlbums", tempList);
			intent.putExtras(bundle);
			startActivityForResult(intent,ADD_ALBUM_CODE);
			break;
		case R.id.delete_album:		
			Intent intent2 = new Intent(this,DeleteAlbum.class);
			Bundle bundle2 = new Bundle();
			ArrayList<String> tempList2 = myAlbums.getAlbums();
			bundle2.putStringArrayList("currentAlbums", tempList2);
			intent2.putExtras(bundle2);
			startActivityForResult(intent2,DELETE_ALBUM_CODE);
			break;
		case R.id.SearchbyLoc:
			Intent intent3 = new Intent(this, SearchTags.class);
			startActivityForResult(intent3,SEARCH_BY_LOC);
			break;
		case R.id.SearchbyPer:
			Intent intent4 = new Intent(this, SearchTags.class);
			startActivityForResult(intent4,SEARCH_BY_PER);
			break;
		}
		return super.onOptionsItemSelected(item);
	} 
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		//super.onActivityResult(requestCode, resultCode, intent);
		if(resultCode != RESULT_OK)
		{
			return;
		}
		Bundle bundle = intent.getExtras();
		if(bundle == null)
		{
			return;
		}
		if (requestCode == ADD_ALBUM_CODE)
		{
			String albumName = bundle.getString(AddAlbum.ALBUMNAME);
			myAlbums.add(albumName);
			Toast.makeText(this, "Made album " + albumName, Toast.LENGTH_SHORT).show();
		}
		if(requestCode == DELETE_ALBUM_CODE)
		{
			String albumName = bundle.getString(AddAlbum.ALBUMNAME);
			myAlbums.remove(albumName);
		}
		listView.setAdapter(
				new ArrayAdapter<String>(this, R.layout.album, myAlbums.getAlbums()));
		if(requestCode == RENAME_ALBUM_CODE)
		{
			String newAlbumName = bundle.getString("currentName");
			String AlbumName = bundle.getString("previousName");
			Toast.makeText(this, 
					"Renamed Album "+ AlbumName + " to " + newAlbumName, 
					Toast.LENGTH_LONG).show();
			changeAlbumName(AlbumName, newAlbumName);
			myAlbums.renameAlbum(AlbumName, newAlbumName);
		}
		if(requestCode == SEARCH_BY_LOC)
		{
			String search = bundle.getString(SearchTags.SEARCH_REQUEST);
			Intent intent2 = new Intent(this, FoundImages.class);
			Bundle bundle2 = new Bundle();
			bundle2.putString(SearchTags.SEARCH_REQUEST, search);
			bundle2.putInt("isPer", 0);
			intent2.putExtras(bundle2);
			startActivity(intent2);
		}
		if(requestCode == SEARCH_BY_PER)
		{
			String search = bundle.getString(SearchTags.SEARCH_REQUEST);
			Intent intent2 = new Intent(this, FoundImages.class);
			Bundle bundle2 = new Bundle();
			bundle2.putString(SearchTags.SEARCH_REQUEST, search);
			bundle2.putInt("isPer", 1);
			intent2.putExtras(bundle2);
			startActivity(intent2);
		}
		
	}
	public void changeAlbumName(String albumname, String newAlbumName)
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
			ArrayList<Photo> photoList = new ArrayList<Photo>();
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
				if(albumName.compareTo(albumname) == 0 )
				{
					PhotoAlbum.myDb.updateRow(id, newAlbumName, photoId, image, person, location);
				}
									
			} while(cursor.moveToNext());
		}
		
	}

}
