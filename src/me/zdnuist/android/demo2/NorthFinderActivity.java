package me.zdnuist.android.demo2;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;
import me.zdnuist.android.BaseActivity;
import me.zdnuist.android.R;

public class NorthFinderActivity extends BaseActivity implements SensorEventListener{
	
	private TextView show;
	private SensorManager sensorManager;
	private Sensor sensor;
	
	private final float[] mRotationMatrix = new float[16];
	private final float[] orientationVals = new float[3];
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState,R.layout.activity_northfinder);
		
		show = (TextView) findViewById(R.id.tv_show);
		
		sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		
		sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		sensorManager.registerListener(this, sensor,10000);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		sensorManager.unregisterListener(this);
	}

	@Override
	public void onSensorChanged(SensorEvent event) {

		if(event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR){
			SensorManager.getRotationMatrixFromVector(mRotationMatrix, event.values);
			SensorManager.remapCoordinateSystem(mRotationMatrix, SensorManager.AXIS_X,
					SensorManager.AXIS_Z, mRotationMatrix);
			SensorManager.getOrientation(mRotationMatrix, orientationVals);
			
			
			orientationVals[0] = (float) Math.toDegrees(orientationVals[0]);
			orientationVals[1] = (float) Math.toDegrees(orientationVals[1]);
			orientationVals[2] = (float) Math.toDegrees(orientationVals[2]);
			
			show.setText(" Yaw: "+ orientationVals[0] + "\n Pitch: "
					+ orientationVals[1] +"\n Roll (not used) :"
					+ orientationVals[2]
					);
		}
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}

}
