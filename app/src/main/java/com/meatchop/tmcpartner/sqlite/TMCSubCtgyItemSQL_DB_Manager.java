package com.meatchop.tmcpartner.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.meatchop.tmcpartner.Constants;
import com.meatchop.tmcpartner.settings.Modal_SubCtgyList;

public class TMCSubCtgyItemSQL_DB_Manager {

    DataBaseHelper dbHelper ;
    SQLiteDatabase sqLiteDatabase;
    Context context;


    public static  String database_Table = "TMCSubCtgy";

    public static final String key = "key";
    public static final String displayno = "displayno";
    public static final String imageurl = "imageurl";
    public static final String iosdisplayno = "iosdisplayno";
    public static final String iosimageheight = "iosimageheight";
    public static final String subctgyname = "subctgyname";
    public static final String tmcctgykey = "tmcctgykey";
    public static final String tmcctgyname = "tmcctgyname";
    public static final String localDB_id = "localDB_id";





    public static String CRETAE_08_QUERY = "CREATE TABLE " + database_Table +" ( "
            + localDB_id + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + displayno                       +" TEXT NOT NULL,"
            + subctgyname                     +" TEXT NOT NULL,"
            + tmcctgykey                      +" TEXT NOT NULL,"
            + key                             +" TEXT NOT NULL,"
            + tmcctgyname                     +" TEXT NOT NULL );";





    public TMCSubCtgyItemSQL_DB_Manager(Context context) {
        this.context = context;
    }






    public TMCSubCtgyItemSQL_DB_Manager open() throws SQLException {

        dbHelper = new DataBaseHelper(context, CRETAE_08_QUERY , database_Table );
        sqLiteDatabase = dbHelper.getWritableDatabase();
        dbHelper.onCreate(sqLiteDatabase);

        return this;
    }


    public void close(){

        dbHelper.close(sqLiteDatabase);
    }




    public Cursor FetchSingleItem(String fieldtoQuery, String valuetoQuery) {
        sqLiteDatabase = dbHelper.getWritableDatabase();

        Cursor cursor = sqLiteDatabase.query(database_Table, null, fieldtoQuery+" = ?", new String[] {valuetoQuery}, null, null, null);


        //   Cursor res =  sqLiteDatabase.rawQuery( "select * from TMCMenuItem where valuetoQuery="+itemuniquecode+"", null );
        return cursor;
    }



    public Cursor Fetch(){
        //String  [] columns = new String[] {localDB_id,menuItemId , itemName, tmcpriceperkg};
        Cursor cursor = sqLiteDatabase.query(database_Table,null,null,null,null,null,null);
        if(cursor != null ){

            cursor.moveToFirst();

        }

        return cursor;


    }

    public void dropTable( boolean isCreateNewTable){

        dbHelper.DropTable(sqLiteDatabase, database_Table,isCreateNewTable);

    }
    public int deleteTable( boolean isCreateNewTable){
        int i = sqLiteDatabase.delete(database_Table,null , null);
        Log.i("delete count ",String.valueOf(i));

        return i;


    }




    public void delete(long ID){

        sqLiteDatabase.delete(database_Table,localDB_id + "=" + ID , null);
    }



    public int update(Modal_SubCtgyList modal_subCtgyList) {
        ContentValues contentValues = new ContentValues();

        try {
            if (!modal_subCtgyList.getDisplayNo().equals("") && !String.valueOf(modal_subCtgyList.getDisplayNo()).toUpperCase().equals("NULL")) {
                if (String.valueOf(modal_subCtgyList.getDisplayNo()).toUpperCase().equals(Constants.Empty_Text)) {

                    contentValues.put(displayno, "");

                } else {

                    contentValues.put(displayno, modal_subCtgyList.getDisplayNo());


                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        try {
            if (!modal_subCtgyList.getSubCtgyName().equals("") && !String.valueOf(modal_subCtgyList.getSubCtgyName()).toUpperCase().equals("NULL")) {
                if (String.valueOf(modal_subCtgyList.getSubCtgyName()).toUpperCase().equals(Constants.Empty_Text)) {

                    contentValues.put(subctgyname, "");

                } else {

                    contentValues.put(subctgyname, modal_subCtgyList.getSubCtgyName());


                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        try {
            if (!modal_subCtgyList.getKey().equals("") && !String.valueOf(modal_subCtgyList.getKey()).toUpperCase().equals("NULL")) {
                if (String.valueOf(modal_subCtgyList.getKey()).toUpperCase().equals(Constants.Empty_Text)) {

                    contentValues.put(key, "");

                } else {

                    contentValues.put(key, modal_subCtgyList.getKey());


                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        try {
            if (!modal_subCtgyList.getLocalDB_id().equals("") && !String.valueOf(modal_subCtgyList.getLocalDB_id()).toUpperCase().equals("NULL")) {
                if (String.valueOf(modal_subCtgyList.getLocalDB_id()).toUpperCase().equals(Constants.Empty_Text)) {

                    contentValues.put(localDB_id, "");

                } else {

                    contentValues.put(localDB_id, modal_subCtgyList.getLocalDB_id());


                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        try {
            if (!modal_subCtgyList.getTmcctgykey().equals("") && !String.valueOf(modal_subCtgyList.getTmcctgykey()).toUpperCase().equals("NULL")) {
                if (String.valueOf(modal_subCtgyList.getTmcctgykey()).toUpperCase().equals(Constants.Empty_Text)) {

                    contentValues.put(tmcctgykey, "");

                } else {

                    contentValues.put(tmcctgykey, modal_subCtgyList.getTmcctgykey());


                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }



        try {
            if (!modal_subCtgyList.getTmcctgyname().equals("") && !String.valueOf(modal_subCtgyList.getTmcctgyname()).toUpperCase().equals("NULL")) {
                if (String.valueOf(modal_subCtgyList.getTmcctgyname()).toUpperCase().equals(Constants.Empty_Text)) {

                    contentValues.put(tmcctgyname, "");

                } else {

                    contentValues.put(tmcctgyname, modal_subCtgyList.getTmcctgyname());


                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        


        int response = sqLiteDatabase.update(database_Table, contentValues, localDB_id + "=" + modal_subCtgyList.getLocalDB_id(), null);
        return response;


    }


    public long insert(Modal_SubCtgyList modal_subCtgyList) {
        long id = 0;
        try {
            ContentValues contentValues = new ContentValues();
            try {
                contentValues.put(displayno, String.valueOf(modal_subCtgyList.getDisplayNo()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                contentValues.put(key, String.valueOf(modal_subCtgyList.getKey()));

            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                contentValues.put(tmcctgyname, String.valueOf(modal_subCtgyList.getTmcctgyname()));

            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                contentValues.put(tmcctgykey, String.valueOf(modal_subCtgyList.getTmcctgykey()));

            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                contentValues.put(subctgyname, String.valueOf(modal_subCtgyList.getSubCtgyName()));

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
