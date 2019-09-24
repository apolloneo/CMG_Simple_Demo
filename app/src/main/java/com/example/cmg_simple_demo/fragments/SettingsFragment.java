package com.example.cmg_simple_demo.fragments;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.example.cmg_simple_demo.MainActivity;
import com.example.cmg_simple_demo.PagerAdapter;
import com.example.cmg_simple_demo.R;
import com.example.cmg_simple_demo.SharedViewModel;
import com.example.cmg_simple_demo.UsbService;

import java.lang.ref.WeakReference;
import java.util.Random;
import java.util.Set;

public class SettingsFragment extends Fragment {
    /*
     * Notifications from UsbService will be received here.
     */
    private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case UsbService.ACTION_USB_PERMISSION_GRANTED: // USB PERMISSION GRANTED
                    Toast.makeText(context, "USB Ready", Toast.LENGTH_SHORT).show();
                    break;
                case UsbService.ACTION_USB_PERMISSION_NOT_GRANTED: // USB PERMISSION NOT GRANTED
                    Toast.makeText(context, "USB Permission not granted", Toast.LENGTH_SHORT).show();
                    break;
                case UsbService.ACTION_NO_USB: // NO USB CONNECTED
                    Toast.makeText(context, "No USB connected", Toast.LENGTH_SHORT).show();
                    break;
                case UsbService.ACTION_USB_DISCONNECTED: // USB DISCONNECTED
                    Toast.makeText(context, "USB disconnected", Toast.LENGTH_SHORT).show();
                    break;
                case UsbService.ACTION_USB_NOT_SUPPORTED: // USB NOT SUPPORTED
                    Toast.makeText(context, "USB device not supported", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    //widgets
    private Button startStopButton;
    private EditText signID, autoSendTimerInterval;
    private ToggleButton panelEffectCodeEnable;
    private Spinner peCode_1, peCode_2;
    //vars
    private Handler mMsgSendHandler;
    private Runnable mRunnableCode;
    private UsbService usbService;
    private MyHandler mHandler;
    private String sign_id;
    private int timer_interval_millisec;
    private SharedViewModel sharedViewModel_settings_fragment;


    private final ServiceConnection usbConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName arg0, IBinder arg1) {
            usbService = ((UsbService.UsbBinder) arg1).getService();
            usbService.setHandler(mHandler);
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            usbService = null;
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_settings, container, false);

        startStopButton = rootview.findViewById(R.id.button_settings_start);
        signID = rootview.findViewById(R.id.editText_settings_sign_id_value);
        autoSendTimerInterval = rootview.findViewById(R.id.editText_settings_timer_interval_value);
        panelEffectCodeEnable = rootview.findViewById(R.id.toggleButton_settings_PE_code_enable);
        peCode_1 = rootview.findViewById(R.id.spinner_PE_1);
        peCode_2 = rootview.findViewById(R.id.spinner_PE_2);

        mHandler = new MyHandler(getActivity());
        mMsgSendHandler = new Handler();

        sign_id = signID.getText().toString();
        timer_interval_millisec = Integer.parseInt(autoSendTimerInterval.getText().toString()) * 1000 ;

        panelEffectCodeEnable.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    peCode_1.setVisibility(View.VISIBLE);
                    peCode_2.setVisibility(View.VISIBLE);
                } else {
                    peCode_1.setVisibility(View.INVISIBLE);
                    peCode_2.setVisibility(View.INVISIBLE);
                }
            }
        });

        startStopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (startStopButton.getText().toString().equals("START")) {
                    startStopButton.setText("STOP");
                    mRunnableCode = new Runnable() {
                        @Override
                        public void run() {
                            // Do something here on the main thread
                            int r1 = new Random().nextInt(999) + 1;
                            int r2 = new Random().nextInt(999) + 1;
                            sharedViewModel_settings_fragment.setValue_W1(String.valueOf(r1));
                            sharedViewModel_settings_fragment.setValue_W2(String.valueOf(r2));
                            String data = assembleProtocol(r1, r2);
                            if (usbService != null) {
                                usbService.write(data.getBytes());
                                Toast.makeText(getContext(), "Running" + r1, Toast.LENGTH_SHORT).show();
                            }
                            mMsgSendHandler.postDelayed(this, timer_interval_millisec);
                        }
                    };
                    mMsgSendHandler.postDelayed(mRunnableCode, timer_interval_millisec);
                    ((MainActivity) getActivity()).setCurrentItem(2, true);
                } else {
                    startStopButton.setText("START");
                    mMsgSendHandler.removeCallbacks(mRunnableCode);
                }
            }
        });
        return rootview;
    }

    @Override
    public void onResume() {
        super.onResume();
        setFilters();  // Start listening notifications from UsbService
        startService(UsbService.class, usbConnection, null); // Start UsbService(if it was not started before) and Bind it
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(mUsbReceiver);
        getActivity().unbindService(usbConnection);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        sharedViewModel_settings_fragment = ViewModelProviders.of(getActivity()).get(SharedViewModel.class);
    }



    private void startService(Class<?> service, ServiceConnection serviceConnection, Bundle extras) {
        if (!UsbService.SERVICE_CONNECTED) {
            Intent startService = new Intent(getContext(), service);
            if (extras != null && !extras.isEmpty()) {
                Set<String> keys = extras.keySet();
                for (String key : keys) {
                    String extra = extras.getString(key);
                    startService.putExtra(key, extra);
                }
            }
            getActivity().startService(startService);
        }
        Intent bindingIntent = new Intent(getContext(), service);
        getActivity().bindService(bindingIntent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    private void setFilters() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(UsbService.ACTION_USB_PERMISSION_GRANTED);
        filter.addAction(UsbService.ACTION_NO_USB);
        filter.addAction(UsbService.ACTION_USB_DISCONNECTED);
        filter.addAction(UsbService.ACTION_USB_NOT_SUPPORTED);
        filter.addAction(UsbService.ACTION_USB_PERMISSION_NOT_GRANTED);
        getActivity().registerReceiver(mUsbReceiver, filter);
    }

    /*
     * This handler will be passed to UsbService. Data received from serial port is displayed through this handler
     */
    private static class MyHandler extends Handler {
        private final WeakReference<FragmentActivity> mFragmentActivity;

        public MyHandler(FragmentActivity activity) {
            mFragmentActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UsbService.MESSAGE_FROM_SERIAL_PORT:
                    String data = (String) msg.obj;
                    //mActivity.get().display.append(data);
                    break;
                case UsbService.CTS_CHANGE:
                    Toast.makeText(mFragmentActivity.get(), "CTS_CHANGE", Toast.LENGTH_LONG).show();
                    break;
                case UsbService.DSR_CHANGE:
                    Toast.makeText(mFragmentActivity.get(), "DSR_CHANGE", Toast.LENGTH_LONG).show();
                    break;
            }
        }
    }

    private String assembleProtocol(int n1, int n2) {
        String protocolStr = "";
        protocolStr = "<CMCMLT><ID003000><W1><FS>" + String.valueOf(n1) + "<W2><FS>" + String.valueOf(n2) + "<E>";

        return protocolStr;
    }
}
