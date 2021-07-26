package com.meatchop.tmcpartner;


import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class FetchAddressIntentService extends IntentService {
    private ResultReceiver resultReceiver;
    List<Address> addresses;
    String AreaName ="0";
    public FetchAddressIntentService()
    {
        super("FetchAddressIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if(intent != null)
        {
            String errormessage="";
            resultReceiver = intent.getParcelableExtra(Constants.RECEIVER);
            Log.i("tagCustomer",  "Fetch Address  "+resultReceiver);

            Geocoder geocoder = new Geocoder(this, Locale.getDefault());

               Location location = intent.getParcelableExtra(Constants.LOCATION_DATA_EXTRA);
                if (location == null) {
                    return;
                }
                addresses = null;
                try {
                    Log.i("tagCustomer",  "lat  lon  "+geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1));

                    addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                 //   Log.i("tagCustomer",  "Fetch Addressssssss  "+addresses.get(0));

                } catch (Exception e) {
                    errormessage = e.getMessage();
                }

            if(addresses == null || addresses.isEmpty())
            {
                deliverResultToReceiver(AreaName,Constants.FAILURE_RESULT,errormessage);
            }
            else{
                Address address = addresses.get(0);
                String s =  address.getThoroughfare();
                Log.i("tagCustomer",  " getThoroughfare "+s);
                s =  address.getPremises();//15/23
                Log.i("tagCustomer",  "getPremises "+s);
                s =  address.getAddressLine(0);// 15/23, Mahalakshmi Colony, Chromepet, Chennai, Tamil Nadu 600047, India
                Log.i("tagCustomer",  " getAddressLine "+s);

                s =  address.getAddressLine(1);//null
                Log.i("tagCustomer",  " getAddressLine 1 "+s);

                s =  address.getAdminArea();//Tamil Nadu
                Log.i("tagCustomer",  " getAdminArea "+s);

                s =  address.getSubAdminArea();//Kanchipuram
                Log.i("tagCustomer",  " getSubAdminArea "+s);
                s =  address.getLocality();//Chennai
                Log.i("tagCustomer",  " getLocality "+s);

                s =  address.getSubLocality();//Chromepet
                Log.i("tagCustomer",  " getSubLocality "+s);

                s =  address.getFeatureName();//15/23
                Log.i("tagCustomer",  " getFeatureName "+s);

                s =  address.getSubThoroughfare();// null
                Log.i("tagCustomer",  " getSubThoroughfare "+s);

                s = String.valueOf(address.getLocale());// en_IN
                Log.i("tagCustomer",  " getLocale "+s);

                AreaName =  address.getThoroughfare();
                Log.i("tagCustomer",  " AreaName 1 "+AreaName);

                if(AreaName == null){
                    AreaName= address.getSubLocality();
                    Log.i("tagCustomer",  " AreaName 2 "+AreaName);

                    if(AreaName == null){
                        AreaName =  address.getFeatureName();
                        Log.i("tagCustomer",  " AreaName 3 "+AreaName);

                    }
                 }

                ArrayList<String> addressFragments = new ArrayList<>();
                for(int i= 0; i <= address.getMaxAddressLineIndex();i++)

                {
                    addressFragments.add(address.getAddressLine(i));
                    Log.i("tagCustomer",  " addressFragments "+addressFragments);

                }
                deliverResultToReceiver(AreaName,Constants.SUCCESS_RESULT, TextUtils.join(Objects.requireNonNull(System.getProperty("line.separator")),addressFragments));
            }
        }
    }
    private void deliverResultToReceiver(String AreaName, int resultCode, String addressmessage){
        Log.i("tagCustomer",  "addressmessage  "+addressmessage);

        Bundle bundle =new Bundle();

        bundle.putString(Constants.TotalLocationResult,addressmessage);
        bundle.putString(Constants.AreaNameOfLocation,AreaName);
        resultReceiver.send(resultCode,bundle);
    }

}

