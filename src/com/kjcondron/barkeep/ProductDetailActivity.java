package com.kjcondron.barkeep;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.os.Bundle;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Pair;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class ProductDetailActivity extends Activity {
	
	public final static String ADD_TO_DB = "com.kjcondron.barkeep.ADD_TO_DB";
	public final static String UPC = "com.kjcondron.barkeep.UPC";
	public final static String[] categories = {"Whisky","Rum","Gin","Vodka","Tequila"};
	public final static Integer IDCOL = 0;
	public final static Integer TYPENAMECOL = 1;
	public final static Integer BARID = 1;
	
	private Boolean mAddToDB = false;
	private Boolean mHaveUPC = false;
	
	private void createAllProducts()
	{
		Intent intent = getIntent();
		String type = intent.getStringExtra(AddActivity.PRODUCT_TYPE);
				
	    ActionBar ab = getActionBar();
		ab.setTitle("Select Product");
		
	    setupListeners();
		setupTypeSpinner(type); // triggers other spinners
		
		findViewById(R.id.prodDetail_commitItem).setVisibility(View.VISIBLE);
		findViewById(R.id.prodDetail_commitItemAddToProducts).setVisibility(View.INVISIBLE);
	}
	
	private void createSingleProduct()
	{

		try{
			Spinner typeSpinner = (Spinner) findViewById(R.id.prodDetail_typeSpinner);
			Spinner brandSpinner = (Spinner) findViewById(R.id.prodDetail_brandSpinner);
			Spinner productSpinner = (Spinner) findViewById(R.id.prodDetail_prodSpinner);
			Spinner sizeSpinner = (Spinner) findViewById(R.id.prodDetail_sizeSpinner);
			
			Intent intent = getIntent();
			
			ArrayAdapter<CharSequence> adapter = 
		    		new ArrayAdapter<CharSequence>(
		    				this,
		    				android.R.layout.simple_spinner_item);
		    
	    	String upc = intent.getStringExtra(UPC);
	    	
	    	// pair because due to (bad) db design type is the name of the 
	    	// table not in the cursor (to-do could be fixed with a view)
	    	Cursor upcDeets = new DBHelper(this).getFromUPC(upc); 
	    	//adapter.add(upcDeets.first);
	    	
	    	adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		    typeSpinner.setAdapter(adapter);
		    
		    setupSpinner(upcDeets, "product_type", typeSpinner); 
		    setupSpinner(upcDeets, "brand", brandSpinner);
		    setupSpinner(upcDeets, "product_name", productSpinner);
		    setupSpinner(upcDeets, "size", sizeSpinner);
		    
		    findViewById(R.id.prodDetail_commitItem).setVisibility(View.VISIBLE);
			findViewById(R.id.prodDetail_commitItemAddToProducts).setVisibility(View.INVISIBLE);
		
		    
		}
    	catch(Exception e)
    	{
    		MainActivity.log_exception(e, "ProductDetailActivity.onCreateAllSingleProduct");
    	}
    
	}
	
	// to-do fix this first
	private void createProductNotFound()
	{
		setContentView(R.layout.activity_product_detail);
		ActionBar ab = getActionBar();
		ab.setTitle("Product Not Found");
		
	    setupListeners();
	    setupTypeSpinner(categories[0]);
	    
	    findViewById(R.id.prodDetail_commitItem).setVisibility(View.INVISIBLE);
		findViewById(R.id.prodDetail_commitItemAddToProducts).setVisibility(View.VISIBLE);
	
	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_product_detail);
		
		// This activity can be use to display
		
		// a) All products in db, with first spinner set up to show current category
		// b) specific product found in db (1 per spinner / should be text boxes)
		// c) an not found upc with editable spinners to allow selection / entry of detail = ADD_TO_DB 
		
		Intent intent = getIntent();
	    mAddToDB = intent.getBooleanExtra(ADD_TO_DB, false);
	    mHaveUPC = intent.hasExtra(UPC);
	    
	    if(mHaveUPC)
	    	if(mAddToDB)
	    		createProductNotFound();
	    	else
	    		createSingleProduct();
	    else
	    	createAllProducts();
	    
	}
	
	private String getType()
	{
		Spinner typeSpinner = (Spinner) findViewById(R.id.prodDetail_typeSpinner);
		Cursor ctype = (Cursor)typeSpinner.getSelectedItem();
    	return ctype.getString(TYPENAMECOL);
	}
	
	private Integer getTypeId()
	{
		Spinner typeSpinner = (Spinner) findViewById(R.id.prodDetail_typeSpinner);
		Cursor ctype = (Cursor)typeSpinner.getSelectedItem();
    	return ctype.getInt(IDCOL);
	}
	
	protected void setupListeners()
	{
		Spinner typeSpinner = (Spinner) findViewById(R.id.prodDetail_typeSpinner);
	    
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
		
		Spinner brandSpinner = (Spinner) findViewById(R.id.prodDetail_brandSpinner);
		
		brandSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
		    @Override
		    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
		    	if(selectedItemView != null)
		    	{
			    	TextView tv = (TextView) selectedItemView;
			    	String brand = tv.getText().toString();
			    	setupProdSpinner(getType(), brand);
		    	}
		    }

		    @Override
		    public void onNothingSelected(AdapterView<?> parentView) {
		        // your code here
		    }

			});
		
		Spinner prodSpinner = (Spinner) findViewById(R.id.prodDetail_prodSpinner);
		
		prodSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
		    @Override
		    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
		    	
		    	Spinner typeSpinner = (Spinner) findViewById(R.id.prodDetail_typeSpinner);
		    	Spinner bs = (Spinner) findViewById(R.id.prodDetail_brandSpinner);
		    	
		    	if(selectedItemView != null && 
		    			typeSpinner.getSelectedItem() != null &&
		    			bs.getSelectedItem() != null)
		    	{
			    	Cursor c = (Cursor)bs.getSelectedItem();
			    	String brand = c.getString(c.getColumnIndex("brand"));
			    	TextView tv = (TextView) selectedItemView;
			    	String prod= tv.getText().toString();
			    	setupSizeSpinner(getType(), brand, prod);
		    	}
		    }

		    @Override
		    public void onNothingSelected(AdapterView<?> parentView) {
		        // your code here
		    }

			});
	    
	}
	
	protected void setupTypeSpinner(String type)
	{
		try
		{
			Spinner typeSpinner = (Spinner) findViewById(R.id.prodDetail_typeSpinner);

			DBHelper db = new DBHelper(this);
		    Cursor c = db.getTypes();
		    
		    SimpleCursorAdapter adapter = new SimpleCursorAdapter(
		    		this, 
		    		android.R.layout.simple_spinner_dropdown_item,
		    		c, 
		    		new String[]{ "product_type" },
		    		new int[] { android.R.id.text1 },
		    		CursorAdapter.NO_SELECTION );
		 
		    typeSpinner.setAdapter(adapter);
		    
		    for(int i=0; i<adapter.getCount(); ++i)
		    {
		    	SQLiteCursor cur = (SQLiteCursor)adapter.getItem(i);
		    	String val = cur.getString(TYPENAMECOL);
		    	if(val.equals(type))
		    	{
		    		typeSpinner.setSelection(i);
		    		break;
		    	}
		    }
		    
		}
		catch(Exception e)
		{
			MainActivity.log_exception(e, "setupTypeSpinner");
		}
	    
	}
		
	protected void setupBrandSpinner(String product)
	{
		try
		{
			Spinner brands = (Spinner) findViewById(R.id.prodDetail_brandSpinner);
			
		    DBHelper db = new DBHelper(this);
		    Cursor c = db.getBrands(product, mAddToDB);
		    
		    setupSpinner(c, "brand", brands);
		}
		catch(Exception e)
		{
			MainActivity.log_exception(e, "setupBrandSpinner");
		}
	    
	}
		
	protected void setupProdSpinner(String type, String brand)
	{
		try
		{
			Spinner prods = (Spinner) findViewById(R.id.prodDetail_prodSpinner);
		    
		    DBHelper db = new DBHelper(this);
		    Cursor c = db.getProducts(type, brand, mAddToDB);
		    
		    setupSpinner(c, "product_name", prods);
		    	    	    
		}
		catch(Exception e){
			MainActivity.log_exception(e, "setupProdSpinner");
		}
	    
	}
	
	protected void setupSizeSpinner(String type, String brand, String product)
	{
		try
		{
			Spinner size = (Spinner) findViewById(R.id.prodDetail_sizeSpinner);
		    
		    DBHelper db = new DBHelper(this);
		    Cursor c = db.getSizes(type, brand, product, mAddToDB);
		    	    	    
		    setupSpinner(c, "size", size);
		}
		catch(Exception e)
		{
			MainActivity.log_exception(e, "setupSizeSpinner");
		}
	    
	}
	
	protected void setupSpinner(Cursor details, String columnName, Spinner spinner)
	{
		try
		{
		    SimpleCursorAdapter adapter = new SimpleCursorAdapter(
		    		this, 
		    		android.R.layout.simple_spinner_dropdown_item,
		    		details, 
		    		new String[]{ columnName },
		    		new int[] { android.R.id.text1 },
		    		CursorAdapter.NO_SELECTION );
		    
		    spinner.setAdapter(adapter);
		}
		
		catch(Exception e)
		{
			MainActivity.log_exception(e, "setupSpinner");
		}
	    
	}
	
	public void commitItem(View view)
	{
		DBHelper db = new DBHelper(this);
		
		db.writeInventory(
				BARID,
				getSpinnerValueInt(R.id.prodDetail_prodSpinner, "_id"),
				1.0);
		
		finish();    	
	}
	
	public void commitItemAddToProducts(View view) throws Exception
	{
		DBHelper db = new DBHelper(this);
		
		db.writeInventory(
				BARID,
				getSpinnerValueInt(R.id.prodDetail_prodSpinner, "_id"),
				1.0);
		
		String upc = getIntent().getStringExtra(UPC);
		db.writeProduct(
				getTypeId(),
				getSpinnerValue(R.id.prodDetail_brandSpinner, "brand"),
				getSpinnerValue(R.id.prodDetail_prodSpinner, "product_name"),
				getSpinnerValue(R.id.prodDetail_sizeSpinner, "size"),
				upc);
		
		finish();
	}
	
	private String getSpinnerValue(int id, String columnName)
	{
		Spinner spin = (Spinner) findViewById(id);
    	Cursor c = (Cursor)spin.getSelectedItem();
	    return c.getString(c.getColumnIndex(columnName));
	}
	
	private Integer getSpinnerValueInt(int id, String columnName)
	{
		Spinner spin = (Spinner) findViewById(id);
    	Cursor c = (Cursor)spin.getSelectedItem();
	    return c.getInt(c.getColumnIndex(columnName));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.product_detail, menu);
		return true;
	}

}
