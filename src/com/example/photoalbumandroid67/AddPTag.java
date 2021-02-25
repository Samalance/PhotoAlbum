package com.example.photoalbumandroid67;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


public class AddPTag extends Activity{
	
	public static final String PTAG_KEY = "ptag";
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addtag);
    }
    public void addPTag(View v) 
   	{
    	EditText text = (EditText)findViewById(R.id.ptag_text);
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
		bundle.putString(PTAG_KEY, temp);
		intent.putExtras(bundle);
		
		setResult(RESULT_OK, intent);
		finish();
   	}
}
