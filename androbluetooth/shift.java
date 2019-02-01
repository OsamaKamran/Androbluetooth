package com.example.dellcom.androbluetooth;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

public class shift extends AppCompatActivity {

    ImageButton arrows, sms, voice, acc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shift);

        arrows= (ImageButton)findViewById(R.id.imageButton2);
        sms= (ImageButton)findViewById(R.id.imageButton4);
        voice= (ImageButton)findViewById(R.id.imageButton5);
        acc= (ImageButton)findViewById(R.id.imageButton);
    }
    public void arrows(View view){

        Intent go= new Intent(getApplicationContext(), arrowkeys.class);
        startActivity(go);
    }
    public void sms(View view){

        Intent go= new Intent(getApplicationContext(), smsactivity.class);
        startActivity(go);
    }
    public void voice(View view){

        Intent go= new Intent(getApplicationContext(), voice.class);
        startActivity(go);
    }
    public void accel(View view){

        Intent go= new Intent(getApplicationContext(), accelerometer.class);
        startActivity(go);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.mybutton) {

            Intent i=new Intent(getBaseContext(), help.class);
            startActivity(i);

        }

        return super.onOptionsItemSelected(item);
    }
}
