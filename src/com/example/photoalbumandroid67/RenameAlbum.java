package com.example.photoalbumandroid67;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class RenameAlbum extends Activity {
	
	public static final String ALBUMS = "currentAlbums";
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.renamealbum);
    }
    public void renameAlbum(View v) 
	{
    		EditText text = (EditText)findViewById(R.id.RenameText);
    		Bundle bundle = this.getIntent().getExtras();
			String temp = bundle.getString("albumName");
			ArrayList<String> tempList = bundle.getStringArrayList("currentAlbums");
			String newName;
			
			newName = text.getText().toString();
			if(newName == null || newName.length() == 0)
			{
				Toast.makeText(this, "Unable to make empty Album\nInput a valid name",
						Toast.LENGTH_LONG).show();
				return;
			}
			else
			{
				for(int i = 0; i<tempList.size(); ++i)
				{
					String albumtemp = tempList.get(i);
					if(albumtemp.compareTo(newName) == 0)
					{
						Toast.makeText(this, "Unable to make album with same name",
								Toast.LENGTH_LONG).show();
						return;
					}
				}
			}
			Bundle bundle2 = new Bundle();
			bundle2.putString("previousName", temp);
			bundle2.putString("currentName", newName);
						
			Intent intent = new Intent();
			intent.putExtras(bundle2);
			setResult(RESULT_OK,intent);
			finish();
	}
    
}