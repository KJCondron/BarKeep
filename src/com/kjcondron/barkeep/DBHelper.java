package com.kjcondron.barkeep;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

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
        String sql = "select * from " + tableName + " where brand='" + brand +"'";
        Cursor c = db.rawQuery(sql, null);
        
        c.moveToFirst();
        return c;
	}
	
	public Cursor getSizes( String tableName, String brand, String product )
	{
		SQLiteDatabase db = getReadableDatabase();
        String sql = "select size from " + tableName + " where brand='" + brand +
        		"and product_name='" + product +"'";
        Cursor c = db.rawQuery(sql, null);
        c.moveToFirst();
        return c;
	}

}
