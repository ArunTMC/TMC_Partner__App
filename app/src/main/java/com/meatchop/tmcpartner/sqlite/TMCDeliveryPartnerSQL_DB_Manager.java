package com.meatchop.tmcpartner.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.meatchop.tmcpartner.Constants;
import com.meatchop.tmcpartner.settings.Modal_DeliveryPartner;

public class TMCDeliveryPartnerSQL_DB_Manager {

    DataBaseHelper dbHelper;
    SQLiteDatabase sqLiteDatabase;
    Context context;


    public static String database_Table = "TMCDeliveryUser";

    public static final String deliveryPartnerStatus = "deliveryPartnerStatus";
    public static final String deliveryPartnerKey = "deliveryPartnerKey";
    public static final String deliveryPartnerMobileNo = "deliveryPartnerMobileNo";
    public static final String deliveryPartnerName = "deliveryPartnerName";
    public static final String vendorkey = "vendorkey";
    public static final String localDB_id = "localDB_id";


    public static String CRETAE_08_QUERY = "CREATE TABLE " + database_Table + " ( "
            + localDB_id + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + deliveryPartnerStatus + " TEXT NOT NULL,"
            + deliveryPartnerKey + " TEXT NOT NULL,"
            + deliveryPartnerMobileNo + " TEXT NOT NULL,"
            + deliveryPartnerName + " TEXT NOT NULL,"
            + vendorkey + " TEXT NOT NULL );";


    public TMCDeliveryPartnerSQL_DB_Manager(Context context) {
        this.context = context;
    }


    public TMCDeliveryPartnerSQL_DB_Manager open() throws SQLException {
        dbHelper = new DataBaseHelper(context, CRETAE_08_QUERY, database_Table);
        sqLiteDatabase = dbHelper.getWritableDatabase();
        dbHelper.onCreate(sqLiteDatabase);

        return this;
    }


    public void close() {
        dbHelper.close();
    }


    public Cursor Fetch() {
        //String  [] columns = new String[] {localDB_id,menuItemId , itemName, tmcpriceperkg};
        Cursor cursor = sqLiteDatabase.query(database_Table, null, null, null, null, null, null);
        if (cursor != null) {

            cursor.moveToFirst();

        }

        return cursor;


    }


    public void dropTable(String tableName, boolean isCreateNewTable) {

        dbHelper.DropTable(sqLiteDatabase, tableName, isCreateNewTable);

    }


    public void delete(long ID) {

        sqLiteDatabase.delete(database_Table, localDB_id + "=" + ID, null);
    }


    public int update(Modal_DeliveryPartner modal_deliveryPartner) {
        ContentValues contentValues = new ContentValues();

        try {
            if (!modal_deliveryPartner.getDeliveryPartnerStatus().equals("") && !String.valueOf(modal_deliveryPartner.getDeliveryPartnerStatus()).toUpperCase().equals("NULL")) {
                if (String.valueOf(modal_deliveryPartner.getDeliveryPartnerStatus()).toUpperCase().equals(Constants.Empty_Text)) {

                    contentValues.put(deliveryPartnerStatus, "");

                } else {

                    contentValues.put(deliveryPartnerStatus, modal_deliveryPartner.getDeliveryPartnerStatus());


                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        try {
            if (!modal_deliveryPartner.getDeliveryPartnerKey().equals("") && !String.valueOf(modal_deliveryPartner.getDeliveryPartnerKey()).toUpperCase().equals("NULL")) {
                if (String.valueOf(modal_deliveryPartner.getDeliveryPartnerKey()).toUpperCase().equals(Constants.Empty_Text)) {

                    contentValues.put(deliveryPartnerKey, "");

                } else {

                    contentValues.put(deliveryPartnerKey, modal_deliveryPartner.getDeliveryPartnerKey());


                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        try {
            if (!modal_deliveryPartner.getDeliveryPartnerMobileNo().equals("") && !String.valueOf(modal_deliveryPartner.getDeliveryPartnerMobileNo()).toUpperCase().equals("NULL")) {
                if (String.valueOf(modal_deliveryPartner.getDeliveryPartnerMobileNo()).toUpperCase().equals(Constants.Empty_Text)) {

                    contentValues.put(deliveryPartnerMobileNo, "");

                } else {

                    contentValues.put(deliveryPartnerMobileNo, modal_deliveryPartner.getDeliveryPartnerMobileNo());


                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        try {
            if (!modal_deliveryPartner.getDeliveryPartnerName().equals("") && !String.valueOf(modal_deliveryPartner.getDeliveryPartnerName()).toUpperCase().equals("NULL")) {
                if (String.valueOf(modal_deliveryPartner.getDeliveryPartnerName()).toUpperCase().equals(Constants.Empty_Text)) {

                    contentValues.put(deliveryPartnerName, "");

                } else {

                    contentValues.put(deliveryPartnerName, modal_deliveryPartner.getDeliveryPartnerName());


                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        try {
            if (!modal_deliveryPartner.getVendorkey().equals("") && !String.valueOf(modal_deliveryPartner.getVendorkey()).toUpperCase().equals("NULL")) {
                if (String.valueOf(modal_deliveryPartner.getVendorkey()).toUpperCase().equals(Constants.Empty_Text)) {

                    contentValues.put(vendorkey, "");

                } else {

                    contentValues.put(vendorkey, modal_deliveryPartner.getVendorkey());


                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        int response = sqLiteDatabase.update(database_Table, contentValues, localDB_id + "=" + modal_deliveryPartner.getLocalDB_id(), null);
        return response;


    }


    public long insert(Modal_DeliveryPartner modal_deliveryPartner) {
        long id = 0;
        try {
            ContentValues contentValues = new ContentValues();
            try {
                contentValues.put(vendorkey, String.valueOf(modal_deliveryPartner.getVendorkey()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                contentValues.put(deliveryPartnerName, String.valueOf(modal_deliveryPartner.getDeliveryPartnerName()));

            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                contentValues.put(deliveryPartnerMobileNo, String.valueOf(modal_deliveryPartner.getDeliveryPartnerMobileNo()));

            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                contentValues.put(deliveryPartnerKey, String.valueOf(modal_deliveryPartner.getDeliveryPartnerKey()));

            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                contentValues.put(deliveryPartnerStatus, String.valueOf(modal_deliveryPartner.getDeliveryPartnerStatus()));

            } catch (Exception e) {
                e.printStackTrace();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }


}
