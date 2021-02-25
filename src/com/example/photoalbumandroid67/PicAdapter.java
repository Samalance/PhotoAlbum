package com.example.photoalbumandroid67;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

public class PicAdapter extends BaseAdapter {
	
	int defaultItemBackground;
	private Context context;
	private ArrayList<Bitmap> myPics;
	
	public PicAdapter(Context c)
	{
		
	}
	public PicAdapter(Context c, ArrayList<Bitmap> pics)
	{
		context = c;
		myPics = pics;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return myPics.size();
	}
	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return myPics.get(position);
	}
	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ImageView imageview = new ImageView(context);
		imageview.setImageBitmap(myPics.get(position));
		imageview.setLayoutParams(new Gallery.LayoutParams(225,200));
		imageview.setBackgroundResource(defaultItemBackground);
		imageview.setScaleType(ImageView.ScaleType.FIT_CENTER);
		return imageview;
	}
}
