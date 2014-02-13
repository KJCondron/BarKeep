package com.kjcondron.barkeep;

import android.R.bool;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;
import android.widget.ViewSwitcher.ViewFactory;

public class MainActivity extends Activity {
	
	public static int BARID;	
	boolean makeNewBar;
		
	/* Checks if external storage is available for read and write */
	public static boolean isExternalStorageWritable() {
	    String state = Environment.getExternalStorageState();
	    if (Environment.MEDIA_MOUNTED.equals(state)) {
	        return true;
	    }
	    return false;
	}

	/* Checks if external storage is available to at least read */
	public static boolean isExternalStorageReadable() {
	    String state = Environment.getExternalStorageState();
	    if (Environment.MEDIA_MOUNTED.equals(state) ||
	        Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
	        return true;
	    }
	    return false;
	}
	
	public static void log_exception(Context c, Exception e, String fromWhere)
	{
		log_message(c, e.getMessage(), fromWhere);
		/*// to-do add logging code
		if(isExternalStorageWritable())
		{
			
		}
		else
		{
			
			try	
			{
				File file = new File(App.context.getFilesDir(), "err_log");
				FileOutputStream stream = new FileOutputStream(file);
				stream.write(fromWhere.getBytes());
				stream.write(e.getMessage().getBytes());	
				stream.close();
			}
			catch(Exception o){}
				
		}*/
	}
	
	public static void log_message(Context c, String s, String fromWhere)
	{
		if(s != null ){
		Log.e("barkeep", s);
		if(c!=null)
			Toast.makeText(c, s, Toast.LENGTH_LONG).show();
		}
	}
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        final DBHelper db = new DBHelper(this);
        makeNewBar = false;
        setContentView(R.layout.layout_splash);
        
        if( db.haveBar() )
        {
        	try
        	{
        		View view = findViewById(R.id.layout_slpash);
				final TextSwitcher ts = (TextSwitcher) findViewById(R.id.textSwitcher1);
				final Cursor barCursor = db.getBars();
				barCursor.moveToFirst();
				ts.setFactory( new ViewFactory() {
					
					@Override
					public View makeView() {
						TextView myText = new TextView(MainActivity.this);
						myText.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);
                        myText.setTextSize(36);
                        myText.setTextColor(Color.BLUE);
                        return myText;
					}
				});
				

				final GestureDetector gd = new GestureDetector(
						this,
						new FrontPageGestureListener(this, ts, barCursor, ts));
        		view.setOnTouchListener( new OnTouchListener() {
        			boolean newBar = false;
					@Override
					public boolean onTouch(View v, MotionEvent event) {
						// TODO Auto-generated method stub
						switch(event.getActionMasked())
						{
						case MotionEvent.ACTION_UP:
							try
							{
								if(newBar)
								{
									ts.setText( "New Bar" );
									makeNewBar = true;
									newBar = false;
									barCursor.moveToFirst();
								}
								else
								{
									ts.setText( barCursor.getString(1) );
									BARID = barCursor.getInt(0);
									makeNewBar = false;
									if(!barCursor.moveToNext())
										newBar = true;
								}
							}
							catch(Exception e)
							{
								MainActivity.log_exception(MainActivity.this, e, "TouchListener");
							}
							return true;
						
						}
						return gd.onTouchEvent(event);
					}
				});		
        	}
        	catch(Exception e)
        	{
        		BARID=1;
        		startMyActvity(UseActivity.class);
            	finish();
            	return;
        	}
        	 
        }
        else
        	makeNewBar();
    	
    }
    
    public void makeNewBar()
    {
    	setContentView(R.layout.layout_splash);
    	final DBHelper db = new DBHelper(this);
    	final EditText edtBarName = (EditText)findViewById(R.id.edtBarName);
    	edtBarName.setVisibility(View.VISIBLE);
    	edtBarName.setOnEditorActionListener(new OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if(actionId == EditorInfo.IME_ACTION_DONE)
				{
					BARID = db.newBar(v.getText().toString());
					startMyActvity(UseActivity.class);
					startMyActvity(AddActivity.class);
					finish();
					edtBarName.setVisibility(View.GONE);
					return true;
				}
				return false;
			}
		});
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    private void startMyActvity(Class<?> cl) {
    	Intent intent = new Intent(this, cl);
    	startActivity(intent);
    }
    
    public void startAdd(View view) {
    	startMyActvity(AddActivity.class);
    }
    
    public void startUse(View view) {
    	if(makeNewBar)
    		makeNewBar();
    	else
    		startMyActvity(UseActivity.class);
    }
    
    public void startShop(View view) {
    	startMyActvity(ShopActivity.class);
    }
    	
}
