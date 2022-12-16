package com.meatchop.tmcpartner.Settings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.crashlytics.internal.report.model.Report;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.meatchop.tmcpartner.AlertDialogClass;
import com.meatchop.tmcpartner.Constants;
import com.meatchop.tmcpartner.MobileScreen_JavaClasses.ManageOrders.Adapter_Mobile_ManageOrders_ListView1;
import com.meatchop.tmcpartner.MobileScreen_JavaClasses.ManageOrders.Mobile_ManageOrders1;
import com.meatchop.tmcpartner.PosScreen_JavaClasses.ManageOrders.Modal_ManageOrders_Pojo_Class;
import com.meatchop.tmcpartner.PosScreen_JavaClasses.Other_javaClasses.Pos_Dashboard_Screen;
import com.meatchop.tmcpartner.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.TimeZone;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.os.Build.VERSION.SDK_INT;
import static com.meatchop.tmcpartner.Constants.TAG;

public class RaisedTicketDetailsForRating extends AppCompatActivity {
    ImageView search_button,search_close_btn;
    LinearLayout newTicketsSync_Layout,loadingpanelmask,loadingPanel;
    ListView raisedtickets_ListView;
    TextView vendorName_textWidget,count_textwidget,generateReport,ticketsinstruction,nameofFacility_Textview;
    String vendorKey, vendorName;
    EditText mobile_search_barEditText;
    ArrayList<Modal_RaisedTicketsRatingDetails> sortedraisedTicketsRatingDetailsArray = new ArrayList();

    static ArrayList<Modal_RaisedTicketsRatingDetails> raisedTicketsRatingDetailsArray = new ArrayList();
    ArrayList<Modal_RaisedTicketsRatingDetails> closedTicketsRatingDetailsArray = new ArrayList();
    boolean isSearchButtonClicked = false;
    private static int REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION = 1;
    private static final int OPENPDF_ACTIVITY_REQUEST_CODE = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_raised_ticket_details_for_rating);
        nameofFacility_Textview= findViewById(R.id.nameofFacility_Textview);
        raisedtickets_ListView = findViewById(R.id.raisedtickets_ListView);
        generateReport = findViewById(R.id.generateReport);
        vendorName_textWidget = findViewById(R.id.vendorName_textWidget);
        count_textwidget = findViewById(R.id.count_textwidget);
        newTicketsSync_Layout = findViewById(R.id.newTicketsSync_Layout);
        search_button = findViewById(R.id.search_button);
        search_close_btn = findViewById(R.id.search_close_btn);
        loadingpanelmask = findViewById(R.id.loadingpanelmask);
        loadingPanel = findViewById(R.id.loadingPanel);
        ticketsinstruction =  findViewById(R.id.ticketsinstruction);
        mobile_search_barEditText =  findViewById(R.id.search_barEdit);



        generateReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                showProgressBar(true);
                getDetailsAboutTicketsCLosedTodayFromDB();
            }
        });


        search_close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(mobile_search_barEditText);
                closeSearchBarEditText();
                mobile_search_barEditText.setText("");
                isSearchButtonClicked =false;

                DisplayRatingTicketsList(raisedTicketsRatingDetailsArray);
            }
        });
        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int textlength = mobile_search_barEditText.getText().toString().length();
                isSearchButtonClicked =true;
                showKeyboard(mobile_search_barEditText);
                showSearchBarEditText();
            }
        });

        try
        {
            SharedPreferences shared = getSharedPreferences("VendorLoginData", MODE_PRIVATE);
            vendorKey = shared.getString("VendorKey", "");
            vendorName = shared.getString("VendorName", "");


        } catch (Exception e) {
            e.printStackTrace();
        }


        try
        {
            vendorName_textWidget.setText(vendorName);
        } catch (Exception e) {
            e.printStackTrace();
        }


        try
        {
            showProgressBar(true);


            raisedTicketsRatingDetailsArray = getRaisedTicketDetailsFromDB(vendorKey);



        } catch (Exception e) {
                e.printStackTrace();
        }



        mobile_search_barEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                sortedraisedTicketsRatingDetailsArray.clear();

            }

            @Override
            public void afterTextChanged(Editable editable) {
                sortedraisedTicketsRatingDetailsArray.clear();
                isSearchButtonClicked =true;
                String mobileNo = (editable.toString());
                if(!mobileNo.equals("")) {
                    String orderstatus = "";

                    for (int i = 0; i < raisedTicketsRatingDetailsArray.size(); i++) {
                        try {


                            mobileNo = mobileNo;
                            final Modal_RaisedTicketsRatingDetails modal_raisedTicketsRatingDetails = new Modal_RaisedTicketsRatingDetails();
                            final Modal_RaisedTicketsRatingDetails modalraisedTicketsRatingDetailss = raisedTicketsRatingDetailsArray.get(i);
                            String mobilenumber = modalraisedTicketsRatingDetailss.getUsermobileno();

                            if (mobilenumber.contains("+91" + mobileNo)) {

                                //Log.d(Constants.TAG, "displayorderDetailsinListview orderid: " + modal_manageOrders_forOrderDetailList.getOrderid());
                                modal_raisedTicketsRatingDetails.orderid = modalraisedTicketsRatingDetailss.getOrderid();
                                modal_raisedTicketsRatingDetails.description = modalraisedTicketsRatingDetailss.getDescription();
                                modal_raisedTicketsRatingDetails.key = modalraisedTicketsRatingDetailss.getKey();
                                modal_raisedTicketsRatingDetails.ticketclosedtime = modalraisedTicketsRatingDetailss.getTicketclosedtime();
                                modal_raisedTicketsRatingDetails.ticketraisedtime = modalraisedTicketsRatingDetailss.getTicketraisedtime();
                                modal_raisedTicketsRatingDetails.status = modalraisedTicketsRatingDetailss.getStatus();
                                modal_raisedTicketsRatingDetails.userkey = modalraisedTicketsRatingDetailss.getUserkey();
                                modal_raisedTicketsRatingDetails.vendorkey = modalraisedTicketsRatingDetailss.getVendorkey();
                                modal_raisedTicketsRatingDetails.vendorname = modalraisedTicketsRatingDetailss.getVendorname();
                                modal_raisedTicketsRatingDetails.usermobileno = modalraisedTicketsRatingDetailss.getUsermobileno();
                                modal_raisedTicketsRatingDetails.isOrderDetailsScreenOpened = modalraisedTicketsRatingDetailss.getisOrderDetailsScreenOpened();
                                modal_raisedTicketsRatingDetails.modal_orderDetails_tracking_ratingDetails = modalraisedTicketsRatingDetailss.getModal_orderDetails_tracking_ratingDetails();


                                sortedraisedTicketsRatingDetailsArray.add(modal_raisedTicketsRatingDetails);


                            }

                        } catch (Exception e) {
                            e.printStackTrace();

                        }
                    }
                    try {
                        if (sortedraisedTicketsRatingDetailsArray.size() > 0) {
                         DisplayRatingTicketsList(sortedraisedTicketsRatingDetailsArray);

                        }
                        else {

                                raisedtickets_ListView.setVisibility(View.GONE);
                                ticketsinstruction.setVisibility(View.VISIBLE);
                                ticketsinstruction.setText("No orders found for this Mobile number");

                            }



                    } catch (Exception E) {
                        E.printStackTrace();
                    }


                }
                else{
                    raisedtickets_ListView.setVisibility(View.GONE);
                    ticketsinstruction.setVisibility(View.VISIBLE);
                    ticketsinstruction.setText("No orders found for this Mobile number");

                }

            }
        });





        newTicketsSync_Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    showProgressBar(true);

                    raisedTicketsRatingDetailsArray = getRaisedTicketDetailsFromDB(vendorKey);




            }
        });






    }

    private void getDetailsAboutTicketsCLosedTodayFromDB() {


    String CurrentDate =getDate();

        Log.d(TAG, "starting:getfullMenuItemStockavldetailsUsingStoreID ");
        closedTicketsRatingDetailsArray.clear();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.api_getratingticketsforvendorkeyandstatusandCloseddate+"?ticketclosedtime="+CurrentDate+"&vendorkey="+vendorKey+"&status=CLOSED",
                null, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(@NonNull JSONObject response) {
                try{
                     closedTicketsRatingDetailsArray.clear();

                }
                catch (Exception e){
                    e.printStackTrace();
                }
                try{
                    Log.d(TAG, "starting:onResponse ");

                    Log.d(TAG, "response for closed: " + response.length());

                    try {
                        JSONArray JArray = response.getJSONArray("content");
                        Log.d(Constants.TAG, "convertingJsonStringintoArray closed: " + JArray);
                        int i1 = 0;
                        int arrayLength = JArray.length();
                        Log.d("Constants.TAG", "convertingJsonStringintoArray closed: " + arrayLength);
                        if(arrayLength==0){
                            showProgressBar(false);
                            AlertDialogClass.showDialog(RaisedTicketDetailsForRating.this, R.string.You_didnot_close_any_tickets_instruction);
                        }
                        String Key="";
                        for (; i1 < (arrayLength); i1++) {
                            JSONObject json = JArray.getJSONObject(i1);
                            Modal_RaisedTicketsRatingDetails modal_raisedTicketsRatingDetails = new Modal_RaisedTicketsRatingDetails();

                            try{
                                if(json.has("key")){
                                    modal_raisedTicketsRatingDetails.key = json.getString("key");
                                }
                                else{
                                    modal_raisedTicketsRatingDetails.key="";
                                }
                            }
                            catch (Exception e){
                                modal_raisedTicketsRatingDetails.key="";
                                e.printStackTrace();
                            }

                            try{
                                if(json.has("qualityrating")){
                                    modal_raisedTicketsRatingDetails.qualityrating = json.getString("qualityrating");
                                }
                                else{
                                    modal_raisedTicketsRatingDetails.qualityrating="";
                                }
                            }
                            catch (Exception e){
                                modal_raisedTicketsRatingDetails.qualityrating="";
                                e.printStackTrace();
                            }


                            try{
                                if(json.has("deliveryrating")){
                                    modal_raisedTicketsRatingDetails.deliveryrating = json.getString("deliveryrating");
                                }
                                else{
                                    modal_raisedTicketsRatingDetails.deliveryrating="";
                                }
                            }
                            catch (Exception e){
                                modal_raisedTicketsRatingDetails.deliveryrating="";
                                e.printStackTrace();
                            }


                            try{
                                if(json.has("feedback")){
                                    modal_raisedTicketsRatingDetails.feedback = json.getString("feedback");
                                }
                                else{
                                    modal_raisedTicketsRatingDetails.feedback="";
                                }
                            }
                            catch (Exception e){
                                modal_raisedTicketsRatingDetails.feedback="";
                                e.printStackTrace();
                            }




                            try{
                                if(json.has("orderid")){
                                    modal_raisedTicketsRatingDetails.orderid = json.getString("orderid");
                                }
                                else{
                                    modal_raisedTicketsRatingDetails.orderid="";
                                }
                            }
                            catch (Exception e){
                                modal_raisedTicketsRatingDetails.orderid="";
                                e.printStackTrace();
                            }

                            try{
                                if(json.has("ticketclosedtime")){
                                    modal_raisedTicketsRatingDetails.ticketclosedtime = json.getString("ticketclosedtime");
                                }
                                else{
                                    modal_raisedTicketsRatingDetails.ticketclosedtime="";
                                }
                            }
                            catch (Exception e){
                                modal_raisedTicketsRatingDetails.ticketclosedtime="";
                                e.printStackTrace();
                            }


                            try{
                                if(json.has("ticketraisedtime")){
                                    modal_raisedTicketsRatingDetails.ticketraisedtime = json.getString("ticketraisedtime");
                                }
                                else{
                                    modal_raisedTicketsRatingDetails.ticketraisedtime="";
                                }
                            }
                            catch (Exception e){
                                modal_raisedTicketsRatingDetails.ticketraisedtime="";
                                e.printStackTrace();
                            }

                            try{
                                if(json.has("usermobileno")){
                                    modal_raisedTicketsRatingDetails.usermobileno = json.getString("usermobileno");
                                }
                                else{
                                    modal_raisedTicketsRatingDetails.usermobileno="";
                                }
                            }
                            catch (Exception e){
                                modal_raisedTicketsRatingDetails.usermobileno="";
                                e.printStackTrace();
                            }



                            try{
                                if(json.has("vendorkey")){
                                    modal_raisedTicketsRatingDetails.vendorkey = json.getString("vendorkey");
                                }
                                else{
                                    modal_raisedTicketsRatingDetails.vendorkey="";
                                }
                            }
                            catch (Exception e){
                                modal_raisedTicketsRatingDetails.vendorkey="";
                                e.printStackTrace();
                            }

                            try{
                                if(json.has("vendorname")){
                                    modal_raisedTicketsRatingDetails.vendorname = json.getString("vendorname");
                                }
                                else{
                                    modal_raisedTicketsRatingDetails.vendorname="";
                                }
                            }
                            catch (Exception e){
                                modal_raisedTicketsRatingDetails.vendorname="";
                                e.printStackTrace();
                            }


                            try{
                                if(json.has("status")){
                                    modal_raisedTicketsRatingDetails.status = json.getString("status");
                                }
                                else{
                                    modal_raisedTicketsRatingDetails.status="";
                                }
                            }
                            catch (Exception e){
                                modal_raisedTicketsRatingDetails.status="";
                                e.printStackTrace();
                            }
                            try{
                                if(json.has("description")){
                                    modal_raisedTicketsRatingDetails.description = json.getString("description");
                                }
                                else{
                                    modal_raisedTicketsRatingDetails.description="";
                                }
                            }
                            catch (Exception e){
                                modal_raisedTicketsRatingDetails.description="";
                                e.printStackTrace();
                            }

                            closedTicketsRatingDetailsArray.add(modal_raisedTicketsRatingDetails);



                            if(arrayLength - i1 == 1){

                                if (SDK_INT >= Build.VERSION_CODES.R) {

                                    if(Environment.isExternalStorageManager()){
                                        try {
                                            exportReport(raisedTicketsRatingDetailsArray,closedTicketsRatingDetailsArray);


                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            ;
                                        }
                                    }
                                    else{
                                        try {
                                            Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                                            intent.addCategory("android.intent.category.DEFAULT");
                                            intent.setData(Uri.parse(String.format("package:%s",getApplicationContext().getPackageName())));
                                            startActivityForResult(intent, 2296);
                                        } catch (Exception e) {
                                            Intent intent = new Intent();
                                            intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                                            startActivityForResult(intent, 2296);
                                        }
                                    }

                                } else {


                                    int writeExternalStoragePermission = ContextCompat.checkSelfPermission(RaisedTicketDetailsForRating.this, WRITE_EXTERNAL_STORAGE);
                                    //Log.d("ExportInvoiceActivity", "writeExternalStoragePermission "+writeExternalStoragePermission);
                                    // If do not grant write external storage permission.
                                    if (writeExternalStoragePermission != PackageManager.PERMISSION_GRANTED) {
                                        // Request user to grant write external storage permission.
                                        ActivityCompat.requestPermissions(RaisedTicketDetailsForRating.this, new String[]{WRITE_EXTERNAL_STORAGE},
                                                REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION);
                                    } else {
                                        showProgressBar(true);
                                        try {
                                            exportReport(raisedTicketsRatingDetailsArray,closedTicketsRatingDetailsArray);

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            ;
                                        }
                                    }
                                }
                                /*
                                int writeExternalStoragePermission = ContextCompat.checkSelfPermission(RaisedTicketDetailsForRating.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                                //Log.d("ExportInvoiceActivity", "writeExternalStoragePermission " + writeExternalStoragePermission);
                                // If do not grant write external storage permission.
                                if (writeExternalStoragePermission != PackageManager.PERMISSION_GRANTED) {
                                    // Request user to grant write external storage permission.
                                    ActivityCompat.requestPermissions(RaisedTicketDetailsForRating.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                            REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION);
                                } else {
                                    showProgressBar(true);
                                    try {
                                        exportReport(raisedTicketsRatingDetailsArray,closedTicketsRatingDetailsArray);
                                    }catch (Exception e ){
                                        e.printStackTrace();
                                    }
                                }

                                 */
                            }
                        }



                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }catch(Exception e){
                    e.printStackTrace();
                }




            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                Log.d(TAG, "Error: " + error.getLocalizedMessage());
                Log.d(TAG, "Error: " + error.getMessage());
                Log.d(TAG, "Error: " + error.toString());

                error.printStackTrace();
            }
        }) {


            @NonNull
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                final Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json");
                params.put("storeid",vendorKey);

                return params;
            }
        };
        RetryPolicy policy = new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);

        // Make the request
        Volley.newRequestQueue(RaisedTicketDetailsForRating.this).add(jsonObjectRequest);









    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case OPENPDF_ACTIVITY_REQUEST_CODE:
                setResult(RESULT_OK);
                finish();
                break;

            default:
                break;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 2296) {
            if (SDK_INT >= Build.VERSION_CODES.R) {
                if (Environment.isExternalStorageManager()) {
                    // perform action when allow permission success
                    try {
                        exportReport(raisedTicketsRatingDetailsArray, closedTicketsRatingDetailsArray);

                    } catch (Exception e) {
                        e.printStackTrace();
                        ;
                    }
                } else {
                    Toast.makeText(this, "Allow permission for storage access!", Toast.LENGTH_SHORT).show();
                }
            }
        }

        else{
        if (requestCode == REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION) {
            int grantResultsLength = grantResults.length;
            if (grantResultsLength > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getApplicationContext(), "You grant write external storage permission. Please click original button again to continue.", Toast.LENGTH_LONG).show();
                // exportInvoice();
                try {
                    exportReport(raisedTicketsRatingDetailsArray, closedTicketsRatingDetailsArray);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(getApplicationContext(), "You denied write external storage permission.", Toast.LENGTH_LONG).show();
            }
        }
    }
    }







    private void exportReport(ArrayList<Modal_RaisedTicketsRatingDetails> raisedTicketsRatingDetailsArray, ArrayList<Modal_RaisedTicketsRatingDetails> closedTicketsRatingDetailsArray) {


        if (((raisedTicketsRatingDetailsArray == null) || (raisedTicketsRatingDetailsArray.size() <= 0))&&(((closedTicketsRatingDetailsArray == null) || (closedTicketsRatingDetailsArray.size() <= 0)) )) {
            return;
        }
        String extstoragedir = Environment.getExternalStorageDirectory().toString();
        String  path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/TMCPartner/RatingTicketDetailsReport/";
        //Log.d("PdfUtil", "external storage state " + state + " extstoragedir " + extstoragedir);
        //File fol = new File(extstoragedir, "RatingTicketDetailsReport");
        File folder = new File(path);
        if (!folder.exists()) {
            boolean bool = folder.mkdirs();
        }
        try {
            String filename = "RatingTicketDetails_" + System.currentTimeMillis() + ".pdf";
            final File file = new File(folder, filename);
            file.createNewFile();
            FileOutputStream fOut = new FileOutputStream(file);

            // if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            // document = new PdfDocument(new PdfWriter("MyFirstInvoice.pdf"));

            Document layoutDocument = new Document();
            PdfWriter.getInstance(layoutDocument, fOut);
            layoutDocument.open();

            addVendorDetails(layoutDocument);
            addItemRows(layoutDocument,raisedTicketsRatingDetailsArray,closedTicketsRatingDetailsArray);

            layoutDocument.close();
            showProgressBar(false);

            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());

            Intent pdfViewIntent = new Intent(Intent.ACTION_VIEW);
            pdfViewIntent.setDataAndType(Uri.fromFile(file), "application/pdf");
            pdfViewIntent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            pdfViewIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            Intent intent = Intent.createChooser(pdfViewIntent, "Open File");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            try {
                showProgressBar(false);

                startActivityForResult(intent, OPENPDF_ACTIVITY_REQUEST_CODE);
            } catch (ActivityNotFoundException e) {
                e.printStackTrace();
                // Instruct the user to install a PDF reader here, or something
            }
            // }
        } catch (IOException e) {
            showProgressBar(false);

            //Log.i("error", e.getLocalizedMessage());
        } catch (Exception ex) {
            showProgressBar(false);

            ex.printStackTrace();
        }


    }



    private void addVendorDetails(Document layoutDocument) {
        try {
            SharedPreferences sharedPreferences
                    = getSharedPreferences("VendorLoginData",
                    MODE_PRIVATE);

            String Vendorname = sharedPreferences.getString("VendorName", "");

            com.itextpdf.text.Font boldFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.TIMES_ROMAN, 22, Font.BOLDITALIC);
            com.itextpdf.text.Paragraph titlepara = new com.itextpdf.text.Paragraph("Raised Rating Ticket Details ");
            titlepara.setSpacingBefore(5);
            titlepara.setFont(boldFont);
            titlepara.setAlignment(Element.ALIGN_CENTER);
            layoutDocument.add(titlepara);

            String vendorname = "Vendor: " + Vendorname;
            com.itextpdf.text.Paragraph vendorpara = new com.itextpdf.text.Paragraph(vendorname);
            vendorpara.setSpacingBefore(20);
            vendorpara.setAlignment(Element.ALIGN_LEFT);
            layoutDocument.add(vendorpara);
            String DateString = getDate_and_time();
            com.itextpdf.text.Paragraph datepara = new com.itextpdf.text.Paragraph("Date: " + DateString);
            datepara.setAlignment(Element.ALIGN_LEFT);
            datepara.setSpacingBefore(5);
            datepara.setSpacingAfter(20);
            layoutDocument.add(datepara);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void addItemRows(Document layoutDocument,ArrayList<Modal_RaisedTicketsRatingDetails> raisedTicketsRatingDetailsArray, ArrayList<Modal_RaisedTicketsRatingDetails> closedTicketsRatingDetailsArray) {
        try {


            PdfPTable openTicketsheadertitle = new PdfPTable(2);
            openTicketsheadertitle.setWidthPercentage(100);
            openTicketsheadertitle.setSpacingBefore(20);


            PdfPCell openTicketsheader;
            openTicketsheader = new PdfPCell(new Phrase("No.of.Tickets remain Open till Date :  " + raisedTicketsRatingDetailsArray.size()));
            openTicketsheader.setBorder(Rectangle.BOX);
          openTicketsheader.setBorderColor(BaseColor.RED);

            openTicketsheader.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
            openTicketsheader.setVerticalAlignment(Element.ALIGN_MIDDLE);
            openTicketsheader.setFixedHeight(25);
            openTicketsheader.setPaddingRight(20);
            openTicketsheadertitle.addCell(openTicketsheader);





            PdfPCell Ticketsclosedtodayheader;
            Ticketsclosedtodayheader = new PdfPCell(new Phrase("No.of.Tickets Closed Today  : " + closedTicketsRatingDetailsArray.size()));
            Ticketsclosedtodayheader.setBorder(Rectangle.BOX);
            Ticketsclosedtodayheader.setBorderColor(BaseColor.RED);
            Ticketsclosedtodayheader.setHorizontalAlignment(Element.ALIGN_RIGHT);
            Ticketsclosedtodayheader.setVerticalAlignment(Element.ALIGN_MIDDLE);
            Ticketsclosedtodayheader.setFixedHeight(25);
            Ticketsclosedtodayheader.setPaddingRight(20);
            openTicketsheadertitle.addCell(Ticketsclosedtodayheader);


            layoutDocument.add(openTicketsheadertitle);


/*

            PdfPTable emptyheadertitle = new PdfPTable(1);
            emptyheadertitle.setWidthPercentage(100);
            emptyheadertitle.setSpacingBefore(20);


            PdfPCell emptyheader;
            emptyheader = new PdfPCell(new Phrase("                                    "));
            emptyheader.setBorder(Rectangle.NO_BORDER);
            emptyheader.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
            emptyheader.setVerticalAlignment(Element.ALIGN_MIDDLE);
            emptyheader.setFixedHeight(5);
            emptyheader.setPaddingRight(20);
            emptyheadertitle.addCell(emptyheader);
            layoutDocument.add(emptyheadertitle);


 */



            PdfPTable tablePaymentModetitle = new PdfPTable(1);
            tablePaymentModetitle.setWidthPercentage(100);
            tablePaymentModetitle.setSpacingBefore(20);


            PdfPCell closedTicketsheader;
            closedTicketsheader = new PdfPCell(new Phrase("  Details of Tickets Closed Today  "));
            closedTicketsheader.setBorder(Rectangle.BOTTOM);
            closedTicketsheader.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
            closedTicketsheader.setVerticalAlignment(Element.ALIGN_MIDDLE);
            closedTicketsheader.setFixedHeight(25);
            closedTicketsheader.setPaddingRight(20);
            tablePaymentModetitle.addCell(closedTicketsheader);
            layoutDocument.add(tablePaymentModetitle);
            for(int i =0; i<closedTicketsRatingDetailsArray.size();i++){
                Modal_RaisedTicketsRatingDetails modal_raisedTicketsRatingDetails = closedTicketsRatingDetailsArray.get(i);

                PdfPTable tablePaymentModetitle1 = new PdfPTable(1);
                tablePaymentModetitle1.setWidthPercentage(100);
                tablePaymentModetitle1.setSpacingBefore(20);


                PdfPCell Orderidcell;
                Orderidcell = new PdfPCell(new Phrase("Orderid :  "+ modal_raisedTicketsRatingDetails.getOrderid()));
                Orderidcell.setBorder(Rectangle.NO_BORDER);
                Orderidcell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
                Orderidcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                Orderidcell.setFixedHeight(25);
                Orderidcell.setPaddingRight(20);
                tablePaymentModetitle1.addCell(Orderidcell);




                PdfPCell UserMobilenumbercell;
                UserMobilenumbercell = new PdfPCell(new Phrase("User Mobile Number :  "+ modal_raisedTicketsRatingDetails.getUsermobileno()));
                UserMobilenumbercell.setBorder(Rectangle.NO_BORDER);
                UserMobilenumbercell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
                UserMobilenumbercell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                UserMobilenumbercell.setFixedHeight(25);
                UserMobilenumbercell.setPaddingRight(20);
                tablePaymentModetitle1.addCell(UserMobilenumbercell);


                PdfPCell QualityANdDeliveryRatingcell;
                QualityANdDeliveryRatingcell = new PdfPCell(new Phrase("Quality  Rating : "+ modal_raisedTicketsRatingDetails.getQualityrating() + "              Delivery  Rating : "+ modal_raisedTicketsRatingDetails.getDeliveryrating()));
                QualityANdDeliveryRatingcell.setBorder(Rectangle.NO_BORDER);
                QualityANdDeliveryRatingcell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
                QualityANdDeliveryRatingcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                QualityANdDeliveryRatingcell.setFixedHeight(25);
                QualityANdDeliveryRatingcell.setPaddingRight(20);
                tablePaymentModetitle1.addCell(QualityANdDeliveryRatingcell);

                PdfPCell userFeedBackcell;
                userFeedBackcell = new PdfPCell(new Phrase("Feed Back : "+ modal_raisedTicketsRatingDetails.getFeedback()));
                userFeedBackcell.setBorder(Rectangle.NO_BORDER);
                userFeedBackcell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
                userFeedBackcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                userFeedBackcell.setPaddingRight(20);
                tablePaymentModetitle1.addCell(userFeedBackcell);


                PdfPCell emptyUserFeedBackcell;
                emptyUserFeedBackcell = new PdfPCell(new Phrase(""));
                emptyUserFeedBackcell.setBorder(Rectangle.NO_BORDER);
                emptyUserFeedBackcell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
                emptyUserFeedBackcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                emptyUserFeedBackcell.setFixedHeight(5);

                emptyUserFeedBackcell.setPaddingRight(20);
                tablePaymentModetitle1.addCell(emptyUserFeedBackcell);


                PdfPCell TicketRaisedTimecell;
                TicketRaisedTimecell = new PdfPCell(new Phrase("Ticket Raised & Closed On  :  "+ modal_raisedTicketsRatingDetails.getTicketraisedtime() +"  -  "+ modal_raisedTicketsRatingDetails.getTicketclosedtime()));
                TicketRaisedTimecell.setBorder(Rectangle.NO_BORDER);
                TicketRaisedTimecell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
                TicketRaisedTimecell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                TicketRaisedTimecell.setFixedHeight(25);
                TicketRaisedTimecell.setPaddingRight(20);
                tablePaymentModetitle1.addCell(TicketRaisedTimecell);

            /*
                PdfPCell TicketClosedTimecell;
                TicketClosedTimecell = new PdfPCell(new Phrase("Ticket Closed On  :  "+ modal_raisedTicketsRatingDetails.getTicketclosedtime()));
                TicketClosedTimecell.setBorder(Rectangle.NO_BORDER);
                TicketClosedTimecell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
                TicketClosedTimecell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                TicketClosedTimecell.setFixedHeight(25);
                TicketClosedTimecell.setPaddingRight(20);
                tablePaymentModetitle1.addCell(TicketClosedTimecell);


             */

                PdfPCell Dscriptioncell;
                Dscriptioncell = new PdfPCell(new Phrase("Description Added When Ticket Closed :  "+ modal_raisedTicketsRatingDetails.getDescription()));
                Dscriptioncell.setBorder(Rectangle.NO_BORDER);
                Dscriptioncell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
                Dscriptioncell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                Dscriptioncell.setPaddingRight(20);
                tablePaymentModetitle1.addCell(Dscriptioncell);

                PdfPCell pricecell2 = new PdfPCell(new Phrase(""));
                pricecell2.setBorder(Rectangle.NO_BORDER);
                pricecell2.setBackgroundColor(BaseColor.WHITE);
                pricecell2.setHorizontalAlignment(Element.ALIGN_RIGHT);
                pricecell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
                pricecell2.setFixedHeight(30);
                pricecell2.setPaddingRight(10);
                tablePaymentModetitle1.addCell(pricecell2);

                PdfPCell pricecell = new PdfPCell(new Phrase(""));
                pricecell.setBorder(Rectangle.NO_BORDER);
                pricecell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                pricecell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                pricecell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                pricecell.setFixedHeight(30);
                pricecell.setPaddingRight(10);
                tablePaymentModetitle1.addCell(pricecell);


                layoutDocument.add(tablePaymentModetitle1);

            }







        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void showProgressBar(boolean show) {
        if(show){
            loadingPanel.setVisibility(View.VISIBLE);
            loadingpanelmask.setVisibility(View.VISIBLE);

        }
        else{
            loadingPanel.setVisibility(View.GONE);
            loadingpanelmask.setVisibility(View.GONE);
        }
    }



    private void DisplayRatingTicketsList(ArrayList<Modal_RaisedTicketsRatingDetails> raisedTicketsRatingDetailsArray) {
        count_textwidget.setText(String.valueOf( raisedTicketsRatingDetailsArray.size()));
        ticketsinstruction.setVisibility(View.GONE);
        raisedtickets_ListView.setVisibility(View.VISIBLE);

        Adapter_RaisedTicketListForRating adapter_raisedTicketListForRating = new Adapter_RaisedTicketListForRating (RaisedTicketDetailsForRating.this,raisedTicketsRatingDetailsArray,RaisedTicketDetailsForRating.this);
        raisedtickets_ListView.setAdapter(adapter_raisedTicketListForRating);

        showProgressBar(false);


    }

    private ArrayList<Modal_RaisedTicketsRatingDetails> getRaisedTicketDetailsFromDB(String vendorKey) {
        ArrayList<Modal_RaisedTicketsRatingDetails> raisedTicketsArrayListt = new ArrayList();

        Log.d(TAG, "starting:getfullMenuItemStockavldetailsUsingStoreID ");
        raisedTicketsRatingDetailsArray.clear();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.api_getListofAllRaisedOpenStateTickets+vendorKey,
                null, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(@NonNull JSONObject response) {
                try{
                    raisedTicketsRatingDetailsArray.clear();

                }
                catch (Exception e){
                    e.printStackTrace();
                }
                try{
                    Log.d(TAG, "starting:onResponse ");

                    Log.d(TAG, "response for addMenuListAdaptertoListView: " + response.length());

                    try {
                        JSONArray JArray = response.getJSONArray("content");
                        Log.d(Constants.TAG, "convertingJsonStringintoArray Response: " + JArray);
                        int i1 = 0;
                        int arrayLength = JArray.length();
                        Log.d("Constants.TAG", "convertingJsonStringintoArray Response: " + arrayLength);
                        if(arrayLength==0){
                            showProgressBar(false);
                            ticketsinstruction.setText("There is no raised ticket available");
                            ticketsinstruction.setVisibility(View.VISIBLE);
                            raisedtickets_ListView.setVisibility(View.GONE);

                        }
                        String Key="";
                        for (; i1 < (arrayLength); i1++) {
                            JSONObject json = JArray.getJSONObject(i1);
                            Modal_RaisedTicketsRatingDetails modal_raisedTicketsRatingDetails = new Modal_RaisedTicketsRatingDetails();
                            
                            try{
                                if(json.has("key")){
                                    modal_raisedTicketsRatingDetails.key = json.getString("key");
                                }
                                else{
                                    modal_raisedTicketsRatingDetails.key="";
                                }
                            }
                            catch (Exception e){
                                modal_raisedTicketsRatingDetails.key="";
                                 e.printStackTrace();
                            }


                            try{
                                if(json.has("orderid")){
                                    modal_raisedTicketsRatingDetails.orderid = json.getString("orderid");
                                }
                                else{
                                    modal_raisedTicketsRatingDetails.orderid="";
                                }
                            }
                            catch (Exception e){
                                modal_raisedTicketsRatingDetails.orderid="";
                                e.printStackTrace();
                            }




                            try{
                                if(json.has("ticketraisedtime")){
                                    modal_raisedTicketsRatingDetails.ticketraisedtime = json.getString("ticketraisedtime");
                                }
                                else{
                                    modal_raisedTicketsRatingDetails.ticketraisedtime="";
                                }
                            }
                            catch (Exception e){
                                modal_raisedTicketsRatingDetails.ticketraisedtime="";
                                e.printStackTrace();
                            }

                            try{
                                if(json.has("usermobileno")){
                                    modal_raisedTicketsRatingDetails.usermobileno = json.getString("usermobileno");
                                }
                                else{
                                    modal_raisedTicketsRatingDetails.usermobileno="";
                                }
                            }
                            catch (Exception e){
                                modal_raisedTicketsRatingDetails.usermobileno="";
                                e.printStackTrace();
                            }



                            try{
                                if(json.has("vendorkey")){
                                    modal_raisedTicketsRatingDetails.vendorkey = json.getString("vendorkey");
                                }
                                else{
                                    modal_raisedTicketsRatingDetails.vendorkey="";
                                }
                            }
                            catch (Exception e){
                                modal_raisedTicketsRatingDetails.vendorkey="";
                                e.printStackTrace();
                            }

                            try{
                                if(json.has("vendorname")){
                                    modal_raisedTicketsRatingDetails.vendorname = json.getString("vendorname");
                                }
                                else{
                                    modal_raisedTicketsRatingDetails.vendorname="";
                                }
                            }
                            catch (Exception e){
                                modal_raisedTicketsRatingDetails.vendorname="";
                                e.printStackTrace();
                            }


                            try{
                                if(json.has("qualityrating")){
                                    modal_raisedTicketsRatingDetails.qualityrating = json.getString("qualityrating");
                                }
                                else{
                                    modal_raisedTicketsRatingDetails.qualityrating="";
                                }
                            }
                            catch (Exception e){
                                modal_raisedTicketsRatingDetails.qualityrating="";
                                e.printStackTrace();
                            }


                            try{
                                if(json.has("deliveryrating")){
                                    modal_raisedTicketsRatingDetails.deliveryrating = json.getString("deliveryrating");
                                }
                                else{
                                    modal_raisedTicketsRatingDetails.deliveryrating="";
                                }
                            }
                            catch (Exception e){
                                modal_raisedTicketsRatingDetails.deliveryrating="";
                                e.printStackTrace();
                            }



                            try{
                                if(json.has("feedback")){
                                    modal_raisedTicketsRatingDetails.feedback = json.getString("feedback");
                                }
                                else{
                                    modal_raisedTicketsRatingDetails.feedback="";
                                }
                            }
                            catch (Exception e){
                                modal_raisedTicketsRatingDetails.feedback="";
                                e.printStackTrace();
                            }



                            try{
                                if(json.has("status")){
                                    modal_raisedTicketsRatingDetails.status = json.getString("status");
                                }
                                else{
                                    modal_raisedTicketsRatingDetails.status="";
                                }
                            }
                            catch (Exception e){
                                modal_raisedTicketsRatingDetails.status="";
                                e.printStackTrace();
                            }
                            try{
                                if(json.has("description")){
                                    modal_raisedTicketsRatingDetails.description = json.getString("description");
                                }
                                else{
                                    modal_raisedTicketsRatingDetails.description="";
                                }
                            }
                            catch (Exception e){
                                modal_raisedTicketsRatingDetails.description="";
                                e.printStackTrace();
                            }


                            try{
                                    modal_raisedTicketsRatingDetails.modal_orderDetails_tracking_ratingDetails =  new Modal_OrderDetails_Tracking_RatingDetails();

                            }
                            catch (Exception e){
                                modal_raisedTicketsRatingDetails.modal_orderDetails_tracking_ratingDetails= new Modal_OrderDetails_Tracking_RatingDetails();
                                e.printStackTrace();
                            }



                            try{
                                    modal_raisedTicketsRatingDetails.isOrderDetailsScreenOpened = false;

                            }
                            catch (Exception e){
                                modal_raisedTicketsRatingDetails.isOrderDetailsScreenOpened = false;
                                e.printStackTrace();
                            }

                            raisedTicketsRatingDetailsArray.add(modal_raisedTicketsRatingDetails);



                            if(arrayLength - i1 == 1){
                                DisplayRatingTicketsList(raisedTicketsRatingDetailsArray);
                            }
                        }



                    } catch (JSONException e) {

                        e.printStackTrace();
                    }


                }catch(Exception e){
                    e.printStackTrace();
                }




            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                Log.d(TAG, "Error: " + error.getLocalizedMessage());
                Log.d(TAG, "Error: " + error.getMessage());
                Log.d(TAG, "Error: " + error.toString());
                showProgressBar(false);

                error.printStackTrace();
            }
        }) {


            @NonNull
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                final Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json");
                params.put("storeid",vendorKey);

                return params;
            }
        };
        RetryPolicy policy = new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);

        // Make the request
        Volley.newRequestQueue(RaisedTicketDetailsForRating.this).add(jsonObjectRequest);




    return raisedTicketsArrayListt;
    }

    public String getDate_and_time()
    {

        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat day = new SimpleDateFormat("EEE", Locale.ENGLISH);
        day.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));


        String  CurrentDay = day.format(c);

        SimpleDateFormat df = new SimpleDateFormat("d MMM yyyy",Locale.ENGLISH);
        df.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));


        String CurrentDatee = df.format(c);
        String CurrentDate = CurrentDay+", "+CurrentDatee;


        SimpleDateFormat dfTime = new SimpleDateFormat("HH:mm:ss",Locale.ENGLISH);
        dfTime.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));


        String FormattedTime = dfTime.format(c);
        String  formattedDate = CurrentDay+", "+CurrentDatee+" "+FormattedTime;
        return formattedDate;
    }



    public String getDate() {

        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => Sat, 9 Jan 2021 13:12:24 " + c);


        SimpleDateFormat df = new SimpleDateFormat("d MMM yyyy",Locale.ENGLISH);
        df.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));


        String CurrentDate = df.format(c);


        return CurrentDate;
    }





    private void hideKeyboard(EditText editText) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        Objects.requireNonNull(imm).hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    private void closeSearchBarEditText() {
        nameofFacility_Textview.setVisibility(View.VISIBLE);
        search_button.setVisibility(View.VISIBLE);
        search_close_btn.setVisibility(View.GONE);
        mobile_search_barEditText.setVisibility(View.GONE);
    }

    private void showSearchBarEditText() {
        nameofFacility_Textview.setVisibility(View.GONE);
        search_button.setVisibility(View.GONE);
        search_close_btn.setVisibility(View.VISIBLE);
        mobile_search_barEditText.setVisibility(View.VISIBLE);
    }
    private void showKeyboard(final EditText editText) {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                editText.requestFocus();
                InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                mgr.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
                editText.setSelection(editText.getText().length());
            }
        },0);
    }



}