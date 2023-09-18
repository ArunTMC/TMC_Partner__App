package com.meatchop.tmcpartner.asynctaskforprinter;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.meatchop.tmcpartner.Constants;
import com.meatchop.tmcpartner.Printer_POJO_Class;
import com.meatchop.tmcpartner.posscreen_javaclasses.pos_new_orders.Modal_NewOrderItems;
import com.pos.printer.PrinterFunctions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class BackgroundServiceForPrintingReceipt2 extends Service {

    Modal_forPrintReceipt modal_forPrintReceipt = new Modal_forPrintReceipt();
    AsyncTaskListener_forPrintReceipt mListener;
    String portName = "USB";
    int portSettings=0,totalGstAmount=0;
    List<String> cart_Item_List = new ArrayList<>() ;
    HashMap<String, Modal_NewOrderItems> cartItem_hashmap = new HashMap<>();
    boolean isPrintedSecondTime = false;
    private boolean isRunning;

    private AsyncTask<Void, Void, Void> mTask;


    @Override
    public void onCreate() {
        // Perform initialization code here


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Put your background code here


        modal_forPrintReceipt = intent.getParcelableExtra("modalData");
        cart_Item_List = modal_forPrintReceipt.getCart_Item_List();
        cartItem_hashmap = (HashMap<String, Modal_NewOrderItems>) intent.getSerializableExtra("hashmapdata");
        isPrintedSecondTime =intent.getBooleanExtra("isCalledSecondtime",true);
        //   mTask = new MyTask().execute();

        printRecipt();


        return START_STICKY;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        /*if (mTask != null) {
            mTask.cancel(true);
        }

         */
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    public class MyBinder extends Binder {
        public BackgroundServiceForPrintingReceipt2 getService() {
            return BackgroundServiceForPrintingReceipt2.this;
        }
    }

    private final IBinder binder = new BackgroundServiceForPrintingReceipt2.MyBinder();


    private class MyTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            // Perform background operation here
            printRecipt();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            // Update UI or perform other tasks after completion
            stopSelf();
        }
    }



    public void printRecipt() {

        String slottime = "";
        try {

            PrinterFunctions.PortDiscovery(portName, portSettings);
            PrinterFunctions.OpenCashDrawer(portName, portSettings, 0, 4);

            // PrinterFunctions.OpenPort( portName, portSettings);
            //    PrinterFunctions.CheckStatus( portName, portSettings,2);
            PrinterFunctions.SelectPrintMode(portName, portSettings, 0);

        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (modal_forPrintReceipt.getOrdertype().equals(Constants.PhoneOrder)) {
                slottime = getSlotTime("120 mins", modal_forPrintReceipt.getOrderplacedTime());

                //  PrinterFunctions.PortDiscovery(portName, portSettings);
                //PrinterFunctions.OpenCashDrawer(portName, portSettings, 0, 4);

                // PrinterFunctions.OpenPort( portName, portSettings);
                //    PrinterFunctions.CheckStatus( portName, portSettings,2);
                //  PrinterFunctions.SelectPrintMode(portName, portSettings, 0);

                double oldSavedAmount = 0;
                // String CouponDiscount = "0";
                String Gstt = "", subtotall = "", quantity = "", price = "", weight = "", netweight = "";
                double gst_double = 0, subtotal_double = 0, price_double = 0;

                for (int i = 0; i < cart_Item_List.size(); i++) {
                    double savedAmount;
                    String itemUniqueCode = cart_Item_List.get(i);
                    String fullitemName = "", itemName = "", tmcSubCtgyKey = "", itemNameAfterBraces = "";
                    Modal_NewOrderItems modal_newOrderItems = cartItem_hashmap.get(itemUniqueCode);
                    try {
                        fullitemName = String.valueOf(modal_newOrderItems.getItemname());
                    } catch (Exception e) {
                        fullitemName = "";
                        e.printStackTrace();
                    }

                    try {
                        tmcSubCtgyKey = String.valueOf(modal_newOrderItems.getTmcsubctgykey());

                    } catch (Exception e) {
                        tmcSubCtgyKey = "";
                        e.printStackTrace();
                    }

                    try {
                        if (tmcSubCtgyKey.equals("tmcsubctgy_6") || tmcSubCtgyKey.equals("tmcsubctgy_3")) {
                            int indexofbraces = fullitemName.indexOf("(");
                            int lastindexofbraces = fullitemName.indexOf(")");
                            int lengthofItemname = fullitemName.length();
                            lastindexofbraces = lastindexofbraces + 1;

                            if ((indexofbraces >= 0) && (lastindexofbraces >= 0) && (lastindexofbraces > indexofbraces)) {
                                itemNameAfterBraces = fullitemName.substring(lastindexofbraces, lengthofItemname);

                                itemName = fullitemName.substring(0, indexofbraces);
                                itemName = itemName + itemNameAfterBraces;
                                fullitemName = fullitemName.substring(0, indexofbraces);
                                fullitemName = fullitemName + itemNameAfterBraces;


                            }

                            if ((indexofbraces >= 0) && (lastindexofbraces >= 0) && (lastindexofbraces == indexofbraces)) {
                                // itemNameAfterBraces = fullitemName.substring(lastindexofbraces,lengthofItemname);

                                itemName = fullitemName.substring(0, indexofbraces);

                                fullitemName = fullitemName.substring(0, indexofbraces);
                                fullitemName = fullitemName;


                            }

                            if (fullitemName.length() > 21) {
                                itemName = fullitemName.substring(0, 21);
                                itemName = itemName + "...";

                                fullitemName = fullitemName.substring(0, 21);
                                fullitemName = fullitemName + "...";
                            }
                            if (fullitemName.length() <= 21) {
                                itemName = fullitemName;

                                fullitemName = fullitemName;

                            }
                        } else {


                    /*
                    int indexofbraces = fullitemName.indexOf("(");
                    if (indexofbraces >= 0) {
                        itemName = fullitemName.substring(0, indexofbraces);

                    }
                    if (fullitemName.length() > 21) {
                        itemName = fullitemName.substring(0, 21);
                        itemName = itemName + "...";
                    }
                    if (fullitemName.length() <= 21) {
                        itemName = fullitemName;

                    }

                     */


                            if (fullitemName.contains("(")) {
                                int openbraces = fullitemName.indexOf("(");
                                int closebraces = fullitemName.indexOf(")");
                                //  System.out.println(fullitemName);
                                itemName = fullitemName.substring(openbraces + 1, closebraces);
                                //   System.out.println(itemName);

                            }
                            if (!itemName.matches("[a-zA-Z0-9]+")) {
                                fullitemName = fullitemName.replaceAll(
                                        "[^a-zA-Z0-9()]", "");
                                fullitemName = fullitemName.replaceAll(
                                        "[()]", " ");
                                //  System.out.println("no english");

                                //  System.out.println(fullitemName);

                            } else {
                                fullitemName = fullitemName.replaceAll(
                                        "[^a-zA-Z0-9()]", "");
                                // System.out.println("have English");

                                //    System.out.println(fullitemName);

                            }


                        }
                    } catch (Exception e) {
                        itemName = fullitemName;

                        e.printStackTrace();
                    }


                    try {
                        price = String.valueOf(modal_newOrderItems.getItemFinalPrice());
                        if (price.equals("null")) {
                            price = "  ";
                        }
                    } catch (Exception e) {
                        price = "0";
                        e.printStackTrace();
                    }

                    try {
                        weight = modal_newOrderItems.getItemFinalWeight().toString();
                    } catch (Exception e) {
                        weight = "0";
                        e.printStackTrace();
                    }


                    try {
                        netweight = modal_newOrderItems.getNetweight().toString();
                    } catch (Exception e) {
                        netweight = "0";
                        e.printStackTrace();
                    }


                    try {
                        Gstt = modal_newOrderItems.getGstAmount();

                    } catch (Exception e) {
                        Gstt = "0";
                        e.printStackTrace();
                    }


                    try {
                        savedAmount = Double.parseDouble(modal_newOrderItems.getSavedAmount());
                    } catch (Exception e) {
                        savedAmount = 0;
                        e.printStackTrace();
                    }


                    try {
                        oldSavedAmount = savedAmount + oldSavedAmount;
                    } catch (Exception e) {
                        weight = "0";
                        e.printStackTrace();
                    }


                    try {
                        subtotall = modal_newOrderItems.getSubTotal_perItem();
                    } catch (Exception e) {
                        subtotall = "0";
                        e.printStackTrace();
                    }

                    try {
                        quantity = modal_newOrderItems.getQuantity();
                    } catch (Exception e) {
                        quantity = "0";
                        e.printStackTrace();
                    }


                    PrinterFunctions.SetLineSpacing(portName, portSettings, 130);
                    PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
                    PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 1, 1, 0, 0, "TokenNo: " + modal_forPrintReceipt.getTokenno() + "\n");


                    PrinterFunctions.SetLineSpacing(portName, portSettings, 70);
                    PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
                    PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 30, 0, "Orderid : " + modal_forPrintReceipt.getOrderid() + "\n");

                    if (tmcSubCtgyKey.equals("tmcsubctgy_16")) {
                        PrinterFunctions.SetLineSpacing(portName, portSettings, 100);
                        PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
                        fullitemName = "Grill House " + fullitemName;
                        PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 1, 0, 30, 0, fullitemName + "\n");


                    } else if (tmcSubCtgyKey.equals("tmcsubctgy_15")) {
                        PrinterFunctions.SetLineSpacing(portName, portSettings, 100);
                        PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
                        fullitemName = "Ready to Cook " + fullitemName;

                        PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 1, 0, 30, 0, fullitemName + "\n");

                    } else {
                        PrinterFunctions.SetLineSpacing(portName, portSettings, 100);
                        PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
                        PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 1, 0, 30, 0, fullitemName + "\n");

                    }


                    PrinterFunctions.SetLineSpacing(portName, portSettings, 70);
                    PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
                    PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 30, 0, "Grossweight : " + weight + "\n");

                    PrinterFunctions.SetLineSpacing(portName, portSettings, 70);
                    PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
                    PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 30, 0, "Netweight  : " + netweight + "\n");


                    PrinterFunctions.SetLineSpacing(portName, portSettings, 70);
                    PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
                    PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 30, 0, "Quantity : " + quantity + "\n");
                    PrinterFunctions.PreformCut(portName,portSettings,1);

                }


            }
            Printer_POJO_Class[] Printer_POJO_ClassArray = new Printer_POJO_Class[cart_Item_List.size()];
            double oldSavedAmount = 0;
            // String CouponDiscount = "0";
            String Gstt = "", subtotall = "", quantity = "", price = "", priceperkg_unitprice ="",weight = "", netweight = "" , priceSuffix ="",pricetypeforpos ="";
            double gst_double = 0, subtotal_double = 0, price_double = 0;

            ///// Full bill
            for (int i = 0; i < cart_Item_List.size(); i++) {

                double savedAmount;
                String itemUniqueCode = cart_Item_List.get(i);
                Modal_NewOrderItems modal_newOrderItems = cartItem_hashmap.get(itemUniqueCode);
                String itemName = String.valueOf(modal_newOrderItems.getItemname());
                int indexofbraces = itemName.indexOf("(");
                if (indexofbraces >= 0) {
                    itemName = itemName.substring(0, indexofbraces);

                }
                if (itemName.length() > 21) {
                    itemName = itemName.substring(0, 21);
                    itemName = itemName + "...";
                }
                try {
                    price = String.valueOf(modal_newOrderItems.getItemFinalPrice());
                    if (price.equals("null")) {
                        price = "  ";
                    }
                } catch (Exception e) {
                    price = "0";
                    e.printStackTrace();
                }

                try {
                    priceperkg_unitprice = String.valueOf(modal_newOrderItems.getTmcpriceperkg());
                    if (priceperkg_unitprice.equals("null")) {
                        priceperkg_unitprice = "  ";
                    }
                } catch (Exception e) {
                    priceperkg_unitprice = "0";
                    e.printStackTrace();
                }

                try {
                    weight = modal_newOrderItems.getItemFinalWeight().toString();
                } catch (Exception e) {
                    weight = "0";
                    e.printStackTrace();
                }

                try {
                    Gstt = modal_newOrderItems.getGstAmount();

                } catch (Exception e) {
                    Gstt = "0";
                    e.printStackTrace();
                }


                try {
                    savedAmount = Double.parseDouble(modal_newOrderItems.getSavedAmount());
                } catch (Exception e) {
                    savedAmount = 0;
                    e.printStackTrace();
                }


                try {
                    oldSavedAmount = savedAmount + oldSavedAmount;
                } catch (Exception e) {
                    weight = "0";
                    e.printStackTrace();
                }


                try {
                    subtotall = modal_newOrderItems.getSubTotal_perItem();
                } catch (Exception e) {
                    subtotall = "0";
                    e.printStackTrace();
                }

                try {
                    quantity = modal_newOrderItems.getQuantity();
                } catch (Exception e) {
                    quantity = "0";
                    e.printStackTrace();
                }
                try {
                    pricetypeforpos = modal_newOrderItems.getPricetypeforpos();
                } catch (Exception e) {
                    pricetypeforpos = "0";
                    e.printStackTrace();
                }

                if(String.valueOf(pricetypeforpos).toUpperCase().equals("TMCPRICE")){

                    if(modal_newOrderItems.getNetweight().equals("")){
                        priceSuffix =  modal_newOrderItems.getPortionsize();
                    }
                    else{
                        priceSuffix = modal_newOrderItems.getNetweight();
                    }
                }


                Printer_POJO_ClassArray[i] = new Printer_POJO_Class("", quantity, modal_forPrintReceipt.getOrderid(), itemName, weight, price, "0.00", Gstt, subtotall, "cutname", priceperkg_unitprice, pricetypeforpos, priceSuffix);

            }

            Printer_POJO_Class Printer_POJO_ClassArraytotal = new Printer_POJO_Class(modal_forPrintReceipt.getItemTotalwithoutGst(), modal_forPrintReceipt.getDiscountAmount_StringGlobal(), modal_forPrintReceipt.getTaxAmount(), modal_forPrintReceipt.getPayableAmount(), oldSavedAmount);
            //  PrinterFunctions.PortDiscovery(portName, portSettings);
            // PrinterFunctions.OpenCashDrawer(portName, portSettings, 0, 4);

            // PrinterFunctions.OpenPort( portName, portSettings);
            //    PrinterFunctions.CheckStatus( portName, portSettings,2);
            //PrinterFunctions.SelectPrintMode(portName, portSettings, 0);

            if ((modal_forPrintReceipt.getVendorKey().equals("vendor_4")) || (modal_forPrintReceipt.getVendorKey().equals("wholesalesvendor_1"))) {


                PrinterFunctions.SetLineSpacing(portName, portSettings, 180);
                PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
                PrinterFunctions.PrintText(portName, portSettings, 0, 0, 0, 0, 2, 1, 0, 1, "MK Proteins" + "\n");

                PrinterFunctions.SetLineSpacing(portName, portSettings, 60);
                PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
                PrinterFunctions.PrintText(portName, portSettings, 0, 0, 0, 0, 0, 0, 0, 1, "Powered by the The Meat Chop" + "\n");

            } else if ((modal_forPrintReceipt.getVendorKey().equals("vendor_5"))) {


                PrinterFunctions.SetLineSpacing(portName, portSettings, 180);
                PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
                PrinterFunctions.PrintText(portName, portSettings, 0, 0, 0, 0, 2, 1, 0, 1, "Bismillah Proteins" + "\n");

                PrinterFunctions.SetLineSpacing(portName, portSettings, 60);
                PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
                PrinterFunctions.PrintText(portName, portSettings, 0, 0, 0, 0, 0, 0, 0, 1, "Powered by the The Meat Chop" + "\n");

            }
            else {

                PrinterFunctions.SetLineSpacing(portName, portSettings, 180);
                PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
                PrinterFunctions.PrintText(portName, portSettings, 0, 0, 0, 0, 2, 1, 0, 1, "The Meat Chop" + "\n");

            }


            PrinterFunctions.SetLineSpacing(portName, portSettings, 60);
            PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
            PrinterFunctions.PrintText(portName, portSettings, 0, 0, 0, 0, 0, 0, 0, 1, "Fresh Meat and SeaFood" + "\n");

            PrinterFunctions.SetLineSpacing(portName, portSettings, 60);
            PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
            PrinterFunctions.PrintText(portName, portSettings, 0, 0, 0, 0, 0, 0, 0, 1, modal_forPrintReceipt.getStoreAddressLine1() + "\n");


            PrinterFunctions.SetLineSpacing(portName, portSettings, 60);
            PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
            PrinterFunctions.PrintText(portName, portSettings, 0, 0, 0, 0, 0, 0, 0, 1, modal_forPrintReceipt.getStoreAddressLine2() + "\n");


            PrinterFunctions.SetLineSpacing(portName, portSettings, 60);
            PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
            PrinterFunctions.PrintText(portName, portSettings, 0, 0, 0, 0, 0, 0, 0, 1, modal_forPrintReceipt.getStoreAddressLine3() + "\n");


            PrinterFunctions.SetLineSpacing(portName, portSettings, 80);
            PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
            PrinterFunctions.PrintText(portName, portSettings, 0, 0, 0, 0, 0, 0, 0, 1, modal_forPrintReceipt.getStoreLanLine() + "\n");


            PrinterFunctions.SetLineSpacing(portName, portSettings, 80);
            PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
            PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 0, 1, "GSTIN :33AAJCC0055D1Z9" + "\n");


            PrinterFunctions.SetLineSpacing(portName, portSettings, 80);
            PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
            PrinterFunctions.PrintText(portName, portSettings, 0, 0, 0, 0, 0, 0, 0, 1, String.valueOf(getDate_and_time()) + "\n");


            PrinterFunctions.SetLineSpacing(portName, portSettings, 80);
            PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
            PrinterFunctions.PrintText(portName, portSettings, 0, 0, 0, 0, 0, 0, 0, 1, "# " + modal_forPrintReceipt.getOrderid() + "\n");


            PrinterFunctions.SetLineSpacing(portName, portSettings, 40);
            PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
            PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 30, 0, "----------------------------------------" + "\n");

            PrinterFunctions.SetLineSpacing(portName, portSettings, 80);
            PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
            PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 30, 0, "ITEM NAME * QTY" + "\n");

            PrinterFunctions.SetLineSpacing(portName, portSettings, 70);
            PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
            PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 30, 0, "RATE                GST         SUBTOTAL" + "\n");

            PrinterFunctions.SetLineSpacing(portName, portSettings, 60);
            PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
            PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 30, 0, "----------------------------------------" + "\n");
            for (int i = 0; i < Printer_POJO_ClassArray.length; i++) {

                PrinterFunctions.SetLineSpacing(portName, portSettings, 80);
                PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
                String itemrate, Gst, subtotal;
                itemrate = "Rs." + Printer_POJO_ClassArray[i].getItemRate();
                Gst = "Rs." + Printer_POJO_ClassArray[i].getGST();
                subtotal = "Rs." + Printer_POJO_ClassArray[i].getSubTotal();
                if (itemrate.length() == 4) {
                    //14spaces
                    itemrate = itemrate + "              ";
                }
                if (itemrate.length() == 5) {
                    //13spaces
                    itemrate = itemrate + "             ";
                }
                if (itemrate.length() == 6) {
                    //12spaces
                    itemrate = itemrate + "            ";
                }
                if (itemrate.length() == 7) {
                    //11spaces
                    itemrate = itemrate + "           ";
                }
                if (itemrate.length() == 8) {
                    //10spaces
                    itemrate = itemrate + "          ";
                }
                if (itemrate.length() == 9) {
                    //9spaces
                    itemrate = itemrate + "         ";
                }
                if (itemrate.length() == 10) {
                    //8spaces
                    itemrate = itemrate + "        ";
                }
                if (itemrate.length() == 11) {
                    //7spaces
                    itemrate = itemrate + "       ";
                }
                if (itemrate.length() == 12) {
                    //6spaces
                    itemrate = itemrate + "      ";
                }
                if (itemrate.length() == 13) {
                    //5spaces
                    itemrate = itemrate + "     ";
                }
                if (itemrate.length() == 14) {
                    //4spaces
                    itemrate = itemrate + "    ";
                }
                if (itemrate.length() == 15) {
                    //3spaces
                    itemrate = itemrate + "   ";
                }
                if (itemrate.length() == 16) {
                    //2spaces
                    itemrate = itemrate + "  ";
                }
                if (itemrate.length() == 17) {
                    //1spaces
                    itemrate = itemrate + " ";
                }
                if (itemrate.length() == 18) {
                    //1spaces
                    itemrate = itemrate + "";
                }


                if (Gst.length() == 7) {
                    //1spaces
                    Gst = Gst + " ";
                }
                if (Gst.length() == 8) {
                    //0space
                    Gst = Gst + "";
                }
                if (Gst.length() == 9) {
                    //no space
                    Gst = Gst;
                }
                if (subtotal.length() == 4) {
                    //5spaces
                    subtotal = "      " + subtotal;
                }
                if (subtotal.length() == 5) {
                    //6spaces
                    subtotal = "      " + subtotal;
                }
                if (subtotal.length() == 6) {
                    //8spaces
                    subtotal = "        " + subtotal;
                }
                if (subtotal.length() == 7) {
                    //7spaces
                    subtotal = "       " + subtotal;
                }
                if (subtotal.length() == 8) {
                    //6spaces
                    subtotal = "      " + subtotal;
                }
                if (subtotal.length() == 9) {
                    //5spaces
                    subtotal = "     " + subtotal;
                }
                if (subtotal.length() == 10) {
                    //4spaces
                    subtotal = "    " + subtotal;
                }
                if (subtotal.length() == 11) {
                    //3spaces
                    subtotal = "   " + subtotal;
                }
                if (subtotal.length() == 12) {
                    //2spaces
                    subtotal = "  " + subtotal;
                }
                if (subtotal.length() == 13) {
                    //1spaces
                    subtotal = " " + subtotal;
                }
                if (subtotal.length() == 14) {
                    //no space
                    subtotal = "" + subtotal;
                }


                PrinterFunctions.PrintText(portName, portSettings, 0, 0, 0, 0, 0, 0, 30, 0, Printer_POJO_ClassArray[i].getItemName() + "  *  " + Printer_POJO_ClassArray[i].getItemWeight() + "(" + Printer_POJO_ClassArray[i].getQuantity() + ")" + "\n");

                PrinterFunctions.SetLineSpacing(portName, portSettings, 80);
                PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
                PrinterFunctions.PrintText(portName, portSettings, 0, 0, 0, 0, 0, 0, 30, 0, itemrate + Gst + subtotal + "\n\n");
            }

            PrinterFunctions.SetLineSpacing(portName, portSettings, 60);
            PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
            PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 30, 0, "----------------------------------------" + "\n");

            String totalRate = "Rs." + Printer_POJO_ClassArraytotal.getTotalRate();
            String totalGst = "Rs." + Printer_POJO_ClassArraytotal.getTotalGST();

            String totalSubtotal = "Rs." + modal_forPrintReceipt.getItemTotalwithoutGst() ;
            if (totalRate.length() == 7) {
                //10spaces
                totalRate = totalRate + "          ";
            }
            if (totalRate.length() == 8) {
                //9spaces
                totalRate = totalRate + "         ";
            }
            if (totalRate.length() == 9) {
                //8spaces
                totalRate = totalRate + "        ";
            }
            if (totalRate.length() == 10) {
                //7spaces
                totalRate = totalRate + "       ";
            }
            if (totalRate.length() == 11) {
                //6spaces
                totalRate = totalRate + "      ";
            }
            if (totalRate.length() == 12) {
                //5spaces
                totalRate = totalRate + "     ";
            }
            if (totalRate.length() == 13) {
                //4spaces
                totalRate = totalRate + "    ";
            }

            if (totalGst.length() == 7) {
                //1spaces
                totalGst = totalGst + " ";
            }
            if (totalGst.length() == 8) {
                //0space
                totalGst = totalGst + "";
            }
            if (totalGst.length() == 9) {
                //no space
                totalGst = totalGst;
            }

            if (totalSubtotal.length() == 6) {
                //8spaces
                totalSubtotal = "        " + totalSubtotal;
            }
            if (totalSubtotal.length() == 7) {
                //7spaces
                totalSubtotal = "       " + totalSubtotal;
            }
            if (totalSubtotal.length() == 8) {
                //6spaces
                totalSubtotal = "      " + totalSubtotal;
            }
            if (totalSubtotal.length() == 9) {
                //5spaces
                totalSubtotal = "     " + totalSubtotal;
            }
            if (totalSubtotal.length() == 10) {
                //4spaces
                totalSubtotal = "    " + totalSubtotal;
            }


            PrinterFunctions.SetLineSpacing(portName, portSettings, 60);
            PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
            PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 30, 0, totalRate + totalGst + totalSubtotal + "\n");

            PrinterFunctions.SetLineSpacing(portName, portSettings, 50);
            PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
            PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 30, 0, "----------------------------------------" + "\n");





     /*

        PrinterFunctions.SetLineSpacing(portName, portSettings, 50);
        PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
        String SavedAmount = "You just saved Rs."+" on these items"+String.valueOf(Printer_POJO_ClassArraytotal.getOldSavedAmount());

        PrinterFunctions. PrintText(portName,portSettings,0, 0,1,0,0, 0,30,1,SavedAmount+"\n");


 */
            String CouponDiscount = "";

            //CouponDiscount = Printer_POJO_ClassArraytotal.getTotaldiscount();

            if (!modal_forPrintReceipt.getDiscountAmount_StringGlobal().equals("0")) {
                //CouponDiscount = "Rs. " + CouponDiscount + ".00";

                if (modal_forPrintReceipt.getDiscountAmount_StringGlobal().contains(".")) {
                    CouponDiscount = "Rs." + modal_forPrintReceipt.getDiscountAmount_StringGlobal();
                } else {
                    CouponDiscount = "Rs." + modal_forPrintReceipt.getDiscountAmount_StringGlobal() + ".00";

                }


                if ((!CouponDiscount.equals("Rs.0.0")) && (!CouponDiscount.equals("Rs.0")) && (!CouponDiscount.equals("Rs.0.00")) && (CouponDiscount != (null)) && (!CouponDiscount.equals("")) && (!CouponDiscount.equals("Rs. .00")) && (!CouponDiscount.equals("Rs..00"))) {

                    if (CouponDiscount.length() == 4) {
                        //20spaces
                        //NEW TOTAL =4
                        CouponDiscount = "Discount Amount                   " + CouponDiscount;
                    } else if (CouponDiscount.length() == 5) {
                        //21spaces
                        //NEW TOTAL =5
                        CouponDiscount = "Discount Amount                 " + CouponDiscount;
                    } else if (CouponDiscount.length() == 6) {
                        //20spaces
                        //NEW TOTAL =6
                        CouponDiscount = "Discount Amount                " + CouponDiscount;
                    } else if (CouponDiscount.length() == 7) {
                        //19spaces
                        //NEW TOTAL =7
                        CouponDiscount = "Discount Amount               " + CouponDiscount;
                    } else if (CouponDiscount.length() == 8) {
                        //18spaces
                        //NEW TOTAL =8
                        CouponDiscount = " Discount Amount              " + CouponDiscount;
                    } else if (CouponDiscount.length() == 9) {
                        //17spaces
                        //NEW TOTAL =9
                        CouponDiscount = " Discount Amount             " + CouponDiscount;
                    } else if (CouponDiscount.length() == 10) {
                        //16spaces
                        //NEW TOTAL =9
                        CouponDiscount = " Discount Amount            " + CouponDiscount;
                    } else if (CouponDiscount.length() == 11) {
                        //15spaces
                        //NEW TOTAL =9
                        CouponDiscount = "Discount Amount            " + CouponDiscount;
                    } else if (CouponDiscount.length() == 12) {
                        //14spaces
                        //NEW TOTAL =9
                        CouponDiscount = "Discount Amount           " + CouponDiscount;
                    } else if (CouponDiscount.length() == 13) {
                        //13spaces
                        //NEW TOTAL =9
                        CouponDiscount = "Discount Amount           " + CouponDiscount;

                    } else {
                        CouponDiscount = "Discount Amount       " + CouponDiscount;

                    }

                    PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 0, 1, CouponDiscount + "\n");


                    PrinterFunctions.SetLineSpacing(portName, portSettings, 50);
                    PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
                    PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 30, 0, "----------------------------------------" + "\n");

                }
            }


            String redeemPoints_String_print = "";
            if (!modal_forPrintReceipt.getRedeemPoints_String().equals("0")) {
                redeemPoints_String_print = "Rs. " + modal_forPrintReceipt.getRedeemPoints_String() + ".00";

                if ((!redeemPoints_String_print.equals("Rs.0.0")) && (!redeemPoints_String_print.equals("Rs.0")) && (!redeemPoints_String_print.equals("Rs.0.00")) && (redeemPoints_String_print != (null)) && (!redeemPoints_String_print.equals("")) && (!redeemPoints_String_print.equals("Rs. .00")) && (!redeemPoints_String_print.equals("Rs..00"))) {

                    if (redeemPoints_String_print.length() == 4) {
                        //20spaces
                        //NEW TOTAL =4
                        redeemPoints_String_print = "Points Redeemed                    " + redeemPoints_String_print;
                    }
                    if (redeemPoints_String_print.length() == 5) {
                        //21spaces
                        //NEW TOTAL =5
                        redeemPoints_String_print = "Points Redeemed                  " + redeemPoints_String_print;
                    }
                    if (redeemPoints_String_print.length() == 6) {
                        //20spaces
                        //NEW TOTAL =6
                        redeemPoints_String_print = "Points Redeemed                 " + redeemPoints_String_print;
                    }

                    if (redeemPoints_String_print.length() == 7) {
                        //19spaces
                        //NEW TOTAL =7
                        redeemPoints_String_print = "Points Redeemed                " + redeemPoints_String_print;
                    }
                    if (redeemPoints_String_print.length() == 8) {
                        //18spaces
                        //NEW TOTAL =8
                        redeemPoints_String_print = "Points Redeemed               " + redeemPoints_String_print;
                    }
                    if (redeemPoints_String_print.length() == 9) {
                        //17spaces
                        //NEW TOTAL =9
                        redeemPoints_String_print = "Points Redeemed               " + redeemPoints_String_print;
                    }
                    if (redeemPoints_String_print.length() == 10) {
                        //16spaces
                        //NEW TOTAL =9
                        redeemPoints_String_print = "Points Redeemed             " + redeemPoints_String_print;
                    }
                    if (redeemPoints_String_print.length() == 11) {
                        //15spaces
                        //NEW TOTAL =9
                        redeemPoints_String_print = " Points Redeemed             " + redeemPoints_String_print;
                    }
                    if (redeemPoints_String_print.length() == 12) {
                        //14spaces
                        //NEW TOTAL =9
                        redeemPoints_String_print = "Points Redeemed            " + redeemPoints_String_print;
                    }

                    if (redeemPoints_String_print.length() == 13) {
                        //13spaces
                        //NEW TOTAL =9
                        redeemPoints_String_print = "Points Redeemed            " + redeemPoints_String_print;

                    }


                    PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 0, 1, redeemPoints_String_print + "\n");


                    PrinterFunctions.SetLineSpacing(portName, portSettings, 50);
                    PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
                    PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 30, 0, "----------------------------------------" + "\n");

                }
            }


            if (modal_forPrintReceipt.getisPhoneOrderSelected()) {

                String deliveryCharge = "";

                //CouponDiscount = Printer_POJO_ClassArraytotal.getTotaldiscount();

                if (!modal_forPrintReceipt.getDeliveryAmount().equals("0")) {
                    //CouponDiscount = "Rs. " + CouponDiscount + ".00";
                    if (modal_forPrintReceipt.getDeliveryAmount().contains(".")) {
                        deliveryCharge = "Rs." + modal_forPrintReceipt.getDeliveryAmount();
                    } else {
                        deliveryCharge = "Rs." + modal_forPrintReceipt.getDeliveryAmount() + ".00";

                    }
                    if ((!deliveryCharge.equals("Rs.0.0")) && (!deliveryCharge.equals("Rs.0")) && (!deliveryCharge.equals("Rs.0.00")) && (deliveryCharge != (null)) && (!deliveryCharge.equals("")) && (!deliveryCharge.equals("Rs. .00")) && (!deliveryCharge.equals("Rs..00"))) {

                        if (deliveryCharge.length() == 4) {
                            //20spaces
                            //NEW TOTAL =4
                            deliveryCharge = "Delivery Charge                   " + deliveryCharge;
                        } else if (deliveryCharge.length() == 5) {
                            //21spaces
                            //NEW TOTAL =5
                            deliveryCharge = "Delivery Charge                 " + deliveryCharge;
                        } else if (deliveryCharge.length() == 6) {
                            //20spaces
                            //NEW TOTAL =6
                            deliveryCharge = "Delivery Charge                " + deliveryCharge;
                        } else if (deliveryCharge.length() == 7) {
                            //19spaces
                            //NEW TOTAL =7
                            deliveryCharge = "Delivery Charge               " + deliveryCharge;
                        } else if (deliveryCharge.length() == 8) {
                            //18spaces
                            //NEW TOTAL =8
                            deliveryCharge = "Delivery Charge              " + deliveryCharge;
                        } else if (deliveryCharge.length() == 9) {
                            //17spaces
                            //NEW TOTAL =9
                            deliveryCharge = "Delivery Charge             " + deliveryCharge;
                        } else if (deliveryCharge.length() == 10) {
                            //16spaces
                            //NEW TOTAL =9
                            deliveryCharge = "Delivery Charge            " + deliveryCharge;
                        } else if (deliveryCharge.length() == 11) {
                            //15spaces
                            //NEW TOTAL =9
                            deliveryCharge = "Delivery Charge            " + deliveryCharge;
                        } else if (deliveryCharge.length() == 12) {
                            //14spaces
                            //NEW TOTAL =9
                            deliveryCharge = "Delivery Charge           " + deliveryCharge;
                        } else if (deliveryCharge.length() == 13) {
                            //13spaces
                            //NEW TOTAL =9
                            deliveryCharge = "Delivery Charge           " + deliveryCharge;

                        } else {
                            deliveryCharge = "Delivery Charge       " + deliveryCharge;

                        }

                        PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 0, 1, " " + deliveryCharge + "\n");


                        PrinterFunctions.SetLineSpacing(portName, portSettings, 50);
                        PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
                        PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 30, 0, "----------------------------------------" + "\n");

                    }
                }

            }


            PrinterFunctions.SetLineSpacing(portName, portSettings, 50);
            PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
            String NetTotal = Printer_POJO_ClassArraytotal.getTotalsubtotal();

            if (NetTotal.length() > 6) {

                if (NetTotal.length() == 7) {
                    //24spaces
                    //NEW TOTAL =9
                    NetTotal = "  NET TOTAL                    Rs. " + NetTotal;
                }
                if (NetTotal.length() == 8) {
                    //23spaces
                    //NEW TOTAL =9
                    NetTotal = "  NET TOTAL                   Rs. " + NetTotal;
                }
                if (NetTotal.length() == 9) {
                    //22spaces
                    //NEW TOTAL =9
                    NetTotal = "  NET TOTAL                  Rs. " + NetTotal;
                }
                if (NetTotal.length() == 10) {
                    //21spaces
                    //NEW TOTAL =9
                    NetTotal = "  NET TOTAL                 Rs. " + NetTotal;
                }
                if (NetTotal.length() == 11) {
                    //20spaces
                    //NEW TOTAL =9
                    NetTotal = "  NET TOTAL                Rs. " + NetTotal;
                }
                if (NetTotal.length() == 12) {
                    //19spaces
                    //NEW TOTAL =9
                    NetTotal = "  NET TOTAL               Rs. " + NetTotal;
                }
            } else {
                NetTotal = " NET TOTAL                   Rs. " + NetTotal;

            }

            PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 0, 1, NetTotal + "\n");

            PrinterFunctions.SetLineSpacing(portName, portSettings, 60);
            PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
            PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 30, 0, "----------------------------------------" + "\n");
            try {
                if (modal_forPrintReceipt.getPayment_mode().toUpperCase().equals(Constants.CASH_ON_DELIVERY)) {
                    if ((!modal_forPrintReceipt.getAmountRecieved_String().equals("null")) && (!modal_forPrintReceipt.getBalanceAmount_String().equals("null"))) {
                        PrinterFunctions.SetLineSpacing(portName, portSettings, 60);
                        PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
                        PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 30, 0, "Amount Given by Customer : ");


                        PrinterFunctions.SetLineSpacing(portName, portSettings, 90);
                        PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
                        PrinterFunctions.PrintText(portName, portSettings, 0, 0, 0, 0, 0, 0, 30, 0, modal_forPrintReceipt.getAmountRecieved_String() + " Rs " + "\n");

                        PrinterFunctions.SetLineSpacing(portName, portSettings, 60);
                        PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
                        PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 30, 0, "----------------------------------------" + "\n");


                        PrinterFunctions.SetLineSpacing(portName, portSettings, 60);
                        PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
                        PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 30, 0, "Balance Amount given : ");


                        PrinterFunctions.SetLineSpacing(portName, portSettings, 90);
                        PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
                        PrinterFunctions.PrintText(portName, portSettings, 0, 0, 0, 0, 0, 0, 30, 0, modal_forPrintReceipt.getBalanceAmount_String() + " Rs" + "\n");

                        PrinterFunctions.SetLineSpacing(portName, portSettings, 60);
                        PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
                        PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 30, 0, "----------------------------------------" + "\n");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            PrinterFunctions.SetLineSpacing(portName, portSettings, 60);
            PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
            PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 30, 0, "Earned Rewards : " + String.valueOf((int) (modal_forPrintReceipt.getTotalredeempointsusergetfromorder())) + "\n");

            PrinterFunctions.SetLineSpacing(portName, portSettings, 50);
            PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
            PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 30, 0, "----------------------------------------" + "\n");
            if (modal_forPrintReceipt.getPayment_mode().toString().toUpperCase().equals(Constants.CREDIT)) {
                PrinterFunctions.SetLineSpacing(portName, portSettings, 60);
                PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
                PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 30, 0, "Old Amount need to be Paid : " + String.valueOf(Math.round(modal_forPrintReceipt.getTotalamountUserHaveAsCredit())) + "\n");


                PrinterFunctions.SetLineSpacing(portName, portSettings, 60);
                PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
                PrinterFunctions.PrintText(portName, portSettings, 0, 0, 0, 0, 0, 1, 30, 0, " total Amount need to be Paid = (Old amount + Current Bill Amount ) \n");


                String payableamountPrint = "";
                try {
                    payableamountPrint = Printer_POJO_ClassArraytotal.getTotalsubtotal();
                } catch (Exception e) {
                    e.printStackTrace();

                }


                double payableamountdoublePrint = 0;
                try {
                    payableamountdoublePrint = Math.round(Double.parseDouble(String.valueOf(payableamountPrint)));
                } catch (Exception e) {
                    payableamountdoublePrint = 0;
                    e.printStackTrace();

                }


                PrinterFunctions.SetLineSpacing(portName, portSettings, 60);
                PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
                PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 30, 0, "total Amount need to be Paid : " + String.valueOf(Math.round(modal_forPrintReceipt.getTotalamountUserHaveAsCredit() + payableamountdoublePrint)) + "\n");

                PrinterFunctions.SetLineSpacing(portName, portSettings, 50);
                PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
                PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 30, 0, "----------------------------------------" + "\n");

            }
            if (!modal_forPrintReceipt.getOrdertype().equals(Constants.POSORDER)) {
                PrinterFunctions.SetLineSpacing(portName, portSettings, 60);
                PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
                PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 30, 0, "Order Type: ");


                PrinterFunctions.SetLineSpacing(portName, portSettings, 90);
                PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
                PrinterFunctions.PrintText(portName, portSettings, 0, 0, 0, 0, 0, 0, 30, 0, modal_forPrintReceipt.getOrdertype() + "\n");
            }

            PrinterFunctions.SetLineSpacing(portName, portSettings, 60);
            PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
            PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 30, 0, "Payment Mode: ");


            PrinterFunctions.SetLineSpacing(portName, portSettings, 90);
            PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
            PrinterFunctions.PrintText(portName, portSettings, 0, 0, 0, 0, 0, 0, 30, 0, modal_forPrintReceipt.getPayment_mode() + "\n");


            PrinterFunctions.SetLineSpacing(portName, portSettings, 60);
            PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
            PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 30, 0, "MobileNo : ");


            PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
            PrinterFunctions.PrintText(portName, portSettings, 0, 0, 0, 0, 0, 0, 0, 0, modal_forPrintReceipt.getUserMobile() + "           " + "\n");

            if (modal_forPrintReceipt.getisPhoneOrderSelected()) {
                PrinterFunctions.SetLineSpacing(portName, portSettings, 60);
                PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
                PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 30, 0, "----------------------------------------" + "\n" + "\n");

                PrinterFunctions.SetLineSpacing(portName, portSettings, 200);
                PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
                PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 2, 2, 0, 1, "Token No : " + modal_forPrintReceipt.getTokenno() + "\n");


                PrinterFunctions.SetLineSpacing(portName, portSettings, 100);
                PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
                PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 1, 0, 30, 0, "Notes : ");


                PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
                PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 1, 0, 0, 0, "" + "  " + "\n\n");


                PrinterFunctions.SetLineSpacing(portName, portSettings, 100);
                PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
                PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 30, 0, "Slot Name : ");


                PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
                PrinterFunctions.PrintText(portName, portSettings, 0, 0, 0, 0, 0, 0, 0, 0, Constants.EXPRESS_DELIVERY_SLOTNAME + "         " + "\n");


                PrinterFunctions.SetLineSpacing(portName, portSettings, 100);
                PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
                PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 30, 0, "Order Placed Time  : ");


                PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
                PrinterFunctions.PrintText(portName, portSettings, 0, 0, 0, 0, 0, 0, 0, 0, modal_forPrintReceipt.getOrderplacedTime() + "         " + "\n");


                PrinterFunctions.SetLineSpacing(portName, portSettings, 100);
                PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
                PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 30, 0, "Delivery Time  : ");


                PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
                PrinterFunctions.PrintText(portName, portSettings, 0, 0, 0, 0, 0, 0, 0, 0, slottime + "         " + "\n");


                PrinterFunctions.SetLineSpacing(portName, portSettings, 60);
                PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
                PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 30, 0, "Delivery  Type: ");


                PrinterFunctions.SetLineSpacing(portName, portSettings, 90);
                PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
                PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 30, 0, Constants.HOME_DELIVERY_DELIVERYTYPE + "\n");


                PrinterFunctions.SetLineSpacing(portName, portSettings, 60);
                PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
                PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 30, 0, "Distance from Store : ");


                PrinterFunctions.SetLineSpacing(portName, portSettings, 90);
                PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
                PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 30, 0, modal_forPrintReceipt.getSelected_Address_modal().getDeliverydistance() + "Km" + "\n");


                PrinterFunctions.SetLineSpacing(portName, portSettings, 60);
                PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
                PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 30, 0, "Address : " + "\n" + "\n");


                String Address = (modal_forPrintReceipt.getSelected_Address_modal().getAddressline1());


                PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
                PrinterFunctions.PrintText(portName, portSettings, 0, 0, 0, 0, 0, 0, 30, 0, Address + "         " + "\n" + "\n");

            }



/*
            PrinterFunctions.SetLineSpacing(portName, portSettings, 120);
            PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
            PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 0, 30, "ID : ");


            PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
            PrinterFunctions.PrintText(portName, portSettings, 0, 0, 0, 0, 0, 0, 0, 50, tokenno + "\n");


 */


            PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
            PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 0, 1, "\n" + "Thank you for choosing us !!!  " + "\n");
            PrinterFunctions.PreformCut(portName,portSettings,1);





            //print second timeee


            //  printReciptForSecondTime();
            if(isPrintedSecondTime){
                Intent intentttt = new Intent("receiverForCompletePrintingtheReceipt");
                intentttt.putExtra("message", "success");
                sendBroadcast(intentttt);


            }
            else {
                Intent intentttt = new Intent("receiverForCompletePrintingtheReceipt");
                intentttt.putExtra("message", "success");
                sendBroadcast(intentttt);

                // Intent newIntent = new Intent(this, BackgroundServiceForPrintingReceipt2.class);
                // newIntent.putExtra("modalData", modal_forPrintReceipt);
                // newIntent.putExtra("hashmapdata", cartItem_hashmap);
                // newIntent.putExtra("isCalledSecondtime", true);
                // startService(newIntent);
            }



        }
        catch (Exception e) {
            e.printStackTrace();



        }




    }

    private String printReciptForSecondTime() {


        String slottime = "";
        try {

            PrinterFunctions.PortDiscovery(portName, portSettings);
            PrinterFunctions.OpenCashDrawer(portName, portSettings, 0, 4);

            // PrinterFunctions.OpenPort( portName, portSettings);
            //    PrinterFunctions.CheckStatus( portName, portSettings,2);
            PrinterFunctions.SelectPrintMode(portName, portSettings, 0);

        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (modal_forPrintReceipt.getOrdertype().equals(Constants.PhoneOrder)) {
                slottime = getSlotTime("120 mins", modal_forPrintReceipt.getOrderplacedTime());

                //  PrinterFunctions.PortDiscovery(portName, portSettings);
                //PrinterFunctions.OpenCashDrawer(portName, portSettings, 0, 4);

                // PrinterFunctions.OpenPort( portName, portSettings);
                //    PrinterFunctions.CheckStatus( portName, portSettings,2);
                //  PrinterFunctions.SelectPrintMode(portName, portSettings, 0);

                double oldSavedAmount = 0;
                // String CouponDiscount = "0";
                String Gstt = "", subtotall = "", quantity = "", price = "", weight = "", netweight = "";
                double gst_double = 0, subtotal_double = 0, price_double = 0;

                for (int i = 0; i < cart_Item_List.size(); i++) {
                    double savedAmount;
                    String itemUniqueCode = cart_Item_List.get(i);
                    String fullitemName = "", itemName = "", tmcSubCtgyKey = "", itemNameAfterBraces = "";
                    Modal_NewOrderItems modal_newOrderItems = cartItem_hashmap.get(itemUniqueCode);
                    try {
                        fullitemName = String.valueOf(modal_newOrderItems.getItemname());
                    } catch (Exception e) {
                        fullitemName = "";
                        e.printStackTrace();
                    }

                    try {
                        tmcSubCtgyKey = String.valueOf(modal_newOrderItems.getTmcsubctgykey());

                    } catch (Exception e) {
                        tmcSubCtgyKey = "";
                        e.printStackTrace();
                    }

                    try {
                        if (tmcSubCtgyKey.equals("tmcsubctgy_6") || tmcSubCtgyKey.equals("tmcsubctgy_3")) {
                            int indexofbraces = fullitemName.indexOf("(");
                            int lastindexofbraces = fullitemName.indexOf(")");
                            int lengthofItemname = fullitemName.length();
                            lastindexofbraces = lastindexofbraces + 1;

                            if ((indexofbraces >= 0) && (lastindexofbraces >= 0) && (lastindexofbraces > indexofbraces)) {
                                itemNameAfterBraces = fullitemName.substring(lastindexofbraces, lengthofItemname);

                                itemName = fullitemName.substring(0, indexofbraces);
                                itemName = itemName + itemNameAfterBraces;
                                fullitemName = fullitemName.substring(0, indexofbraces);
                                fullitemName = fullitemName + itemNameAfterBraces;


                            }

                            if ((indexofbraces >= 0) && (lastindexofbraces >= 0) && (lastindexofbraces == indexofbraces)) {
                                // itemNameAfterBraces = fullitemName.substring(lastindexofbraces,lengthofItemname);

                                itemName = fullitemName.substring(0, indexofbraces);

                                fullitemName = fullitemName.substring(0, indexofbraces);
                                fullitemName = fullitemName;


                            }

                            if (fullitemName.length() > 21) {
                                itemName = fullitemName.substring(0, 21);
                                itemName = itemName + "...";

                                fullitemName = fullitemName.substring(0, 21);
                                fullitemName = fullitemName + "...";
                            }
                            if (fullitemName.length() <= 21) {
                                itemName = fullitemName;

                                fullitemName = fullitemName;

                            }
                        } else {


                    /*
                    int indexofbraces = fullitemName.indexOf("(");
                    if (indexofbraces >= 0) {
                        itemName = fullitemName.substring(0, indexofbraces);

                    }
                    if (fullitemName.length() > 21) {
                        itemName = fullitemName.substring(0, 21);
                        itemName = itemName + "...";
                    }
                    if (fullitemName.length() <= 21) {
                        itemName = fullitemName;

                    }

                     */


                            if (fullitemName.contains("(")) {
                                int openbraces = fullitemName.indexOf("(");
                                int closebraces = fullitemName.indexOf(")");
                                //  System.out.println(fullitemName);
                                itemName = fullitemName.substring(openbraces + 1, closebraces);
                                //   System.out.println(itemName);

                            }
                            if (!itemName.matches("[a-zA-Z0-9]+")) {
                                fullitemName = fullitemName.replaceAll(
                                        "[^a-zA-Z0-9()]", "");
                                fullitemName = fullitemName.replaceAll(
                                        "[()]", " ");
                                //  System.out.println("no english");

                                //  System.out.println(fullitemName);

                            } else {
                                fullitemName = fullitemName.replaceAll(
                                        "[^a-zA-Z0-9()]", "");
                                // System.out.println("have English");

                                //    System.out.println(fullitemName);

                            }


                        }
                    } catch (Exception e) {
                        itemName = fullitemName;

                        e.printStackTrace();
                    }


                    try {
                        price = String.valueOf(modal_newOrderItems.getItemFinalPrice());
                        if (price.equals("null")) {
                            price = "  ";
                        }
                    } catch (Exception e) {
                        price = "0";
                        e.printStackTrace();
                    }

                    try {
                        weight = modal_newOrderItems.getItemFinalWeight().toString();
                    } catch (Exception e) {
                        weight = "0";
                        e.printStackTrace();
                    }


                    try {
                        netweight = modal_newOrderItems.getNetweight().toString();
                    } catch (Exception e) {
                        netweight = "0";
                        e.printStackTrace();
                    }


                    try {
                        Gstt = modal_newOrderItems.getGstAmount();

                    } catch (Exception e) {
                        Gstt = "0";
                        e.printStackTrace();
                    }


                    try {
                        savedAmount = Double.parseDouble(modal_newOrderItems.getSavedAmount());
                    } catch (Exception e) {
                        savedAmount = 0;
                        e.printStackTrace();
                    }


                    try {
                        oldSavedAmount = savedAmount + oldSavedAmount;
                    } catch (Exception e) {
                        weight = "0";
                        e.printStackTrace();
                    }


                    try {
                        subtotall = modal_newOrderItems.getSubTotal_perItem();
                    } catch (Exception e) {
                        subtotall = "0";
                        e.printStackTrace();
                    }

                    try {
                        quantity = modal_newOrderItems.getQuantity();
                    } catch (Exception e) {
                        quantity = "0";
                        e.printStackTrace();
                    }


                    PrinterFunctions.SetLineSpacing(portName, portSettings, 130);
                    PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
                    PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 1, 1, 0, 0, "TokenNo: " + modal_forPrintReceipt.getTokenno() + "\n");


                    PrinterFunctions.SetLineSpacing(portName, portSettings, 70);
                    PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
                    PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 30, 0, "Orderid : " + modal_forPrintReceipt.getOrderid() + "\n");

                    if (tmcSubCtgyKey.equals("tmcsubctgy_16")) {
                        PrinterFunctions.SetLineSpacing(portName, portSettings, 100);
                        PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
                        fullitemName = "Grill House " + fullitemName;
                        PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 1, 0, 30, 0, fullitemName + "\n");


                    } else if (tmcSubCtgyKey.equals("tmcsubctgy_15")) {
                        PrinterFunctions.SetLineSpacing(portName, portSettings, 100);
                        PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
                        fullitemName = "Ready to Cook " + fullitemName;

                        PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 1, 0, 30, 0, fullitemName + "\n");

                    } else {
                        PrinterFunctions.SetLineSpacing(portName, portSettings, 100);
                        PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
                        PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 1, 0, 30, 0, fullitemName + "\n");

                    }


                    PrinterFunctions.SetLineSpacing(portName, portSettings, 70);
                    PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
                    PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 30, 0, "Grossweight : " + weight + "\n");

                    PrinterFunctions.SetLineSpacing(portName, portSettings, 70);
                    PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
                    PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 30, 0, "Netweight  : " + netweight + "\n");


                    PrinterFunctions.SetLineSpacing(portName, portSettings, 70);
                    PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
                    PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 30, 0, "Quantity : " + quantity + "\n");
                    PrinterFunctions.PreformCut(portName,portSettings,1);

                }


            }
            Printer_POJO_Class[] Printer_POJO_ClassArray = new Printer_POJO_Class[cart_Item_List.size()];
            double oldSavedAmount = 0;
            // String CouponDiscount = "0";
            String Gstt = "", subtotall = "", quantity = "", price = "",priceperkg_unitprice ="", weight = "", netweight = "" , priceSuffix ="",pricetypeforpos ="";
            double gst_double = 0, subtotal_double = 0, price_double = 0;

            ///// Full bill
            for (int i = 0; i < cart_Item_List.size(); i++) {

                double savedAmount;
                String itemUniqueCode = cart_Item_List.get(i);
                Modal_NewOrderItems modal_newOrderItems = cartItem_hashmap.get(itemUniqueCode);
                String itemName = String.valueOf(modal_newOrderItems.getItemname());
                int indexofbraces = itemName.indexOf("(");
                if (indexofbraces >= 0) {
                    itemName = itemName.substring(0, indexofbraces);

                }
                if (itemName.length() > 21) {
                    itemName = itemName.substring(0, 21);
                    itemName = itemName + "...";
                }
                try {
                    price = String.valueOf(modal_newOrderItems.getItemFinalPrice());
                    if (price.equals("null")) {
                        price = "  ";
                    }
                } catch (Exception e) {
                    price = "0";
                    e.printStackTrace();
                }


                try {
                    priceperkg_unitprice = String.valueOf(modal_newOrderItems.getTmcpriceperkg());
                    if (priceperkg_unitprice.equals("null")) {
                        priceperkg_unitprice = "  ";
                    }
                } catch (Exception e) {
                    priceperkg_unitprice = "0";
                    e.printStackTrace();
                }



                try {
                    weight = modal_newOrderItems.getItemFinalWeight().toString();
                } catch (Exception e) {
                    weight = "0";
                    e.printStackTrace();
                }

                try {
                    Gstt = modal_newOrderItems.getGstAmount();

                } catch (Exception e) {
                    Gstt = "0";
                    e.printStackTrace();
                }


                try {
                    savedAmount = Double.parseDouble(modal_newOrderItems.getSavedAmount());
                } catch (Exception e) {
                    savedAmount = 0;
                    e.printStackTrace();
                }


                try {
                    oldSavedAmount = savedAmount + oldSavedAmount;
                } catch (Exception e) {
                    weight = "0";
                    e.printStackTrace();
                }


                try {
                    subtotall = modal_newOrderItems.getSubTotal_perItem();
                } catch (Exception e) {
                    subtotall = "0";
                    e.printStackTrace();
                }

                try {
                    quantity = modal_newOrderItems.getQuantity();
                } catch (Exception e) {
                    quantity = "0";
                    e.printStackTrace();
                }

                try {
                    pricetypeforpos = modal_newOrderItems.getPricetypeforpos();
                } catch (Exception e) {
                    pricetypeforpos = "0";
                    e.printStackTrace();
                }
                if(String.valueOf(pricetypeforpos).toUpperCase().equals("TMCPRICE")){

                    if(modal_newOrderItems.getNetweight().equals("")){
                        priceSuffix =  modal_newOrderItems.getPortionsize();
                    }
                    else{
                        priceSuffix = modal_newOrderItems.getNetweight();
                    }
                }

                Printer_POJO_ClassArray[i] = new Printer_POJO_Class("", quantity, modal_forPrintReceipt.getOrderid(), itemName, weight, price, "0.00", Gstt, subtotall, "cutname", priceperkg_unitprice, pricetypeforpos, priceSuffix);

            }

            Printer_POJO_Class Printer_POJO_ClassArraytotal = new Printer_POJO_Class(modal_forPrintReceipt.getItemTotalwithoutGst(), modal_forPrintReceipt.getDiscountAmount_StringGlobal(), modal_forPrintReceipt.getTaxAmount(), modal_forPrintReceipt.getPayableAmount(), oldSavedAmount);
            //  PrinterFunctions.PortDiscovery(portName, portSettings);
            // PrinterFunctions.OpenCashDrawer(portName, portSettings, 0, 4);

            // PrinterFunctions.OpenPort( portName, portSettings);
            //    PrinterFunctions.CheckStatus( portName, portSettings,2);
            //PrinterFunctions.SelectPrintMode(portName, portSettings, 0);

            if ((modal_forPrintReceipt.getVendorKey().equals("vendor_4")) || (modal_forPrintReceipt.getVendorKey().equals("wholesalesvendor_1"))) {


                PrinterFunctions.SetLineSpacing(portName, portSettings, 180);
                PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
                PrinterFunctions.PrintText(portName, portSettings, 0, 0, 0, 0, 2, 1, 0, 1, "MK Proteins" + "\n");

                PrinterFunctions.SetLineSpacing(portName, portSettings, 60);
                PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
                PrinterFunctions.PrintText(portName, portSettings, 0, 0, 0, 0, 0, 0, 0, 1, "Powered by the The Meat Chop" + "\n");

            } else if ((modal_forPrintReceipt.getVendorKey().equals("vendor_5"))) {


                PrinterFunctions.SetLineSpacing(portName, portSettings, 180);
                PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
                PrinterFunctions.PrintText(portName, portSettings, 0, 0, 0, 0, 2, 1, 0, 1, "Bismillah Proteins" + "\n");

                PrinterFunctions.SetLineSpacing(portName, portSettings, 60);
                PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
                PrinterFunctions.PrintText(portName, portSettings, 0, 0, 0, 0, 0, 0, 0, 1, "Powered by the The Meat Chop" + "\n");

            } else {

                PrinterFunctions.SetLineSpacing(portName, portSettings, 180);
                PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
                PrinterFunctions.PrintText(portName, portSettings, 0, 0, 0, 0, 2, 1, 0, 1, "The Meat Chop" + "\n");

            }


            PrinterFunctions.SetLineSpacing(portName, portSettings, 60);
            PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
            PrinterFunctions.PrintText(portName, portSettings, 0, 0, 0, 0, 0, 0, 0, 1, "Fresh Meat and SeaFood" + "\n");

            PrinterFunctions.SetLineSpacing(portName, portSettings, 60);
            PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
            PrinterFunctions.PrintText(portName, portSettings, 0, 0, 0, 0, 0, 0, 0, 1, modal_forPrintReceipt.getStoreAddressLine1() + "\n");


            PrinterFunctions.SetLineSpacing(portName, portSettings, 60);
            PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
            PrinterFunctions.PrintText(portName, portSettings, 0, 0, 0, 0, 0, 0, 0, 1, modal_forPrintReceipt.getStoreAddressLine2() + "\n");


            PrinterFunctions.SetLineSpacing(portName, portSettings, 60);
            PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
            PrinterFunctions.PrintText(portName, portSettings, 0, 0, 0, 0, 0, 0, 0, 1, modal_forPrintReceipt.getStoreAddressLine3() + "\n");


            PrinterFunctions.SetLineSpacing(portName, portSettings, 80);
            PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
            PrinterFunctions.PrintText(portName, portSettings, 0, 0, 0, 0, 0, 0, 0, 1, modal_forPrintReceipt.getStoreLanLine() + "\n");


            PrinterFunctions.SetLineSpacing(portName, portSettings, 80);
            PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
            PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 0, 1, "GSTIN :33AAJCC0055D1Z9" + "\n");


            PrinterFunctions.SetLineSpacing(portName, portSettings, 80);
            PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
            PrinterFunctions.PrintText(portName, portSettings, 0, 0, 0, 0, 0, 0, 0, 1, String.valueOf(getDate_and_time()) + "\n");


            PrinterFunctions.SetLineSpacing(portName, portSettings, 80);
            PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
            PrinterFunctions.PrintText(portName, portSettings, 0, 0, 0, 0, 0, 0, 0, 1, "# " + modal_forPrintReceipt.getOrderid() + "\n");


            PrinterFunctions.SetLineSpacing(portName, portSettings, 40);
            PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
            PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 30, 0, "----------------------------------------" + "\n");

            PrinterFunctions.SetLineSpacing(portName, portSettings, 80);
            PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
            PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 30, 0, "ITEM NAME * QTY" + "\n");

            PrinterFunctions.SetLineSpacing(portName, portSettings, 70);
            PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
            PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 30, 0, "RATE                GST         SUBTOTAL" + "\n");

            PrinterFunctions.SetLineSpacing(portName, portSettings, 60);
            PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
            PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 30, 0, "----------------------------------------" + "\n");
            for (int i = 0; i < Printer_POJO_ClassArray.length; i++) {

                PrinterFunctions.SetLineSpacing(portName, portSettings, 80);
                PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
                String itemrate, Gst, subtotal;
                itemrate = "Rs." + Printer_POJO_ClassArray[i].getItemRate();
                Gst = "Rs." + Printer_POJO_ClassArray[i].getGST();
                subtotal = "Rs." + Printer_POJO_ClassArray[i].getSubTotal();
                if (itemrate.length() == 4) {
                    //14spaces
                    itemrate = itemrate + "              ";
                }
                if (itemrate.length() == 5) {
                    //13spaces
                    itemrate = itemrate + "             ";
                }
                if (itemrate.length() == 6) {
                    //12spaces
                    itemrate = itemrate + "            ";
                }
                if (itemrate.length() == 7) {
                    //11spaces
                    itemrate = itemrate + "           ";
                }
                if (itemrate.length() == 8) {
                    //10spaces
                    itemrate = itemrate + "          ";
                }
                if (itemrate.length() == 9) {
                    //9spaces
                    itemrate = itemrate + "         ";
                }
                if (itemrate.length() == 10) {
                    //8spaces
                    itemrate = itemrate + "        ";
                }
                if (itemrate.length() == 11) {
                    //7spaces
                    itemrate = itemrate + "       ";
                }
                if (itemrate.length() == 12) {
                    //6spaces
                    itemrate = itemrate + "      ";
                }
                if (itemrate.length() == 13) {
                    //5spaces
                    itemrate = itemrate + "     ";
                }
                if (itemrate.length() == 14) {
                    //4spaces
                    itemrate = itemrate + "    ";
                }
                if (itemrate.length() == 15) {
                    //3spaces
                    itemrate = itemrate + "   ";
                }
                if (itemrate.length() == 16) {
                    //2spaces
                    itemrate = itemrate + "  ";
                }
                if (itemrate.length() == 17) {
                    //1spaces
                    itemrate = itemrate + " ";
                }
                if (itemrate.length() == 18) {
                    //1spaces
                    itemrate = itemrate + "";
                }


                if (Gst.length() == 7) {
                    //1spaces
                    Gst = Gst + " ";
                }
                if (Gst.length() == 8) {
                    //0space
                    Gst = Gst + "";
                }
                if (Gst.length() == 9) {
                    //no space
                    Gst = Gst;
                }
                if (subtotal.length() == 4) {
                    //5spaces
                    subtotal = "      " + subtotal;
                }
                if (subtotal.length() == 5) {
                    //6spaces
                    subtotal = "      " + subtotal;
                }
                if (subtotal.length() == 6) {
                    //8spaces
                    subtotal = "        " + subtotal;
                }
                if (subtotal.length() == 7) {
                    //7spaces
                    subtotal = "       " + subtotal;
                }
                if (subtotal.length() == 8) {
                    //6spaces
                    subtotal = "      " + subtotal;
                }
                if (subtotal.length() == 9) {
                    //5spaces
                    subtotal = "     " + subtotal;
                }
                if (subtotal.length() == 10) {
                    //4spaces
                    subtotal = "    " + subtotal;
                }
                if (subtotal.length() == 11) {
                    //3spaces
                    subtotal = "   " + subtotal;
                }
                if (subtotal.length() == 12) {
                    //2spaces
                    subtotal = "  " + subtotal;
                }
                if (subtotal.length() == 13) {
                    //1spaces
                    subtotal = " " + subtotal;
                }
                if (subtotal.length() == 14) {
                    //no space
                    subtotal = "" + subtotal;
                }


                PrinterFunctions.PrintText(portName, portSettings, 0, 0, 0, 0, 0, 0, 30, 0, Printer_POJO_ClassArray[i].getItemName() + "  *  " + Printer_POJO_ClassArray[i].getItemWeight() + "(" + Printer_POJO_ClassArray[i].getQuantity() + ")" + "\n");

                PrinterFunctions.SetLineSpacing(portName, portSettings, 80);
                PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
                PrinterFunctions.PrintText(portName, portSettings, 0, 0, 0, 0, 0, 0, 30, 0, itemrate + Gst + subtotal + "\n\n");
            }

            PrinterFunctions.SetLineSpacing(portName, portSettings, 60);
            PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
            PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 30, 0, "----------------------------------------" + "\n");

            String totalRate = "Rs." + Printer_POJO_ClassArraytotal.getTotalRate();
            String totalGst = "Rs." + Printer_POJO_ClassArraytotal.getTotalGST();

            String totalSubtotal = "Rs." + modal_forPrintReceipt.getItemTotalwithoutGst() ;
            if (totalRate.length() == 7) {
                //10spaces
                totalRate = totalRate + "          ";
            }
            if (totalRate.length() == 8) {
                //9spaces
                totalRate = totalRate + "         ";
            }
            if (totalRate.length() == 9) {
                //8spaces
                totalRate = totalRate + "        ";
            }
            if (totalRate.length() == 10) {
                //7spaces
                totalRate = totalRate + "       ";
            }
            if (totalRate.length() == 11) {
                //6spaces
                totalRate = totalRate + "      ";
            }
            if (totalRate.length() == 12) {
                //5spaces
                totalRate = totalRate + "     ";
            }
            if (totalRate.length() == 13) {
                //4spaces
                totalRate = totalRate + "    ";
            }

            if (totalGst.length() == 7) {
                //1spaces
                totalGst = totalGst + " ";
            }
            if (totalGst.length() == 8) {
                //0space
                totalGst = totalGst + "";
            }
            if (totalGst.length() == 9) {
                //no space
                totalGst = totalGst;
            }

            if (totalSubtotal.length() == 6) {
                //8spaces
                totalSubtotal = "        " + totalSubtotal;
            }
            if (totalSubtotal.length() == 7) {
                //7spaces
                totalSubtotal = "       " + totalSubtotal;
            }
            if (totalSubtotal.length() == 8) {
                //6spaces
                totalSubtotal = "      " + totalSubtotal;
            }
            if (totalSubtotal.length() == 9) {
                //5spaces
                totalSubtotal = "     " + totalSubtotal;
            }
            if (totalSubtotal.length() == 10) {
                //4spaces
                totalSubtotal = "    " + totalSubtotal;
            }


            PrinterFunctions.SetLineSpacing(portName, portSettings, 60);
            PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
            PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 30, 0, totalRate + totalGst + totalSubtotal + "\n");

            PrinterFunctions.SetLineSpacing(portName, portSettings, 50);
            PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
            PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 30, 0, "----------------------------------------" + "\n");






            String CouponDiscount = "";

            //CouponDiscount = Printer_POJO_ClassArraytotal.getTotaldiscount();

            if (!modal_forPrintReceipt.getDiscountAmount_StringGlobal().equals("0")) {
                //CouponDiscount = "Rs. " + CouponDiscount + ".00";

                if (modal_forPrintReceipt.getDiscountAmount_StringGlobal().contains(".")) {
                    CouponDiscount = "Rs." + modal_forPrintReceipt.getDiscountAmount_StringGlobal();
                } else {
                    CouponDiscount = "Rs." + modal_forPrintReceipt.getDiscountAmount_StringGlobal() + ".00";

                }


                if ((!CouponDiscount.equals("Rs.0.0")) && (!CouponDiscount.equals("Rs.0")) && (!CouponDiscount.equals("Rs.0.00")) && (CouponDiscount != (null)) && (!CouponDiscount.equals("")) && (!CouponDiscount.equals("Rs. .00")) && (!CouponDiscount.equals("Rs..00"))) {

                    if (CouponDiscount.length() == 4) {
                        //20spaces
                        //NEW TOTAL =4
                        CouponDiscount = "Discount Amount                   " + CouponDiscount;
                    } else if (CouponDiscount.length() == 5) {
                        //21spaces
                        //NEW TOTAL =5
                        CouponDiscount = "Discount Amount                 " + CouponDiscount;
                    } else if (CouponDiscount.length() == 6) {
                        //20spaces
                        //NEW TOTAL =6
                        CouponDiscount = "Discount Amount                " + CouponDiscount;
                    } else if (CouponDiscount.length() == 7) {
                        //19spaces
                        //NEW TOTAL =7
                        CouponDiscount = "Discount Amount               " + CouponDiscount;
                    } else if (CouponDiscount.length() == 8) {
                        //18spaces
                        //NEW TOTAL =8
                        CouponDiscount = " Discount Amount              " + CouponDiscount;
                    } else if (CouponDiscount.length() == 9) {
                        //17spaces
                        //NEW TOTAL =9
                        CouponDiscount = " Discount Amount             " + CouponDiscount;
                    } else if (CouponDiscount.length() == 10) {
                        //16spaces
                        //NEW TOTAL =9
                        CouponDiscount = " Discount Amount            " + CouponDiscount;
                    } else if (CouponDiscount.length() == 11) {
                        //15spaces
                        //NEW TOTAL =9
                        CouponDiscount = "Discount Amount            " + CouponDiscount;
                    } else if (CouponDiscount.length() == 12) {
                        //14spaces
                        //NEW TOTAL =9
                        CouponDiscount = "Discount Amount           " + CouponDiscount;
                    } else if (CouponDiscount.length() == 13) {
                        //13spaces
                        //NEW TOTAL =9
                        CouponDiscount = "Discount Amount           " + CouponDiscount;

                    } else {
                        CouponDiscount = "Discount Amount       " + CouponDiscount;

                    }

                    PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 0, 1, CouponDiscount + "\n");


                    PrinterFunctions.SetLineSpacing(portName, portSettings, 50);
                    PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
                    PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 30, 0, "----------------------------------------" + "\n");

                }
            }


            String redeemPoints_String_print = "";
            if (!modal_forPrintReceipt.getRedeemPoints_String().equals("0")) {
                redeemPoints_String_print = "Rs. " + modal_forPrintReceipt.getRedeemPoints_String() + ".00";

                if ((!redeemPoints_String_print.equals("Rs.0.0")) && (!redeemPoints_String_print.equals("Rs.0")) && (!redeemPoints_String_print.equals("Rs.0.00")) && (redeemPoints_String_print != (null)) && (!redeemPoints_String_print.equals("")) && (!redeemPoints_String_print.equals("Rs. .00")) && (!redeemPoints_String_print.equals("Rs..00"))) {

                    if (redeemPoints_String_print.length() == 4) {
                        //20spaces
                        //NEW TOTAL =4
                        redeemPoints_String_print = "Points Redeemed                    " + redeemPoints_String_print;
                    }
                    if (redeemPoints_String_print.length() == 5) {
                        //21spaces
                        //NEW TOTAL =5
                        redeemPoints_String_print = "Points Redeemed                  " + redeemPoints_String_print;
                    }
                    if (redeemPoints_String_print.length() == 6) {
                        //20spaces
                        //NEW TOTAL =6
                        redeemPoints_String_print = "Points Redeemed                 " + redeemPoints_String_print;
                    }

                    if (redeemPoints_String_print.length() == 7) {
                        //19spaces
                        //NEW TOTAL =7
                        redeemPoints_String_print = "Points Redeemed                " + redeemPoints_String_print;
                    }
                    if (redeemPoints_String_print.length() == 8) {
                        //18spaces
                        //NEW TOTAL =8
                        redeemPoints_String_print = "Points Redeemed               " + redeemPoints_String_print;
                    }
                    if (redeemPoints_String_print.length() == 9) {
                        //17spaces
                        //NEW TOTAL =9
                        redeemPoints_String_print = "Points Redeemed               " + redeemPoints_String_print;
                    }
                    if (redeemPoints_String_print.length() == 10) {
                        //16spaces
                        //NEW TOTAL =9
                        redeemPoints_String_print = "Points Redeemed             " + redeemPoints_String_print;
                    }
                    if (redeemPoints_String_print.length() == 11) {
                        //15spaces
                        //NEW TOTAL =9
                        redeemPoints_String_print = " Points Redeemed             " + redeemPoints_String_print;
                    }
                    if (redeemPoints_String_print.length() == 12) {
                        //14spaces
                        //NEW TOTAL =9
                        redeemPoints_String_print = "Points Redeemed            " + redeemPoints_String_print;
                    }

                    if (redeemPoints_String_print.length() == 13) {
                        //13spaces
                        //NEW TOTAL =9
                        redeemPoints_String_print = "Points Redeemed            " + redeemPoints_String_print;

                    }


                    PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 0, 1, redeemPoints_String_print + "\n");


                    PrinterFunctions.SetLineSpacing(portName, portSettings, 50);
                    PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
                    PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 30, 0, "----------------------------------------" + "\n");

                }
            }


            if (modal_forPrintReceipt.getisPhoneOrderSelected()) {

                String deliveryCharge = "";

                //CouponDiscount = Printer_POJO_ClassArraytotal.getTotaldiscount();

                if (!modal_forPrintReceipt.getDeliveryAmount().equals("0")) {
                    //CouponDiscount = "Rs. " + CouponDiscount + ".00";
                    if (modal_forPrintReceipt.getDeliveryAmount().contains(".")) {
                        deliveryCharge = "Rs." + modal_forPrintReceipt.getDeliveryAmount();
                    } else {
                        deliveryCharge = "Rs." + modal_forPrintReceipt.getDeliveryAmount() + ".00";

                    }
                    if ((!deliveryCharge.equals("Rs.0.0")) && (!deliveryCharge.equals("Rs.0")) && (!deliveryCharge.equals("Rs.0.00")) && (deliveryCharge != (null)) && (!deliveryCharge.equals("")) && (!deliveryCharge.equals("Rs. .00")) && (!deliveryCharge.equals("Rs..00"))) {

                        if (deliveryCharge.length() == 4) {
                            //20spaces
                            //NEW TOTAL =4
                            deliveryCharge = "Delivery Charge                   " + deliveryCharge;
                        } else if (deliveryCharge.length() == 5) {
                            //21spaces
                            //NEW TOTAL =5
                            deliveryCharge = "Delivery Charge                 " + deliveryCharge;
                        } else if (deliveryCharge.length() == 6) {
                            //20spaces
                            //NEW TOTAL =6
                            deliveryCharge = "Delivery Charge                " + deliveryCharge;
                        } else if (deliveryCharge.length() == 7) {
                            //19spaces
                            //NEW TOTAL =7
                            deliveryCharge = "Delivery Charge               " + deliveryCharge;
                        } else if (deliveryCharge.length() == 8) {
                            //18spaces
                            //NEW TOTAL =8
                            deliveryCharge = "Delivery Charge              " + deliveryCharge;
                        } else if (deliveryCharge.length() == 9) {
                            //17spaces
                            //NEW TOTAL =9
                            deliveryCharge = "Delivery Charge             " + deliveryCharge;
                        } else if (deliveryCharge.length() == 10) {
                            //16spaces
                            //NEW TOTAL =9
                            deliveryCharge = "Delivery Charge            " + deliveryCharge;
                        } else if (deliveryCharge.length() == 11) {
                            //15spaces
                            //NEW TOTAL =9
                            deliveryCharge = "Delivery Charge            " + deliveryCharge;
                        } else if (deliveryCharge.length() == 12) {
                            //14spaces
                            //NEW TOTAL =9
                            deliveryCharge = "Delivery Charge           " + deliveryCharge;
                        } else if (deliveryCharge.length() == 13) {
                            //13spaces
                            //NEW TOTAL =9
                            deliveryCharge = "Delivery Charge           " + deliveryCharge;

                        } else {
                            deliveryCharge = "Delivery Charge       " + deliveryCharge;

                        }

                        PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 0, 1, " " + deliveryCharge + "\n");


                        PrinterFunctions.SetLineSpacing(portName, portSettings, 50);
                        PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
                        PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 30, 0, "----------------------------------------" + "\n");

                    }
                }

            }


            PrinterFunctions.SetLineSpacing(portName, portSettings, 50);
            PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
            String NetTotal = Printer_POJO_ClassArraytotal.getTotalsubtotal();

            if (NetTotal.length() > 6) {

                if (NetTotal.length() == 7) {
                    //24spaces
                    //NEW TOTAL =9
                    NetTotal = "  NET TOTAL                    Rs. " + NetTotal;
                }
                if (NetTotal.length() == 8) {
                    //23spaces
                    //NEW TOTAL =9
                    NetTotal = "  NET TOTAL                   Rs. " + NetTotal;
                }
                if (NetTotal.length() == 9) {
                    //22spaces
                    //NEW TOTAL =9
                    NetTotal = "  NET TOTAL                  Rs. " + NetTotal;
                }
                if (NetTotal.length() == 10) {
                    //21spaces
                    //NEW TOTAL =9
                    NetTotal = "  NET TOTAL                 Rs. " + NetTotal;
                }
                if (NetTotal.length() == 11) {
                    //20spaces
                    //NEW TOTAL =9
                    NetTotal = "  NET TOTAL                Rs. " + NetTotal;
                }
                if (NetTotal.length() == 12) {
                    //19spaces
                    //NEW TOTAL =9
                    NetTotal = "  NET TOTAL               Rs. " + NetTotal;
                }
            } else {
                NetTotal = " NET TOTAL                   Rs. " + NetTotal;

            }

            PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 0, 1, NetTotal + "\n");

            PrinterFunctions.SetLineSpacing(portName, portSettings, 60);
            PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
            PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 30, 0, "----------------------------------------" + "\n");
            try {
                if (modal_forPrintReceipt.getPayment_mode().toUpperCase().equals(Constants.CASH_ON_DELIVERY)) {
                    if ((!modal_forPrintReceipt.getAmountRecieved_String().equals("null")) && (!modal_forPrintReceipt.getBalanceAmount_String().equals("null"))) {
                        PrinterFunctions.SetLineSpacing(portName, portSettings, 60);
                        PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
                        PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 30, 0, "Amount Given by Customer : ");


                        PrinterFunctions.SetLineSpacing(portName, portSettings, 90);
                        PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
                        PrinterFunctions.PrintText(portName, portSettings, 0, 0, 0, 0, 0, 0, 30, 0, modal_forPrintReceipt.getAmountRecieved_String() + " Rs " + "\n");

                        PrinterFunctions.SetLineSpacing(portName, portSettings, 60);
                        PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
                        PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 30, 0, "----------------------------------------" + "\n");


                        PrinterFunctions.SetLineSpacing(portName, portSettings, 60);
                        PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
                        PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 30, 0, "Balance Amount given : ");


                        PrinterFunctions.SetLineSpacing(portName, portSettings, 90);
                        PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
                        PrinterFunctions.PrintText(portName, portSettings, 0, 0, 0, 0, 0, 0, 30, 0, modal_forPrintReceipt.getBalanceAmount_String() + " Rs" + "\n");

                        PrinterFunctions.SetLineSpacing(portName, portSettings, 60);
                        PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
                        PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 30, 0, "----------------------------------------" + "\n");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            PrinterFunctions.SetLineSpacing(portName, portSettings, 60);
            PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
            PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 30, 0, "Earned Rewards : " + String.valueOf((int) (modal_forPrintReceipt.getTotalredeempointsusergetfromorder())) + "\n");

            PrinterFunctions.SetLineSpacing(portName, portSettings, 50);
            PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
            PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 30, 0, "----------------------------------------" + "\n");
            if (modal_forPrintReceipt.getPayment_mode().toString().toUpperCase().equals(Constants.CREDIT)) {
                PrinterFunctions.SetLineSpacing(portName, portSettings, 60);
                PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
                PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 30, 0, "Old Amount need to be Paid : " + String.valueOf(Math.round(modal_forPrintReceipt.getTotalamountUserHaveAsCredit())) + "\n");


                PrinterFunctions.SetLineSpacing(portName, portSettings, 60);
                PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
                PrinterFunctions.PrintText(portName, portSettings, 0, 0, 0, 0, 0, 1, 30, 0, " total Amount need to be Paid = (Old amount + Current Bill Amount ) \n");


                String payableamountPrint = "";
                try {
                    payableamountPrint = Printer_POJO_ClassArraytotal.getTotalsubtotal();
                } catch (Exception e) {
                    e.printStackTrace();

                }


                double payableamountdoublePrint = 0;
                try {
                    payableamountdoublePrint = Math.round(Double.parseDouble(String.valueOf(payableamountPrint)));
                } catch (Exception e) {
                    payableamountdoublePrint = 0;
                    e.printStackTrace();

                }


                PrinterFunctions.SetLineSpacing(portName, portSettings, 60);
                PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
                PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 30, 0, "total Amount need to be Paid : " + String.valueOf(Math.round(modal_forPrintReceipt.getTotalamountUserHaveAsCredit() + payableamountdoublePrint)) + "\n");

                PrinterFunctions.SetLineSpacing(portName, portSettings, 50);
                PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
                PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 30, 0, "----------------------------------------" + "\n");

            }
            if (!modal_forPrintReceipt.getOrdertype().equals(Constants.POSORDER)) {
                PrinterFunctions.SetLineSpacing(portName, portSettings, 60);
                PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
                PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 30, 0, "Order Type: ");


                PrinterFunctions.SetLineSpacing(portName, portSettings, 90);
                PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
                PrinterFunctions.PrintText(portName, portSettings, 0, 0, 0, 0, 0, 0, 30, 0, modal_forPrintReceipt.getOrdertype() + "\n");
            }

            PrinterFunctions.SetLineSpacing(portName, portSettings, 60);
            PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
            PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 30, 0, "Payment Mode: ");


            PrinterFunctions.SetLineSpacing(portName, portSettings, 90);
            PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
            PrinterFunctions.PrintText(portName, portSettings, 0, 0, 0, 0, 0, 0, 30, 0, modal_forPrintReceipt.getPayment_mode() + "\n");


            PrinterFunctions.SetLineSpacing(portName, portSettings, 60);
            PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
            PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 30, 0, "MobileNo : ");


            PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
            PrinterFunctions.PrintText(portName, portSettings, 0, 0, 0, 0, 0, 0, 0, 0, modal_forPrintReceipt.getUserMobile() + "           " + "\n");

            if (modal_forPrintReceipt.getisPhoneOrderSelected()) {
                PrinterFunctions.SetLineSpacing(portName, portSettings, 60);
                PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
                PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 30, 0, "----------------------------------------" + "\n" + "\n");

                PrinterFunctions.SetLineSpacing(portName, portSettings, 200);
                PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
                PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 2, 2, 0, 1, "Token No : " + modal_forPrintReceipt.getTokenno() + "\n");


                PrinterFunctions.SetLineSpacing(portName, portSettings, 100);
                PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
                PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 1, 0, 30, 0, "Notes : ");


                PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
                PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 1, 0, 0, 0, "" + "  " + "\n\n");


                PrinterFunctions.SetLineSpacing(portName, portSettings, 100);
                PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
                PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 30, 0, "Slot Name : ");


                PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
                PrinterFunctions.PrintText(portName, portSettings, 0, 0, 0, 0, 0, 0, 0, 0, Constants.EXPRESS_DELIVERY_SLOTNAME + "         " + "\n");


                PrinterFunctions.SetLineSpacing(portName, portSettings, 100);
                PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
                PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 30, 0, "Order Placed Time  : ");


                PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
                PrinterFunctions.PrintText(portName, portSettings, 0, 0, 0, 0, 0, 0, 0, 0, modal_forPrintReceipt.getOrderplacedTime() + "         " + "\n");


                PrinterFunctions.SetLineSpacing(portName, portSettings, 100);
                PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
                PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 30, 0, "Delivery Time  : ");


                PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
                PrinterFunctions.PrintText(portName, portSettings, 0, 0, 0, 0, 0, 0, 0, 0, slottime + "         " + "\n");


                PrinterFunctions.SetLineSpacing(portName, portSettings, 60);
                PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
                PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 30, 0, "Delivery  Type: ");


                PrinterFunctions.SetLineSpacing(portName, portSettings, 90);
                PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
                PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 30, 0, Constants.HOME_DELIVERY_DELIVERYTYPE + "\n");


                PrinterFunctions.SetLineSpacing(portName, portSettings, 60);
                PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
                PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 30, 0, "Distance from Store : ");


                PrinterFunctions.SetLineSpacing(portName, portSettings, 90);
                PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
                PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 30, 0, modal_forPrintReceipt.getSelected_Address_modal().getDeliverydistance() + "Km" + "\n");


                PrinterFunctions.SetLineSpacing(portName, portSettings, 60);
                PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
                PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 30, 0, "Address : " + "\n" + "\n");


                String Address = (modal_forPrintReceipt.getSelected_Address_modal().getAddressline1());


                PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
                PrinterFunctions.PrintText(portName, portSettings, 0, 0, 0, 0, 0, 0, 30, 0, Address + "         " + "\n" + "\n");

            }



/*
            PrinterFunctions.SetLineSpacing(portName, portSettings, 120);
            PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
            PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 0, 30, "ID : ");


            PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
            PrinterFunctions.PrintText(portName, portSettings, 0, 0, 0, 0, 0, 0, 0, 50, tokenno + "\n");


 */


            PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
            PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 0, 1, "\n" + "Thank you for choosing us !!!  " + "\n");
            PrinterFunctions.PreformCut(portName,portSettings,1);





            //print second timeee






            Intent intent = new Intent("receiveDataFromprinterService");
            intent.putExtra("message", "success");
            sendBroadcast(intent);



            return "success";


        }
        catch (Exception e) {
            e.printStackTrace();

            return "error";

        }




    }




    private String getSlotTime(String slottime, String orderplacedtime) {
        String result = "", lastFourDigits = "";
        //Log.d(TAG, "slottime  "+slottime);
        if (slottime.contains("mins")) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss", Locale.ENGLISH);
                sdf.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));

                final Date date = sdf.parse(orderplacedtime);
                final Calendar calendar = Calendar.getInstance();
                String timeoftheSlot ="";
                try {
                    timeoftheSlot = (slottime.replaceAll("[^\\d.]", ""));
                }
                catch(Exception e){
                    e.printStackTrace();
                }
                int timeoftheSlotDouble =0;
                try {
                    timeoftheSlotDouble = Integer.parseInt(timeoftheSlot);
                }
                catch(Exception e){
                    e.printStackTrace();
                }
                calendar.setTime(date);
                SimpleDateFormat sdff = new SimpleDateFormat("HH:mm",Locale.ENGLISH);
                sdff.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));

                String placedtime = String.valueOf(sdff.format(calendar.getTime()));
                calendar.add(Calendar.MINUTE, timeoftheSlotDouble);

                System.out.println("Time here " + sdff.format(calendar.getTime()));
                System.out.println("Time here 90 mins" + orderplacedtime);
                result = placedtime +" - "+String.valueOf(sdff.format(calendar.getTime()));
                System.out.println("Time here 90 mins" + result);

                result = result.replaceAll("GMT[+]05:30", "");

                //  System.out.println("Time here "+result);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            if (slottime.length() > 5) {
                lastFourDigits = slottime.substring(slottime.length() - 5);
            } else {
                lastFourDigits = slottime;
            }

            //  result = slotdate + " " + lastFourDigits + ":00";

        }
        return result;
    }

    public String getDate_and_time()
    {

        Date c = Calendar.getInstance().getTime();
        //  System.out.println("Current time => Sat, 9 Jan 2021 13:12:24 " + c);

        SimpleDateFormat day = new SimpleDateFormat("EEE", Locale.ENGLISH);
        day.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));

        String   CurrentDay = day.format(c);

        SimpleDateFormat df = new SimpleDateFormat("d MMM yyyy",Locale.ENGLISH);
        df.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));

        String CurrentDatee = df.format(c);
        String CurrentDate = CurrentDay+", "+CurrentDatee;


        SimpleDateFormat dfTime = new SimpleDateFormat("HH:mm:ss",Locale.ENGLISH);
        dfTime.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));

        String  FormattedTime = dfTime.format(c);
        String formattedDate = CurrentDay+", "+CurrentDatee+" "+FormattedTime;
        return formattedDate;
    }









}
