package com.kjcondron.barkeep;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.support.v4.app.NavUtils;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.annotation.TargetApi;
import android.database.Cursor;
import android.os.Build;

public class UseActivity extends Activity {

	private Boolean gridView = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Show the Up button in the action bar.
		setupActionBar();
		
		setupView(gridView);
				
	}
	
	protected void setupView(Boolean gv)
	{
		AbsListView v = gv ? getGridView() : getListView(); 
		try
		{
			SimpleCursorAdapter invAdapter = getInvetory();
			v.setAdapter(invAdapter);
		}
		catch(Exception e)
		{
			
		}
	}
	
	
	protected AbsListView getListView()
	{
		setContentView(R.layout.layout_inventory_list_view);
		return (AbsListView) findViewById(R.id.listview);
	}
	
	protected AbsListView getGridView()
	{
		setContentView(R.layout.layout_inventory_grid_view);
		return (AbsListView) findViewById(R.id.gridview);
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
		case R.id.viewactionitem:
			gridView = !gridView;
			setupView(gridView);
			item.setTitle(gridView ? "List View" : "Grid View");
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	protected SimpleCursorAdapter getInvetory() throws Exception
	{
		int barID=1;
		String[] coulmnNames = new String[]{ "brand", "product_name", "size", "amt" };
	    DBHelper db = new DBHelper(this);
	    Cursor c = db.getInventory(barID);
	    c.moveToFirst();
	    SimpleCursorAdapter invAdapter = new SimpleCursorAdapter(
	    		this, 
	    		R.layout.layout_inventory_item,
	    		c, 
	    		coulmnNames,
	    		new int[] { R.id.textView1, R.id.textView2,R.id.textView3,R.id.textView4 },
	    		CursorAdapter.NO_SELECTION );
	    
	    return invAdapter;
	}
	

}
