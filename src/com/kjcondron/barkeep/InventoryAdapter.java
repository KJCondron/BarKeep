package com.kjcondron.barkeep;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.SimpleCursorAdapter;


public class InventoryAdapter extends SimpleCursorAdapter {
	
	private int mQuantityId;
	
	public InventoryAdapter(
			Context ctxt,
			int layout,
			Cursor cursor,
			String[] textColumns,
			int[] to,
			int flag,
			int quantityTo
			)
	{
		super(ctxt, layout, cursor, textColumns, to, flag);
		mQuantityId = quantityTo;
		mQuantityId = R.id.progressBar1;
	}
	
	@Override
	public View newView(Context ctxt, Cursor cursor, ViewGroup parent)
	{
		return super.newView(ctxt, cursor, parent);
	}
	
	public void bindView(View view, Context context, Cursor cursor)
	{
		super.bindView(view, context, cursor);
		ProgressBar progQ =(ProgressBar)view.findViewById(mQuantityId);
		
		
		int qIdx = cursor.getColumnIndexOrThrow("quantity");
		int q = (int)(cursor.getDouble(qIdx) * 100);
        
		progQ.setProgress(q);
	}

}
