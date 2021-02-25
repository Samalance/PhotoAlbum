package com.example.photoalbumandroid67;

import java.util.ArrayList;

import com.example.photoalbumandroid67.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


public class AddAlbum extends Activity {
	
	public static final String ALBUMNAME = "albumname";
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addalbum);
    }
    public void addAlbum(View v) 
	{
    		Bundle bundle = this.getIntent().getExtras();
			String temp;
			ArrayList<String> tempList = bundle.getStringArrayList("currentAlbums");
			EditText text = (EditText)findViewById(R.id.addinput);
			
			
			temp = text.getText().toString();
			if(temp == null || temp.length() == 0)
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
					if(albumtemp.compareTo(temp) == 0)
					{
						Toast.makeText(this, "Unable to make album with same name",
								Toast.LENGTH_LONG).show();
						return;
					}
				}
			}
			Bundle bundle2 = new Bundle();
			bundle2.putString(ALBUMNAME, temp);
			
			Intent intent = new Intent();
			intent.putExtras(bundle2);
			setResult(RESULT_OK,intent);
			finish();
	}

}

