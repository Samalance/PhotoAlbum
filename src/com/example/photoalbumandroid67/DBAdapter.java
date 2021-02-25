package com.example.photoalbumandroid67;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBAdapter {

	public static final String DATABASE_NAME = "photoAlbum";
	public static final String KEY_ROWID = "_id";
	public static final int COLUMN_ROWID = 0;
	public static final String ALBUMNAME = "AlbumName";
	public static final String PHOTO_ID = "PhotoName";
	public static final String PHOTO_BITMAP = "PhotoBitmap";
	public static final String LOCATION_TAG = "Location";
	public static final String PERSON_TAG = "Person";
	
	public static final String[] ALL_KEYS = new String[] {KEY_ROWID,
		ALBUMNAME, PHOTO_ID, PHOTO_BITMAP, LOCATION_TAG, PERSON_TAG};
	
	private static final String DATABASE_CREATE = "CREATE TABLE "
			+ DATABASE_NAME + " ( " +
			KEY_ROWID + " INTEGER PRIMARY KEY, " 
			+ ALBUMNAME + " TEXT, "
			+ PHOTO_ID + " TEXT, " 
			+ PHOTO_BITMAP + " BLOB, "
			+ LOCATION_TAG + " TEXT, " 
			+ PERSON_TAG + " TEXT )";
	
	private final Context context;
	private DatabaseHelper myDBHelper;
	private SQLiteDatabase database;
	
	public DBAdapter(Context ctx)
	{
		this.context = ctx;
		myDBHelper = new DatabaseHelper(context);
	}
	public DBAdapter open() {
		database = myDBHelper.getWritableDatabase();
		return this;
	}
	public void close() {
		myDBHelper.close();
	}
	public long insertRow(String albumName, String photoId, byte[] image, String location,
						String person)
	{
		ContentValues initialValues = new ContentValues();
		initialValues.put(ALBUMNAME, albumName);
		initialValues.put(PHOTO_ID, photoId);
		initialValues.put(PHOTO_BITMAP, image);
		initialValues.put(LOCATION_TAG, location);
		initialValues.put(PERSON_TAG, person);
		
		
		return database.insert(DATABASE_NAME, null, initialValues);
	}
	public boolean deleteRow(long rowId)
	{
		String where = KEY_ROWID + "=" + rowId;
		return database.delete(DATABASE_NAME, where, null) > 0;
	}
	public void deleteAll()
	{
		Cursor c = getAllRows();
		long rowId = c.getColumnIndexOrThrow(KEY_ROWID);
		if(c.moveToFirst()) {
			do 
			{
				deleteRow(c.getLong((int) rowId));
			}
			while(c.moveToNext());
		}
		c.close();
	}
	public Cursor getAllRows() {
		String where = null;
		Cursor c = 	database.query(true, DATABASE_NAME, ALL_KEYS, 
							where, null, null, null, null, null);
		if (c != null) {
			c.moveToFirst();
		}
		return c;
	}
	public Cursor getRow(long rowId) {
		String where = KEY_ROWID + "=" + rowId;
		Cursor c = 	database.query(true, DATABASE_NAME, ALL_KEYS, 
						where, null, null, null, null, null);
		if (c != null) {
			c.moveToFirst();
		}
		return c;
	}
	public boolean updateRow(long rowId, String albumName, String photoId, byte[] image,
							String location, String person) {
		String where = KEY_ROWID + "=" + rowId;

		ContentValues newValues = new ContentValues();
		newValues.put(ALBUMNAME, albumName);
		newValues.put(PHOTO_ID, photoId);
		newValues.put(PHOTO_BITMAP, image);
		newValues.put(LOCATION_TAG, location);
		newValues.put(PERSON_TAG, person);
		
		// Insert it into the database.
		return database.update(DATABASE_NAME, newValues, where, null) != 0;
	}
	private static class DatabaseHelper extends SQLiteOpenHelper
	{
		DatabaseHelper(Context context) 
		{
			super(context, DATABASE_NAME, null, 1);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(DATABASE_CREATE);			
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			String query = "DROP TABLE IF EXISTS " + DATABASE_NAME;
			
			db.execSQL(query);
			onCreate(db);
			
		}
	}
	
}
