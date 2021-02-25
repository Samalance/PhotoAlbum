package com.example.photoalbumandroid67;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
 
public class ImageAdapter extends BaseAdapter{

private Context mContext;
private List<Photo> pics;


public ImageAdapter(Context c)
{
	mContext = c;
}
public ImageAdapter(Context c, List<Photo> pics) {
    mContext = c;
    this.pics = pics;
}

@Override
public int getCount() {
    return pics.size();
}

@Override
public Photo getItem(int position) {
    return pics.get(position);
}

@Override
public long getItemId(int position) {
    return 0;
}

// create a new ImageView for each item referenced by the Adapter
@Override
public View getView(int position, View convertView, ViewGroup parent) {
    ImageView imageView;
    if (convertView == null) {  // if it's not recycled, initialize some attributes
        imageView = new ImageView(mContext);
        imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setPadding(8, 8, 8, 8);
    } else {
        imageView = (ImageView) convertView;
    }
    Photo temp = pics.get(position); 
    Bitmap pic = temp.getPicture();
    imageView.setImageBitmap(pic);
    return imageView;
}


}
