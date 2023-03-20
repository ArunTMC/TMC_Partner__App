package com.meatchop.tmcpartner.settings;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.meatchop.tmcpartner.R;

import java.util.Set;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.BLUETOOTH_SCAN;

public class DeviceListActivity extends AppCompatActivity {
    private static final String TAG = "DeviceListActivity";
    private static final boolean D = true;

    // Return Intent extra
    final int CODE = 5;

    String[] permissionsToRequest = new String[]{};


    boolean allPermissionsGranted = true;
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public static String EXTRA_DEVICE_ADDRESS = "device_address";
    LinearLayout loadingPanel;
    LinearLayout loadingpanelmask;
    ListView pairedListView;
    ListView newDevicesListView;
    // Member fields
    private BluetoothAdapter mBtAdapter;
    private ArrayAdapter<String> mPairedDevicesArrayAdapter;
    private ArrayAdapter<String> mNewDevicesArrayAdapter;
    Context mContext;

    //  BroadcastReceiver  mReceiver=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_list);
        Button pairedDevices_button = (Button) findViewById(R.id.pairedDevices_button);
        Button allavailable_Devices_button = (Button) findViewById(R.id.allavailable_Devices_button);
        LinearLayout scanAvailableDevices_Layout = (LinearLayout) findViewById(R.id.scanAvailableDevices_Layout);
        LinearLayout paired_devices_layout = (LinearLayout) findViewById(R.id.paired_devices_layout);
        LinearLayout allAvailabledevices_layout = (LinearLayout) findViewById(R.id.allAvailabledevices_layout);
        loadingPanel = (LinearLayout) findViewById(R.id.loadingPanel_dailyItemWisereport);
        loadingpanelmask = (LinearLayout) findViewById(R.id.loadingpanelmask_dailyItemWisereport);
        pairedListView = (ListView) findViewById(R.id.paired_devices);
        newDevicesListView = (ListView) findViewById(R.id.new_devices);
        mContext = getApplicationContext();

        if (Build.VERSION.SDK_INT >= 31) {
            permissionsToRequest = new String[]{
                    Manifest.permission.BLUETOOTH_ADMIN,
                    Manifest.permission.BLUETOOTH,
                    Manifest.permission.BLUETOOTH_ADVERTISE,
                    Manifest.permission.BLUETOOTH_CONNECT,
                    BLUETOOTH_SCAN,
                    ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            };
        } else {
            permissionsToRequest = new String[]{
                    Manifest.permission.BLUETOOTH,

                    ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            };
        }
       // checkLocationPermissionforOurApp();
        // Initialize the button to perform device discovery
        // Button scanButton = (Button) findViewById(R.id.button_scan);
        scanAvailableDevices_Layout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                checkLocationPermissionforOurApp();
                doDiscovery();

                // mBtAdapter.startLeScan(mLeScanCallback);

                // v.setVisibility(View.GONE);
            }
        });


        pairedDevices_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paired_devices_layout.setVisibility(View.VISIBLE);
                allAvailabledevices_layout.setVisibility(View.GONE);


                pairedDevices_button.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.orange_selected_button_background));
                pairedDevices_button.setTextColor(Color.WHITE);

                allavailable_Devices_button.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.orange_non_selected_button_background));
                allavailable_Devices_button.setTextColor(Color.BLACK);

            }
        });


        allavailable_Devices_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkLocationPermissionforOurApp();
                doDiscovery();
                paired_devices_layout.setVisibility(View.GONE);
                allAvailabledevices_layout.setVisibility(View.VISIBLE);


                allavailable_Devices_button.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.orange_selected_button_background));
                allavailable_Devices_button.setTextColor(Color.WHITE);

                pairedDevices_button.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.orange_non_selected_button_background));
                pairedDevices_button.setTextColor(Color.BLACK);
            }
        });


    }

    private void IntializeArrayAdapter() {


        // Initialize array adapters. One for already paired devices and
        // one for newly discovered devices
        mPairedDevicesArrayAdapter = new ArrayAdapter<String>(this, R.layout.device_name);
        mNewDevicesArrayAdapter = new ArrayAdapter<String>(this, R.layout.device_name);
        //   doDiscovery();
        // Find and set up the ListView for paired devices
        pairedListView.setAdapter(mPairedDevicesArrayAdapter);
        pairedListView.setOnItemClickListener(mDeviceClickListener);

        // Find and set up the ListView for newly discovered devices
        newDevicesListView.setAdapter(mNewDevicesArrayAdapter);
        newDevicesListView.setOnItemClickListener(mDeviceClickListener);

        // Get the local Bluetooth adapter
        // getApplicationContext().registerReceiver(mReceiver,new IntentFilter(BluetoothDevice.ACTION_FOUND));
        // Register for broadcasts when a device is discovered

        mBtAdapter = BluetoothAdapter.getDefaultAdapter();
        //  checkLocationPermissionforOurApp();
        // doDiscovery();
        // Get a set of currently paired devices
        try {
            if (Build.VERSION.SDK_INT >= 31) {

                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
            }
            Set<BluetoothDevice> pairedDevices = mBtAdapter.getBondedDevices();

            // If there are paired devices, add each one to the ArrayAdapter
            if (pairedDevices.size() > 0) {
                findViewById(R.id.title_paired_devices).setVisibility(View.VISIBLE);
                for (BluetoothDevice device : pairedDevices) {
                    //Toast.makeText(DeviceListActivity.this, "device  " + device, Toast.LENGTH_SHORT).show();
                    // Toast.makeText(DeviceListActivity.this, "device.getName()   d" + device.getName(), Toast.LENGTH_SHORT).show();
                    if (Build.VERSION.SDK_INT >= 31) {

                        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }
                    }
                    String deviceidentity = device.getName() + "\n" + device.getAddress();
                    if (mPairedDevicesArrayAdapter.getPosition(deviceidentity) < 0) {
                        mPairedDevicesArrayAdapter.add(deviceidentity);

                    }
                }
            } else {
                String noDevices = getResources().getText(R.string.none_paired).toString();
                mPairedDevicesArrayAdapter.add(noDevices);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    /*BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
            // BLE device was found, we can get its information now
            Toast.makeText(DeviceListActivity.this, "device  " + device.getName(), Toast.LENGTH_SHORT).show();
            mNewDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());

            Log.i(TAG, "BLE device found: " + device.getName() + "; MAC " + device.getAddress());
        }
    };

     */

    // The on-click listener for all devices in the ListViews
    private AdapterView.OnItemClickListener mDeviceClickListener = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {
            // Cancel discovery because it's costly and we're about to connect
            mBtAdapter.cancelDiscovery();

            // Get the device MAC address, which is the last 17 chars in the View
            String info = ((TextView) v).getText().toString();
            String address = info.substring(info.length() - 17);

            // Create the result Intent and include the MAC address
            Intent intent = new Intent();
            intent.putExtra(EXTRA_DEVICE_ADDRESS, address);

            // Set result and finish this Activity
            setResult(Activity.RESULT_OK, intent);
            finish();
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        //checkLocationPermissionforOurApp();
       // doDiscovery();
    }

    void Adjusting_Widgets_Visibility(boolean show) {
        if (show) {

            loadingPanel.setVisibility(View.VISIBLE);
            loadingpanelmask.setVisibility(View.VISIBLE);


        } else {
            loadingPanel.setVisibility(View.GONE);
            loadingpanelmask.setVisibility(View.GONE);

        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        checkLocationPermissionforOurApp();
        doDiscovery();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Make sure we're not doing discovery anymore
        if (mBtAdapter != null) {
            if (Build.VERSION.SDK_INT >= 31) {

                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
            }
            mBtAdapter.cancelDiscovery();
        }
        try {
            mReceiver.abortBroadcast();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Unregister broadcast listeners
        try {


            unregisterReceiver(mReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void OnPermissionGranted() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();

        }

        IntializeArrayAdapter();


    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    /**
     * Start device discover with the BluetoothAdapter
     */
    public boolean checkLocationPermissionforOurApp() {
        if (Build.VERSION.SDK_INT >= 31) {
            if (ActivityCompat.checkSelfPermission(this, BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED) {

                // Asking user if explanation is needed
                if ((ActivityCompat.shouldShowRequestPermissionRationale(this,
                        BLUETOOTH_SCAN)) && (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.BLUETOOTH_ADMIN))) {

                    // Show an explanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.

                    //Prompt the user once explanation has been shown
                    ActivityCompat.requestPermissions(this,
                            permissionsToRequest,
                            MY_PERMISSIONS_REQUEST_LOCATION);


                } else {
                    // No explanation needed, we can request the permission.
                    ActivityCompat.requestPermissions(this,
                            permissionsToRequest,
                            MY_PERMISSIONS_REQUEST_LOCATION);
                }
                return false;

            } else {
                OnPermissionGranted();

                //Toast.makeText(this, "permission Granted", Toast.LENGTH_LONG).show();

                //  SwitchOnGps(Dashboard.this);
                return true;

            }
        }
        else{
            if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                // Asking user if explanation is needed
                if ((ActivityCompat.shouldShowRequestPermissionRationale(this,
                        ACCESS_FINE_LOCATION)) && (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION))) {

                    // Show an explanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.

                    //Prompt the user once explanation has been shown
                    ActivityCompat.requestPermissions(this,
                            permissionsToRequest,
                            MY_PERMISSIONS_REQUEST_LOCATION);


                } else {
                    // No explanation needed, we can request the permission.
                    ActivityCompat.requestPermissions(this,
                            permissionsToRequest,
                            MY_PERMISSIONS_REQUEST_LOCATION);
                }
                return false;

            } else {
                OnPermissionGranted();

                //Toast.makeText(this, "permission Granted", Toast.LENGTH_LONG).show();

                //  SwitchOnGps(Dashboard.this);
                return true;

            }
        }



    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    //  Toast.makeText(this, "permission Granted", Toast.LENGTH_SHORT).show();

                    //   SwitchOnGps(Dashboard.this);
                    OnPermissionGranted();

                    // permission was granted. Do the
                    // contacts-related task you need to do.


                } else {

                    // Permission denied, Disable the functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_SHORT).show();
                }
            }

            // other 'case' lines to check for other permissions this app might request.
            // You can add here other case statements according to your requirement.
        }
    }

    private void doDiscovery() {
        //  if (D) Log.d(TAG, "doDiscovery()");
        try {

            // Indicate scanning in the title
            //  setProgressBarIndeterminateVisibility(true);
            setTitle(R.string.scanning);

            // Turn on sub-title for new devices
            findViewById(R.id.title_new_devices).setVisibility(View.VISIBLE);
            if (Build.VERSION.SDK_INT >= 31) {

                // If we're already discovering, stop it
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
            }
            if (mBtAdapter.isDiscovering()) {
                mBtAdapter.cancelDiscovery();
            }

            Adjusting_Widgets_Visibility(true);

            IntentFilter filter = new IntentFilter();

            filter.addAction(BluetoothDevice.ACTION_FOUND);
            filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
            filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);

            getApplicationContext().registerReceiver(mReceiver, filter);
            mBtAdapter = BluetoothAdapter.getDefaultAdapter();

            // Request discover from BluetoothAdapter
            mBtAdapter.startDiscovery();
            // Toast.makeText(this, "permission Granted", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
                //discovery starts, we can show progress dialog or perform other tasks
                Toast.makeText(DeviceListActivity.this, "discovery Started ", Toast.LENGTH_SHORT).show();

            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                //discovery finishes, dismis progress dialog
                Toast.makeText(DeviceListActivity.this, "discovery finished ", Toast.LENGTH_SHORT).show();

            } else if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                //bluetooth device found
                    Toast.makeText(DeviceListActivity.this, "New device found ", Toast.LENGTH_SHORT).show();

                BluetoothDevice device = (BluetoothDevice) intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (Build.VERSION.SDK_INT >= 31) {

                    if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                }
                String deviceidentity = device.getName() + "\n" + device.getAddress();
                 if(mNewDevicesArrayAdapter.getPosition(deviceidentity)<0){
                     mNewDevicesArrayAdapter.add(deviceidentity);

                 }

                 Adjusting_Widgets_Visibility(false);

                 //  mNewDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());

            //    Toast.makeText(DeviceListActivity.this, "New device  " + device.getName(), Toast.LENGTH_SHORT).show();
            }
        }
    };



  /*  public void checkPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            } else {
                ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,}, 1);
            }
        }
    }
   @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
        } else {
            checkPermission();
        }
    }

   */
/*
    // The BroadcastReceiver that listens for discovered devices and
    // changes the title when discovery is finished
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                String action = intent.getAction();

                // When discovery finds a device
                if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                    // Get the BluetoothDevice object from the Intent
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    // If it's already paired, skip it, because it's been listed already
                    if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                        mNewDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                    }
                    // When discovery is finished, change the Activity title
                } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                    //  setProgressBarIndeterminateVisibility(false);
                    setTitle(R.string.select_device);
                    if (mNewDevicesArrayAdapter.getCount() == 0) {
                        String noDevices = getResources().getText(R.string.none_found).toString();
                        mNewDevicesArrayAdapter.add(noDevices);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    };


 */

}