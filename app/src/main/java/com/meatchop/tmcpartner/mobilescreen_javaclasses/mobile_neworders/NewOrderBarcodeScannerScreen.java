package com.meatchop.tmcpartner.mobilescreen_javaclasses.mobile_neworders;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import com.meatchop.tmcpartner.R;

import java.io.IOException;

public class NewOrderBarcodeScannerScreen extends AppCompatActivity {
    private SurfaceView surfaceView;
    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private static final int REQUEST_CAMERA_PERMISSION = 201;
    //This class provides methods to play DTMF tones
    private ToneGenerator toneGen1;
    private TextView barcodeText;
    private String barcodeData;
    ImageView barcodescannerIcon;
    boolean isItemAdded = false;

    SparseArray<Barcode> barcodes = new SparseArray<Barcode>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_order_barcode_scanner_screen);

        isItemAdded = false;
        barcodes = new SparseArray<Barcode>();
        toneGen1 = new ToneGenerator(AudioManager.STREAM_MUSIC,     100);
        surfaceView = findViewById(R.id.surface_view);
        barcodeText = findViewById(R.id.barcode_text);

        toneGen1 = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
        surfaceView = findViewById(R.id.surface_view);
        barcodeText = findViewById(R.id.barcode_text);

        barcodescannerIcon= findViewById(R.id.barcodescannerIcon);

        final Animation animation = AnimationUtils.loadAnimation(NewOrderBarcodeScannerScreen.this, R.anim.scanneranimation);
        barcodescannerIcon.startAnimation(animation);

        barcodeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initialiseDetectorsAndSources();

            }
        });



    }




    private void initialiseDetectorsAndSources() {

        //Toast.makeText(getApplicationContext(), "Barcode scanner started", Toast.LENGTH_SHORT).show();

        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();

        cameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setRequestedPreviewSize(1920, 1080)
                .setAutoFocusEnabled(true) //you should add this feature
                .build();

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {

                try {
                    if (ActivityCompat.checkSelfPermission(NewOrderBarcodeScannerScreen.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        cameraSource.start(surfaceView.getHolder());
                    } else {
                        ActivityCompat.requestPermissions(NewOrderBarcodeScannerScreen.this, new
                                String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);

                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });


        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
                // Toast.makeText(getApplicationContext(), "To prevent memory leaks barcode scanner has been stopped", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                if(barcodes.size()==0) {
                      barcodes = detections.getDetectedItems();
                    if (barcodes.size() != 0) {
                        Log.e("TAG", "receiveDetections  1    " + barcodes);


                        barcodeText.post(new Runnable() {

                            @Override
                            public void run() {
                                Log.e("TAG", "receiveDetections  2    " + barcodes);

                                if (barcodes.valueAt(0).email != null) {
                                    barcodeText.removeCallbacks(null);
                                    barcodeData = barcodes.valueAt(0).email.address;
                                    barcodeText.setText(barcodeData);
                                    toneGen1.startTone(ToneGenerator.TONE_CDMA_PIP, 150);
                                    Toast.makeText(NewOrderBarcodeScannerScreen.this, barcodeData, Toast.LENGTH_SHORT).show();
                                    if (barcodeData.length() > 4) {
                                        isItemAdded = true;
                                        if (barcodeData.startsWith("9990")) {
                                            Adapter_NewOrderScreenFragment_Mobile.isTMCproduct = true;
                                            if (barcodeData.length() == 14) {
                                                Adapter_NewOrderScreenFragment_Mobile.getMenuItemUsingBarCode(barcodeData);

                                            }
                                        } else {
                                            Adapter_NewOrderScreenFragment_Mobile.isTMCproduct = false;

                                        }
                                    }
                                    finish();
                                } else {

                                    barcodeData = barcodes.valueAt(0).displayValue;
                                    barcodeText.setText(barcodeData);
                                    toneGen1.startTone(ToneGenerator.TONE_CDMA_PIP, 150);
                                    Toast.makeText(NewOrderBarcodeScannerScreen.this, barcodeData, Toast.LENGTH_SHORT).show();

                                    if (barcodeData.length() > 4) {
                                        isItemAdded = true;
                                        if (barcodeData.startsWith("9990")) {
                                            Adapter_NewOrderScreenFragment_Mobile.isTMCproduct = true;
                                            if (barcodeData.length() == 14) {
                                                Adapter_NewOrderScreenFragment_Mobile.getMenuItemUsingBarCode(barcodeData);

                                            }
                                        } else {
                                            Adapter_NewOrderScreenFragment_Mobile.isTMCproduct = false;

                                        }
                                    }
                                    finish();

                                }


                            }
                        });

                    }
                }
            }
        });
    }


    @Override
    protected void onPause() {
        super.onPause();
//        getSupportActionBar().hide();
        cameraSource.release();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        getSupportActionBar().hide();
        initialiseDetectorsAndSources();
    }

}