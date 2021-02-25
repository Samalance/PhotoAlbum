package com.example.photoalbumandroid67;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class DeleteAlbum extends Activity {
	
	public static final String ALBUMNAME = "albumname";
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.deletealbum);
        
        Bundle bundle = this.getIntent().getExtras();
		ArrayList<String> tempList = bundle.getStringArrayList("currentAlbums");
		
		Spinner spinner = (Spinner) findViewById(R.id.AlbumSpinner);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				this,android.R.layout.simple_spinner_dropdown_item,tempList);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		
		
    }
    public void deleteAlbum(View v) 
	{
			String temp = "";
			Spinner spinner = (Spinner) findViewById(R.id.AlbumSpinner);
			temp = spinner.getSelectedItem().toString();
			Bundle bundle2 = new Bundle();
			bundle2.putString(ALBUMNAME, temp);
			
			Toast.makeText(this, 
					"Deleted Album "+ temp, 
					Toast.LENGTH_LONG).show();	
			
			Intent intent = new Intent();
			intent.putExtras(bundle2);
			setResult(RESULT_OK,intent);
			finish();
	}

}
