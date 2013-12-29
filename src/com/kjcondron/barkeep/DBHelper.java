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
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String [] sqlSelect = {"0 _id", "Brand"};
       
        qb.setTables(tableName);
        Cursor c = qb.query(db, sqlSelect, null, null,
                        null, null, null);

        c.moveToFirst();
        return c;
	}

}
