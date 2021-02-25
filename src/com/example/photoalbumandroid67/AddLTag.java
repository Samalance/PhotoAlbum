package com.example.photoalbumandroid67;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class AddLTag extends Activity{
	
	public static final String LTAG_KEY = "ltag";
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addltag);
    }
    public void addLTag(View v) 
   	{
    	EditText text = (EditText)findViewById(R.id.ltag_text);
    	String temp;
    	temp = text.getText().toString();
    	
		if(temp == null || temp.length() == 0)
		{
			Toast.makeText(this, "Tag cannot be left empty!",
					Toast.LENGTH_LONG).show();
			return;
		}
		
		Intent intent = new Intent();
		Bundle bundle = new Bundle();
		bundle.putString(LTAG_KEY, temp);
		intent.putExtras(bundle);
		
		setResult(RESULT_OK, intent);
		finish();
   	}
}
