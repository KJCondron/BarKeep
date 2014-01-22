package com.kjcondron.barkeep;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;

public class DBHelper extends SQLiteAssetHelper  {
	
	private static final String DATABASE_NAME = "barkeep";
	private static final int DATABASE_VERSION = 1;

	public DBHelper(Context ctxt) {
		super(ctxt, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	public Cursor getBrands( String tableName )
	{
		SQLiteDatabase db = getReadableDatabase();
        //SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        //String [] sqlSelect = {"0 _id", "brand"};
       
        //qb.setTables(tableName);
        
        // hacky sql to return [_id, brand] columns
        String sql = "select distinct _id, brand from " + tableName + " Group by brand Order by brand";
        
        Cursor c2 = db.rawQuery(sql, null);
        
        //Cursor c = qb.query(db, sqlSelect, null, null,
        //                null, null, null);

        //c.moveToFirst();
        c2.moveToFirst();
        return c2;
	}
			
	public Cursor getProducts( String tableName, String brand )
	{
		SQLiteDatabase db = getReadableDatabase();
		String sql = "select _id, product_name from " + 
        		tableName + " where brand=\"" + brand +
        		"\" group by product_name";
        Cursor c = db.rawQuery(sql, null);
        
        c.moveToFirst();
        return c;
	}
	
	public Cursor getSizes( String tableName, String brand, String product )
	{
		SQLiteDatabase db = getReadableDatabase();
        String sql = "select _id, size from " + tableName + " where brand=\"" +
        		brand + "\" and product_name=\"" + product +"\" group by size";
        Cursor c = db.rawQuery(sql, null);
        c.moveToFirst();
        return c;
	}
	
	public Cursor getInventory( int barID )
	{
		SQLiteDatabase db = getReadableDatabase();
        String sql = "select * from Inventory where bar_id=" + barID;
        Cursor c = db.rawQuery(sql, null);
        c.moveToFirst();
        return c;
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
