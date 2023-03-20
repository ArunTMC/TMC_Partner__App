package com.meatchop.tmcpartner.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.meatchop.tmcpartner.Constants;
import com.meatchop.tmcpartner.posscreen_javaclasses.other_java_classes.Modal_vendor;

public class VendorSQL_DB_Manager {

    DataBaseHelper dbHelper;
    SQLiteDatabase sqLiteDatabase;
    Context context;

    public static final String pincode = "pincode", localDB_id = "localDB_id", addressline1 = "addressline1", addressline2 = "addressline2", vendortype = "vendortype", vendormobile = "vendormobile",
            status = "status", key = "key", inventorycheck = "inventorycheck", istestvendor = "istestvendor", vendorname = "vendorname", localdbcheck = "localdbcheck" , isweightmachineconnected ="isweightmachineconnected";
    public static final String defaultprintertype = "defaultprintertype", vendorfssaino = "vendorfssaino", locationlat = "locationlat", locationlong = "locationlong", inventorycheckpos = "inventorycheckpos", minimumscreensizeforpos = "minimumscreensizeforpos"
            ,isbarcodescannerconnected  = "isbarcodescannerconnected";


    public static String database_Table = "Vendor";


    public static String CRETAE_08_QUERY = "CREATE TABLE " + database_Table + " ( "
            + localDB_id + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + pincode + " TEXT NOT NULL,"
            + addressline1 + " TEXT NOT NULL,"
            + addressline2 + " TEXT NOT NULL,"
            + vendortype + " TEXT NOT NULL,"
            + status + " TEXT NOT NULL,"
            + key + " TEXT NOT NULL,"
            + inventorycheck + " TEXT NOT NULL,"
            + istestvendor + " TEXT NOT NULL,"
            + vendorname + " TEXT NOT NULL,"
            + localdbcheck + " TEXT NOT NULL,"
            + defaultprintertype + " TEXT NOT NULL,"
            + vendorfssaino + " TEXT NOT NULL,"
            + locationlat + " TEXT NOT NULL,"
            + locationlong + " TEXT NOT NULL,"
            + inventorycheckpos + " TEXT NOT NULL,"
            + minimumscreensizeforpos + " TEXT NOT NULL,"
            + isweightmachineconnected + " TEXT NOT NULL,"
            + isbarcodescannerconnected + " TEXT NOT NULL,"
            + vendormobile + " TEXT NOT NULL );";


    public VendorSQL_DB_Manager(Context context) {
        this.context = context;
    }


    public VendorSQL_DB_Manager open() throws SQLException {
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

    public void close() {

        dbHelper.close(sqLiteDatabase);
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



    public int update(Modal_vendor modalVendor) {
        ContentValues contentValues = new ContentValues();

        try {
            if (!modalVendor.getPincode().equals("") && !String.valueOf(modalVendor.getPincode()).toUpperCase().equals("NULL")) {
                if (String.valueOf(modalVendor.getPincode()).toUpperCase().equals(Constants.Empty_Text)) {

                    contentValues.put(pincode, "");

                } else {

                    contentValues.put(pincode, modalVendor.getPincode());


                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        try {
            if (!modalVendor.getAddressline1().equals("") && !String.valueOf(modalVendor.getAddressline1()).toUpperCase().equals("NULL")) {
                if (String.valueOf(modalVendor.getAddressline1()).toUpperCase().equals(Constants.Empty_Text)) {

                    contentValues.put(addressline1, "");

                } else {

                    contentValues.put(addressline1, modalVendor.getAddressline1());


                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        try {
            if (!modalVendor.getAddressline2().equals("") && !String.valueOf(modalVendor.getAddressline2()).toUpperCase().equals("NULL")) {
                if (String.valueOf(modalVendor.getAddressline2()).toUpperCase().equals(Constants.Empty_Text)) {

                    contentValues.put(addressline2, "");

                } else {

                    contentValues.put(addressline2, modalVendor.getAddressline2());


                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        try {
            if (!modalVendor.getVendortype().equals("") && !String.valueOf(modalVendor.getVendortype()).toUpperCase().equals("NULL")) {
                if (String.valueOf(modalVendor.getVendortype()).toUpperCase().equals(Constants.Empty_Text)) {

                    contentValues.put(vendortype, "");

                } else {

                    contentValues.put(vendortype, modalVendor.getVendortype());


                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        try {
            if (!modalVendor.getVendormobile().equals("") && !String.valueOf(modalVendor.getVendormobile()).toUpperCase().equals("NULL")) {
                if (String.valueOf(modalVendor.getVendormobile()).toUpperCase().equals(Constants.Empty_Text)) {

                    contentValues.put(vendormobile, "");

                } else {

                    contentValues.put(vendormobile, modalVendor.getVendormobile());


                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }



        try {
            if (!modalVendor.getKey().equals("") && !String.valueOf(modalVendor.getKey()).toUpperCase().equals("NULL")) {
                if (String.valueOf(modalVendor.getKey()).toUpperCase().equals(Constants.Empty_Text)) {

                    contentValues.put(key, "");

                } else {

                    contentValues.put(key, modalVendor.getKey());


                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        try {
            if (!modalVendor.getInventorycheck().equals("") && !String.valueOf(modalVendor.getInventorycheck()).toUpperCase().equals("NULL")) {
                if (String.valueOf(modalVendor.getInventorycheck()).toUpperCase().equals(Constants.Empty_Text)) {

                    contentValues.put(inventorycheck, "");

                } else {

                    contentValues.put(inventorycheck, modalVendor.getInventorycheck());


                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        try {
            if (!modalVendor.getIstestvendor().equals("") && !String.valueOf(modalVendor.getIstestvendor()).toUpperCase().equals("NULL")) {
                if (String.valueOf(modalVendor.getIstestvendor()).toUpperCase().equals(Constants.Empty_Text)) {

                    contentValues.put(istestvendor, "");

                } else {

                    contentValues.put(istestvendor, modalVendor.getIstestvendor());


                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }



        try {
            if (!modalVendor.getVendorname().equals("") && !String.valueOf(modalVendor.getVendorname()).toUpperCase().equals("NULL")) {
                if (String.valueOf(modalVendor.getVendorname()).toUpperCase().equals(Constants.Empty_Text)) {

                    contentValues.put(vendorname, "");

                } else {

                    contentValues.put(vendorname, modalVendor.getVendorname());


                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }




        try {
            if (!modalVendor.getLocaldbcheck().equals("") && !String.valueOf(modalVendor.getLocaldbcheck()).toUpperCase().equals("NULL")) {
                if (String.valueOf(modalVendor.getLocaldbcheck()).toUpperCase().equals(Constants.Empty_Text)) {

                    contentValues.put(localdbcheck, "");

                } else {

                    contentValues.put(localdbcheck, modalVendor.getLocaldbcheck());


                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }



        try {
            if (!modalVendor.getDefaultprintertype().equals("") && !String.valueOf(modalVendor.getDefaultprintertype()).toUpperCase().equals("NULL")) {
                if (String.valueOf(modalVendor.getDefaultprintertype()).toUpperCase().equals(Constants.Empty_Text)) {

                    contentValues.put(defaultprintertype, "");

                } else {

                    contentValues.put(defaultprintertype, modalVendor.getDefaultprintertype());


                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        try {
            if (!modalVendor.getVendorfssaino().equals("") && !String.valueOf(modalVendor.getVendorfssaino()).toUpperCase().equals("NULL")) {
                if (String.valueOf(modalVendor.getVendorfssaino()).toUpperCase().equals(Constants.Empty_Text)) {

                    contentValues.put(vendorfssaino, "");

                } else {

                    contentValues.put(vendorfssaino, modalVendor.getVendorfssaino());


                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if (!modalVendor.getLocationlat().equals("") && !String.valueOf(modalVendor.getLocationlat()).toUpperCase().equals("NULL")) {
                if (String.valueOf(modalVendor.getLocationlat()).toUpperCase().equals(Constants.Empty_Text)) {

                    contentValues.put(locationlat, "");

                } else {

                    contentValues.put(locationlat, modalVendor.getLocationlat());


                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }



        try {
            if (!modalVendor.getLocationlong().equals("") && !String.valueOf(modalVendor.getLocationlong()).toUpperCase().equals("NULL")) {
                if (String.valueOf(modalVendor.getLocationlong()).toUpperCase().equals(Constants.Empty_Text)) {

                    contentValues.put(locationlong, "");

                } else {

                    contentValues.put(locationlong, modalVendor.getLocationlong());


                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }



        try {
            if (!modalVendor.getInventorycheckpos().equals("") && !String.valueOf(modalVendor.getInventorycheckpos()).toUpperCase().equals("NULL")) {
                if (String.valueOf(modalVendor.getInventorycheckpos()).toUpperCase().equals(Constants.Empty_Text)) {

                    contentValues.put(inventorycheckpos, "");

                } else {

                    contentValues.put(inventorycheckpos, modalVendor.getInventorycheckpos());


                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        try {
            if (!modalVendor.getMinimumscreensizeforpos().equals("") && !String.valueOf(modalVendor.getMinimumscreensizeforpos()).toUpperCase().equals("NULL")) {
                if (String.valueOf(modalVendor.getMinimumscreensizeforpos()).toUpperCase().equals(Constants.Empty_Text)) {

                    contentValues.put(minimumscreensizeforpos, "");

                } else {

                    contentValues.put(minimumscreensizeforpos, modalVendor.getMinimumscreensizeforpos());


                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }



        try {
            if (!modalVendor.getIsweightmachineconnected().equals("") && !String.valueOf(modalVendor.getIsweightmachineconnected()).toUpperCase().equals("NULL")) {
                if (String.valueOf(modalVendor.getIsweightmachineconnected()).toUpperCase().equals(Constants.Empty_Text)) {

                    contentValues.put(isweightmachineconnected, "");

                } else {

                    contentValues.put(isweightmachineconnected, modalVendor.getIsweightmachineconnected());


                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }





        try {
            if (!modalVendor.getIsbarcodescannerconnected().equals("") && !String.valueOf(modalVendor.getIsbarcodescannerconnected()).toUpperCase().equals("NULL")) {
                if (String.valueOf(modalVendor.getIsbarcodescannerconnected()).toUpperCase().equals(Constants.Empty_Text)) {

                    contentValues.put(isbarcodescannerconnected, "");

                } else {

                    contentValues.put(isbarcodescannerconnected, modalVendor.getIsbarcodescannerconnected());


                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }




        int response = sqLiteDatabase.update(database_Table, contentValues, localDB_id + "=" + modalVendor.getLocalDB_id(), null);
        return response;


    }


    public long insert(Modal_vendor modalVendor) {
        long id = 0;
        try {
            ContentValues contentValues = new ContentValues();
            try {
                contentValues.put(pincode, String.valueOf(modalVendor.getPincode()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                contentValues.put(addressline1, String.valueOf(modalVendor.getAddressline1()));

            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                contentValues.put(addressline2, String.valueOf(modalVendor.getAddressline2()));

            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                contentValues.put(vendortype, String.valueOf(modalVendor.getVendortype()));

            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                contentValues.put(vendormobile, String.valueOf(modalVendor.getVendormobile()));

            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                contentValues.put(minimumscreensizeforpos, String.valueOf(modalVendor.getMinimumscreensizeforpos()));

            } catch (Exception e) {
                e.printStackTrace();
            }


            try {
                contentValues.put(status, String.valueOf(modalVendor.getStatus()));

            } catch (Exception e) {
                e.printStackTrace();
            }


            try {
                contentValues.put(key, String.valueOf(modalVendor.getKey()));

            } catch (Exception e) {
                e.printStackTrace();
            }


            try {
                contentValues.put(inventorycheck, String.valueOf(modalVendor.getInventorycheck()));

            } catch (Exception e) {
                e.printStackTrace();
            }



            try {
                contentValues.put(istestvendor, String.valueOf(modalVendor.getIstestvendor()));

            } catch (Exception e) {
                e.printStackTrace();
            }



            try {
                contentValues.put(vendorname, String.valueOf(modalVendor.getVendorname()));

            } catch (Exception e) {
                e.printStackTrace();
            }


            try {
                contentValues.put(localdbcheck, String.valueOf(modalVendor.getLocaldbcheck()));

            } catch (Exception e) {
                e.printStackTrace();
            }


            try {
                contentValues.put(defaultprintertype, String.valueOf(modalVendor.getDefaultprintertype()));

            } catch (Exception e) {
                e.printStackTrace();
            }


            try {
                contentValues.put(vendorfssaino, String.valueOf(modalVendor.getVendorfssaino()));

            } catch (Exception e) {
                e.printStackTrace();
            }


            try {
                contentValues.put(locationlat, String.valueOf(modalVendor.getLocationlat()));

            } catch (Exception e) {
                e.printStackTrace();
            }


            try {
                contentValues.put(locationlong, String.valueOf(modalVendor.getLocationlong()));

            } catch (Exception e) {
                e.printStackTrace();
            }


            try {
                contentValues.put(inventorycheckpos, String.valueOf(modalVendor.getInventorycheckpos()));

            } catch (Exception e) {
                e.printStackTrace();
            }



            try {
                contentValues.put(minimumscreensizeforpos, String.valueOf(modalVendor.getMinimumscreensizeforpos()));

            } catch (Exception e) {
                e.printStackTrace();
            }


            try {
                contentValues.put(isweightmachineconnected, String.valueOf(modalVendor.getIsweightmachineconnected()));

            } catch (Exception e) {
                e.printStackTrace();
            }





            try {
                contentValues.put(isbarcodescannerconnected, String.valueOf(modalVendor.getIsbarcodescannerconnected()));

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