package com.kjcondron.barkeep;

import java.io.FileDescriptor;
import java.io.InputStream;

import android.R.drawable;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class TypesAdapter extends SimpleCursorAdapter {
	
	private Context mctxt;
	private Cursor mcrsr;
	private int[] mto;


	public TypesAdapter(
			Context context,
			int layout,
			Cursor c,
			String[] from,
			int[] to,
			int flag) {
		super(context, layout, c, from, to, flag);
		mctxt=context;
		mcrsr=c;
		mto=to;
	}
		
	@Override
	public int getCount()
	{
		return super.getCount() + 1;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		// in super
		// calls newView to inflate view
		// then bind view to populate
		View v;
		if(position == getCount() - 1)
		{
			View nv = newView(mctxt, mcrsr, parent);
			TextView tv = (TextView)nv.findViewById(mto[0]);
			tv.setText("Scan");
			
			ImageView iv = (ImageView)nv.findViewById(mto[1]);
			String loc = Environment.getExternalStorageDirectory().getAbsolutePath()+"/images/scan.jpg";			
			iv.setImageURI(Uri.parse(loc));
			
			v = nv;
		}
		else 
			v = super.getView(position, convertView, parent);
		
		return v;
	}
	 
}
