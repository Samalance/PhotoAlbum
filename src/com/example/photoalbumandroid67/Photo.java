package com.example.photoalbumandroid67;

import java.util.ArrayList;

import android.graphics.Bitmap;

public class Photo {
	
	private int photoId;
	private String albumName;
	private String photoName;
	private Bitmap picture;
	private String locationTag;
	private String personTag;

	public Photo(int id, String albumname, String photoname, Bitmap picture,
				String location, String person)
	{
		photoId = id;
		albumName = albumname;
		photoName = photoname;
		this.picture = picture;
		personTag = person;
		locationTag = location;
	}
	public int getID()
	{
		return photoId;
	}
	public String getAlbumName()
	{
		return albumName;
	}
	public String getPhotoName()
	{
		return photoName;
	}
	public Bitmap getPicture()
	{
		return picture;
	}
	public String getLocation()
	{
		return locationTag;
	}
	public String getPerson()
	{
		return personTag;
	}
	public void setAlbumName(String albumname)
	{
		albumName = albumname;
	}
	public void setPhotoName(String photoname)
	{
		photoName = photoname;
	}
	public void setPicture(Bitmap pic)
	{
		picture = pic;
	}
	public void setLocation(String loc)
	{
		locationTag = loc;
	}
	public void setPerson(String person)
	{
		personTag = person;
	}


	public String displayTags()
	{
/*		if(personTag.compareTo("null") == 0 || locationTag.compareTo("null") == 0)
		{
				return 	"Photo Name: " + photoName
						+"\nPerson Tag: " 
						+"\nLocation Tag: " ;
		}
		else if(personTag.compareTo("null") == 0)
		{
			return 	"Photo Name: " + photoName
					+"\nPerson Tag: " 
					+"\nLocation Tag: " + locationTag;
		}
		else if(locationTag.compareTo("null") == 0)
		{
			return 	"Photo Name: " + photoName
					+"\nPerson Tag: " + personTag
					+"\nLocation Tag: ";
		}
		else*/
		return 	"Photo Name: " + photoName
				+"\nPerson Tag: " + personTag
				+"\nLocation Tag: " + locationTag;
	}
	public String toString()
	{
		return "ID: " + photoId
				+"\nPhoto Name: " + photoName
				+"\nAlbum Name: " + albumName
				+"\nPerson Tag: " + personTag
				+"\nLocation Tag: " + locationTag;
	}
}
