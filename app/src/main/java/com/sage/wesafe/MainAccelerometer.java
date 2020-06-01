package com.sage.wesafe;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.app.Activity;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import static com.sage.wesafe.AccelerometerManager.threshold;

public class MainAccelerometer extends Activity implements AccelerometerListener{
    EditText txt_number;

    int count_shakes=0;
    TextView shake;

    SeekBar seekBar;
    TextView txt_seek_bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.accelerometer_example_main);
        txt_number = (EditText)findViewById(R.id.number);
        shake=(TextView)findViewById(R.id.shakes);

        seekBar = (SeekBar)findViewById(R.id.seek_bar);
        txt_seek_bar =(TextView)findViewById(R.id.txt_seek_bar);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                threshold=30.0f;
                Log.v("Threshold ki value ","Yeh hai uski value = "+threshold);
                txt_seek_bar.setText(progress+"/100");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        // Check onResume Method to start accelerometer listener
    }

    public void onAccelerationChanged(float x, float y, float z) {
        // TODO Auto-generated method stub

    }

    public void onShake(float force) {

        // Do your stuff here

        // Called when Motion Detected
//        Toast.makeText(getBaseContext(), "Motion detected",Toast.LENGTH_SHORT).show();
        count_shakes+=1;
        if(count_shakes==10){
            count_shakes=0;
            MyMessage();
        }
        txt_number.setText("8286320104");
        shake.setText("no of shakes detected : "+count_shakes);



    }

    @Override
    public void onResume() {
        super.onResume();

        //Check device supported Accelerometer senssor or not
        if (AccelerometerManager.isSupported(this)) {

            //Start Accelerometer Listening
            AccelerometerManager.startListening(this);
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        //Check device supported Accelerometer senssor or not
        if (AccelerometerManager.isListening()) {

            //Start Accelerometer Listening
            AccelerometerManager.stopListening();

            Toast.makeText(getBaseContext(), "onStop Accelerometer Stoped",
                    Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("Sensor", "Service  distroy");

        //Check device supported Accelerometer senssor or not
        if (AccelerometerManager.isListening()) {

            //Start Accelerometer Listening
            AccelerometerManager.stopListening();

            Toast.makeText(getBaseContext(), "onDestroy Accelerometer Stoped",
                    Toast.LENGTH_SHORT).show();
        }

    }

    public void btn_send(View view) {

            int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS);

            if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                MyMessage();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, 0);
            }
    }

    private void MyMessage() {
        String phoneNumber = txt_number.getText().toString().trim();
        String Message = "Help Me.! I am in danger.! Track my location from here:" +
                "http://maps.google.com/maps?daddr=(18.9690552,72.8297629)";
        if(!txt_number.getText().toString().equals("")){
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage("8286320104",null,Message,null,null);
            Toast.makeText(this,"Message sent",Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(this,"Enter Number",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case 0:
                if(grantResults.length>=0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    MyMessage();
                }
                else{
                    Toast.makeText(this,"No permission",Toast.LENGTH_SHORT).show();

                }
                break;
        }


    }
}