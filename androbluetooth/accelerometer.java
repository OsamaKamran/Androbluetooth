package com.example.dellcom.androbluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;


public class accelerometer extends Activity implements SensorEventListener {

    private float lastX, lastY;

    private SensorManager sensorManager;

    private Sensor accelerometer;

    private float deltaX = 0;

    private float deltaY = 0;

    private TextView currentX, currentY;
    private final String DEVICE_ADDRESS = "98:D3:32:20:5B:96"; //MAC Address of Bluetooth Module
    private final UUID PORT_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");

    private BluetoothDevice device;
    private BluetoothSocket socket;
    private OutputStream outputStream;
    String s;

    @Override

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_accelerometer);

        initializeViews();
        if(BTinit())
        {
            BTconnect();
        }


        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        if (sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {

            // success! we have an accelerometer


            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        } else {

            Toast.makeText(accelerometer.this, "Your Android donnot have Accelerometer!", Toast.LENGTH_LONG).show();

        }
    }
    public void initializeViews() {

        currentX = (TextView) findViewById(R.id.currentX);

        currentY = (TextView) findViewById(R.id.currentY);


    }
        //onResume() register the accelerometer for listening the events

    protected void onResume() {

        super.onResume();

        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);

    }
        //onPause() unregister the accelerometer for stop listening the events

    protected void onPause() {

        super.onPause();

        sensorManager.unregisterListener(this);

    }
    @Override
    protected void onStop(){
        super.onStop();
        BTdisconnect();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
    @Override
    public void onSensorChanged(SensorEvent event) {

        // clean current values

        displayCleanValues();

        // display the current x,y,z accelerometer values

        displayCurrentValues();

        // get the change of the x,y,z values of the accelerometer

        deltaX = Math.abs(lastX - event.values[0]);

        deltaY = Math.abs(lastY - event.values[1]);

        // if the change is below 2, it is just plain noise

        if (deltaX < 2)

        deltaX = 0;

        if (deltaY < 2)

        deltaY = 0;

        if (deltaX > 4 && deltaY < 4)  {

            s = "right";

            try
            {
                outputStream.write(s.getBytes()); //transmits the value of command to the bluetooth module
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

        }
        if (deltaY > 4 && deltaX < 4)  {

            s = "forward";

            try
            {
                outputStream.write(s.getBytes()); //transmits the value of command to the bluetooth module
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

        }
        if(deltaX < 4 && deltaY < 4){
            s = "stop";

            try
            {
                outputStream.write(s.getBytes()); //transmits the value of command to the bluetooth module
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }

    }

    public void displayCleanValues() {

        currentX.setText("0.0");

        currentY.setText("0.0");


    }
    // display the current x,y,z accelerometer values

    public void displayCurrentValues() {

        currentX.setText(Float.toString(deltaX));

        currentY.setText(Float.toString(deltaY));

    }
    public boolean BTinit()
    {
        boolean found = false;

        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if(bluetoothAdapter == null) //Checks if the device supports bluetooth
        {
            Toast.makeText(getApplicationContext(), "Device doesn't support bluetooth", Toast.LENGTH_SHORT).show();
        }

        if(!bluetoothAdapter.isEnabled()) //Checks if bluetooth is enabled. If not, the program will ask permission from the user to enable it
        {
            Intent enableAdapter = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableAdapter,0);

            try
            {
                Thread.sleep(1000);
            }
            catch(InterruptedException e)
            {
                e.printStackTrace();
            }
        }

        Set<BluetoothDevice> bondedDevices = bluetoothAdapter.getBondedDevices();

        if(bondedDevices.isEmpty()) //Checks for paired bluetooth devices
        {
            Toast.makeText(getApplicationContext(), "Please pair the device first", Toast.LENGTH_SHORT).show();
        }
        else
        {
            for(BluetoothDevice iterator : bondedDevices)
            {
                if(iterator.getAddress().equals(DEVICE_ADDRESS))
                {
                    device = iterator;
                    found = true;
                    break;
                }
            }
        }

        return found;
    }

    public boolean BTconnect()
    {
        boolean connected = true;

        try
        {
            socket = device.createRfcommSocketToServiceRecord(PORT_UUID); //Creates a socket to handle the outgoing connection
            socket.connect();

            Toast.makeText(getApplicationContext(),
                    "Connection to bluetooth device successful", Toast.LENGTH_LONG).show();
        }
        catch(IOException e)
        {
            e.printStackTrace();
            connected = false;
        }

        if(connected)
        {
            try
            {
                outputStream = socket.getOutputStream(); //gets the output stream of the socket
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
        }

        return connected;
    }
    public void BTdisconnect(){

        try
        {
            socket.close();

        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStart()
    {
        super.onStart();
    }

}