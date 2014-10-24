package com.kjcondron.barkeep;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;


public class InventoryAdapter extends SimpleCursorAdapter {
	
	private int mQuantityId;
	protected int[] mTo;

	protected int brandColId;
	protected int prodColId;
	protected int sizeColId;
	protected int typeColId;
	
	
	public InventoryAdapter(
			Context ctxt,
			int layout,
			Cursor cursor,
			String[] colNames,
			int[] to,
			int flag,
			int quantityTo
			)
	{
		super(ctxt, layout, cursor, colNames, to, flag);
		// TODO assert to.length == 3
		// TODO assert colNames.length == 3
		mTo = to;
		brandColId = cursor.getColumnIndexOrThrow(colNames[0]);
		prodColId  = cursor.getColumnIndexOrThrow(colNames[1]);
		sizeColId  = cursor.getColumnIndexOrThrow(colNames[2]);
		typeColId  = cursor.getColumnIndexOrThrow(colNames[3]);
		mQuantityId = quantityTo;
	}
		
	public void bindView(View view, Context context, Cursor cursor)
	{
		sbindView(view, context, cursor);
		ProgressBar progQ =(ProgressBar)view.findViewById(mQuantityId);
		
		int qIdx = cursor.getColumnIndexOrThrow("quantity");
		int q = (int)(cursor.getDouble(qIdx) * 100);
        
		progQ.setProgress(q);
	}
	
	public void sbindView(View view, Context context, Cursor cursor) {
		final Paint p = new Paint();
		final int[] to = mTo;
		
		final TextView bv = (TextView)view.findViewById(to[0]);
		final TextView pv = (TextView)view.findViewById(to[1]);
		final TextView sv = (TextView)view.findViewById(to[2]);
		final ImageView iv = (ImageView)view.findViewById(to[3]);
		
		String bText = cursor.getString(brandColId);
		String pText = cursor.getString(prodColId);
		String sText = cursor.getString(sizeColId);
		String tText = cursor.getString(typeColId);
		
		String imagePath = "/storage/emulated/0/images/" + tText + ".jpg";
		                    
		float h = p.measureText(bText + pText);
		setViewText(bv, bText);
		setViewText(pv, pText);
		setViewText(sv, sText);
		setViewImage(iv, imagePath);
		
	}

}
