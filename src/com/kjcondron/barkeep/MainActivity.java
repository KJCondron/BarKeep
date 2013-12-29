package com.kjcondron.barkeep;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;

public class MainActivity extends Activity {
	
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
    
}
