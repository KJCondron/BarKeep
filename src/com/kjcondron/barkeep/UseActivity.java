package com.kjcondron.barkeep;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Spinner;
import android.widget.TextView;
import android.support.v4.app.NavUtils;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.annotation.TargetApi;
import android.database.Cursor;
import android.os.Build;

public class UseActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_use);
		// Show the Up button in the action bar.
		setupActionBar();
		setupInvSpinner();
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.use, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	protected void setupInvSpinner()
	{
		int barID=1;
		Spinner inv = (Spinner) findViewById(R.id.spinInventory);
		TextView tv = (TextView) findViewById(R.id.textCount);
	    
	    DBHelper db = new DBHelper(this);
	    Cursor c = db.getInventory(barID);
	    c.moveToFirst();
	    SimpleCursorAdapter invAdapter = new SimpleCursorAdapter(
	    		this, 
	    		android.R.layout.simple_spinner_dropdown_item,
	    		c, 
	    		new String[]{ "product_name" },
	    		new int[] { android.R.id.text1 },
	    		CursorAdapter.NO_SELECTION );
	    
	    int count = c.getCount();
	    String str = "Items=" + count;
	    tv.setText(str);
	    
	    inv.setAdapter(invAdapter);
	    
	}

}
