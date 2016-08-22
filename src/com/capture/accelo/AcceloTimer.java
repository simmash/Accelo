package com.capture.accelo;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;
import android.app.Activity;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

public class AcceloTimer extends Activity implements SensorEventListener
{
	public TextView timerview, actionnameview, statusview;
	
	private CountDownTimer countDownTimer;
	private boolean timerHasStarted = false;
	private ImageButton startImage;
	private long startTime = 30 * 1000;
	private final long interval = 1 * 1000;
	private String actionName;
	private String delay;
	private String freq;
	private MediaPlayer playAlert, playFinal;
	private SensorManager sensorManager;
	private double ax,ay,az;   // these are the acceleration in x,y and z axis
	private File gpxfile;
	private FileWriter gpxwriter;
	private BufferedWriter out;
	private SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss:SSS");
	private boolean sensorStarted = false;
	private long sensorTimeReference = 0l;
	private long myTimeReference = 0l;
	 
	 @Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.accelotimer);
		
		statusview = (TextView) this.findViewById(R.id.activitystatus);
		
		Intent intent = getIntent();
		actionName = intent.getStringExtra(AcceloDefines.ID_ACTION_NAME); 
		delay = intent.getStringExtra(AcceloDefines.ID_DELAY);
		freq = intent.getStringExtra(AcceloDefines.ID_FREQ);
		
		startTime = Integer.valueOf(delay) * 1000;
		
		actionnameview = (TextView) this.findViewById(R.id.activityname);
		actionnameview.setText(actionName);
		
		timerview = (TextView) this.findViewById(R.id.timer);
		countDownTimer = new MyCountDownTimer(startTime, interval);
		timerview.setText(timerview.getText() + String.valueOf(startTime / 1000));
		
		statusview.setText("Long press to start");
		
		startImage = (ImageButton)this.findViewById(R.id.imageButton);
		startImage.setOnLongClickListener(new View.OnLongClickListener() 
		{
			@Override
	        public boolean onLongClick(View v) 
	        {
				if(!timerHasStarted)
	        	{
					timerHasStarted = true;
	        		startImage.setImageResource(R.drawable.stop256);
	     			
	        		try {
	    				Log.d("File : ", "Create the output file");
	    				File storagePath = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separator + "com.capture.accelo");
	    				storagePath.mkdirs();
	    			    if (storagePath.canWrite()){
	    			        gpxfile = new File(storagePath, actionName+".txt");
	    			        gpxwriter = new FileWriter(gpxfile);
	    			        out = new BufferedWriter(gpxwriter);
	    			    }
	    			} catch (IOException e) {
	    			    Log.e("Could not open file : ", e.getMessage());
	    			}
	    			
	    			Log.d("Timer : ", "Start the timer");
	    			countDownTimer.start();
	    			timerHasStarted = true;
	    			
	    			String strDate = sdf.format(Calendar.getInstance().getTime());
	    			writeToFile(strDate + " Started" + "\n");
	        	}else{
	        		
	        		timerHasStarted = false;
	        		timerview.setText("");
	        		startImage.setImageResource(R.drawable.start256);
	     			
	        		countDownTimer.cancel();
	    			timerHasStarted = false;
	    			
	    			String strDate = sdf.format(Calendar.getInstance().getTime());
	    			writeToFile(strDate + " Stopped" + "\n");
	    			
	    			if(sensorStarted == true)
	    			{
	    				stopSensor();
	    				statusview.setText("Accelerometer Stopped, Long press to restart - file will be overwritten");
	    			}else{
	    				statusview.setText("Long press to start");
	    			}
	    			
	    			try {
	    			    out.close();
	    			    Log.d("File : ", "Close the output file");
	    			    //refreshing directory to ensure file is flushed
	    		        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
	    		        mediaScanIntent.setData(Uri.fromFile(gpxfile));
	    		        sendBroadcast(mediaScanIntent);
	    		        Log.d("Refresh : ", "Refresh the directory");
	    			} catch (IOException e) {
	    			    Log.e("Error closing file : ", e.getMessage());
	    			}	        		 
	        	}
				return true; 
	        }
	    });
		
		playAlert = MediaPlayer.create(this, R.raw.beep2);
		playFinal = MediaPlayer.create(this, R.raw.beep9);
		
		sensorManager=(SensorManager) getSystemService(SENSOR_SERVICE);
		
		Calendar.getInstance();
		
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	}

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
	}

	@Override
	public void onSensorChanged(SensorEvent event) 
	{
		// set reference times
	    if(sensorTimeReference == 0l && myTimeReference == 0l) 
	    {
	        sensorTimeReference = event.timestamp;
	        myTimeReference = System.currentTimeMillis();
	    }
	    // set event timestamp to current time in milliseconds
	    event.timestamp = myTimeReference + Math.round((event.timestamp - sensorTimeReference) / 1000000.0);
	    String strDate = sdf.format(event.timestamp);
		
		//String nowTimeStamp = calculateTimeString(now);
		if (event.sensor.getType()==Sensor.TYPE_ACCELEROMETER)
		{
			ax=event.values[0];
	        ay=event.values[1];
	        az=event.values[2];
	    }
		
		writeToFile(strDate + ", " + ax + ", " + ay + ", " + az + "\n");
	}
	
	public void startSensor()
	{
		sensorStarted = true;
		Log.d("Sensor : ", "Start Sensor");
		sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), Integer.valueOf(freq)*1000); // 50ms gap
	}
	
	public void stopSensor()
	{
		sensorStarted = false;
		Log.d("Sensor : ", "Stop Sensor");
		sensorManager.unregisterListener(this);
	}
	
	private void writeToFile(String data) 
	{		
		try {
		    out.append(data);
		} catch (IOException e) {
		    Log.e("Could not appnd file ", e.getMessage());
		}
	}
	
	public static String calculateTimeString(Long totalmilisec) 
	{      		
		long hours = TimeUnit.MILLISECONDS.toHours(totalmilisec) ;
		long tempminutes = totalmilisec - (TimeUnit.HOURS.toMillis(hours));
		
		long minute = TimeUnit.MILLISECONDS.toMinutes(tempminutes); 
		long tempSec = tempminutes - (TimeUnit.MINUTES.toMillis(minute));
		
		long second = TimeUnit.MILLISECONDS.toSeconds(tempSec);
		long milisec = tempSec - (TimeUnit.MINUTES.toMillis(second));

		String time = hours + ":" + minute + ":" + second + ":" + milisec;
		return time;
	}	
	
	public class MyCountDownTimer extends CountDownTimer 
	{
		String upDn;
		public MyCountDownTimer(long startTime, long interval) 
		{
		   super(startTime, interval);
		}
		@Override
		public void onFinish() 
		{
			timerview.setText("");
			statusview.setText("Accelerometer Started, writing to file");
			playFinal.start();
			startSensor();
		}

		@Override
		public void onTick(long millisUntilFinished) 
		{
			Log.d("MyCountDownTimer : ", "onTick");
			if(millisUntilFinished/1000 < 5)
				playAlert.start();
			timerview.setText("" + millisUntilFinished / 1000);

		}
	}	
}