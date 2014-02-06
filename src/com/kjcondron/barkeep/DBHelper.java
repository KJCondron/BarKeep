package com.kjcondron.barkeep;

import java.text.MessageFormat;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class DBHelper extends SQLiteAssetHelper  {
	
	private static final String DATABASE_NAME = "barkeep";
	private static final int DATABASE_VERSION = 1;
	public static final String NOT_FOUND = "NOT_FOUND";
	private Context m_context;
	
	public DBHelper(Context ctxt) {
		super(ctxt, DATABASE_NAME, null, DATABASE_VERSION);
		m_context = ctxt;
	}
	
	private String makeOptional(String name)
	{
		return "select distinct -1 as _id, ' Other' as " + name + " union ";
	}
	
	public Cursor getBrands( String tableName) throws Exception
	{
		return getBrands(tableName,false);
	}
	
	public Boolean haveBar()
	{
		SQLiteDatabase db = getReadableDatabase();
        
		String sql = "select * from Bars";    
        Cursor c = db.rawQuery(sql, null);
        c.moveToFirst();
        return c.getCount() > 0;
	}
	
	public Cursor getTypes() throws Exception
	{
		try{
			SQLiteDatabase db = getReadableDatabase();
	        
			String sql ="select * from Types";
	        
	        Cursor c2 = db.rawQuery(sql, null);
	   
	        c2.moveToFirst();
	        return c2;
		}
		catch(Exception e)
		{
			MainActivity.log_exception(e, "getTypes");
			throw e;
		}
	}
	
	public Cursor getBrands( String type, Boolean includeOther ) throws Exception
	{
		try{
			SQLiteDatabase db = getReadableDatabase();
	        
			String optionSql = includeOther ? makeOptional("brand") : "";
			
	        //String sql = optionSql + "select distinct _id, brand from products" + tableName + " Group by brand Order by brand";
	        
	        String sql = optionSql + "select _id, brand from vProducts " +
	        		"where product_type=\"" + type + 
	        		"\" group by brand";
	        
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
	
	public Cursor getProducts( String type, String brand, Boolean includeOther  ) throws Exception
	{
		try{
			SQLiteDatabase db = getReadableDatabase();
			
			String optionSql = includeOther ? makeOptional("product_name") : "";
			
	        String sql = optionSql + "select _id, product_name from vProducts " +
	        		"where product_type=\"" + type + 
	        		"\" and brand=\"" + brand + "\" group by product_name";
	        
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
	
	public Cursor getSizes( String type, String brand, String product, Boolean includeOther  ) throws Exception
	{
		try{
			SQLiteDatabase db = getReadableDatabase();
			String optionSql = includeOther ? makeOptional("size") : "";
	        String sql =  optionSql + "select _id, size from vProducts " +
					"where product_type=\"" + type + 
					"\" and brand=\"" + brand + "\" and product_name=\"" + product + 
					"\" group by size";
	        
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
	        String sql = "select * from vInventory where bar_id=" + barID;
	        Cursor c = db.rawQuery(sql, null);
	        c.moveToFirst();
	        return c;
		}
		catch(Exception e)
		{
			MainActivity.log_exception(e, "getInventory");
			throw e;
		}
	}
	
	public Cursor getShoppingList( int barID ) throws Exception
	{
		try{
			SQLiteDatabase db = getReadableDatabase();
	        String sql = "select * from vShoppingList where bar_id=" + barID;
	        Cursor c = db.rawQuery(sql, null);
	        c.moveToFirst();
	        return c;
		}
		catch(Exception e)
		{
			MainActivity.log_exception(e, "getShoppingList");
			throw e;
		}
	}
	
	public Cursor getFromUPC( String upc ) throws Exception
	{
		// look up product by UPC in global product tables
		try
		{
			SQLiteDatabase db = getReadableDatabase();
			
			// data is currently stored in multiple tables.
			// and the have to match the categories hard coded. 
			// to-do fix that maybe.
			
			String sql = MessageFormat.format("Select * from vProducts where upc=\"{0}\" ", upc);
			Cursor c = db.rawQuery(sql, null);
			if(c.getCount() > 0)
				return c;
			
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
			Integer prodId,
			double quantity)
	{
		SQLiteDatabase dbw = getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put("bar_id", barId);
		values.put("product_id", prodId);
		values.put("quantity", quantity);
		
		dbw.insert("Inventory", "product_name", values);
		 
	}
	
	public void writeProduct(
			Integer typeId,
			String brand,
			String name,
			String size,
			String UPC )
	{
		SQLiteDatabase dbw = getWritableDatabase();
					
			ContentValues values = new ContentValues();
			values.put("product_key", typeId);
			values.put("product_name", name);
			values.put("brand", brand);
			values.put("upc", UPC);
			values.put("ean", "1111");
			values.put("size", size);
			
			dbw.insert("Products", "", values);
			
			try{
				if(UPCExsits(UPC))
					Toast.makeText(m_context, "UPC Exisits", Toast.LENGTH_SHORT).show();
				else
					Toast.makeText(m_context, "UPC Still Missing!", Toast.LENGTH_SHORT).show();
			}
			catch(Exception e)
			{
				Toast.makeText(m_context, "Exception Finding UPC", Toast.LENGTH_SHORT).show();
			}
			
	}
	
	public int updateQuantity(Integer invId)
	{
		String sql = "select * from vInventory where _id=" + invId;
		SQLiteDatabase db = getReadableDatabase();
        
		Cursor c = db.rawQuery(sql, null);
        c.moveToFirst();
         
        Double quantity = c.getDouble(c.getColumnIndex("quantity"));
        
        SQLiteDatabase dbw = getWritableDatabase();
        
        ContentValues values = new ContentValues();
        double newQ = quantity-0.25;
        values.put("quantity", newQ);
        
        int prodId = -1;
        try
        {
        	if( newQ <= 0.0 )
        	{
        		String sql2 = "select product_id from Inventory where _id=" + invId;
        		Cursor c2 = db.rawQuery(sql2, null);
                c2.moveToFirst();
                prodId = c2.getInt(0);
                dbw.delete("Inventory", "_id="+invId, null);
        	}
        	else
        		dbw.update("Inventory", values, "_id="+invId, null);
        	
        	return prodId;
        }
        catch(Exception e)
        {
        	Toast.makeText(m_context, e.getMessage(), Toast.LENGTH_SHORT).show();
        	return -1; // fix me
        }		
	}
	
	public void addToShopping(int prodID, int barId)
	{
		ContentValues values = new ContentValues();
	    values.put("bar_id", barId);
	    values.put("product_id", prodID);
		
	    SQLiteDatabase dbw = getWritableDatabase();
		dbw.insert("ShoppingList", "", values);
	}
	
	public void removeFromShopping(int iid)
	{
		SQLiteDatabase dbw = getWritableDatabase();
		dbw.delete("ShoppingList", "_id=" + iid, null);
	}
	
	
	public void newBar(String barName)
	{
		ContentValues values = new ContentValues();
	    values.put("name", barName);
		
	    SQLiteDatabase dbw = getWritableDatabase();
		dbw.insert("Bars", "", values);
	}
	
	public void clearBars()
	{
		getWritableDatabase().execSQL("delete from Bars");
	}
	
	public Cursor getBars() throws Exception
	{
		try{
			SQLiteDatabase db = getReadableDatabase();
	        
			String sql ="select * from Bars";
	        Cursor c2 = db.rawQuery(sql, null);
	   
	        c2.moveToFirst();
	        return c2;
		}
		catch(Exception e)
		{
			MainActivity.log_exception(e, "getBars");
			throw e;
		}
	}
}
