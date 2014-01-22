package com.kjcondron.barkeep;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

public class ProductDetailActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_product_detail);
		// Get the message from the intent
	    Intent intent = getIntent();
	    String product = intent.getStringExtra(AddActivity.PRODUCT_TYPE);

	    Spinner typeSpinner = (Spinner) findViewById(R.id.spinner1);
	    
	    String[] choices = {"Whisky","Rum","Gin","Vodka","Tequila"};
	    
	    ArrayAdapter<CharSequence> adapter = 
	    		new ArrayAdapter<CharSequence>(
	    				this,
	    				android.R.layout.simple_spinner_item);
	    
	    adapter.add(product);
	    for(String ci : choices) {
	    	if(ci != product)
	    		adapter.add(ci);
	    }
	    
	    setupBrandSpinner(product);
	    
	    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    typeSpinner.setAdapter(adapter);
	}
	
	protected void onStart()
	{
		super.onStart();
		Spinner typeSpinner = (Spinner) findViewById(R.id.spinner1);
	    
		typeSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
	    @Override
	    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
	        // your code here
	    	TextView tv = (TextView) selectedItemView;
	    	String type = tv.getText().toString();
	    	TextView txt = (TextView) findViewById(R.id.textView3);	
		    txt.setText(type);
		    
		    setupBrandSpinner(type);
		    
	    }

	    @Override
	    public void onNothingSelected(AdapterView<?> parentView) {
	        // your code here
	    }

		});
	}
		
	protected void setupBrandSpinner(String product)
	{
		Spinner brands = (Spinner) findViewById(R.id.spinner2);
	    
	    DBHelper db = new DBHelper(this);
	    Cursor c = db.getBrands(product);
	    
	    ArrayAdapter<CharSequence> brandArrayAdapter = 
	    		new ArrayAdapter<CharSequence>(
	    				this,
	    				android.R.layout.simple_spinner_item);
	    
	    c.moveToFirst();
	    do{
	    	String s = c.getString(1);
	    	brandArrayAdapter.add(s);
	    }while(c.moveToNext());
	    
	    String[] names = c.getColumnNames();
	    int i = c.getCount();
	    
	    SimpleCursorAdapter brandAdapter = new SimpleCursorAdapter(
	    		this, 
	    		android.R.layout.simple_spinner_dropdown_item,
	    		c, 
	    		new String[]{ "brand" },
	    		new int[] { android.R.id.text1 },
	    		CursorAdapter.NO_SELECTION );
	    
	    brands.setAdapter(brandAdapter);
	    
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.product_detail, menu);
		return true;
	}

}
