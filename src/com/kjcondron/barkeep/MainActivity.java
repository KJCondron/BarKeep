package com.kjcondron.barkeep;

import java.io.File;
import java.io.FileOutputStream;

import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	
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
	
	public static void log_exception(Exception e, String fromWhere)
	{
		// to-do add logging code
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
				
		}
	}
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
    	startMyActvity(UseActivity.class);
    }
    
    public void startRemove(View view) {
    	startMyActvity(RemoveActivity.class);
    }
    
    public void startScan(View view){
    	IntentIntegrator.initiateScan(this);
    }
    
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	switch(requestCode) {
    		case IntentIntegrator.REQUEST_CODE: {
    			if (resultCode != RESULT_CANCELED) {
    				IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
    				if (scanResult != null) {
    					try
    					{
	    					String upc = scanResult.getContents();
	    	 
	    					//put whatever you want to do with the code here
	    					DBHelper db = new DBHelper(this);
	    					Cursor c = db.getFromUPC(upc).second;
	    					Intent intent = new Intent(this, ProductDetailActivity.class);
	    					intent.putExtra(
									ProductDetailActivity.UPC,
									upc);
	    					if( c.getCount() == 0 )
	    						intent.putExtra(ProductDetailActivity.ADD_TO_DB, true);	
	    					
	    					startActivity(intent);
    					}
    					catch(Exception e)
    					{
    						MainActivity.log_exception(e, "MainActivity.onActivityResult");
    						// do nothing we are showing main activity
    					}
    				}
    			}
    			break;
    		}
    	}
    }    
}
