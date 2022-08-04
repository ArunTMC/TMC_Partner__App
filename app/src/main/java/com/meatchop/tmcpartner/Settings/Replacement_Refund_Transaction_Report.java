package com.meatchop.tmcpartner.Settings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.DatePickerDialog;
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
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPCellEvent;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.meatchop.tmcpartner.Constants;
import com.meatchop.tmcpartner.PosScreen_JavaClasses.ManageOrders.Modal_ManageOrders_Pojo_Class;
import com.meatchop.tmcpartner.R;
import com.meatchop.tmcpartner.Settings.Add_Replacement_Refund_Order.Modal_ReplacementTransactionDetails;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.os.Build.VERSION.SDK_INT;

public class Replacement_Refund_Transaction_Report extends AppCompatActivity {
    LinearLayout dateSelectorLayout,newOrdersSync_Layout;
    EditText search_barEdit;
    ImageView search_button,search_close_btn;
    TextView transaction_Counttextwidget,details_textWidget,dateSelector_text;
    public static LinearLayout loadingpanelmask;
    public static LinearLayout loadingPanel;
    DatePickerDialog datepicker;
    ListView replacementTransaction_ListView;
    String CurrentDate,vendorKey,vendorname,TAG = "Tag";

    String StoreAddressLine1 = "No 57, Rajendra Prasad Road,";
    String StoreAddressLine2 = "Hasthinapuram Chromepet";
    String StoreAddressLine3 = "Chennai - 600044";
    String StoreLanLine = "PH No :4445568499";
    boolean isSearchButtonClicked = false;

    List<Modal_ReplacementTransactionDetails> replacementTransactionDetailsList = new ArrayList<>();
    List<String> orderidlist = new ArrayList<>();
    List<String> orderStatuslist = new ArrayList<>();
    HashMap<String,List<Modal_ReplacementTransactionDetails>> hashMap_replacementTransactionDetails = new HashMap<>();
    HashMap<String,String> orderidHashMap = new HashMap<>();


    List<Modal_ReplacementTransactionDetails> replacementTransactionDetails_SortedList = new ArrayList<>();
    TextView generateReport;
    private static final int OPENPDF_ACTIVITY_REQUEST_CODE = 2;
    private static int REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION = 1;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_replacement__refund__transaction__report);
        dateSelectorLayout = findViewById(R.id.dateSelectorLayout);
        newOrdersSync_Layout = findViewById(R.id.newOrdersSync_Layout);
        search_barEdit = findViewById(R.id.search_barEdit);
        search_button = findViewById(R.id.search_button);
        search_close_btn = findViewById(R.id.search_close_btn);
        transaction_Counttextwidget = findViewById(R.id.transaction_Counttextwidget);
        details_textWidget = findViewById(R.id.details_textWidget);
        loadingPanel = findViewById(R.id.loadingPanel);
        loadingpanelmask = findViewById(R.id.loadingpanelmask);
        replacementTransaction_ListView = findViewById(R.id.replacementTransaction_ListView);
        dateSelector_text  = findViewById(R.id.dateSelector_text);
        generateReport = findViewById(R.id.generateReport);
        try{
            SharedPreferences shared = getSharedPreferences("VendorLoginData", MODE_PRIVATE);
            vendorKey = (shared.getString("VendorKey", ""));
            vendorname = (shared.getString("VendorName", ""));
            StoreAddressLine1 = (shared.getString("VendorAddressline1", ""));
            StoreAddressLine2 = (shared.getString("VendorAddressline2", ""));
            StoreAddressLine3 = (shared.getString("VendorPincode", ""));
            StoreLanLine = (shared.getString("VendorMobileNumber", ""));
            //orderdetailsnewschema = (shared.getBoolean("orderdetailsnewschema", false));


        }
        catch (Exception e){
            e.printStackTrace();
        }

        replacementTransactionDetailsList.clear();
        orderidlist.clear();
        orderStatuslist.clear();
        hashMap_replacementTransactionDetails.clear();
        orderidHashMap.clear();
        replacementTransactionDetails_SortedList.clear();


        String Todaysdate = getDatewithNameoftheDay();
        dateSelector_text.setText(Todaysdate);
        String newFormatOfDate = changeOldDatetoNewFormat(Todaysdate);
        getReplacementTransactionDetails(newFormatOfDate, vendorKey);


        generateReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    if (SDK_INT >= Build.VERSION_CODES.R) {

                        if (Environment.isExternalStorageManager()) {
                            try {

                                AddDatatoPDFFile();
                            } catch (Exception e) {
                                e.printStackTrace();
                                ;
                            }
                        } else {
                            try {
                                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                                intent.addCategory("android.intent.category.DEFAULT");
                                intent.setData(Uri.parse(String.format("package:%s", getApplicationContext().getPackageName())));
                                startActivityForResult(intent, 2296);
                            } catch (Exception e) {
                                Intent intent = new Intent();
                                intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                                startActivityForResult(intent, 2296);
                            }
                        }

                    } else {


                        int writeExternalStoragePermission = ContextCompat.checkSelfPermission(Replacement_Refund_Transaction_Report.this, WRITE_EXTERNAL_STORAGE);
                        //Log.d("ExportInvoiceActivity", "writeExternalStoragePermission "+writeExternalStoragePermission);
                        // If do not grant write external storage permission.
                        if (writeExternalStoragePermission != PackageManager.PERMISSION_GRANTED) {
                            // Request user to grant write external storage permission.
                            ActivityCompat.requestPermissions(Replacement_Refund_Transaction_Report.this, new String[]{WRITE_EXTERNAL_STORAGE},
                                    REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION);
                        } else {
                            Adjusting_Widgets_Visibility(true);
                            try {
                                AddDatatoPDFFile();

                            } catch (Exception e) {
                                e.printStackTrace();
                                ;
                            }
                        }
                    }


                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });




        search_barEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                replacementTransactionDetails_SortedList.clear();

            }

            @Override
            public void afterTextChanged(Editable editable) {
                replacementTransactionDetails_SortedList.clear();
                isSearchButtonClicked =true;
                String mobileNo = (editable.toString());
                if(!mobileNo.equals("")) {
                    String orderstatus = "";

                    for (int i = 0; i < replacementTransactionDetailsList.size(); i++) {
                        try {
                            //Log.d(Constants.TAG, "displayorderDetailsinListview ordersList: " + ordersList.get(i));
                            mobileNo = mobileNo;
                            final Modal_ReplacementTransactionDetails modal_replacementTransactionDetails1 = new Modal_ReplacementTransactionDetails();
                            final Modal_ReplacementTransactionDetails modalReplacementTransactionDetails = replacementTransactionDetailsList.get(i);
                            String mobilenumber = modalReplacementTransactionDetails.getMobileno().toString();
                              if (mobilenumber.contains("+91" + mobileNo)) {

                                modal_replacementTransactionDetails1.transactionstatus =                modalReplacementTransactionDetails.getTransactionstatus();
                                modal_replacementTransactionDetails1.transactiontime =        modalReplacementTransactionDetails.getTransactiontime();
                                modal_replacementTransactionDetails1.transactiontype =          modalReplacementTransactionDetails.getTransactiontype();
                                modal_replacementTransactionDetails1.discountamount =            modalReplacementTransactionDetails.getDiscountamount();
                                modal_replacementTransactionDetails1.markeditemdesp_String =  modalReplacementTransactionDetails.getMarkeditemdesp_String();
                                modal_replacementTransactionDetails1.mobileno =              modalReplacementTransactionDetails.getMobileno();
                                modal_replacementTransactionDetails1.orderid =             modalReplacementTransactionDetails.getOrderid();
                                modal_replacementTransactionDetails1.vendorkey =              modalReplacementTransactionDetails.getVendorkey();
                                modal_replacementTransactionDetails1.refundamount =       modalReplacementTransactionDetails.getRefundamount();
                                modal_replacementTransactionDetails1.replacementitemdesp_string =               modalReplacementTransactionDetails.getReplacementitemdesp_string();
                                modal_replacementTransactionDetails1.replacementorderamount = modalReplacementTransactionDetails.getReplacementorderamount();
                                modal_replacementTransactionDetails1.replacementorderid =     modalReplacementTransactionDetails.getReplacementorderid();
                                modal_replacementTransactionDetails1.discountamount =modalReplacementTransactionDetails.getDiscountamount();
                                  modal_replacementTransactionDetails1.reasonformarked =modalReplacementTransactionDetails.getReasonformarked();

                                replacementTransactionDetails_SortedList.add(modal_replacementTransactionDetails1);


                            }

                        } catch (Exception e) {
                            e.printStackTrace();

                        }
                    }
                    try {
                        if (replacementTransactionDetails_SortedList.size() > 0) {
                            replacementTransaction_ListView.setVisibility(View.VISIBLE);
                            details_textWidget.setVisibility(View.GONE);

                            CallAdapter(replacementTransactionDetails_SortedList);

                        } else {
                            replacementTransaction_ListView.setVisibility(View.GONE);
                            details_textWidget.setVisibility(View.VISIBLE);
                            details_textWidget.setText("No orders found for this Mobile number");


                        }
                    } catch (Exception E) {
                        E.printStackTrace();
                    }


                }
                else{
                    replacementTransaction_ListView.setVisibility(View.GONE);
                    details_textWidget.setVisibility(View.VISIBLE);
                    details_textWidget.setText("No orders found for this Mobile number");

                }

            }
        });


        newOrdersSync_Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replacementTransactionDetailsList.clear();

                Adjusting_Widgets_Visibility(true);
                String Todaysdate =  dateSelector_text.getText().toString();
                String newFormatOfDate = changeOldDatetoNewFormat(Todaysdate);
                getReplacementTransactionDetails(newFormatOfDate, vendorKey);


            }
        });
        search_close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(search_barEdit);
                closeSearchBarEditText();
                search_barEdit.setText("");
                isSearchButtonClicked = false;

                CallAdapter(replacementTransactionDetailsList);
            }
        });
        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int textlength = search_barEdit.getText().toString().length();
                isSearchButtonClicked = true;

                showKeyboard(search_barEdit);
                showSearchBarEditText();
            }
        });
        dateSelectorLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    openDatePicker();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        loadingpanelmask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Replacement_Refund_Transaction_Report.this, "Loading.... Please Wait", Toast.LENGTH_SHORT).show();
            }
        });






    }


    private void AddDatatoPDFFile() {


        if ((orderidHashMap == null)) {
            Adjusting_Widgets_Visibility(false);

            return;
        }
        String extstoragedir = Environment.getExternalStorageDirectory().toString();
        String state = Environment.getExternalStorageState();
        //Log.d("PdfUtil", "external storage state "+state+" extstoragedir "+extstoragedir);
        String path = Environment.getExternalStorageDirectory().getAbsolutePath();

        File folder = new File(path);
        //  File folder = new File(fol, "pdf");
        if (!folder.exists()) {
            boolean bool = folder.mkdirs();
        }
        try {
            String filename = "Replacement_RefundTransactionReport_" + System.currentTimeMillis() + ".pdf";
            final File file = new File(folder, filename);
            file.createNewFile();
            try {
                FileOutputStream fOut = new FileOutputStream(file);
                Document layoutDocument = new Document();
                PdfWriter.getInstance(layoutDocument, fOut);
                layoutDocument.open();
                addVendorDetails(layoutDocument);
               // addItemRows(layoutDocument);
                addItemRowsinNewFormat(layoutDocument);

                layoutDocument.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            // if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            // document = new PdfDocument(new PdfWriter("MyFirstInvoice.pdf"));


            Adjusting_Widgets_Visibility(false);

            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());

            Intent pdfViewIntent = new Intent(Intent.ACTION_VIEW);
            pdfViewIntent.setDataAndType(Uri.fromFile(file), "application/pdf");
            pdfViewIntent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            pdfViewIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            Intent intent = Intent.createChooser(pdfViewIntent, "Open File");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            try {
                Adjusting_Widgets_Visibility(false);

                startActivityForResult(intent, OPENPDF_ACTIVITY_REQUEST_CODE);
            } catch (ActivityNotFoundException e) {
                // Instruct the user to install a PDF reader here, or something
            }
            // }
        } catch (IOException e) {
            Adjusting_Widgets_Visibility(false);

            Log.i("error", e.getLocalizedMessage());
        } catch (Exception ex) {
            Adjusting_Widgets_Visibility(false);

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
            com.itextpdf.text.Paragraph titlepara = new com.itextpdf.text.Paragraph("REPLACEMENT & REFUND TRANSACTION REPORT");
            titlepara.setSpacingBefore(5);
            titlepara.setFont(boldFont);
            titlepara.setAlignment(Element.ALIGN_CENTER);
            layoutDocument.add(titlepara);

            String vendorname = "Vendor: " + Vendorname;
            com.itextpdf.text.Paragraph vendorpara = new com.itextpdf.text.Paragraph(vendorname);
            vendorpara.setSpacingBefore(10);
            vendorpara.setAlignment(Element.ALIGN_LEFT);
            layoutDocument.add(vendorpara);

            com.itextpdf.text.Paragraph datepara = new com.itextpdf.text.Paragraph("Date: " + getDatewithNameoftheDay());
            datepara.setAlignment(Element.ALIGN_LEFT);
            datepara.setSpacingBefore(5);

            PdfPTable Table = new PdfPTable(1);
            Table.setWidthPercentage(100);
            Table.setSpacingBefore(20);

            PdfPCell titlepara2cell = new PdfPCell(new Paragraph(datepara));
            titlepara2cell.setBorder(Rectangle.BOTTOM);
            titlepara2cell.setFixedHeight(30);
            Table.addCell(titlepara2cell);

            layoutDocument.add(Table);


            //layoutDocument.add(datepara);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public class RoundRectangle implements PdfPCellEvent {
        public void cellLayout(PdfPCell cell, Rectangle rect,
                               PdfContentByte[] canvas) {
            PdfContentByte cb = canvas[PdfPTable.LINECANVAS];
            cb.roundRectangle(
                    rect.getLeft() + 1.5f, rect.getBottom() + 1.5f, rect.getWidth() - 3,
                    rect.getHeight() - 3, 4);
            cb.stroke();
        }
    }
    private void addItemRowsinNewFormat(Document layoutDocument) {
        int markedCount = 0;
        int refundCount =0;
        int replacementCount = 0;

        List<Modal_ReplacementTransactionDetails> markedStatusArray = new ArrayList<>();
        List<Modal_ReplacementTransactionDetails>  refundStatusArray = new ArrayList<>();
        List<Modal_ReplacementTransactionDetails> replacementStatusArray = new ArrayList<>();


        List<String> markedStatusTitleArray = new ArrayList<>();
        List<String>  refundStatusTitleArray = new ArrayList<>();
        List<String> replacementStatusTitleArray = new ArrayList<>();

        markedStatusTitleArray .add("MobileNo");markedStatusTitleArray .add("Delivered Date");
        markedStatusTitleArray .add("Marked Date");markedStatusTitleArray .add("Item Details");
        markedStatusTitleArray .add("Order Amount");markedStatusTitleArray .add("Comments");


        refundStatusTitleArray .add("MobileNo");refundStatusTitleArray .add("Marked Date");
        refundStatusTitleArray .add("Refunded Date");refundStatusTitleArray .add("Item Details");
        refundStatusTitleArray .add("Order Amount");refundStatusTitleArray .add("Refunded Amount");


        replacementStatusTitleArray .add("MobileNo");replacementStatusTitleArray .add("Marked Date");
        replacementStatusTitleArray .add("Replacement Date");replacementStatusTitleArray .add("Item Details");
        replacementStatusTitleArray .add("Order Amount");replacementStatusTitleArray .add("Replaced ItemDetails");
        replacementStatusTitleArray .add("Replaced Order AMT ");
        RoundRectangle roundRectange = new RoundRectangle();

        try {
            if (hashMap_replacementTransactionDetails.size() > 0) {
                for (String transactionType : orderStatuslist) {
                    List<Modal_ReplacementTransactionDetails> arrayFromHashmap = new ArrayList<>();
                    arrayFromHashmap = hashMap_replacementTransactionDetails.get(transactionType);

                    if (arrayFromHashmap.size() > 0) {


                        if (transactionType.equals("MARKED")) {
                            markedCount = arrayFromHashmap.size();
                            markedStatusArray = arrayFromHashmap;
                        }
                        if (transactionType.equals("REPLACEMENT")) {
                            replacementCount= arrayFromHashmap.size();
                            replacementStatusArray = arrayFromHashmap;

                        }
                        if (transactionType.equals("REFUND")) {
                            refundCount= arrayFromHashmap.size();
                            refundStatusArray = arrayFromHashmap;

                        }

                    }

                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        try {
            PdfPTable markedorderCountTable = new PdfPTable(1);
            markedorderCountTable.setWidthPercentage(100);
            markedorderCountTable.setSpacingBefore(3);
            com.itextpdf.text.Font boldFont = new com.itextpdf.text.Font(Font.FontFamily.TIMES_ROMAN, 26, Font.BOLDITALIC);
            com.itextpdf.text.Paragraph titlepara = new com.itextpdf.text.Paragraph("Orders marked for refund / replacement : "+String.valueOf(markedCount));
            titlepara.setFont(boldFont);
            titlepara.setAlignment(Element.ALIGN_LEFT);
           // layoutDocument.add(titlepara);
            PdfPCell markedordercell = new PdfPCell(new Paragraph(titlepara));
            markedordercell.setBorder(Rectangle.NO_BORDER);
            markedorderCountTable.setSpacingBefore(10);
            markedorderCountTable.addCell(markedordercell);
            layoutDocument.add(markedorderCountTable);





            PdfPTable refundCountTable = new PdfPTable(1);
            refundCountTable.setWidthPercentage(100);
            refundCountTable.setSpacingBefore(3);
            com.itextpdf.text.Font boldFont1 = new com.itextpdf.text.Font(Font.FontFamily.TIMES_ROMAN, 26, Font.BOLDITALIC);
            com.itextpdf.text.Paragraph titlepara1 = new com.itextpdf.text.Paragraph("No of refunds processed : "+String.valueOf(refundCount));
            titlepara1.setFont(boldFont1);
            titlepara1.setAlignment(Element.ALIGN_LEFT);
          //  markedTransCountTable.addCell(titlepara1);
            PdfPCell titlepara1cell = new PdfPCell(new Paragraph(titlepara1));
            titlepara1cell.setBorder(Rectangle.NO_BORDER);
            refundCountTable.addCell(titlepara1cell);
            layoutDocument.add(refundCountTable);



            PdfPTable replacementCountTable = new PdfPTable(1);
            replacementCountTable.setWidthPercentage(100);
            replacementCountTable.setSpacingBefore(5);
            com.itextpdf.text.Font boldFont2 = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.TIMES_ROMAN, 26, Font.BOLDITALIC);
            com.itextpdf.text.Paragraph titlepara2 = new com.itextpdf.text.Paragraph("No of orders replaced : "+String.valueOf(replacementCount));
            titlepara2.setFont(boldFont2);
            titlepara2.setAlignment(Element.ALIGN_LEFT);
            //markedTransCountTable.addCell(titlepara2);
            PdfPCell titlepara2cell = new PdfPCell(new Paragraph(titlepara2));
            titlepara2cell.setBorder(Rectangle.BOTTOM);
            titlepara2cell.setFixedHeight(30);
            replacementCountTable.addCell(titlepara2cell);
            layoutDocument.add(replacementCountTable);




        } catch (Exception e) {
            e.printStackTrace();
        }


        if(markedStatusArray.size()>0){
            try {
            PdfPTable markedtypeArraytable = new PdfPTable(1);
            markedtypeArraytable.setSpacingBefore(5);
            markedtypeArraytable.setWidthPercentage(100);

            try {
            com.itextpdf.text.Font boldFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.TIMES_ROMAN, 25, Font.BOLDITALIC);
            com.itextpdf.text.Paragraph titlepara = new com.itextpdf.text.Paragraph("Marked Order Details");
            titlepara.setSpacingAfter(5);
            titlepara.setFont(boldFont);
            titlepara.setAlignment(Element.ALIGN_LEFT);

            layoutDocument.add(titlepara);
            } catch (DocumentException e) {
                e.printStackTrace();
            }


            try{
/*
                PdfPTable markedStatusTitleArraytable = new PdfPTable(markedStatusTitleArray.size()+1);
                markedStatusTitleArraytable.setHeaderRows(1);
                markedStatusTitleArraytable.setWidthPercentage(95);
                 for(String title :markedStatusTitleArray) {

                     PdfPCell markedStatusTitleArrayCell = new PdfPCell(new Phrase(title));
                     markedStatusTitleArrayCell.setBorder(Rectangle.NO_BORDER);
                     markedStatusTitleArrayCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                     markedStatusTitleArrayCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                     markedStatusTitleArrayCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                     markedStatusTitleArrayCell.setFixedHeight(30);
                     markedStatusTitleArrayCell.setNoWrap(true);
                     markedStatusTitleArrayCell.setBorderWidthBottom(01);

                     markedStatusTitleArraytable.addCell(markedStatusTitleArrayCell);

 */


                PdfPTable markedStatusTitleArraytable = new PdfPTable(6);
                markedStatusTitleArraytable.setWidthPercentage(100);
                for (int i = 0; i < markedStatusTitleArray.size(); i++) {
                    String deliveryType = markedStatusTitleArray.get(i);


                    PdfPCell deliveryTypecell = new PdfPCell(new Phrase(String.valueOf(deliveryType)));
                    deliveryTypecell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    deliveryTypecell.setBorder(Rectangle.NO_BORDER);
                    deliveryTypecell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    deliveryTypecell.setBorderWidthBottom(01);
                    deliveryTypecell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    deliveryTypecell.setBorderWidthRight(01);
                    deliveryTypecell.setFixedHeight(35);
                    markedStatusTitleArraytable.addCell(deliveryTypecell);



                 }
               // layoutDocument.add(markedStatusTitleArraytable);
                PdfPCell finalmarkedStatusTitleArraytable = new PdfPCell(markedStatusTitleArraytable);
                finalmarkedStatusTitleArraytable.setBorder(Rectangle.NO_BORDER);
                markedtypeArraytable.addCell(new PdfPCell(finalmarkedStatusTitleArraytable));
            }
            catch (Exception e){
                e.printStackTrace();
            }

            try{

                for(Modal_ReplacementTransactionDetails modal_replacementTransactionDetails : markedStatusArray){
                    PdfPTable   markedStatusContentArraytable = new PdfPTable(6);
                    markedStatusContentArraytable.setWidthPercentage(100);


                    String mobileno = String.valueOf(modal_replacementTransactionDetails.getMobileno());
                    PdfPCell mobilenocell = new PdfPCell(new Phrase(String.valueOf(mobileno)));
                    mobilenocell.setBorder(Rectangle.NO_BORDER);

                    mobilenocell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    mobilenocell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    mobilenocell.setBorderWidthBottom(01);
                    mobilenocell.setBorderWidthRight(01);
                    markedStatusContentArraytable.addCell(mobilenocell);



                    String deliveredDate = String.valueOf(modal_replacementTransactionDetails.getOrderDeliveredDate());
                    PdfPCell deliveredDatecell = new PdfPCell(new Phrase(String.valueOf(deliveredDate)));
                    deliveredDatecell.setBorder(Rectangle.NO_BORDER);
                    deliveredDatecell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    deliveredDatecell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    deliveredDatecell.setBorderWidthBottom(01);
                    deliveredDatecell.setBorderWidthRight(01);
                    markedStatusContentArraytable.addCell(deliveredDatecell);



                    String orderMarkedDate = String.valueOf(modal_replacementTransactionDetails.getOrderMarkedDate());
                    PdfPCell orderMarkedDatecell = new PdfPCell(new Phrase(String.valueOf(orderMarkedDate)));
                    orderMarkedDatecell.setBorder(Rectangle.NO_BORDER);
                    orderMarkedDatecell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    orderMarkedDatecell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    orderMarkedDatecell.setBorderWidthBottom(01);
                    orderMarkedDatecell.setBorderWidthRight(01);
                    markedStatusContentArraytable.addCell(orderMarkedDatecell);



                    String itemDespString = String.valueOf(modal_replacementTransactionDetails.getMarkeditemdesp_String());
                    String itemDesp = "";
                    String subCtgyKey = "";

                    try {

                        JSONArray array = new JSONArray(itemDespString);

                        //Log.i("tag","array.length()"+ array.length());

                        for (int l = 0; l < array.length(); l++) {
                            JSONObject json = array.getJSONObject(l);

                            //Log.i("tag", "array.lengrh(i" + json.length());
                            try {
                                if (json.has("tmcsubctgykey")) {
                                    subCtgyKey = String.valueOf(json.get("tmcsubctgykey"));
                                } else {
                                    subCtgyKey = " ";
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                            String cutname = "";

                            try {
                                if (json.has("cutname")) {
                                    cutname = String.valueOf(json.get("cutname"));
                                } else {
                                    cutname = "";
                                }
                            } catch (Exception e) {
                                cutname = "";
                                e.printStackTrace();
                            }

                            try {
                                if ((cutname.length() > 0) && (!cutname.equals(null)) && (!cutname.equals("null"))) {
                                    cutname = " [ " + cutname + " ] ";
                                } else {
                                    //cutname="";
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                            String itemName = String.valueOf(json.get("itemname"));
                            String price = String.valueOf(json.get("tmcprice"));
                            String quantity = String.valueOf(json.get("quantity"));
                            if (itemDesp.length() > 0) {
                                if (subCtgyKey.equals("tmcsubctgy_16")) {
                                    itemDesp = String.format("%s ,\n%s %s", itemDesp, "Grill House " + itemName, cutname);

                                } else if (subCtgyKey.equals("tmcsubctgy_15")) {
                                    itemDesp = String.format("%s ,\n%s  %s", itemDesp, itemName, cutname);

                                } else {
                                    itemDesp = String.format("%s ,\n%s  %s", itemDesp, itemName, cutname);

                                }
                            } else {
                                if (subCtgyKey.equals("tmcsubctgy_16")) {
                                    itemDesp = String.format("%s  %s", "Grill House " + itemName, cutname);

                                } else if (subCtgyKey.equals("tmcsubctgy_15")) {
                                    itemDesp = String.format("%s  %s", itemName, cutname);

                                } else {
                                    itemDesp = String.format("%s  %s", itemName, cutname);

                                }

                            }

                        }


                    }
                    catch (JSONException e) {
                        e.printStackTrace();

                    }
                    PdfPCell itemDespcell = new PdfPCell(new Phrase(String.valueOf(itemDesp)));
                    itemDespcell.setBorder(Rectangle.NO_BORDER);
                    itemDespcell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    itemDespcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    itemDespcell.setBorderWidthBottom(01);
                    itemDespcell.setBorderWidthRight(01);
                    markedStatusContentArraytable.addCell(itemDespcell);



                    String orderAmountString = String.valueOf(modal_replacementTransactionDetails.getMarkedOrderAmountString());
                    PdfPCell orderAmountStringcell = new PdfPCell(new Phrase(String.valueOf(orderAmountString)));
                    orderAmountStringcell.setBorder(Rectangle.NO_BORDER);
                    orderAmountStringcell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    orderAmountStringcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    orderAmountStringcell.setBorderWidthBottom(01);
                    orderAmountStringcell.setBorderWidthRight(01);
                    markedStatusContentArraytable.addCell(orderAmountStringcell);




                    String reasonForMarkedString = String.valueOf(modal_replacementTransactionDetails.getReasonformarked());
                    PdfPCell reasonForMarkedcell = new PdfPCell(new Phrase(String.valueOf(reasonForMarkedString)));
                    reasonForMarkedcell.setBorder(Rectangle.NO_BORDER);
                    reasonForMarkedcell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    reasonForMarkedcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    reasonForMarkedcell.setBorderWidthBottom(01);
                    reasonForMarkedcell.setBorderWidthRight(01);
                    markedStatusContentArraytable.addCell(reasonForMarkedcell);




                    PdfPCell finalmarkedStatusContentArraytable = new PdfPCell(markedStatusContentArraytable);
                    finalmarkedStatusContentArraytable.setBorder(Rectangle.NO_BORDER);
                    markedtypeArraytable.addCell(new PdfPCell(finalmarkedStatusContentArraytable));


                }


            }
            catch (Exception e){
                e.printStackTrace();
            }

                PdfPTable outertable = new PdfPTable(1);
                PdfPCell  outercell = new PdfPCell(markedtypeArraytable);
                outercell.setCellEvent(roundRectange);
                outercell.setBorder(Rectangle.NO_BORDER);
                outercell.setPadding(8);
                outertable.addCell(outercell);
                outertable.setWidthPercentage(100);
                outertable.setSpacingBefore(5);

                layoutDocument.add(outertable);
               // layoutDocument.add(markedtypeArraytable);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if(refundStatusArray.size()>0){
            try {
                PdfPTable refundtypeArraytable = new PdfPTable(1);
                refundtypeArraytable.setSpacingBefore(5);
                refundtypeArraytable.setWidthPercentage(100);

                try {
                com.itextpdf.text.Font boldFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.TIMES_ROMAN, 25, Font.BOLDITALIC);
                com.itextpdf.text.Paragraph titlepara = new com.itextpdf.text.Paragraph("Refund Order Details");
                titlepara.setSpacingAfter(5);
                titlepara.setFont(boldFont);
                titlepara.setAlignment(Element.ALIGN_LEFT);

                layoutDocument.add(titlepara);
            } catch (DocumentException e) {
                e.printStackTrace();
            }


            try{

               /* PdfPTable refundStatusTitleArraytable = new PdfPTable(4);
                refundStatusTitleArraytable.setHeaderRows(1);
                refundStatusTitleArraytable.setWidthPercentage(100);
                for(String title :refundStatusTitleArray) {

                    PdfPCell refundStatusTitleArrayCell = new PdfPCell(new Phrase(title));
                    refundStatusTitleArrayCell.setBorder(Rectangle.NO_BORDER);
                    refundStatusTitleArrayCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    refundStatusTitleArrayCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    refundStatusTitleArrayCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    refundStatusTitleArrayCell.setFixedHeight(30);
                    refundStatusTitleArrayCell.setNoWrap(true);
                    refundStatusTitleArrayCell.setBorderWidthBottom(01);

                    refundStatusTitleArraytable.addCell(refundStatusTitleArrayCell);

                }


                */

                PdfPTable refundStatusTitleArraytable = new PdfPTable(6);
                refundStatusTitleArraytable.setWidthPercentage(100);
                for (int i = 0; i < refundStatusTitleArray.size(); i++) {
                    String deliveryType = refundStatusTitleArray.get(i);


                    PdfPCell deliveryTypecell = new PdfPCell(new Phrase(String.valueOf(deliveryType)));
                    deliveryTypecell.setBorder(Rectangle.NO_BORDER);
                    deliveryTypecell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    deliveryTypecell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    deliveryTypecell.setBorderWidthBottom(01);
                    deliveryTypecell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    deliveryTypecell.setBorderWidthRight(01);
                    deliveryTypecell.setFixedHeight(35);

                    refundStatusTitleArraytable.addCell(deliveryTypecell);


                }


                PdfPCell finalrefundStatusTitleArraytable = new PdfPCell(refundStatusTitleArraytable);
                finalrefundStatusTitleArraytable.setBorder(Rectangle.NO_BORDER);

                refundtypeArraytable.addCell(new PdfPCell(finalrefundStatusTitleArraytable));




                try{


                    for (int i = 0; i < refundStatusArray.size(); i++) {
                        Modal_ReplacementTransactionDetails modal_replacementTransactionDetails = refundStatusArray.get(i);
                        PdfPTable   refundStatusContentArraytable = new PdfPTable(6);
                        refundStatusContentArraytable.setWidthPercentage(100);


                        String mobileno = String.valueOf(modal_replacementTransactionDetails.getMobileno());
                        PdfPCell mobilenocell = new PdfPCell(new Phrase(String.valueOf(mobileno)));
                        mobilenocell.setBorder(Rectangle.NO_BORDER);
                        mobilenocell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        mobilenocell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        mobilenocell.setBorderWidthBottom(01);
                        mobilenocell.setBorderWidthRight(01);
                        refundStatusContentArraytable.addCell(mobilenocell);



                        String markedDate = String.valueOf(modal_replacementTransactionDetails.getOrderMarkedDate());
                        PdfPCell markedDatecell = new PdfPCell(new Phrase(String.valueOf(markedDate)));
                        markedDatecell.setBorder(Rectangle.NO_BORDER);
                        markedDatecell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        markedDatecell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        markedDatecell.setBorderWidthBottom(01);
                        markedDatecell.setBorderWidthRight(01);
                        refundStatusContentArraytable.addCell(markedDatecell);


                        String orderrefundedDate = String.valueOf(modal_replacementTransactionDetails.getTransactiontime());
                        PdfPCell orderRefundedDatecell = new PdfPCell(new Phrase(String.valueOf(orderrefundedDate)));
                        orderRefundedDatecell.setBorder(Rectangle.NO_BORDER);
                        orderRefundedDatecell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        orderRefundedDatecell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        orderRefundedDatecell.setBorderWidthBottom(01);
                        orderRefundedDatecell.setBorderWidthRight(01);
                        refundStatusContentArraytable.addCell(orderRefundedDatecell);



                        String itemDespString = String.valueOf(modal_replacementTransactionDetails.getMarkeditemdesp_String());
                        String itemDesp = "";
                        String subCtgyKey = "";

                        try {

                            JSONArray array = new JSONArray(itemDespString);

                            //Log.i("tag","array.length()"+ array.length());

                            for (int l = 0; l < array.length(); l++) {
                                JSONObject json = array.getJSONObject(l);

                                //Log.i("tag", "array.lengrh(i" + json.length());
                                try {
                                    if (json.has("tmcsubctgykey")) {
                                        subCtgyKey = String.valueOf(json.get("tmcsubctgykey"));
                                    } else {
                                        subCtgyKey = " ";
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }


                                String cutname = "";

                                try {
                                    if (json.has("cutname")) {
                                        cutname = String.valueOf(json.get("cutname"));
                                    } else {
                                        cutname = "";
                                    }
                                } catch (Exception e) {
                                    cutname = "";
                                    e.printStackTrace();
                                }

                                try {
                                    if ((cutname.length() > 0) && (!cutname.equals(null)) && (!cutname.equals("null"))) {
                                        cutname = " [ " + cutname + " ] ";
                                    } else {
                                        //cutname="";
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }


                                String itemName = String.valueOf(json.get("itemname"));
                                String price = String.valueOf(json.get("tmcprice"));
                                String quantity = String.valueOf(json.get("quantity"));
                                if (itemDesp.length() > 0) {
                                    if (subCtgyKey.equals("tmcsubctgy_16")) {
                                        itemDesp = String.format("%s ,\n%s %s", itemDesp, "Grill House " + itemName, cutname);

                                    } else if (subCtgyKey.equals("tmcsubctgy_15")) {
                                        itemDesp = String.format("%s ,\n%s  %s", itemDesp, itemName, cutname);

                                    } else {
                                        itemDesp = String.format("%s ,\n%s  %s", itemDesp, itemName, cutname);

                                    }
                                } else {
                                    if (subCtgyKey.equals("tmcsubctgy_16")) {
                                        itemDesp = String.format("%s  %s", "Grill House " + itemName, cutname);

                                    } else if (subCtgyKey.equals("tmcsubctgy_15")) {
                                        itemDesp = String.format("%s  %s", "Ready to Cook  " + itemName, cutname);

                                    } else {
                                        itemDesp = String.format("%s  %s", itemName, cutname);

                                    }

                                }

                            }


                        }
                        catch (JSONException e) {
                            e.printStackTrace();

                        }
                        PdfPCell itemDespcell = new PdfPCell(new Phrase(String.valueOf(itemDesp)));
                        itemDespcell.setBorder(Rectangle.NO_BORDER);
                        itemDespcell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        itemDespcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        itemDespcell.setBorderWidthBottom(01);
                        itemDespcell.setBorderWidthRight(01);
                        refundStatusContentArraytable.addCell(itemDespcell);



                        String orderAmountString = String.valueOf(modal_replacementTransactionDetails.getMarkedOrderAmountString());
                        PdfPCell orderAmountStringcell = new PdfPCell(new Phrase(String.valueOf(orderAmountString)));
                        orderAmountStringcell.setBorder(Rectangle.NO_BORDER);
                        orderAmountStringcell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        orderAmountStringcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        orderAmountStringcell.setBorderWidthBottom(01);
                        orderAmountStringcell.setBorderWidthRight(01);
                        refundStatusContentArraytable.addCell(orderAmountStringcell);



                        String refundAmountString = String.valueOf(modal_replacementTransactionDetails.getRefundamount());
                        PdfPCell refundAmountcell = new PdfPCell(new Phrase(String.valueOf(refundAmountString)));
                        refundAmountcell.setBorder(Rectangle.NO_BORDER);
                        refundAmountcell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        refundAmountcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        refundAmountcell.setBorderWidthBottom(01);
                        refundAmountcell.setBorderWidthRight(01);
                        refundStatusContentArraytable.addCell(refundAmountcell);


                        PdfPCell finalrefundStatusContentArraytable = new PdfPCell(refundStatusContentArraytable);
                        finalrefundStatusContentArraytable.setBorder(Rectangle.NO_BORDER);
                        refundtypeArraytable.addCell(new PdfPCell(finalrefundStatusContentArraytable));


                    }


                }
                catch (Exception e){
                    e.printStackTrace();
                }
                //layoutDocument.add(refundtypeArraytable);

                PdfPTable outertable = new PdfPTable(1);
                PdfPCell  outercell = new PdfPCell(refundtypeArraytable);
                outercell.setCellEvent(roundRectange);
                outercell.setBorder(Rectangle.NO_BORDER);
                outercell.setPadding(8);
                outertable.setSpacingBefore(5);

                outertable.addCell(outercell);
                outertable.setWidthPercentage(100);

                layoutDocument.add(outertable);
            } catch (Exception e) {
                e.printStackTrace();
            }
            }
            catch (Exception e){
                e.printStackTrace();
            }


        }

        if(replacementStatusArray.size()>0){
            try {
            PdfPTable replacementtypeArraytable = new PdfPTable(1);
            replacementtypeArraytable.setSpacingBefore(5);
            replacementtypeArraytable.setWidthPercentage(100);



            try {
                com.itextpdf.text.Font boldFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.TIMES_ROMAN, 25, Font.BOLDITALIC);
                com.itextpdf.text.Paragraph titlepara = new com.itextpdf.text.Paragraph("Replacement Order Details");
                titlepara.setSpacingAfter(5);

                titlepara.setFont(boldFont);
                titlepara.setAlignment(Element.ALIGN_LEFT);

                layoutDocument.add(titlepara);
            } catch (DocumentException e) {
                e.printStackTrace();
            }




            try{

                PdfPTable replacementStatusTitleArraytable = new PdfPTable(7);
                replacementStatusTitleArraytable.setWidthPercentage(100);

                for (int i = 0; i < replacementStatusTitleArray.size(); i++) {
                    String deliveryType = replacementStatusTitleArray.get(i);


                    PdfPCell deliveryTypecell = new PdfPCell(new Phrase(String.valueOf(deliveryType)));
                    deliveryTypecell.setBorder(Rectangle.NO_BORDER);
                    deliveryTypecell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    deliveryTypecell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    deliveryTypecell.setFixedHeight(30);
                    deliveryTypecell.setBorderWidthBottom(01);
                    deliveryTypecell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    deliveryTypecell.setBorderWidthRight(01);
                    replacementStatusTitleArraytable.addCell(deliveryTypecell);


                }



                PdfPCell finalreplacementStatusTitleArraytable = new PdfPCell(replacementStatusTitleArraytable);
                finalreplacementStatusTitleArraytable.setBorder(Rectangle.NO_BORDER);
                replacementtypeArraytable.addCell(new PdfPCell(finalreplacementStatusTitleArraytable));



            }
            catch (Exception e){
                e.printStackTrace();
            }



            try{


                for (int i = 0; i < replacementStatusArray.size(); i++) {
                    Modal_ReplacementTransactionDetails modal_replacementTransactionDetails = replacementStatusArray.get(i);
                    PdfPTable   replacementStatusContentArraytable = new PdfPTable(7);

                    replacementStatusContentArraytable.setWidthPercentage(100);


                    String mobileno = String.valueOf(modal_replacementTransactionDetails.getMobileno());
                    PdfPCell mobilenocell = new PdfPCell(new Phrase(String.valueOf(mobileno)));
                    mobilenocell.setBorder(Rectangle.NO_BORDER);
                    mobilenocell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    mobilenocell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    mobilenocell.setBorderWidthBottom(01);
                    mobilenocell.setBorderWidthRight(01);
                    replacementStatusContentArraytable.addCell(mobilenocell);



                    String markedDate = String.valueOf(modal_replacementTransactionDetails.getOrderMarkedDate());
                    PdfPCell markedDatecell = new PdfPCell(new Phrase(String.valueOf(markedDate)));
                    markedDatecell.setBorder(Rectangle.NO_BORDER);
                    markedDatecell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    markedDatecell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    markedDatecell.setBorderWidthBottom(01);
                    markedDatecell.setBorderWidthRight(01);
                    replacementStatusContentArraytable.addCell(markedDatecell);



                    String orderrefundedDate = String.valueOf(modal_replacementTransactionDetails.getTransactiontime());
                    PdfPCell orderRefundedDatecell = new PdfPCell(new Phrase(String.valueOf(orderrefundedDate)));
                    orderRefundedDatecell.setBorder(Rectangle.NO_BORDER);
                    orderRefundedDatecell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    orderRefundedDatecell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    orderRefundedDatecell.setBorderWidthBottom(01);
                    orderRefundedDatecell.setBorderWidthRight(01);
                    replacementStatusContentArraytable.addCell(orderRefundedDatecell);



                    String itemDespString = String.valueOf(modal_replacementTransactionDetails.getMarkeditemdesp_String());
                    String itemDesp = "";
                    String subCtgyKey = "";

                    try {

                        JSONArray array = new JSONArray(itemDespString);

                        //Log.i("tag","array.length()"+ array.length());

                        for (int l = 0; l < array.length(); l++) {
                            JSONObject json = array.getJSONObject(l);

                            //Log.i("tag", "array.lengrh(i" + json.length());
                            try {
                                if (json.has("tmcsubctgykey")) {
                                    subCtgyKey = String.valueOf(json.get("tmcsubctgykey"));
                                } else {
                                    subCtgyKey = " ";
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                            String cutname = "";

                            try {
                                if (json.has("cutname")) {
                                    cutname = String.valueOf(json.get("cutname"));
                                } else {
                                    cutname = "";
                                }
                            } catch (Exception e) {
                                cutname = "";
                                e.printStackTrace();
                            }

                            try {
                                if ((cutname.length() > 0) && (!cutname.equals(null)) && (!cutname.equals("null"))) {
                                    cutname = " [ " + cutname + " ] ";
                                } else {
                                    //cutname="";
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                            String itemName = String.valueOf(json.get("itemname"));
                            String price = String.valueOf(json.get("tmcprice"));
                            String quantity = String.valueOf(json.get("quantity"));
                            if (itemDesp.length() > 0) {
                                if (subCtgyKey.equals("tmcsubctgy_16")) {
                                    itemDesp = String.format("%s ,\n%s %s", itemDesp, "Grill House " + itemName, cutname);

                                } else if (subCtgyKey.equals("tmcsubctgy_15")) {
                                    itemDesp = String.format("%s ,\n%s  %s", itemDesp,  itemName, cutname);

                                } else {
                                    itemDesp = String.format("%s ,\n%s  %s", itemDesp, itemName, cutname);

                                }
                            } else {
                                if (subCtgyKey.equals("tmcsubctgy_16")) {
                                    itemDesp = String.format("%s  %s", "Grill House " + itemName, cutname);

                                } else if (subCtgyKey.equals("tmcsubctgy_15")) {
                                    itemDesp = String.format("%s  %s", itemName, cutname);

                                } else {
                                    itemDesp = String.format("%s  %s", itemName, cutname);

                                }

                            }

                        }


                    }
                    catch (JSONException e) {
                        e.printStackTrace();

                    }
                    PdfPCell itemDespcell = new PdfPCell(new Phrase(String.valueOf(itemDesp)));
                    itemDespcell.setBorder(Rectangle.NO_BORDER);
                    itemDespcell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    itemDespcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    itemDespcell.setBorderWidthBottom(01);
                    itemDespcell.setBorderWidthRight(01);
                    replacementStatusContentArraytable.addCell(itemDespcell);



                    String orderAmountString = String.valueOf(modal_replacementTransactionDetails.getMarkedOrderAmountString());
                    PdfPCell orderAmountStringcell = new PdfPCell(new Phrase(String.valueOf(orderAmountString)));
                    orderAmountStringcell.setBorder(Rectangle.NO_BORDER);
                    orderAmountStringcell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    orderAmountStringcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    orderAmountStringcell.setBorderWidthBottom(01);
                    orderAmountStringcell.setBorderWidthRight(01);
                    replacementStatusContentArraytable.addCell(orderAmountStringcell);




                    String replacementitemDespString = String.valueOf(modal_replacementTransactionDetails.getReplacementitemdesp_string());
                    String  replacementitemDesp = "";
                    String  replacementsubCtgyKey = "";

                    try {

                        JSONArray array = new JSONArray(replacementitemDespString);

                        //Log.i("tag","array.length()"+ array.length());

                        for (int l = 0; l < array.length(); l++) {
                            JSONObject json = array.getJSONObject(l);

                            //Log.i("tag", "array.lengrh(i" + json.length());
                            try {
                                if (json.has("tmcsubctgykey")) {
                                    replacementsubCtgyKey = String.valueOf(json.get("tmcsubctgykey"));
                                } else {
                                    replacementsubCtgyKey = " ";
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                            String cutname = "";

                            try {
                                if (json.has("cutname")) {
                                    cutname = String.valueOf(json.get("cutname"));
                                } else {
                                    cutname = "";
                                }
                            } catch (Exception e) {
                                cutname = "";
                                e.printStackTrace();
                            }

                            try {
                                if ((cutname.length() > 0) && (!cutname.equals(null)) && (!cutname.equals("null"))) {
                                    cutname = " [ " + cutname + " ] ";
                                } else {
                                    //cutname="";
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                            String itemName = String.valueOf(json.get("itemname"));
                            String price = String.valueOf(json.get("tmcprice"));
                            String quantity = String.valueOf(json.get("quantity"));
                            if (replacementitemDesp.length() > 0) {
                                if (replacementsubCtgyKey.equals("tmcsubctgy_16")) {
                                    replacementitemDesp = String.format("%s ,\n%s %s", replacementitemDesp, "Grill House " + itemName, cutname);

                                } else if (replacementsubCtgyKey.equals("tmcsubctgy_15")) {
                                    replacementitemDesp = String.format("%s ,\n%s  %s", replacementitemDesp,  itemName, cutname);

                                } else {
                                    replacementitemDesp = String.format("%s ,\n%s  %s", replacementitemDesp, itemName, cutname);

                                }
                            } else {
                                if (replacementsubCtgyKey.equals("tmcsubctgy_16")) {
                                    replacementitemDesp = String.format("%s  %s", "Grill House " + itemName, cutname);

                                } else if (replacementsubCtgyKey.equals("tmcsubctgy_15")) {
                                    replacementitemDesp = String.format("%s  %s", itemName, cutname);

                                } else {
                                    replacementitemDesp = String.format("%s  %s", itemName, cutname);

                                }

                            }

                        }


                    }
                    catch (JSONException e) {
                        e.printStackTrace();

                    }

                    PdfPCell replacementitemDespcell = new PdfPCell(new Phrase(String.valueOf(replacementitemDesp)));
                    replacementitemDespcell.setBorder(Rectangle.NO_BORDER);
                    replacementitemDespcell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    replacementitemDespcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    replacementitemDespcell.setBorderWidthBottom(01);
                    replacementitemDespcell.setBorderWidthRight(01);
                    replacementStatusContentArraytable.addCell(replacementitemDespcell);



                    String replacementAmountString = String.valueOf(modal_replacementTransactionDetails.getReplacementorderamount());

                    PdfPCell replacementAmountcell = new PdfPCell(new Phrase(String.valueOf(replacementAmountString)));
                    replacementAmountcell.setBorder(Rectangle.NO_BORDER);
                    replacementAmountcell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    replacementAmountcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    replacementAmountcell.setBorderWidthBottom(01);
                    replacementAmountcell.setBorderWidthRight(01);
                    replacementStatusContentArraytable.addCell(replacementAmountcell);


                    PdfPCell finalreplacementStatusContentArraytable = new PdfPCell(replacementStatusContentArraytable);
                    finalreplacementStatusContentArraytable.setBorder(Rectangle.NO_BORDER);
                    replacementtypeArraytable.addCell(new PdfPCell(finalreplacementStatusContentArraytable));

                }


            }
            catch (Exception e){
                e.printStackTrace();
            }
                PdfPTable outertable = new PdfPTable(1);
                PdfPCell  outercell = new PdfPCell(replacementtypeArraytable);
                outercell.setCellEvent(roundRectange);
                outercell.setBorder(Rectangle.NO_BORDER);
                outercell.setPadding(8);
                outertable.setSpacingBefore(5);

                outertable.addCell(outercell);
                outertable.setWidthPercentage(100);

                layoutDocument.add(outertable);
            } catch (Exception e) {
                e.printStackTrace();
            }


        }


    }
    private void addItemRows(Document layoutDocument) {
        RoundRectangle roundRectange = new RoundRectangle();

        for(int i1 =0 ; i1< orderidlist.size(); i1++) {
            String orderid = orderidlist.get(i1);
            String CustomerMobileno = orderidHashMap.get(orderid);

            String orderid_Forpdf = "Order Which has marked for Refund / Replacement : " + orderid;


            try {

                if (i1 != 0) {
                    PdfPTable borderrTable = new PdfPTable(1);
                    borderrTable.setWidthPercentage(100);

                    PdfPCell pricecell = new PdfPCell(new Phrase(""));
                    pricecell.setBorder(Rectangle.NO_BORDER);
                    pricecell.setBackgroundColor(BaseColor.GRAY);
                    pricecell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    pricecell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    pricecell.setFixedHeight(15);
                    pricecell.setPaddingRight(10);
                    borderrTable.setSpacingBefore(15);

                    borderrTable.addCell(pricecell);
                    layoutDocument.add(borderrTable);
                }






                for(int i=0; i<replacementTransactionDetailsList.size();i++){

                    if(i==0){
                        com.itextpdf.text.Paragraph orderidpara = new com.itextpdf.text.Paragraph(orderid_Forpdf);
                        orderidpara.setSpacingBefore(30);
                        orderidpara.setAlignment(Element.ALIGN_LEFT);
                        layoutDocument.add(orderidpara);

                        CustomerMobileno = "Customer Mobile No : "+CustomerMobileno;
                        com.itextpdf.text.Paragraph CustomerMobilenopara = new com.itextpdf.text.Paragraph(CustomerMobileno);
                        CustomerMobilenopara.setSpacingBefore(10);
                        CustomerMobilenopara.setAlignment(Element.ALIGN_LEFT);
                        layoutDocument.add(CustomerMobilenopara);



                        PdfPTable titleTable = new PdfPTable(3);
                        titleTable.setWidthPercentage(100);

                        titleTable.setSpacingBefore(30);




                        com.itextpdf.text.Font boldFont = new com.itextpdf.text.Font(Font.FontFamily.TIMES_ROMAN, 20, Font.BOLD);
                        com.itextpdf.text.Paragraph Markedtitlepara = new com.itextpdf.text.Paragraph("Marked Order Details");
                        Markedtitlepara.setSpacingBefore(5);
                        Markedtitlepara.setFont(boldFont);
                        Markedtitlepara.setAlignment(Element.ALIGN_LEFT);

                        PdfPCell marked_itemDesptitleCell = new PdfPCell(Markedtitlepara);
                        marked_itemDesptitleCell.setBackgroundColor(BaseColor.LIGHT_GRAY);

                        marked_itemDesptitleCell.setBorder(Rectangle.NO_BORDER);
                        marked_itemDesptitleCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        marked_itemDesptitleCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        marked_itemDesptitleCell.setPaddingLeft(10);
                        marked_itemDesptitleCell.setFixedHeight(30);
                        marked_itemDesptitleCell.setPaddingTop(8);
                        marked_itemDesptitleCell.setCellEvent(roundRectange);
                        marked_itemDesptitleCell.setBorder(Rectangle.NO_BORDER);

                        titleTable.addCell(marked_itemDesptitleCell);

                        com.itextpdf.text.Paragraph refundtitlepara = new com.itextpdf.text.Paragraph("Refund Details");
                        refundtitlepara.setSpacingBefore(5);
                        refundtitlepara.setFont(boldFont);
                        refundtitlepara.setAlignment(Element.ALIGN_CENTER);

                        PdfPCell refund_titleCell = new PdfPCell(new Phrase(refundtitlepara));

                        refund_titleCell.setBorder(Rectangle.NO_BORDER);
                        refund_titleCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        refund_titleCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        refund_titleCell.setPaddingLeft(10);
                        refund_titleCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                        refund_titleCell.setFixedHeight(30);
                        refund_titleCell.setPaddingTop(8);
                        refund_titleCell.setCellEvent(roundRectange);
                        refund_titleCell.setBorder(Rectangle.NO_BORDER);

                        titleTable.addCell(refund_titleCell);


                        com.itextpdf.text.Paragraph replacementtitlepara = new com.itextpdf.text.Paragraph("Replacement Details");
                        replacementtitlepara.setSpacingBefore(5);
                        replacementtitlepara.setFont(boldFont);
                        replacementtitlepara.setAlignment(Element.ALIGN_CENTER);

                        PdfPCell replacement_titleCell = new PdfPCell(new Phrase(replacementtitlepara));

                        replacement_titleCell.setBorder(Rectangle.NO_BORDER);
                        replacement_titleCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        replacement_titleCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        replacement_titleCell.setPaddingLeft(10);
                        replacement_titleCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                        replacement_titleCell.setPaddingTop(8);
                        replacement_titleCell.setCellEvent(roundRectange);
                        replacement_titleCell.setBorder(Rectangle.NO_BORDER);

                        replacement_titleCell.setFixedHeight(30);
                        titleTable.addCell(replacement_titleCell);

                        layoutDocument.add(titleTable);

                    }


                    String transationtype = String.valueOf(replacementTransactionDetailsList.get(i).getTransactiontype());



                    String reasonForMarked ="",orderidFromarray = "", transactionTime = "",refundedAmnt = "" ,replacementAmount ="", replacementOrderid ="" , discountAmount ="",replacementItemDesp ="";


                    try{
                        orderidFromarray = String.valueOf(replacementTransactionDetailsList.get(i).getOrderid());
                    }
                    catch ( Exception e){
                        orderidFromarray ="";
                        e.printStackTrace();
                    }

                    if(orderid.equals(orderidFromarray)) {
                        try {
                            transactionTime = String.valueOf(replacementTransactionDetailsList.get(i).getTransactiontime());
                            transactionTime =changeNewDatetoOldFormat(transactionTime);


                        } catch (Exception e) {
                            transactionTime = "";
                            e.printStackTrace();
                        }

                        try {
                            refundedAmnt = String.valueOf(replacementTransactionDetailsList.get(i).getRefundamount());
                        } catch (Exception e) {
                            refundedAmnt = "";
                            e.printStackTrace();
                        }

                        try {
                            reasonForMarked = String.valueOf(replacementTransactionDetailsList.get(i).getReasonformarked());
                        } catch (Exception e) {
                            reasonForMarked = "";
                            e.printStackTrace();
                        }


                        try {
                            replacementAmount = String.valueOf(replacementTransactionDetailsList.get(i).getReplacementorderamount());
                        } catch (Exception e) {
                            replacementAmount = "";
                            e.printStackTrace();
                        }

                        try {
                            replacementItemDesp = String.valueOf(replacementTransactionDetailsList.get(i).getReplacementitemdesp_string());
                        } catch (Exception e) {
                            replacementItemDesp = "";
                            e.printStackTrace();
                        }
                        try {
                            replacementOrderid = String.valueOf(replacementTransactionDetailsList.get(i).getReplacementorderid());
                        } catch (Exception e) {
                            replacementOrderid = "";
                            e.printStackTrace();
                        }
                        try {
                            discountAmount = String.valueOf(replacementTransactionDetailsList.get(i).getDiscountamount());
                        } catch (Exception e) {
                            discountAmount = "";
                            e.printStackTrace();
                        }
                        PdfPTable markedItem_table = new PdfPTable(1);
                        markedItem_table.setSpacingBefore(15);

                        markedItem_table.setWidthPercentage(100);

                        if (transationtype.toString().toUpperCase().equals("MARKED")) {
                            String itemDesp = "";
                            String subCtgyKey = "";

                            try {

                                JSONArray array = new JSONArray(replacementTransactionDetailsList.get(i).getMarkeditemdesp_String());

                                //Log.i("tag","array.length()"+ array.length());

                                for (int l = 0; l < array.length(); l++) {
                                    JSONObject json = array.getJSONObject(l);

                                    //Log.i("tag", "array.lengrh(i" + json.length());
                                    try {
                                        if (json.has("tmcsubctgykey")) {
                                            subCtgyKey = String.valueOf(json.get("tmcsubctgykey"));
                                        } else {
                                            subCtgyKey = " ";
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }


                                    String cutname = "";

                                    try {
                                        if (json.has("cutname")) {
                                            cutname = String.valueOf(json.get("cutname"));
                                        } else {
                                            cutname = "";
                                        }
                                    } catch (Exception e) {
                                        cutname = "";
                                        e.printStackTrace();
                                    }

                                    try {
                                        if ((cutname.length() > 0) && (!cutname.equals(null)) && (!cutname.equals("null"))) {
                                            cutname = " [ " + cutname + " ] ";
                                        } else {
                                            //cutname="";
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }


                                    String itemName = String.valueOf(json.get("itemname"));
                                    String price = String.valueOf(json.get("tmcprice"));
                                    String quantity = String.valueOf(json.get("quantity"));
                                    if (itemDesp.length() > 0) {
                                        if (subCtgyKey.equals("tmcsubctgy_16")) {
                                            itemDesp = String.format("%s ,\n%s %s * %s", itemDesp, "Grill House " + itemName, cutname, quantity);

                                        } else if (subCtgyKey.equals("tmcsubctgy_15")) {
                                            itemDesp = String.format("%s ,\n%s  %s * %s", itemDesp, "Ready to Cook  " + itemName, cutname, quantity);

                                        } else {
                                            itemDesp = String.format("%s ,\n%s  %s * %s", itemDesp, itemName, cutname, quantity);

                                        }
                                    } else {
                                        if (subCtgyKey.equals("tmcsubctgy_16")) {
                                            itemDesp = String.format("%s  %s * %s", "Grill House " + itemName, cutname, quantity);

                                        } else if (subCtgyKey.equals("tmcsubctgy_15")) {
                                            itemDesp = String.format("%s  %s * %s", "Ready to Cook  " + itemName, cutname, quantity);

                                        } else {
                                            itemDesp = String.format("%s  %s * %s", itemName, cutname, quantity);

                                        }

                                    }

                                }


                            }
                            catch (JSONException e) {
                                e.printStackTrace();

                            }

                            PdfPCell marked_timeLabelCell = new PdfPCell(new Phrase("Marked Time  :  "));

                            marked_timeLabelCell.setBorder(Rectangle.NO_BORDER);
                            marked_timeLabelCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                            marked_timeLabelCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                            marked_timeLabelCell.setPaddingLeft(10);

                            markedItem_table.addCell(marked_timeLabelCell);


                            PdfPCell marked_timeCell = new PdfPCell(new Phrase(transactionTime));

                            marked_timeCell.setBorder(Rectangle.NO_BORDER);
                            marked_timeCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                            marked_timeCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                            marked_timeCell.setPaddingLeft(10);

                            markedItem_table.addCell(marked_timeCell);


                            PdfPCell marked_itemDespLabelCell = new PdfPCell(new Phrase("Marked Item Details : "));

                            marked_itemDespLabelCell.setBorder(Rectangle.NO_BORDER);
                            marked_itemDespLabelCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                            marked_itemDespLabelCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                            marked_itemDespLabelCell.setPaddingLeft(10);
                            marked_itemDespLabelCell.setPaddingTop(15);

                            markedItem_table.addCell(marked_itemDespLabelCell);


                            PdfPCell marked_itemDespCell = new PdfPCell(new Phrase(itemDesp));

                            marked_itemDespCell.setBorder(Rectangle.NO_BORDER);
                            marked_itemDespCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                            marked_itemDespCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                            marked_itemDespCell.setPaddingLeft(10);
                            markedItem_table.addCell(marked_itemDespCell);

                            if(!reasonForMarked.equals("") && !reasonForMarked.equals(null) && !reasonForMarked.equals("null") && !reasonForMarked.equals("0")) {

                                PdfPCell reasonForMarked_LabelCell = new PdfPCell(new Phrase("Marked Item Details : "));

                                reasonForMarked_LabelCell.setBorder(Rectangle.NO_BORDER);
                                reasonForMarked_LabelCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                                reasonForMarked_LabelCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                                reasonForMarked_LabelCell.setPaddingLeft(10);
                                reasonForMarked_LabelCell.setPaddingTop(15);

                                markedItem_table.addCell(reasonForMarked_LabelCell);


                                PdfPCell reasonForMarked_Cell = new PdfPCell(new Phrase(reasonForMarked));

                                reasonForMarked_Cell.setBorder(Rectangle.NO_BORDER);
                                reasonForMarked_Cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                                reasonForMarked_Cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                                reasonForMarked_Cell.setPaddingLeft(10);
                                markedItem_table.addCell(reasonForMarked_Cell);

                            }

                        } else {

                            PdfPCell marked_itemDespLabelCell = new PdfPCell(new Phrase(""));

                            marked_itemDespLabelCell.setBorder(Rectangle.NO_BORDER);
                            marked_itemDespLabelCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                            marked_itemDespLabelCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                            marked_itemDespLabelCell.setPaddingLeft(10);
                            marked_itemDespLabelCell.setFixedHeight(10);
                            markedItem_table.addCell(marked_itemDespLabelCell);


                            PdfPCell marked_itemDespCell = new PdfPCell(new Phrase(""));

                            marked_itemDespCell.setBorder(Rectangle.NO_BORDER);
                            marked_itemDespCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                            marked_itemDespCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                            marked_itemDespCell.setPaddingLeft(10);
                            marked_itemDespCell.setFixedHeight(10);
                            markedItem_table.addCell(marked_itemDespCell);
                        }


                        PdfPTable refundItem_table = new PdfPTable(1);
                        refundItem_table.setSpacingBefore(15);

                        refundItem_table.setWidthPercentage(80);
                        if (transationtype.toString().toUpperCase().equals("REFUND")) {

                            PdfPCell refundedTimeLabelCell = new PdfPCell(new Phrase("Refunded Time : "));

                            refundedTimeLabelCell.setBorder(Rectangle.NO_BORDER);
                            refundedTimeLabelCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                            refundedTimeLabelCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                            refundedTimeLabelCell.setPaddingLeft(10);

                            refundItem_table.addCell(refundedTimeLabelCell);


                            PdfPCell marked_itemDespCell = new PdfPCell(new Phrase(transactionTime));

                            marked_itemDespCell.setBorder(Rectangle.NO_BORDER);
                            marked_itemDespCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                            marked_itemDespCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                            marked_itemDespCell.setPaddingLeft(10);

                            refundItem_table.addCell(marked_itemDespCell);


                            PdfPCell refundedAmtLabelCell = new PdfPCell(new Phrase("Refunded Amount : "));

                            refundedAmtLabelCell.setBorder(Rectangle.NO_BORDER);
                            refundedAmtLabelCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                            refundedAmtLabelCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                            refundedAmtLabelCell.setPaddingLeft(10);
                            refundedAmtLabelCell.setPaddingTop(15);
                            refundItem_table.addCell(refundedAmtLabelCell);


                            PdfPCell refundedAmt_Cell = new PdfPCell(new Phrase(String.valueOf(refundedAmnt + "   Rs")));

                            refundedAmt_Cell.setBorder(Rectangle.NO_BORDER);
                            refundedAmt_Cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                            refundedAmt_Cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                            refundedAmt_Cell.setPaddingLeft(10);
                            refundedAmt_Cell.setPaddingTop(3);

                            refundItem_table.addCell(refundedAmt_Cell);
                        }
                        else {


                            PdfPCell refundedTimeLabelCell = new PdfPCell(new Phrase("     "));

                            refundedTimeLabelCell.setBorder(Rectangle.NO_BORDER);
                            refundedTimeLabelCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                            refundedTimeLabelCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                            refundedTimeLabelCell.setPaddingLeft(10);
                            refundedTimeLabelCell.setFixedHeight(10);
                            refundItem_table.addCell(refundedTimeLabelCell);


                            PdfPCell marked_itemDespCell = new PdfPCell(new Phrase("                "));

                            marked_itemDespCell.setBorder(Rectangle.NO_BORDER);
                            marked_itemDespCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                            marked_itemDespCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                            marked_itemDespCell.setPaddingLeft(10);
                            marked_itemDespCell.setFixedHeight(10);
                            refundItem_table.addCell(marked_itemDespCell);



                        }

                        PdfPTable replacementItem_table = new PdfPTable(1);
                        replacementItem_table.setSpacingBefore(15);

                        replacementItem_table.setWidthPercentage(120);


                        if (transationtype.toString().toUpperCase().equals("REPLACEMENT")) {

                            PdfPCell replacementTimeLabelCell = new PdfPCell(new Phrase("Replacement  Time : "));

                            replacementTimeLabelCell.setBorder(Rectangle.NO_BORDER);
                            replacementTimeLabelCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                            replacementTimeLabelCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                            replacementTimeLabelCell.setPaddingLeft(10);

                            replacementItem_table.addCell(replacementTimeLabelCell);


                            PdfPCell replacementTimeCell = new PdfPCell(new Phrase(transactionTime));

                            replacementTimeCell.setBorder(Rectangle.NO_BORDER);
                            replacementTimeCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                            replacementTimeCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                            replacementTimeCell.setPaddingLeft(10);
                            replacementTimeCell.setPaddingTop(5);

                            replacementItem_table.addCell(replacementTimeCell);


                            PdfPCell replacementAmtLabelCell = new PdfPCell(new Phrase("Replacement Amount : "));

                            replacementAmtLabelCell.setBorder(Rectangle.NO_BORDER);
                            replacementAmtLabelCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                            replacementAmtLabelCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                            replacementAmtLabelCell.setPaddingLeft(10);
                            replacementAmtLabelCell.setPaddingTop(15);

                            replacementItem_table.addCell(replacementAmtLabelCell);


                            PdfPCell replacementAmt_Cell = new PdfPCell(new Phrase(String.valueOf(replacementAmount + "   Rs")));

                            replacementAmt_Cell.setBorder(Rectangle.NO_BORDER);
                            replacementAmt_Cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                            replacementAmt_Cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                            replacementAmt_Cell.setPaddingLeft(10);

                            replacementAmt_Cell.setPaddingTop(3);

                            replacementItem_table.addCell(replacementAmt_Cell);


                            PdfPCell replacementorderidLabelCell = new PdfPCell(new Phrase("Replaced Orderid : "));

                            replacementorderidLabelCell.setBorder(Rectangle.NO_BORDER);
                            replacementorderidLabelCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                            replacementorderidLabelCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                            replacementorderidLabelCell.setPaddingLeft(10);
                            replacementorderidLabelCell.setPaddingTop(15);
                            replacementorderidLabelCell.setNoWrap(false);
                            replacementItem_table.addCell(replacementorderidLabelCell);


                            PdfPCell replacementorderid_Cell = new PdfPCell(new Phrase(String.valueOf(replacementOrderid)));
                            replacementorderid_Cell.setPaddingTop(4);

                            replacementorderid_Cell.setBorder(Rectangle.NO_BORDER);
                            replacementorderid_Cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                            replacementorderid_Cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                            replacementorderid_Cell.setPaddingLeft(10);
                            replacementorderidLabelCell.setPaddingTop(3);
                            replacementorderidLabelCell.setNoWrap(false);

                            replacementItem_table.addCell(replacementorderid_Cell);
                            discountAmount = String.valueOf(discountAmount);
                            if(!discountAmount.equals("") && !discountAmount.equals(null) && !discountAmount.equals("null") && !discountAmount.equals("0")){
                                PdfPCell discountAmountLabelCell = new PdfPCell(new Phrase("Discount Amount : "));

                                discountAmountLabelCell.setBorder(Rectangle.NO_BORDER);
                                discountAmountLabelCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                                discountAmountLabelCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                                discountAmountLabelCell.setPaddingLeft(10);
                                discountAmountLabelCell.setPaddingTop(15);
                                discountAmountLabelCell.setNoWrap(false);
                                replacementItem_table.addCell(discountAmountLabelCell);


                                PdfPCell discountAmount_Cell = new PdfPCell(new Phrase(String.valueOf(discountAmount+" Rs")));
                                discountAmount_Cell.setPaddingTop(4);

                                discountAmount_Cell.setBorder(Rectangle.NO_BORDER);
                                discountAmount_Cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                                discountAmount_Cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                                discountAmount_Cell.setPaddingLeft(10);
                                discountAmount_Cell.setPaddingTop(3);

                                replacementItem_table.addCell(discountAmount_Cell);

                            }
                        } else {

                            PdfPCell refundedTimeLabelCell = new PdfPCell(new Phrase("     "));

                            refundedTimeLabelCell.setBorder(Rectangle.NO_BORDER);
                            refundedTimeLabelCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                            refundedTimeLabelCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                            refundedTimeLabelCell.setPaddingLeft(10);
                            refundedTimeLabelCell.setFixedHeight(10);
                            refundItem_table.addCell(refundedTimeLabelCell);


                            PdfPCell marked_itemDespCell = new PdfPCell(new Phrase("                "));

                            marked_itemDespCell.setBorder(Rectangle.NO_BORDER);
                            marked_itemDespCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                            marked_itemDespCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                            marked_itemDespCell.setPaddingLeft(10);
                            marked_itemDespCell.setFixedHeight(10);
                            refundItem_table.addCell(marked_itemDespCell);

                        }


                        PdfPTable outertable = new PdfPTable(3);
                        PdfPCell outercell1 = new PdfPCell(markedItem_table);
                        outercell1.setCellEvent(roundRectange);
                        outercell1.setBorder(Rectangle.NO_BORDER);
                        outercell1.setBorder(Rectangle.NO_BORDER);
                        outercell1.setPadding(8);
                        outercell1.setPaddingTop(4);

                        outertable.addCell(outercell1);
                        outertable.setWidthPercentage(100);


                        PdfPCell outercell2 = new PdfPCell(refundItem_table);
                        outercell2.setCellEvent(roundRectange);
                        outercell2.setBorder(Rectangle.NO_BORDER);
                        outercell2.setBorder(Rectangle.NO_BORDER);
                        outercell2.setPadding(8);
                        outercell2.setPaddingTop(4);

                        outertable.addCell(outercell2);
                        outertable.setWidthPercentage(100);


                        PdfPCell outercell3 = new PdfPCell(replacementItem_table);
                        outercell3.setCellEvent(roundRectange);
                        outercell3.setBorder(Rectangle.NO_BORDER);
                        outercell3.setBorder(Rectangle.NO_BORDER);
                        outercell3.setPadding(8);
                        outercell3.setPaddingTop(4);
                        outertable.addCell(outercell3);
                        outertable.setWidthPercentage(100);


                        layoutDocument.add(outertable);


                    }

                    }





            } catch (DocumentException e) {
                e.printStackTrace();
            }

        }
    }



    private String changeNewDatetoOldFormat(String transactiontime) {
        String CurrentDate1 = "";

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                Date date = sdf.parse(transactiontime);

                SimpleDateFormat day = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");
                CurrentDate1 = day.format(date);


            } catch (ParseException e) {
                e.printStackTrace();
            }


        } catch (Exception e) {

            try {
                SimpleDateFormat sdff = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssZ");
                try {
                    Date date = sdff.parse(transactiontime);

                    SimpleDateFormat day = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");
                    CurrentDate1 = day.format(date);

                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            } catch (Exception e2) {

                try {
                    SimpleDateFormat sdff = new SimpleDateFormat("yyyy-MM-dd");
                    try {
                        Date date = sdff.parse(transactiontime);

                        SimpleDateFormat day = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");
                        CurrentDate1 = day.format(date);

                    } catch (Exception e3) {
                        e3.printStackTrace();
                    }
                } catch (Exception e4) {
                    CurrentDate1 = transactiontime;

                    e4.printStackTrace();
                }
                e2.printStackTrace();
            }
            e.printStackTrace();
        }

        return CurrentDate1;

    }


        private String changeOldDatetoNewFormat(String todaysdate) {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy");
        String CurrentDate1 ="";
        try {
            Date date = sdf.parse(todaysdate);

            SimpleDateFormat day = new SimpleDateFormat("yyyy-MM-dd");
             CurrentDate1 = day.format(date);



        } catch (ParseException e) {
            e.printStackTrace();
        }
        return CurrentDate1;
    }


    private void openDatePicker() {


        final Calendar cldr = Calendar.getInstance();
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);
        // date picker dialog
        datepicker = new DatePickerDialog(Replacement_Refund_Transaction_Report.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        try {

                            replacementTransactionDetails_SortedList.clear();
                            replacementTransactionDetailsList.clear();

                            String month_in_String = getMonthString(monthOfYear);
                            String monthstring = String.valueOf(monthOfYear+1);
                            String datestring =  String.valueOf(dayOfMonth);

                            if(datestring.length()==1){
                                datestring="0"+datestring;
                            }
                            if(monthstring.length()==1){
                                monthstring="0"+monthstring;
                            }



                            Calendar myCalendar = new GregorianCalendar(year, monthOfYear, dayOfMonth);

                            int dayOfWeek = myCalendar.get(Calendar.DAY_OF_WEEK);

                            String CurrentDay =   getDayString(dayOfWeek);
                            //Log.d(Constants.TAG, "dayOfWeek Response: " + dayOfWeek);

                            String CurrentDateString =datestring+monthstring+String.valueOf(year);
                           String  DateString = (CurrentDay+", "+dayOfMonth + " " + month_in_String + " " + year);

                            dateSelector_text.setText(DateString);
                            //getOrderForSelectedDate(DateString, vendorKey);

                            String newFormatOfDate = changeOldDatetoNewFormat(DateString);
                            getReplacementTransactionDetails(newFormatOfDate, vendorKey);

                        }
                        catch (Exception e ){
                            e.printStackTrace();
                        }
                    }
                }, year, month, day);
        datepicker.show();

    }





    private void getReplacementTransactionDetails(String todaysdate, String vendorKey) {
        Adjusting_Widgets_Visibility(true);
        replacementTransactionDetailsList.clear();
        orderidlist.clear();
        orderStatuslist.clear();
        hashMap_replacementTransactionDetails.clear();
        orderidHashMap.clear();
        replacementTransactionDetails_SortedList.clear();
        String transactiontime1 = todaysdate+" 00:00:00";
        String transactiontime2 = todaysdate+" 23:59:59";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.api_GetReplacementTransactionDetailsForTransactionTimeVendorkey + "?transactiontime1=" + transactiontime1 + "&vendorkey=" + vendorKey + "&transactiontime2=" + transactiontime2, null,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(@NonNull JSONObject response) {
                        try {
                            JSONArray JArray = response.getJSONArray("content");
                            if (JArray.length() > 0) {
                               String replacementOrderDetailsString = JArray.toString();
                                convertReplacementTransactionDetailsJsonIntoArray(replacementOrderDetailsString);
                            } else {
                                Toast.makeText(Replacement_Refund_Transaction_Report.this, "There is no Transcation on this Date ", Toast.LENGTH_LONG).show();
                                Adjusting_Widgets_Visibility(false);

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }

                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                Toast.makeText(Replacement_Refund_Transaction_Report.this, "There is no Transcation on this Date "+error, Toast.LENGTH_LONG).show();
                Adjusting_Widgets_Visibility(false);
                //Log.d(Constants.TAG, "getOrderDetailsUsingApi Error: " + error.getLocalizedMessage());
                //Log.d(Constants.TAG, "getOrderDetailsUsingApi Error: " + error.getMessage());
                //Log.d(Constants.TAG, "getOrderDetailsUsingApi Error: " + error.toString());

                error.printStackTrace();
            }
        }) {
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                final Map<String, String> params = new HashMap<>();
                params.put("vendorkey", vendorKey);

                return params;
            }


            @NonNull
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                final Map<String, String> header = new HashMap<>();
                header.put("Content-Type", "application/json");

                return header;
            }
        };
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(40000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Make the request
        Volley.newRequestQueue(Replacement_Refund_Transaction_Report.this).add(jsonObjectRequest);




    }

    private void convertReplacementTransactionDetailsJsonIntoArray(String stringOfArray) {

        try {
            JSONArray JArray = new JSONArray(stringOfArray);
            //Log.d(Constants.TAG, "convertingJsonStringintoArray Response: " + JArray);
            int arrayLength = JArray.length();
            //Log.d("Constants.TAG", "convertingJsonStringintoArray Response: " + arrayLength);
            int i1 = 0;


            for (; i1 < arrayLength; i1++) {
                Modal_ReplacementTransactionDetails modal_replacementTransactionDetails = new Modal_ReplacementTransactionDetails();
                String transactionStatus = "", transactionType = "",transactionTime = "" , ordermarkeddate ="" , orderdelivereddate ="";
                List<Modal_ReplacementTransactionDetails> replacementTransactionDetailsArray = new ArrayList<>();
                String orderid = "", userMobile="";

                try {
                    JSONObject json = JArray.getJSONObject(i1);
                      try {
                        if (json.has("discountamount")) {
                            if(String.valueOf(json.getString("discountamount")).equals("")){
                                modal_replacementTransactionDetails.setDiscountamount(String.valueOf("0"));

                            }
                            else{
                                modal_replacementTransactionDetails.setDiscountamount(String.valueOf(json.getString("discountamount")));

                            }

                        } else {
                            modal_replacementTransactionDetails.setDiscountamount("0");

                        }
                    } catch (Exception e) {
                        modal_replacementTransactionDetails.setDiscountamount("0");

                        e.printStackTrace();
                    }
                    try {
                        if (json.has("reasonformarked")) {
                            modal_replacementTransactionDetails.setReasonformarked(String.valueOf(json.getString("reasonformarked")));

                        } else {
                            modal_replacementTransactionDetails.setReasonformarked("");

                        }
                    } catch (Exception e) {
                        modal_replacementTransactionDetails.setReasonformarked("");

                        e.printStackTrace();
                    }

                    try {
                        if (json.has("markeditemdesp")) {
                            modal_replacementTransactionDetails.setMarkeditemdesp_String(String.valueOf(json.getString("markeditemdesp")));

                        } else {
                            modal_replacementTransactionDetails.setMarkeditemdesp_String("");

                        }
                    } catch (Exception e) {
                        modal_replacementTransactionDetails.setMarkeditemdesp_String("");

                        e.printStackTrace();
                    }


                    try {
                        if (json.has("mobileno")) {
                            userMobile = String.valueOf(json.getString("mobileno"));
                            userMobile = userMobile.replace("+91","");
                            modal_replacementTransactionDetails.setMobileno(String.valueOf(userMobile));

                        } else {
                            modal_replacementTransactionDetails.setMobileno("");
                            userMobile = "";
                        }
                    } catch (Exception e) {
                        modal_replacementTransactionDetails.setMobileno("");
                        userMobile = "";

                        e.printStackTrace();
                    }


                    try {
                        if (json.has("orderid")) {
                             orderid = String.valueOf(json.getString("orderid"));
                            modal_replacementTransactionDetails.setOrderid(orderid);
                            if(!orderidlist.contains(orderid)){
                                orderidlist.add(orderid);
                                orderidHashMap.put(orderid,userMobile);
                            }
                        } else {
                            modal_replacementTransactionDetails.setOrderid("");

                        }
                    } catch (Exception e) {
                        modal_replacementTransactionDetails.setOrderid("");

                        e.printStackTrace();
                    }


                    try {
                        if (json.has("refundamount")) {
                            modal_replacementTransactionDetails.setRefundamount(String.valueOf(json.getString("refundamount")));

                        } else {
                            modal_replacementTransactionDetails.setRefundamount("");

                        }
                    } catch (Exception e) {
                        modal_replacementTransactionDetails.setRefundamount("");

                        e.printStackTrace();
                    }


                    try {
                        if (json.has("replacementitemdesp")) {
                            modal_replacementTransactionDetails.setReplacementitemdesp_string(String.valueOf(json.getString("replacementitemdesp")));

                        } else {
                            modal_replacementTransactionDetails.setReplacementitemdesp_string("");

                        }
                    } catch (Exception e) {
                        modal_replacementTransactionDetails.setReplacementitemdesp_string("");

                        e.printStackTrace();
                    }

                    try {
                        if (json.has("replacementorderamount")) {
                            modal_replacementTransactionDetails.setReplacementorderamount(String.valueOf(json.getString("replacementorderamount")));

                        } else {
                            modal_replacementTransactionDetails.setReplacementorderamount("");

                        }
                    } catch (Exception e) {
                        modal_replacementTransactionDetails.setReplacementorderamount("");

                        e.printStackTrace();
                    }

                    try {
                        if (json.has("replacementorderid")) {
                            modal_replacementTransactionDetails.setReplacementorderid(String.valueOf(json.getString("replacementorderid")));

                        } else {
                            modal_replacementTransactionDetails.setReplacementorderid("");

                        }
                    } catch (Exception e) {
                        modal_replacementTransactionDetails.setReplacementorderid("");

                        e.printStackTrace();
                    }

                    try {
                        if (json.has("transactionstatus")) {
                            modal_replacementTransactionDetails.setTransactionstatus(String.valueOf(json.getString("transactionstatus")).toUpperCase());
                            transactionStatus = String.valueOf(json.getString("transactionstatus").toUpperCase());

                        } else {
                            transactionStatus = "";
                            modal_replacementTransactionDetails.setTransactionstatus("");

                        }
                    } catch (Exception e) {
                        transactionStatus = "";

                        modal_replacementTransactionDetails.setTransactionstatus("");

                        e.printStackTrace();
                    }

                    try {
                        if (json.has("transactiontime")) {
                            transactionTime = String.valueOf(json.getString("transactiontime"));
                           String formatedTime =  getTimeinOLDFormat(transactionTime);
                            modal_replacementTransactionDetails.setTransactiontime(formatedTime);

                        } else {
                            modal_replacementTransactionDetails.setTransactiontime("");

                        }
                    } catch (Exception e) {
                        modal_replacementTransactionDetails.setTransactiontime("");

                        e.printStackTrace();
                    }

                    try {
                        if (json.has("ordermarkeddate")) {
                            ordermarkeddate = String.valueOf(json.getString("ordermarkeddate"));
                           // String formatedTime =  getTimeinOLDFormat(ordermarkeddate);
                            modal_replacementTransactionDetails.setOrderMarkedDate(ordermarkeddate);

                        } else {
                            modal_replacementTransactionDetails.setOrderMarkedDate("");

                        }
                    } catch (Exception e) {

                        modal_replacementTransactionDetails.setOrderMarkedDate(ordermarkeddate);

                        e.printStackTrace();
                    }


                    try {
                        if (json.has("orderdelivereddate")) {
                            orderdelivereddate = String.valueOf(json.getString("orderdelivereddate"));
                            //String formatedTime =  getTimeinOLDFormat(orderdelivereddate);
                            modal_replacementTransactionDetails.setOrderDeliveredDate(orderdelivereddate);

                        } else {
                            modal_replacementTransactionDetails.setOrderDeliveredDate("");

                        }
                    } catch (Exception e) {
                        modal_replacementTransactionDetails.setOrderDeliveredDate(orderdelivereddate);

                        e.printStackTrace();
                    }

                    try {
                        if (json.has("transactiontype")) {
                            modal_replacementTransactionDetails.setTransactiontype(String.valueOf(json.getString("transactiontype")));
                            transactionType = String.valueOf(json.getString("transactiontype").toString().toUpperCase());

                        } else {
                            modal_replacementTransactionDetails.setTransactiontype("");
                            transactionType = "";

                        }
                    } catch (Exception e) {
                        transactionType = "";

                        modal_replacementTransactionDetails.setTransactiontype("");

                        e.printStackTrace();
                    }

                    try {
                        if (json.has("markeditemamount")) {
                            modal_replacementTransactionDetails.setMarkedOrderAmountString(String.valueOf(json.getString("markeditemamount")));

                        } else {
                            modal_replacementTransactionDetails.setMarkedOrderAmountString("");

                        }
                    } catch (Exception e) {
                        modal_replacementTransactionDetails.setMarkedOrderAmountString("");

                        e.printStackTrace();
                    }


                    try {
                        if (json.has("vendorkey")) {
                            modal_replacementTransactionDetails.setVendorkey(String.valueOf(json.getString("vendorkey")));

                        } else {
                            modal_replacementTransactionDetails.setVendorkey("");

                        }
                    } catch (Exception e) {
                        modal_replacementTransactionDetails.setVendorkey("");

                        e.printStackTrace();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try{

                    if(!transactionType.equals("")){
                        if(!orderStatuslist.contains(transactionType)){
                            orderStatuslist.add(transactionType);
                            if(hashMap_replacementTransactionDetails.containsKey(transactionType)){
                                List<Modal_ReplacementTransactionDetails> arrayFromHashmap = hashMap_replacementTransactionDetails.get(transactionType);
                                arrayFromHashmap.add(modal_replacementTransactionDetails);


                            }
                            else{
                                List<Modal_ReplacementTransactionDetails> arrayForHashmap = new ArrayList<>();
                                arrayForHashmap.add(modal_replacementTransactionDetails);
                                hashMap_replacementTransactionDetails.put(transactionType,arrayForHashmap);
                            }
                        }
                        else{
                            if(hashMap_replacementTransactionDetails.containsKey(transactionType)){
                                List<Modal_ReplacementTransactionDetails> arrayFromHashmap = hashMap_replacementTransactionDetails.get(transactionType);
                                arrayFromHashmap.add(modal_replacementTransactionDetails);


                            }
                            else{
                                List<Modal_ReplacementTransactionDetails> arrayForHashmap = new ArrayList<>();
                                arrayForHashmap.add(modal_replacementTransactionDetails);
                                hashMap_replacementTransactionDetails.put(transactionType,arrayForHashmap);
                            }
                        }
                    }

                }
                catch (Exception e){
                    e.printStackTrace();
                }


                try {
                        try {
                            replacementTransactionDetailsList.add(modal_replacementTransactionDetails);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                        try {
                            if (arrayLength - 1 == i1) {

                                CallAdapter(replacementTransactionDetailsList);


                                try{
                                    transaction_Counttextwidget.setText(String.valueOf(replacementTransactionDetailsList.size()));
                                }
                                catch (Exception e){
                                    e.printStackTrace();
                                }

                            }
                        } catch (Exception e) {
                            Adjusting_Widgets_Visibility(false);

                            e.printStackTrace();
                        }

                } catch (Exception e) {
                    Adjusting_Widgets_Visibility(false);

                    e.printStackTrace();
                }


            }


        } catch (Exception e) {
            Adjusting_Widgets_Visibility(false);

            e.printStackTrace();
        }

    }


    public void CallAdapter(List<Modal_ReplacementTransactionDetails> replacementTransactionDetailsList) {
        Collections.sort(replacementTransactionDetailsList, new Comparator<Modal_ReplacementTransactionDetails>() {
            public int compare(final Modal_ReplacementTransactionDetails object1, final Modal_ReplacementTransactionDetails object2) {
                return object1.getTransactiontime().compareTo(object2.getTransactiontime());
            }
        });
        Adjusting_Widgets_Visibility(false);

        Context mContext = Replacement_Refund_Transaction_Report.this;
        Adapter_Replacement_Refund_TransactionList adapter_replacement_refund_transactionList = new Adapter_Replacement_Refund_TransactionList(mContext, replacementTransactionDetailsList, Replacement_Refund_Transaction_Report.this);
        replacementTransaction_ListView.setAdapter(adapter_replacement_refund_transactionList);

    }



    private String getTimeinOLDFormat(String transactiontime) {
        String CurrentDate1 = "";

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                Date date = sdf.parse(transactiontime);

                SimpleDateFormat day = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");
                CurrentDate1 = day.format(date);


            } catch (ParseException e) {
                e.printStackTrace();
            }


        } catch (Exception e) {

            try {
                SimpleDateFormat sdff = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {
                    Date date = sdff.parse(transactiontime);

                    SimpleDateFormat day = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");
                    CurrentDate1 = day.format(date);

                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            } catch (Exception e2) {

                try {
                    SimpleDateFormat sdff = new SimpleDateFormat("yyyy-MM-dd");
                    try {
                        Date date = sdff.parse(transactiontime);

                        SimpleDateFormat day = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");
                        CurrentDate1 = day.format(date);

                    } catch (Exception e3) {
                        e3.printStackTrace();
                    }
                } catch (Exception e4) {
                    CurrentDate1 = transactiontime;

                    e4.printStackTrace();
                }
                e2.printStackTrace();
            }
            e.printStackTrace();
        }

        return CurrentDate1;

    }


    private void hideKeyboard(EditText editText) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        Objects.requireNonNull(imm).hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    private void closeSearchBarEditText() {
        dateSelectorLayout.setVisibility(View.VISIBLE);
        search_button.setVisibility(View.VISIBLE);
        search_close_btn.setVisibility(View.GONE);
        search_barEdit.setVisibility(View.GONE);
    }

    private void showSearchBarEditText() {
        dateSelectorLayout.setVisibility(View.GONE);
        search_button.setVisibility(View.GONE);
        search_close_btn.setVisibility(View.VISIBLE);
        search_barEdit.setVisibility(View.VISIBLE);
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



    private String getMonthString(int value) {
        if (value == 0) {
            return "Jan";
        } else if (value == 1) {
            return "Feb";
        } else if (value == 2) {
            return "Mar";
        } else if (value == 3) {
            return "Apr";
        } else if (value == 4) {
            return "May";
        } else if (value == 5) {
            return "Jun";
        } else if (value == 6) {
            return "Jul";
        } else if (value == 7) {
            return "Aug";
        } else if (value == 8) {
            return "Sep";
        } else if (value == 9) {
            return "Oct";
        } else if (value == 10) {
            return "Nov";
        } else if (value == 11) {
            return "Dec";
        }
        return "";
    }


    private String getDayString(int value) {
        if (value == 1) {
            return "Sun";
        }  else if (value == 2) {
            return "Mon";
        } else if (value == 3) {
            return "Tue";
        } else if (value == 4) {
            return "Wed";
        } else if (value == 5) {
            return "Thu";
        } else if (value == 6) {
            return "Fri";
        }
        else if (value == 7) {
            return "Sat";
        }
        return "";
    }




    private String getDatewithNameoftheDay() {
        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat day = new SimpleDateFormat("EEE");
       String CurrentDay = day.format(c);



        SimpleDateFormat df = new SimpleDateFormat("d MMM yyyy");
        CurrentDate = df.format(c);

        CurrentDate = CurrentDay+", "+CurrentDate;


        //CurrentDate = CurrentDay+", "+CurrentDate;
        System.out.println("todays Date  " + CurrentDate);


        return CurrentDate;
    }









    void Adjusting_Widgets_Visibility(boolean show) {
        if(show) {
            loadingPanel.setVisibility(View.VISIBLE);
            loadingpanelmask.setVisibility(View.VISIBLE);

            replacementTransaction_ListView.setVisibility(View.GONE);

        }
        else{
            loadingPanel.setVisibility(View.GONE);
            loadingpanelmask.setVisibility(View.GONE);
            replacementTransaction_ListView.setVisibility(View.VISIBLE);

        }

    }








}