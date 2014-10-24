package com.kjcondron.barkeep;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

public class SearchActivity extends Activity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	
	    //setContentView(R.layout.layout_search);
	    
	    Toast.makeText(this, "search activity started ", Toast.LENGTH_LONG).show();
	    
	    handleIntent(getIntent());
	    
	}
	
	@Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

	
	public void search(View view) {
		
		TextView srchField = (TextView) findViewById(R.id.searchField);
		String srchVal = srchField.getText().toString();
		
		Toast.makeText(this, "searched for " + srchVal, Toast.LENGTH_LONG).show();
		finish();
	}
	
	private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            //use the query to search your data somehow
            Toast.makeText(this, "search intent " + query, Toast.LENGTH_LONG).show();
    	    
        }
        
        finish();
    }


}
