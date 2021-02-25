package com.example.photoalbumandroid67;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class InfoAdapter extends BaseAdapter{
	
	private Context context;
	private ArrayList<Photo> myPics;
	
	
	public InfoAdapter(Context c)
	{
		context = c;
	}
	public InfoAdapter(Context c, ArrayList<Photo> pics)
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
		
		TextView textView = new TextView(context);
		Photo temp = myPics.get(position);
		String info = temp.displayTags();
		textView.setText(info);
		return textView;
	}

}
