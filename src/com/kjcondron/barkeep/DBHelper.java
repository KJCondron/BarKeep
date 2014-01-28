package com.kjcondron.barkeep;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Pair;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class DBHelper extends SQLiteAssetHelper  {
	
	private static final String DATABASE_NAME = "barkeep";
	private static final int DATABASE_VERSION = 1;
	public static final String NOT_FOUND = "NOT_FOUND";

	public DBHelper(Context ctxt) {
		super(ctxt, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	private String makeOptional(String name)
	{
		return "select distinct -1 as _id, ' Other' as " + name + " union ";
	}
	
	public Cursor getBrands( String tableName) throws Exception
	{
		return getBrands(tableName,false);
	}
	
	public Cursor getBrands( String tableName, Boolean includeOther ) throws Exception
	{
		try{
			SQLiteDatabase db = getReadableDatabase();
	        
			String optionSql = includeOther ? makeOptional("brand") : "";
			
			// hacky sql to return [_id, brand] columns
	        String sql = optionSql + "select distinct _id, brand from " + tableName + " Group by brand Order by brand";
	        
	        Cursor c2 = db.rawQuery(sql, null);
	   
	        c2.moveToFirst();
	        return c2;
		}
		catch(Exception e)
		{
			MainActivity.log_exception(e, "getBrands");
			throw e;
		}
	}
	
	public Cursor getProducts( String tableName, String brand) throws Exception
	{
		return getProducts(tableName, brand, false);
	}
	
	public Cursor getProducts( String tableName, String brand, Boolean includeOther  ) throws Exception
	{
		try{
			SQLiteDatabase db = getReadableDatabase();
			
			String optionSql = includeOther ? makeOptional("product_name") : "";
			String sql =  optionSql + "select _id, product_name from " + 
		    		tableName + " where brand=\"" + brand +
		    		"\" group by product_name";
		    Cursor c = db.rawQuery(sql, null);
		    
		    c.moveToFirst();
		    return c;
		}
		catch(Exception e)
		{
			MainActivity.log_exception(e, "getProducts");
			throw e;
		}
	}
	
	public Cursor getSizes( String tableName, String brand, String product) throws Exception
	{
		return getSizes(tableName, brand, product, false);
	}
	
	public Cursor getSizes( String tableName, String brand, String product, Boolean includeOther  ) throws Exception
	{
		try{
			SQLiteDatabase db = getReadableDatabase();
			String optionSql = includeOther ? makeOptional("size") : "";
	        String sql =  optionSql + "select _id, size from " + tableName + " where brand=\"" +
	        		brand + "\" and product_name=\"" + product +"\" group by size";
	        Cursor c = db.rawQuery(sql, null);
	        c.moveToFirst();
	        return c;
		}
		catch(Exception e)
		{
			MainActivity.log_exception(e, "getSizes");
			throw e;
		}
	}
	
	public Cursor getInventory( int barID ) throws Exception
	{
		try{
			SQLiteDatabase db = getReadableDatabase();
	        String sql = "select * from Inventory where bar_id=" + barID;
	        Cursor c = db.rawQuery(sql, null);
	        c.moveToFirst();
	        return c;
		}
		catch(Exception e)
		{
			MainActivity.log_exception(e, "getInvenory");
			throw e;
		}
	}
	
	public Pair< String, Cursor > getFromUPC( String upc ) throws Exception
	{
		// look up product by UPC in global product tables
		try
		{
			SQLiteDatabase db = getReadableDatabase();
			
			// data is currently stored in multiple tables.
			// and the have to match the categories hard coded. 
			// to-do fix that maybe.
			
			String [] cats = ProductDetailActivity.categories;
			for(String c : cats)
			{
				String sql = "select * from " +
						c + 
						" where upc='" + upc + "'";
				
				Cursor c2 = db.rawQuery(sql, null);
				if(c2.getCount() > 0)
					return new Pair<String, Cursor>(c, c2);
			}
		}
		catch(Exception e)
		{
			MainActivity.log_exception(e, "getFromUPC");
			throw e;
		}
		
		throw new Exception(NOT_FOUND);
	}
	
	// helper here as we don't have idiomatic use of Option[..]
	// to keep this code together
	public Boolean UPCExsits( String upc ) throws Exception
	{
		try{
			getFromUPC(upc);
		}
		catch(Exception e){
			return false;
		}
		return true;
	}
	
	public void writeInventory(
			Integer barId,
			String type,
			String brand,
			String name,
			String size,
			double amount)
	{
		SQLiteDatabase dbw = getWritableDatabase();
		
		String rcSQL = "select count(*) from Inventory";
		Cursor c = dbw.rawQuery(rcSQL, null);
		c.moveToFirst();
		Integer rowID = c.getInt(0)+1;
		
		String sql = "select * from " + type + " where brand=\"" +
        		brand + "\" and product_name=\"" + name + "\" and size=\"" +
				size + "\"";
        
		Cursor item = dbw.rawQuery(sql, null);
		item.moveToFirst();
		String upc = item.getString(item.getColumnIndex("upc"));
		String ean = item.getString(item.getColumnIndex("ean"));
		
		ContentValues values = new ContentValues();
		values.put("rowid", rowID);
		values.put("_id", rowID);
		values.put("bar_id", barId);
		values.put("type", type);
		values.put("product_name", name);
		values.put("brand", brand);
		values.put("upc", upc);
		values.put("ean", ean);
		values.put("size", size);
		values.put("amt", amount);
		
		dbw.insert("Inventory", "product_name", values);
		 
		//dbw.execSQL(sql)
	}

}
