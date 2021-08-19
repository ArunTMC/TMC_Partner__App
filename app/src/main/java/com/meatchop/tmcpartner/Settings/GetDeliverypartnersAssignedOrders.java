package com.meatchop.tmcpartner.Settings;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.meatchop.tmcpartner.Constants;
import com.meatchop.tmcpartner.MobileScreen_JavaClasses.ManageOrders.Adapter_Mobile_ManageOrders_ListView1;
import com.meatchop.tmcpartner.NukeSSLCerts;
import com.meatchop.tmcpartner.PosScreen_JavaClasses.ManageOrders.Modal_ManageOrders_Pojo_Class;
import com.meatchop.tmcpartner.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class GetDeliverypartnersAssignedOrders extends AppCompatActivity {
    String assignedOrdersString = "",deliveryUserMobileno="";
    public static List<Modal_ManageOrders_Pojo_Class> ordersList;
    boolean isnewOrdersSyncButtonClicked = false;
    public static Adapter_Mobile_GetDeliveryPartnersAssignedOrders adapter_mobile_getDeliveryPartnersAssignedOrders;
    ListView assignedOrdersListview;
    TextView deliveryUserMobileno_textWidget;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.get_deliverypartners_assigned_orders_activity);
        new NukeSSLCerts();
        NukeSSLCerts.nuke();
        ordersList = new ArrayList<Modal_ManageOrders_Pojo_Class>();
        assignedOrdersListview = findViewById(R.id.assignedOrdersListview);
        deliveryUserMobileno_textWidget = findViewById(R.id.deliveryUserMobileno_textWidget);
        assignedOrdersString = getIntent().getStringExtra("assignedOrdersString");
        deliveryUserMobileno = getIntent().getStringExtra("deliveryPartnerMobileNo");
        deliveryUserMobileno_textWidget.setText(deliveryUserMobileno);
        convertStringtoJsonandGettingData(assignedOrdersString);


    }

    private void convertStringtoJsonandGettingData(String assignedOrdersString) {

        try {
            JSONObject jsonObject = new JSONObject(assignedOrdersString);
            JSONArray JArray = jsonObject.getJSONArray("content");
            //Log.d(Constants.TAG, "convertingJsonStringintoArray Response: " + JArray);
            int i1 = 0;
            String ordertype = "#";
            int arrayLength = JArray.length();
            //Log.d("Constants.TAG", "convertingJsonStringintoArray Response: " + arrayLength);


            for (; i1 < (arrayLength); i1++) {


                try {
                    JSONObject json = JArray.getJSONObject(i1);
                    Modal_ManageOrders_Pojo_Class manageOrdersPojoClass = new Modal_ManageOrders_Pojo_Class();
                    //Log.d(Constants.TAG, "convertingJsonStringintoArray orderstatus: " + String.valueOf(json.get("orderstatus")));


                    if (json.has("orderid")) {
                        manageOrdersPojoClass.orderid = String.valueOf(json.get("orderid"));

                    } else {
                        manageOrdersPojoClass.orderid = "";
                    }


                    if (json.has("orderplacedtime")) {
                        manageOrdersPojoClass.orderplacedtime = String.valueOf(json.get("orderplacedtime"));

                    } else {
                        manageOrdersPojoClass.orderplacedtime = "";
                    }


                    if (json.has("payableamount")) {
                        manageOrdersPojoClass.payableamount = String.valueOf(json.get("payableamount"));

                    } else {
                        manageOrdersPojoClass.payableamount = "";
                    }


                    if (json.has("paymentmode")) {
                        manageOrdersPojoClass.paymentmode = String.valueOf(json.get("paymentmode"));

                    } else {
                        manageOrdersPojoClass.paymentmode = "";
                    }


                    if (json.has("tokenno")) {
                        manageOrdersPojoClass.tokenno = String.valueOf(json.get("tokenno"));

                    } else {
                        manageOrdersPojoClass.tokenno = "";
                    }


                    try {
                        if (json.has("itemdesp")) {
                            JSONArray itemdesp = json.getJSONArray("itemdesp");

                            manageOrdersPojoClass.itemdesp = itemdesp;

                        } else {
                            //Log.i(Constants.TAG, "Can't Get itemDesp");
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (json.has("orderstatus")) {
                        manageOrdersPojoClass.orderstatus = String.valueOf(json.get("orderstatus"));

                    } else {
                        manageOrdersPojoClass.orderstatus = "";
                    }


                    if (json.has("usermobile")) {
                        manageOrdersPojoClass.usermobile = String.valueOf(json.get("usermobile"));

                    } else {
                        manageOrdersPojoClass.usermobile = "";
                    }


                    if (json.has("vendorkey")) {
                        manageOrdersPojoClass.vendorkey = String.valueOf(json.get("vendorkey"));

                    } else {
                        manageOrdersPojoClass.vendorkey = "vendor_1";
                    }


                    if (json.has("orderdetailskey")) {
                        manageOrdersPojoClass.orderdetailskey = String.valueOf(json.get("orderdetailskey"));

                    } else {
                        manageOrdersPojoClass.orderdetailskey = "";
                    }


                    if (json.has("orderdeliverytime")) {
                        manageOrdersPojoClass.orderdeliveredtime = String.valueOf(json.get("orderdeliverytime"));

                    } else {
                        manageOrdersPojoClass.orderdeliveredtime = "";
                    }
                    if (json.has("useraddresskey")) {
                        manageOrdersPojoClass.useraddresskey = String.valueOf(json.get("useraddresskey"));

                    } else {
                        manageOrdersPojoClass.useraddresskey = "";
                    }


                    if (json.has("orderreadytime")) {
                        manageOrdersPojoClass.orderreadytime = String.valueOf(json.get("orderreadytime"));

                    } else {
                        manageOrdersPojoClass.orderreadytime = "";
                    }


                    if (json.has("orderpickeduptime")) {
                        manageOrdersPojoClass.orderpickeduptime = String.valueOf(json.get("orderpickeduptime"));

                    } else {
                        manageOrdersPojoClass.orderpickeduptime = "";
                    }


                    if (json.has("orderconfirmedtime")) {
                        manageOrdersPojoClass.orderconfirmedtime = String.valueOf(json.get("orderconfirmedtime"));

                    } else {
                        manageOrdersPojoClass.orderconfirmedtime = "";
                    }


                    if (json.has("coupondiscount")) {
                        manageOrdersPojoClass.coupondiscamount = String.valueOf(json.get("coupondiscount"));

                    } else {
                        manageOrdersPojoClass.coupondiscamount = "";
                    }


                    if (json.has("deliverytype")) {
                        manageOrdersPojoClass.deliverytype = String.valueOf(json.get("deliverytype"));

                    } else {
                        manageOrdersPojoClass.deliverytype = "";
                    }


                    if (json.has("slottimerange")) {
                        manageOrdersPojoClass.slottimerange = String.valueOf(json.get("slottimerange"));

                    } else {
                        manageOrdersPojoClass.slottimerange = "";
                    }


                    if (json.has("slotdate")) {
                        manageOrdersPojoClass.slotdate = String.valueOf(json.get("slotdate"));

                    } else {
                        manageOrdersPojoClass.slotdate = "";
                    }


                    if (json.has("slotname")) {
                        manageOrdersPojoClass.slotname = String.valueOf(json.get("slotname"));

                    } else {
                        manageOrdersPojoClass.slotname = "";
                    }
                    if (json.has("slottimerange")) {
                        manageOrdersPojoClass.slottimerange = String.valueOf(json.get("slottimerange"));

                    } else {
                        manageOrdersPojoClass.slottimerange = "";
                    }


                    if (json.has("ordertype")) {
                        manageOrdersPojoClass.orderType = String.valueOf(json.get("ordertype"));
                        ordertype = String.valueOf(json.get("ordertype"));
                    } else {
                        ordertype = "#";
                        manageOrdersPojoClass.orderType = "";
                    }

                    if (json.has("useraddresslat")) {
                        manageOrdersPojoClass.useraddresslat = String.valueOf(json.get("useraddresslat"));

                    } else {
                        manageOrdersPojoClass.useraddresslat = "";
                    }
                    if (json.has("paymenttranscationimageurl")) {
                        manageOrdersPojoClass.paymenttranscationimageurl = String.valueOf(json.get("paymenttranscationimageurl"));

                    } else {
                        manageOrdersPojoClass.paymenttranscationimageurl = "";
                    }


                    if (json.has("useraddresslong")) {
                        manageOrdersPojoClass.useraddresslon = String.valueOf(json.get("useraddresslong"));

                    } else {
                        manageOrdersPojoClass.useraddresslon = "";
                    }

                    if (json.has("keyfromtrackingDetails")) {
                        manageOrdersPojoClass.keyfromtrackingDetails = String.valueOf(json.get("keyfromtrackingDetails"));

                    } else {
                        manageOrdersPojoClass.keyfromtrackingDetails = "";
                    }

                    try {
                        if (ordertype.toUpperCase().equals(Constants.APPORDER)) {
                            String addresss = "";

                            if (json.has("addressline1")) {
                                addresss = String.valueOf(json.get("addressline1"));

                            } else {
                                addresss = "no value";

                            }
                            if (json.has("addressline2")) {
                                addresss = addresss + "   " + String.valueOf(json.get("addressline2"));

                            } else {
                                addresss = addresss + "   " + "no value";

                            }
                            manageOrdersPojoClass.useraddress = addresss;

                        }
                    } catch (Exception E) {
                        manageOrdersPojoClass.useraddress = "-";
                        E.printStackTrace();
                    }
                    try {
                        if (ordertype.toUpperCase().equals(Constants.APPORDER)) {


                            if (json.has("deliverydistance")) {

                                String deliverydistance = String.valueOf(json.get("deliverydistance"));
                                if (!deliverydistance.equals(null) && (!deliverydistance.equals("null"))) {
                                    manageOrdersPojoClass.deliverydistance = String.valueOf(json.get("deliverydistance"));

                                } else {
                                    manageOrdersPojoClass.deliverydistance = "0";

                                }
                            } else {
                                manageOrdersPojoClass.deliverydistance = "0";
                            }


                        }
                    } catch (Exception E) {
                        manageOrdersPojoClass.deliverydistance = "0";
                        E.printStackTrace();
                    }


                    if (!String.valueOf(json.get("orderstatus")).equals("NEW")) {

                        if (json.has("deliveryusername")) {
                            manageOrdersPojoClass.deliveryPartnerName = String.valueOf(json.get("deliveryusername"));

                        }
                        if (json.has("deliveryuserkey")) {
                            manageOrdersPojoClass.deliveryPartnerKey = String.valueOf(json.get("deliveryuserkey"));
                            ;

                        }
                        if (json.has("deliveryusermobileno")) {
                            manageOrdersPojoClass.deliveryPartnerMobileNo = String.valueOf(json.get("deliveryusermobileno"));

                        }


                    }
                    ordersList.add(manageOrdersPojoClass);

                    //Log.d(Constants.TAG, "convertingJsonStringintoArray ordersList: " + ordersList);

                } catch (JSONException e) {
                    e.printStackTrace();
                    isnewOrdersSyncButtonClicked = false;

                    //Log.d(Constants.TAG, "convertingJsonStringintoArray e: " + e.getLocalizedMessage());
                    //Log.d(Constants.TAG, "convertingJsonStringintoArray e: " + e.getMessage());
                    //Log.d(Constants.TAG, "convertingJsonStringintoArray e: " + e.toString());

                }


            }

            //Log.d(Constants.TAG, "convertingJsonStringintoArray orderlist: " + ordersList);

            //saveorderDetailsInLocal(ordersList);
            displayorderDetailsinListview(ordersList);


        } catch (JSONException e) {
            e.printStackTrace();
            isnewOrdersSyncButtonClicked = false;

        }
    }


    private void displayorderDetailsinListview( List<Modal_ManageOrders_Pojo_Class> ordersList) {
        assignedOrdersListview.setVisibility(View.VISIBLE);

        try {


            Collections.sort(ordersList, new Comparator<Modal_ManageOrders_Pojo_Class>() {
                public int compare(final Modal_ManageOrders_Pojo_Class object1, final Modal_ManageOrders_Pojo_Class object2) {
                    return object2.getOrderdeliveredtime().compareTo(object2.getOrderdeliveredtime());
                }
            });


            isnewOrdersSyncButtonClicked = false;

            adapter_mobile_getDeliveryPartnersAssignedOrders = new Adapter_Mobile_GetDeliveryPartnersAssignedOrders(GetDeliverypartnersAssignedOrders.this, ordersList, GetDeliverypartnersAssignedOrders.this);
            assignedOrdersListview.setAdapter(adapter_mobile_getDeliveryPartnersAssignedOrders);

        //0   loadingpanelmask.setVisibility(View.GONE);
           // loadingPanel.setVisibility(View.GONE);
            assignedOrdersListview.setVisibility(View.VISIBLE);
          //  mobile_orderinstruction.setVisibility(View.GONE);


        } catch (Exception e) {
            isnewOrdersSyncButtonClicked = false;

            e.printStackTrace();
        }


    }
}