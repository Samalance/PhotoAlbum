package com.example.photoalbumandroid67;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class MoveToAlbum extends Activity {

	public static final String NEWALBUM = "newAlbumname";
	public static final String CURRPOSITION = "currentPosition";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movetoalbum);
        
        Bundle bundle = this.getIntent().getExtras();
		ArrayList<String> tempList = bundle.getStringArrayList("currentAlbums");
		
		Spinner spinner = (Spinner) findViewById(R.id.Album_Spinne);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				this,android.R.layout.simple_spinner_dropdown_item,tempList);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
    }
    public void movePicture(View v)
    {
		String temp = "";
		int pos = getPos();
		Spinner spinner = (Spinner) findViewById(R.id.Album_Spinne);
		temp = spinner.getSelectedItem().toString();
		Intent intent = new Intent();
		Bundle bundle2 = new Bundle();
		bundle2.putInt(CURRPOSITION, pos);
		bundle2.putString(NEWALBUM, temp);
		
		
		Toast.makeText(this, 
				"Moving Picture to Album: "+ temp
				+"pos: " + pos, 
				Toast.LENGTH_SHORT).show();	
		
		intent.putExtras(bundle2);
		setResult(RESULT_OK,intent);
		finish();
    }
    public int getPos()
    {
    	Bundle bundle = this.getIntent().getExtras();
    	int pos = bundle.getInt("pos");
    	return pos;
    }
}
