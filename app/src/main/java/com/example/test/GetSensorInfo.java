package com.example.test;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.test.GetSensorInfo;
import com.example.test.PhoneInfoItem;
import com.example.test.R;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.app.Activity;

public class GetSensorInfo extends Activity implements SensorEventListener{

        private SensorManager mSensorManager;
        private TextView mTxtValue1;
        private TextView mTxtValue2;
        private TextView mTxtValue3;
        private TextView mTxtValue4;
        private TextView mTxtValue5;
        private TextView mTxtValue6;
        private TextView mTxtValue7;
        private TextView mTxtValue8;
        private TextView mTxtValue9;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.sensorinfo_item);
            mTxtValue1 = (TextView) findViewById(R.id.infocontext1);
            mTxtValue2 = (TextView) findViewById(R.id.infocontext2);
            mTxtValue3 = (TextView) findViewById(R.id.infocontext3);
            mTxtValue4 = (TextView) findViewById(R.id.infocontext4);
            mTxtValue5 = (TextView) findViewById(R.id.infocontext5);
            mTxtValue6 = (TextView) findViewById(R.id.infocontext6);
            mTxtValue7 = (TextView) findViewById(R.id.infocontext7);
            mTxtValue8 = (TextView) findViewById(R.id.infocontext8);
//            mTxtValue9 = (TextView) findViewById(R.id.infocontext9);

            // 获取传感器管理对象
            mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        }

        @Override
        public void onResume() {
            super.onResume();
            // 为加速度传感器注册监听器
            mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_GAME);
//            // 为方向传感器注册监听器
//            mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION), SensorManager.SENSOR_DELAY_GAME);
            // 为陀螺仪传感器注册监听器
            mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE), SensorManager.SENSOR_DELAY_GAME);
            // 为磁场传感器注册监听器
            mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD), SensorManager.SENSOR_DELAY_GAME);
            // 为重力传感器注册监听器
            mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY), SensorManager.SENSOR_DELAY_GAME);
            // 为线性加速度传感器注册监听器
            mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION), SensorManager.SENSOR_DELAY_GAME);
            // 为温度传感器注册监听器
            mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE), SensorManager.SENSOR_DELAY_GAME);
            // 为光传感器注册监听器
            mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT), SensorManager.SENSOR_DELAY_GAME);
            // 为压力传感器注册监听器
            mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE), SensorManager.SENSOR_DELAY_GAME);
        }

        @Override
        public void onStop() {
            super.onStop();
            // 取消监听
            mSensorManager.unregisterListener(this);
        }

        // 当传感器的值改变的时候回调该方法
        @Override
        public void onSensorChanged(SensorEvent event) {
            float[] values = event.values;
            // 获取传感器类型
            int type = event.sensor.getType();
            StringBuilder sb;
            switch (type){
                case Sensor.TYPE_ACCELEROMETER:
                    sb = new StringBuilder();
//                    sb.append("加速度传感器返回数据：");
//                    sb.append("\nX方向的加速度：");

                    sb.append(values[0]);
                    sb.append("\n");
                    sb.append(values[1]);
                    sb.append("\n");
                    sb.append(values[2]);
                    mTxtValue1.setText(sb.toString());
                    break;
                case Sensor.TYPE_GYROSCOPE:
                    sb = new StringBuilder();
//                    sb.append("\n陀螺仪传感器返回数据：");
//                    sb.append("\n绕X轴旋转的角速度：");
                    sb.append(values[0]);
                    sb.append("\n");
                    sb.append(values[1]);
                    sb.append("\n");
                    sb.append(values[2]);
                    mTxtValue2.setText(sb.toString());
                    break;
                case Sensor.TYPE_MAGNETIC_FIELD:
                    sb = new StringBuilder();
//                    sb.append("\n磁场传感器返回数据：");
//                    sb.append("\nX轴方向上的磁场强度：");
                    sb.append(values[0]);
                    sb.append("\n");
                    sb.append(values[1]);
                    sb.append("\n");
                    sb.append(values[2]);
                    mTxtValue3.setText(sb.toString());
                    break;
                case Sensor.TYPE_GRAVITY:
                    sb = new StringBuilder();
//                    sb.append("\n重力传感器返回数据：");
//                    sb.append("\nX轴方向上的重力：");
                    sb.append(values[0]);
                    sb.append("\n");
                    sb.append(values[1]);
                    sb.append("\n");
                    sb.append(values[2]);
                    mTxtValue4.setText(sb.toString());
                    break;
                case Sensor.TYPE_LINEAR_ACCELERATION:
                    sb = new StringBuilder();
//                    sb.append("\n线性加速度传感器返回数据：");
//                    sb.append("\nX轴方向上的线性加速度：");
                    sb.append(values[0]);
                    sb.append("\n");
                    sb.append(values[1]);
                    sb.append("\n");
                    sb.append(values[2]);
                    mTxtValue5.setText(sb.toString());
                    break;
                case Sensor.TYPE_AMBIENT_TEMPERATURE:
                    sb = new StringBuilder();
//                    sb.append("\n温度传感器返回数据：");
//                    sb.append("\n当前温度为：");
                    sb.append(values[0]);
                    mTxtValue6.setText(sb.toString());
                    break;
                case Sensor.TYPE_LIGHT:
                    sb = new StringBuilder();
//                    sb.append("\n光传感器返回数据：");
//                    sb.append("\n当前光的强度为：");
                    sb.append(values[0]);
                    mTxtValue7.setText(sb.toString());
                    break;
                case Sensor.TYPE_PRESSURE:
                    sb = new StringBuilder();
//                    sb.append("\n压力传感器返回数据：");
//                    sb.append("\n当前压力为：");
                    sb.append(values[0]);
                    mTxtValue8.setText(sb.toString());
                    break;
            }
        }

        // 当传感器精度发生改变时回调该方法
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
}