package com.kjcondron.barkeep;

import android.app.AlertDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

public class FullScreenPagerActivity extends FragmentActivity {
	
	private Boolean gridView = false;
	private AlertDialog.Builder mbuilder; 
	private int mItemLayoutID = R.layout.layout_inv_item_for_list;
	private MenuItem mSearchMenuItem;
	private DBHelper mdb; 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_fullscreen_pager);
		// Show the Up button in the action bar.
		mdb = new DBHelper(this);
		mbuilder = new AlertDialog.Builder(this); 
		
		setupActionBar();
		
		ViewPager pager = (ViewPager) findViewById(R.id.full_screen_pager);
		TypesScrollAdapter pagerAdapter = new TypesScrollAdapter(getSupportFragmentManager());
		pager.setAdapter(pagerAdapter);  	
		
	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.full_screen_scroll, menu);
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
	
	private class TypesScrollAdapter extends FragmentStatePagerAdapter {
		
		private int mCount; 
		
		public TypesScrollAdapter(FragmentManager fm) {
			super(fm);
			try{
				Cursor c = mdb.getTypes();
				// 1 more fragment than types for the 'all types' front page
				mCount = c.getCount() + 1;
			}
			catch(Exception e){
				mCount = 0;
			}
		}
		
		@Override
		public Fragment getItem(int pos) {
			if(pos > mCount) MainActivity.log_message(FullScreenPagerActivity.this, "pos greater than count", "TypesScrollAdapter.getItem");
			DisplayFragment frag = new UseFragment();
			/*if(0 == pos)
				bsf.setText( "All"  );
			else {
				try
				{
					Cursor c = mdb.getTypes();
					c.move(pos-1);
					bsf.setText( c.getString(c.getColumnIndex("product_type"))  );
				}
				catch(Exception e)
				{
					bsf.setText("Error");
				}
			}*/
			return frag;
		}
		
		@Override
		public int getItemPosition(Object item){
			return POSITION_NONE;
		}
	
		@Override
		public int getCount() {
			return mCount;
		}
	}
}