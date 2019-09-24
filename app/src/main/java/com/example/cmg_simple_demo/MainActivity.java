package com.example.cmg_simple_demo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.util.Random;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    ///*
    // * Notifications from UsbService will be received here.
    // */
    //private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
    //    @Override
    //    public void onReceive(Context context, Intent intent) {
    //        switch (intent.getAction()) {
    //            case UsbService.ACTION_USB_PERMISSION_GRANTED: // USB PERMISSION GRANTED
    //                Toast.makeText(context, "USB Ready", Toast.LENGTH_SHORT).show();
    //                break;
    //            case UsbService.ACTION_USB_PERMISSION_NOT_GRANTED: // USB PERMISSION NOT GRANTED
    //                Toast.makeText(context, "USB Permission not granted", Toast.LENGTH_SHORT).show();
    //                break;
    //            case UsbService.ACTION_NO_USB: // NO USB CONNECTED
    //                Toast.makeText(context, "No USB connected", Toast.LENGTH_SHORT).show();
    //                break;
    //            case UsbService.ACTION_USB_DISCONNECTED: // USB DISCONNECTED
    //                Toast.makeText(context, "USB disconnected", Toast.LENGTH_SHORT).show();
    //                break;
    //            case UsbService.ACTION_USB_NOT_SUPPORTED: // USB NOT SUPPORTED
    //                Toast.makeText(context, "USB device not supported", Toast.LENGTH_SHORT).show();
    //                break;
    //        }
    //    }
    //};
    //private UsbService usbService;
    //private MyHandler mHandler;
    //private Handler mMsgSendHandler;
    //private Runnable mRunnableCode;

    //private final ServiceConnection usbConnection = new ServiceConnection() {
    //    @Override
    //    public void onServiceConnected(ComponentName arg0, IBinder arg1) {
    //        usbService = ((UsbService.UsbBinder) arg1).getService();
    //        usbService.setHandler(mHandler);
    //    }
//
    //    @Override
    //    public void onServiceDisconnected(ComponentName arg0) {
    //        usbService = null;
    //    }
    //};

    public ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        //page adapter
        PagerAdapter mPagerAdapter = new PagerAdapter(this, getSupportFragmentManager());
        //view pager
        viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(mPagerAdapter);
        viewPager.setOffscreenPageLimit(2);


        //widgets settings
        //mHandler = new MyHandler(this);

        //mMsgSendHandler = new Handler();

        //mMsgSendHandler.postDelayed(new Runnable() {
        //    @Override
        //    public void run() {
        //        // Do something here on the main thread
        //        int r1 = new Random().nextInt(999) + 1;
        //        int r2 = new Random().nextInt(999) + 1;
        //        String data = assembleProtocol(r1, r2);
        //        if (usbService != null) {
        //            usbService.write(data.getBytes());
        //            Toast.makeText(getApplicationContext(), "Running" + r1, Toast.LENGTH_SHORT).show();
        //        }
        //        mMsgSendHandler.postDelayed(this, 5000);
        //    }
        //}, 5000);



        // Run the above code block on the main thread after 3 seconds
        //mMsgSendHandler.postDelayed(mRunnableCode, 3000);

    }

    @Override
    public void onResume() {
        super.onResume();
        //setFilters();  // Start listening notifications from UsbService
        //startService(UsbService.class, usbConnection, null); // Start UsbService(if it was not started before) and Bind it
    }

    @Override
    public void onPause() {
        super.onPause();
        //unregisterReceiver(mUsbReceiver);
        //unbindService(usbConnection);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
    }

    //set up a public method for fragment to call to change current fragment
    public void setCurrentItem (int item, boolean smoothScroll) {
        viewPager.setCurrentItem(item, smoothScroll);
    }

    // Define the code block to be executed
    //private Runnable mRunnableCode = new Runnable() {
    //    @Override
    //    public void run() {
    //        // Do something here on the main thread
    //        int r1 = new Random().nextInt(999) + 1;
    //        int r2 = new Random().nextInt(999) + 1;
    //        String data = assembleProtocol(r1, r2);
    //        if (usbService != null) {
    //            usbService.write(data.getBytes());
    //            Toast.makeText(getApplicationContext(), "Running" + r1, Toast.LENGTH_SHORT).show();
    //        }
    //        mMsgSendHandler.postDelayed(mRunnableCode, 3000);
    //    }
    //};

    private void hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    // Shows the system bars by removing all the flags
    // except for the ones that make the content appear under the system bars.
    private void showSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    //private void startService(Class<?> service, ServiceConnection serviceConnection, Bundle extras) {
    //    if (!UsbService.SERVICE_CONNECTED) {
    //        Intent startService = new Intent(this, service);
    //        if (extras != null && !extras.isEmpty()) {
    //            Set<String> keys = extras.keySet();
    //            for (String key : keys) {
    //                String extra = extras.getString(key);
    //                startService.putExtra(key, extra);
    //            }
    //        }
    //        startService(startService);
    //    }
    //    Intent bindingIntent = new Intent(this, service);
    //    bindService(bindingIntent, serviceConnection, Context.BIND_AUTO_CREATE);
    //}
//
    //private void setFilters() {
    //    IntentFilter filter = new IntentFilter();
    //    filter.addAction(UsbService.ACTION_USB_PERMISSION_GRANTED);
    //    filter.addAction(UsbService.ACTION_NO_USB);
    //    filter.addAction(UsbService.ACTION_USB_DISCONNECTED);
    //    filter.addAction(UsbService.ACTION_USB_NOT_SUPPORTED);
    //    filter.addAction(UsbService.ACTION_USB_PERMISSION_NOT_GRANTED);
    //    registerReceiver(mUsbReceiver, filter);
    //}
//
    ///*
    // * This handler will be passed to UsbService. Data received from serial port is displayed through this handler
    // */
    //private static class MyHandler extends Handler {
    //    private final WeakReference<MainActivity> mActivity;
//
    //    public MyHandler(MainActivity activity) {
    //        mActivity = new WeakReference<>(activity);
    //    }
//
    //    @Override
    //    public void handleMessage(Message msg) {
    //        switch (msg.what) {
    //            case UsbService.MESSAGE_FROM_SERIAL_PORT:
    //                String data = (String) msg.obj;
    //                //mActivity.get().display.append(data);
    //                break;
    //            case UsbService.CTS_CHANGE:
    //                Toast.makeText(mActivity.get(), "CTS_CHANGE", Toast.LENGTH_LONG).show();
    //                break;
    //            case UsbService.DSR_CHANGE:
    //                Toast.makeText(mActivity.get(), "DSR_CHANGE", Toast.LENGTH_LONG).show();
    //                break;
    //        }
    //    }
    //}
//
//
    //private String assembleProtocol(int n1, int n2) {
    //    String protocolStr = "";
    //    protocolStr = "<CMCMLT><ID003000><W1><FS>" + String.valueOf(n1) + "<W2><FS>" + String.valueOf(n2) + "<E>";
//
    //    return protocolStr;
    //}
}
