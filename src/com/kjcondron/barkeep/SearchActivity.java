package com.kjcondron.barkeep;

import android.app.SearchManager;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.CursorAdapter;
import android.widget.SimpleCursorAdapter;

public class SearchActivity extends DisplayActivity {

	private String mQuery;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    handleIntent(getIntent());
	    super.onCreate(savedInstanceState);
	}
	
	@Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

	private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            mQuery = intent.getStringExtra(SearchManager.QUERY);
        }
    }
	
	protected SimpleCursorAdapter getInvetory(DBHelper db, int itemLayoutID) throws Exception
	{
		String[] coulmnNames = new String[]{ "brand", "product_name", "size", "product_type" };
	    Cursor c = db.searchBrands(mQuery, MainActivity.BARID);
	    c.moveToFirst();
	    
	    InventoryAdapter invAdapter = new InventoryAdapter(
	    		this, 
	    		itemLayoutID,
	    		c, 
	    		coulmnNames,
	    		new int[] { R.id.textView1, R.id.textView2,R.id.textView3,R.id.itemImageView },
	    		CursorAdapter.NO_SELECTION,
	    		R.id.progressBar1);
	    
	   
	    return invAdapter;
	}
	
}
