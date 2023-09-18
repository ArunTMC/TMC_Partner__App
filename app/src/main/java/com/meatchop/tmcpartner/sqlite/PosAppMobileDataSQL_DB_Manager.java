package com.meatchop.tmcpartner.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.meatchop.tmcpartner.Constants;

import com.meatchop.tmcpartner.settings.Modal_PosAppMobileData;

public class PosAppMobileDataSQL_DB_Manager {


    DataBaseHelper dbHelper;
    SQLiteDatabase sqLiteDatabase;
    Context context;

    public static final  String orderdeliveredtextmsg ="orderdeliveredtextmsg" , orderdetailsnewschema ="orderdetailsnewschema",orderpickeduptextmsg ="orderpickeduptextmsg",
            appacessdetails_admin ="appacessdetails_admin",
            appacessdetails_cashier ="appacessdetails_cashier" , appacessdetails_deliverymanager ="appacessdetails_deliverymanager" ,appacessdetails_reportsviewer ="appacessdetails_reportsviewer",
            appacessdetails_storemanager ="appacessdetails_storemanager" ,redeemdata_maxpointsinaday ="redeemdata_maxpointsinaday" , redeemdata_minordervalueforredeem ="redeemdata_minordervalueforredeem" ,
            updateweightforonlineorders ="updateweightforonlineorders" , redeemdata_pointsfor100rs = "redeemdata_pointsfor100rs" ,localDB_id ="localDB_id" , enableorderplacingmicroservice = "enableorderplacingmicroservice";


    public static String database_Table = "PosAppMobileData";



    public static String CRETAE_08_QUERY = "CREATE TABLE " + database_Table + " ( "
            + localDB_id + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + orderdeliveredtextmsg + " TEXT NOT NULL,"
            + orderdetailsnewschema + " TEXT NOT NULL,"
            + orderpickeduptextmsg + " TEXT NOT NULL,"
            + appacessdetails_admin + " TEXT NOT NULL,"
            + appacessdetails_cashier + " TEXT NOT NULL,"
            + appacessdetails_deliverymanager + " TEXT NOT NULL,"
            + appacessdetails_reportsviewer + " TEXT NOT NULL,"
            + appacessdetails_storemanager + " TEXT NOT NULL,"
            + redeemdata_maxpointsinaday + " TEXT NOT NULL,"
            + redeemdata_minordervalueforredeem + " TEXT NOT NULL,"
            + updateweightforonlineorders + " TEXT NOT NULL,"
            + enableorderplacingmicroservice + " TEXT NOT NULL,"
            + redeemdata_pointsfor100rs + " TEXT NOT NULL );";





    public PosAppMobileDataSQL_DB_Manager(Context context) {
        this.context = context;
    }


    public PosAppMobileDataSQL_DB_Manager open() throws SQLException {
        dbHelper = new DataBaseHelper(context, CRETAE_08_QUERY, database_Table);
        sqLiteDatabase = dbHelper.getWritableDatabase();
        dbHelper.onCreate(sqLiteDatabase);

        return this;
    }



    public int deleteTable( boolean isCreateNewTable){
        int i = sqLiteDatabase.delete(database_Table,null , null);
        Log.i("delete count vendor",String.valueOf(i));

        return i;


    }




    public Cursor Fetch() {
        //String  [] columns = new String[] {localDB_id,menuItemId , itemName, tmcpriceperkg};
        Cursor cursor = sqLiteDatabase.query(database_Table, null, null, null, null, null, null);
        if (cursor != null) {

            cursor.moveToFirst();

        }

        return cursor;


    }


    public void dropTable( boolean isCreateNewTable) {

        dbHelper.DropTable(sqLiteDatabase, database_Table, isCreateNewTable);

    }



    public void delete(long ID) {

        sqLiteDatabase.delete(database_Table, localDB_id + "=" + ID, null);
    }


    public void close () {

        dbHelper.close(sqLiteDatabase);
    }



    public int update(Modal_PosAppMobileData modal_Pos_appMobileData) {
        ContentValues contentValues = new ContentValues();

        try {
            if (!modal_Pos_appMobileData.getOrderdeliveredtextmsg().equals("") && !String.valueOf(modal_Pos_appMobileData.getOrderdeliveredtextmsg()).toUpperCase().equals("NULL")) {
                if (String.valueOf(modal_Pos_appMobileData.getOrderdeliveredtextmsg()).toUpperCase().equals(Constants.Empty_Text)) {

                    contentValues.put(orderdeliveredtextmsg, "");

                } else {

                    contentValues.put(orderdeliveredtextmsg, modal_Pos_appMobileData.getOrderdeliveredtextmsg());


                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        try {
            if (!modal_Pos_appMobileData.getEnableorderplacingmicroservice().equals("") && !String.valueOf(modal_Pos_appMobileData.getEnableorderplacingmicroservice()).toUpperCase().equals("NULL")) {
                if (String.valueOf(modal_Pos_appMobileData.getEnableorderplacingmicroservice()).toUpperCase().equals(Constants.Empty_Text)) {

                    contentValues.put(enableorderplacingmicroservice, false);

                } else {

                    contentValues.put(enableorderplacingmicroservice, modal_Pos_appMobileData.getEnableorderplacingmicroservice());


                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }




        try {
            if (!modal_Pos_appMobileData.getOrderdetailsnewschema().equals("") && !String.valueOf(modal_Pos_appMobileData.getOrderdetailsnewschema()).toUpperCase().equals("NULL")) {
                if (String.valueOf(modal_Pos_appMobileData.getOrderdetailsnewschema()).toUpperCase().equals(Constants.Empty_Text)) {

                    contentValues.put(orderdetailsnewschema, "");

                } else {

                    contentValues.put(orderdetailsnewschema, modal_Pos_appMobileData.getOrderdetailsnewschema());


                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        try {
            if (!modal_Pos_appMobileData.getOrderpickeduptextmsg().equals("") && !String.valueOf(modal_Pos_appMobileData.getOrderpickeduptextmsg()).toUpperCase().equals("NULL")) {
                if (String.valueOf(modal_Pos_appMobileData.getOrderpickeduptextmsg()).toUpperCase().equals(Constants.Empty_Text)) {

                    contentValues.put(orderpickeduptextmsg, "");

                } else {

                    contentValues.put(orderpickeduptextmsg, modal_Pos_appMobileData.getOrderpickeduptextmsg());


                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        try {
            if (!modal_Pos_appMobileData.getAppacessdetails_admin().equals("") && !String.valueOf(modal_Pos_appMobileData.getAppacessdetails_admin()).toUpperCase().equals("NULL")) {
                if (String.valueOf(modal_Pos_appMobileData.getAppacessdetails_admin()).toUpperCase().equals(Constants.Empty_Text)) {

                    contentValues.put(appacessdetails_admin, "");

                } else {

                    contentValues.put(appacessdetails_admin, modal_Pos_appMobileData.getAppacessdetails_admin());


                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        try {
            if (!modal_Pos_appMobileData.getAppacessdetails_cashier().equals("") && !String.valueOf(modal_Pos_appMobileData.getAppacessdetails_cashier()).toUpperCase().equals("NULL")) {
                if (String.valueOf(modal_Pos_appMobileData.getAppacessdetails_cashier()).toUpperCase().equals(Constants.Empty_Text)) {

                    contentValues.put(appacessdetails_cashier, "");

                } else {

                    contentValues.put(appacessdetails_cashier, modal_Pos_appMobileData.getAppacessdetails_cashier());


                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }



        try {
            if (!modal_Pos_appMobileData.getAppacessdetails_deliverymanager().equals("") && !String.valueOf(modal_Pos_appMobileData.getAppacessdetails_deliverymanager()).toUpperCase().equals("NULL")) {
                if (String.valueOf(modal_Pos_appMobileData.getAppacessdetails_deliverymanager()).toUpperCase().equals(Constants.Empty_Text)) {

                    contentValues.put(appacessdetails_deliverymanager, "");

                } else {

                    contentValues.put(appacessdetails_deliverymanager, modal_Pos_appMobileData.getAppacessdetails_deliverymanager());


                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        try {
            if (!modal_Pos_appMobileData.getAppacessdetails_reportsviewer().equals("") && !String.valueOf(modal_Pos_appMobileData.getAppacessdetails_reportsviewer()).toUpperCase().equals("NULL")) {
                if (String.valueOf(modal_Pos_appMobileData.getAppacessdetails_reportsviewer()).toUpperCase().equals(Constants.Empty_Text)) {

                    contentValues.put(appacessdetails_reportsviewer, "");

                } else {

                    contentValues.put(appacessdetails_reportsviewer, modal_Pos_appMobileData.getAppacessdetails_reportsviewer());


                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        try {
            if (!modal_Pos_appMobileData.getAppacessdetails_storemanager().equals("") && !String.valueOf(modal_Pos_appMobileData.getAppacessdetails_storemanager()).toUpperCase().equals("NULL")) {
                if (String.valueOf(modal_Pos_appMobileData.getAppacessdetails_storemanager()).toUpperCase().equals(Constants.Empty_Text)) {

                    contentValues.put(appacessdetails_storemanager, "");

                } else {

                    contentValues.put(appacessdetails_storemanager, modal_Pos_appMobileData.getAppacessdetails_storemanager());


                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }



        try {
            if (!modal_Pos_appMobileData.getRedeemdata_maxpointsinaday().equals("") && !String.valueOf(modal_Pos_appMobileData.getRedeemdata_maxpointsinaday()).toUpperCase().equals("NULL")) {
                if (String.valueOf(modal_Pos_appMobileData.getRedeemdata_maxpointsinaday()).toUpperCase().equals(Constants.Empty_Text)) {

                    contentValues.put(redeemdata_maxpointsinaday, "");

                } else {

                    contentValues.put(redeemdata_maxpointsinaday, modal_Pos_appMobileData.getRedeemdata_maxpointsinaday());


                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }




        try {
            if (!modal_Pos_appMobileData.getRedeemdata_minordervalueforredeem().equals("") && !String.valueOf(modal_Pos_appMobileData.getRedeemdata_minordervalueforredeem()).toUpperCase().equals("NULL")) {
                if (String.valueOf(modal_Pos_appMobileData.getRedeemdata_minordervalueforredeem()).toUpperCase().equals(Constants.Empty_Text)) {

                    contentValues.put(redeemdata_minordervalueforredeem, "");

                } else {

                    contentValues.put(redeemdata_minordervalueforredeem, modal_Pos_appMobileData.getRedeemdata_minordervalueforredeem());


                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }



        try {
            if (!modal_Pos_appMobileData.getUpdateweightforonlineorders().equals("") && !String.valueOf(modal_Pos_appMobileData.getUpdateweightforonlineorders()).toUpperCase().equals("NULL")) {
                if (String.valueOf(modal_Pos_appMobileData.getUpdateweightforonlineorders()).toUpperCase().equals(Constants.Empty_Text)) {

                    contentValues.put(updateweightforonlineorders, "");

                } else {

                    contentValues.put(updateweightforonlineorders, modal_Pos_appMobileData.getUpdateweightforonlineorders());


                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        try {
            if (!modal_Pos_appMobileData.getRedeemdata_pointsfor100rs().equals("") && !String.valueOf(modal_Pos_appMobileData.getRedeemdata_pointsfor100rs()).toUpperCase().equals("NULL")) {
                if (String.valueOf(modal_Pos_appMobileData.getRedeemdata_pointsfor100rs()).toUpperCase().equals(Constants.Empty_Text)) {

                    contentValues.put(redeemdata_pointsfor100rs, "");

                } else {

                    contentValues.put(redeemdata_pointsfor100rs, modal_Pos_appMobileData.getRedeemdata_pointsfor100rs());


                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }







        int response = sqLiteDatabase.update(database_Table, contentValues, localDB_id + "=" + modal_Pos_appMobileData.getLocalDB_id(), null);
        return response;


    }


    public long insert(Modal_PosAppMobileData modal_Pos_appMobileData) {
        long id = 0;
        try {
            ContentValues contentValues = new ContentValues();
            try {
                contentValues.put(orderdeliveredtextmsg, String.valueOf(modal_Pos_appMobileData.getOrderdeliveredtextmsg()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                contentValues.put(orderdetailsnewschema, String.valueOf(modal_Pos_appMobileData.getOrderdetailsnewschema()));

            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                contentValues.put(orderpickeduptextmsg, String.valueOf(modal_Pos_appMobileData.getOrderpickeduptextmsg()));

            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                contentValues.put(appacessdetails_admin, String.valueOf(modal_Pos_appMobileData.getAppacessdetails_admin()));

            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                contentValues.put(appacessdetails_cashier, String.valueOf(modal_Pos_appMobileData.getAppacessdetails_cashier()));

            } catch (Exception e) {
                e.printStackTrace();
            }




            try {
                contentValues.put(appacessdetails_deliverymanager, String.valueOf(modal_Pos_appMobileData.getAppacessdetails_deliverymanager()));

            } catch (Exception e) {
                e.printStackTrace();
            }


            try {
                contentValues.put(appacessdetails_reportsviewer, String.valueOf(modal_Pos_appMobileData.getAppacessdetails_reportsviewer()));

            } catch (Exception e) {
                e.printStackTrace();
            }



            try {
                contentValues.put(appacessdetails_storemanager, String.valueOf(modal_Pos_appMobileData.getAppacessdetails_storemanager()));

            } catch (Exception e) {
                e.printStackTrace();
            }



            try {
                contentValues.put(redeemdata_maxpointsinaday, String.valueOf(modal_Pos_appMobileData.getRedeemdata_maxpointsinaday()));

            } catch (Exception e) {
                e.printStackTrace();
            }


            try {
                contentValues.put(redeemdata_minordervalueforredeem, String.valueOf(modal_Pos_appMobileData.getRedeemdata_minordervalueforredeem()));

            } catch (Exception e) {
                e.printStackTrace();
            }


            try {
                contentValues.put(updateweightforonlineorders, String.valueOf(modal_Pos_appMobileData.getUpdateweightforonlineorders()));

            } catch (Exception e) {
                e.printStackTrace();
            }


            try {
                contentValues.put(redeemdata_pointsfor100rs, String.valueOf(modal_Pos_appMobileData.getRedeemdata_pointsfor100rs()));

            } catch (Exception e) {
                e.printStackTrace();
            }


            try {
                contentValues.put(enableorderplacingmicroservice, String.valueOf(modal_Pos_appMobileData.getEnableorderplacingmicroservice()));

            } catch (Exception e) {
                e.printStackTrace();
            }


            try {
                id = sqLiteDatabase.insert(database_Table, null, contentValues);

            }
            catch (Exception e){
                e.printStackTrace();
            }



        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }







}
