package com.kjcondron.barkeep;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Pair;
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
	
	public final static String ADD_TO_DB = "com.kjcondron.barkeep.ADD_TO_DB";
	public final static String UPC = "com.kjcondron.barkeep.UPC";
	
	private Boolean mAddToDB = false;
	private Boolean mHaveUPC = false;
	private Cursor mUpcCursor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_product_detail);
		// Get the message from the intent
	    Intent intent = getIntent();
	    String type = intent.getStringExtra(AddActivity.PRODUCT_TYPE);
	    
	    mAddToDB = intent.getBooleanExtra(ADD_TO_DB, false);
	    mHaveUPC = intent.hasExtra(UPC);
	    
	    ArrayAdapter<CharSequence> adapter = 
	    		new ArrayAdapter<CharSequence>(
	    				this,
	    				android.R.layout.simple_spinner_item);
	    
	    Spinner typeSpinner = (Spinner) findViewById(R.id.spinInventory);
	    
	    if(mHaveUPC)
	    {
	    	String upc = intent.getStringExtra(UPC);
	    	Pair<String, Cursor> upcDeets = new DBHelper(this).getFromUPC(upc); 
	    	mUpcCursor = upcDeets.second;
	    	adapter.add(upcDeets.first);
	    }
	    else
	    {    
		    String[] choices = {"Whisky","Rum","Gin","Vodka","Tequila"};
		        
		    adapter.add(type);
		    for(String ci : choices) {
		    	if(ci != type)
		    		adapter.add(ci);
		    }
		    
		    setupBrandSpinner(type);
		       
	    }
	    
	    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    typeSpinner.setAdapter(adapter);
	    
	}
	
	protected void onStart()
	{
		super.onStart();
		Spinner typeSpinner = (Spinner) findViewById(R.id.spinInventory);
	    
		typeSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
	    @Override
	    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
	    	TextView tv = (TextView) selectedItemView;
	    	String type = tv.getText().toString();
	    	setupBrandSpinner(type);
	    }

	    @Override
	    public void onNothingSelected(AdapterView<?> parentView) {
	    }

		});
		
		Spinner brandSpinner = (Spinner) findViewById(R.id.brandSpinner);
		
		brandSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
		    @Override
		    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
		    	if(selectedItemView != null)
		    	{
			    	Spinner typeSpinner = (Spinner) findViewById(R.id.spinInventory);
			    	String type = typeSpinner.getSelectedItem().toString();
			    	TextView tv = (TextView) selectedItemView;
			    	String brand = tv.getText().toString();
			    	setupProdSpinner(type, brand);
		    	}
		    }

		    @Override
		    public void onNothingSelected(AdapterView<?> parentView) {
		        // your code here
		    }

			});
		
		Spinner prodSpinner = (Spinner) findViewById(R.id.prodSpinner);
		
		prodSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
		    @Override
		    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
		    	
		    	Spinner typeSpinner = (Spinner) findViewById(R.id.spinInventory);
		    	Spinner bs = (Spinner) findViewById(R.id.brandSpinner);
		    	
		    	if(selectedItemView != null && 
		    			typeSpinner.getSelectedItem() != null &&
		    			bs.getSelectedItem() != null)
		    	{
			    	String type = typeSpinner.getSelectedItem().toString();
			    	Cursor c = (Cursor)bs.getSelectedItem();
			    	String brand = c.getString(c.getColumnIndex("brand"));
			    	TextView tv = (TextView) selectedItemView;
			    	String prod= tv.getText().toString();
			    	setupSizeSpinner(type, brand, prod);
		    	}
		    }

		    @Override
		    public void onNothingSelected(AdapterView<?> parentView) {
		        // your code here
		    }

			});
	    
	}
		
	protected void setupBrandSpinner(String product)
	{
		Spinner brands = (Spinner) findViewById(R.id.brandSpinner);
	    
	    DBHelper db = new DBHelper(this);
	    Cursor c = db.getBrands(product);
	         
	    SimpleCursorAdapter brandAdapter = new SimpleCursorAdapter(
	    		this, 
	    		android.R.layout.simple_spinner_dropdown_item,
	    		c, 
	    		new String[]{ "brand" },
	    		new int[] { android.R.id.text1 },
	    		CursorAdapter.NO_SELECTION );
	    
	    brands.setAdapter(brandAdapter);
	    
	}
	
	protected void setupProdSpinner(String type, String brand)
	{
		Spinner prods = (Spinner) findViewById(R.id.prodSpinner);
	    
	    DBHelper db = new DBHelper(this);
	    Cursor c = db.getProducts(type, brand);
	    	    	    
	    SimpleCursorAdapter brandAdapter = new SimpleCursorAdapter(
	    		this, 
	    		android.R.layout.simple_spinner_dropdown_item,
	    		c, 
	    		new String[]{ "product_name" },
	    		new int[] { android.R.id.text1 },
	    		CursorAdapter.NO_SELECTION ); 
	    
	    prods.setAdapter(brandAdapter);
	    
	}
	
	protected void setupSizeSpinner(String type, String brand, String product)
	{
		Spinner size = (Spinner) findViewById(R.id.sizeSpinner);
	    
	    DBHelper db = new DBHelper(this);
	    Cursor c = db.getSizes(type, brand, product);
	    	    	    
	    SimpleCursorAdapter brandAdapter = new SimpleCursorAdapter(
	    		this, 
	    		android.R.layout.simple_spinner_dropdown_item,
	    		c, 
	    		new String[]{ "size" },
	    		new int[] { android.R.id.text1 },
	    		CursorAdapter.NO_SELECTION ); 
	    
	    size.setAdapter(brandAdapter);
	    
	}
	
	public void commitItem(View view)
	{
		DBHelper db = new DBHelper(this);
		Spinner typeSpinner = (Spinner) findViewById(R.id.spinInventory);
		
		db.writeInventory(
				1,
				typeSpinner.getSelectedItem().toString(),
				getSpinnerValue(R.id.brandSpinner, "brand"),
				getSpinnerValue(R.id.prodSpinner, "product_name"),
				getSpinnerValue(R.id.sizeSpinner, "size"),
				1.0);
		
		finish();    	
	}
	
	private String getSpinnerValue(int id, String columnName)
	{
		Spinner spin = (Spinner) findViewById(id);
    	Cursor c = (Cursor)spin.getSelectedItem();
	    return c.getString(c.getColumnIndex(columnName));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.product_detail, menu);
		return true;
	}

}
