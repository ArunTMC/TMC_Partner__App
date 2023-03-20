package com.meatchop.tmcpartner.sqlite;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.meatchop.tmcpartner.Constants;

import java.util.Objects;

public class DataBaseHelper extends SQLiteOpenHelper  {

   public static final String database_Name = "TMCPartnerApp.DB";
   public static int Database_Version = 9;
   public static  String database_Table = "d" ,CRETAE_08_QUERY ="";





    public DataBaseHelper(@Nullable Context context , String CRETAE_DB_QUERY , String tableName) {
        super(context, database_Name, null, Database_Version);
            this.CRETAE_08_QUERY = CRETAE_DB_QUERY;
            this.database_Table = tableName;
    }

    public synchronized void close (SQLiteDatabase sqLiteDatabase) {
        if (sqLiteDatabase != null) {
            sqLiteDatabase.close();
            super.close();
        }
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        @SuppressLint("Recycle") Cursor cursor = sqLiteDatabase.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
        boolean isTableAlreadyCreated = false;
        if(cursor != null ){

            cursor.moveToFirst();

        }
        if (Objects.requireNonNull(cursor).moveToFirst()) {
            while (!cursor.isAfterLast()) {
                if( cursor.getString(0).equals(database_Table)){
                    isTableAlreadyCreated = true;
                }
                cursor.moveToNext();
            }

        }
        if(!isTableAlreadyCreated){
            sqLiteDatabase.execSQL(CRETAE_08_QUERY);
        }
        else{
        //    DropTable(sqLiteDatabase,database_Table,true);
        }



    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL( "DROP TABLE IF EXISTS " + database_Table);



    }

    public void DropTable(SQLiteDatabase sqLiteDatabase, String tableName, boolean isCreateNewTable) {
       sqLiteDatabase.execSQL( "DROP TABLE IF EXISTS " + tableName);
        if(isCreateNewTable){
            sqLiteDatabase.execSQL(CRETAE_08_QUERY);
        }

    }
}
