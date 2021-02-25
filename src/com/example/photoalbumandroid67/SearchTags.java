package com.example.photoalbumandroid67;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class SearchTags extends Activity {
	
		public static final String SEARCH_REQUEST = "searchRequest";
		
		@Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.searchlayout);
	    }
	    public void searchTags(View v)
	    {
	    	EditText text = (EditText)findViewById(R.id.search_text);
	    	String temp = text.getText().toString();
			if(temp == null || temp.length() == 0)
			{
				Toast.makeText(this, "Unable to search blank input",
						Toast.LENGTH_LONG).show();
				return;
			}
			Bundle bundle = new Bundle();
			Intent  intent = new Intent();
			bundle.putString(SEARCH_REQUEST, temp);
			intent.putExtras(bundle);
			setResult(RESULT_OK,intent);
			finish();
	    }

	}

