package com.meatchop.tmcpartner.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.meatchop.tmcpartner.Constants;
import com.meatchop.tmcpartner.posscreen_javaclasses.other_java_classes.Modal_MenuItem;

public class TMCMenuItemSQL_DB_Manager {

    DataBaseHelper dbHelper ;
    SQLiteDatabase sqLiteDatabase;
    Context context;


    public static  String database_Table = "TMCMenuItem";

    public static final String key = "key";
    public static final String itemName = "name";
    public static final String tmcPrice = "tmcprice";
    public static final String applieddiscountpercentage = "applieddiscountpercentage";
    public static final String barcode = "barcode";
    public static final String checkoutimageurl = "checkoutimageurl";
    public static final String displayno = "displayno";
    public static final String grossweight = "grossweight";
    public static final String gstpercentage = "gstpercentage";
    public static final String imageurl = "imageurl";
    public static final String itemavailability = "itemavailability";
    public static final String itemcalories = "itemcalories";
    public static final String itemuniquecode = "itemuniquecode";
    public static final String marinadelinkedcodes = "marinadelinkedcodes";
    public static final String menuboarddisplayname = "menuboarddisplayname";
    public static final String menutype = "menutype";
    public static final String netweight = "netweight";
    public static final String portionsize = "portionsize";
    public static final String pricetypeforpos = "pricetypeforpos";
    public static final String showinmenuboard = "showinmenuboard";
    public static final String tmcctgykey = "tmcctgykey";
    public static final String tmcpriceperkg = "tmcpriceperkg";
    public static final String tmcsubctgykey = "tmcsubctgykey";
    public static final String vendorkey = "vendorkey";
    public static final String vendorname = "vendorname";
    public static final String menuItemId = "menuItemId";
    public static final String grossweightingrams = "grossweightingrams";
    public static final String reportname = "reportname";
    public static final String swiggyprice = "swiggyprice";
    public static final String dunzoprice = "dunzoprice";
    public static final String bigbasketprice = "bigbasketprice";
    public static final String wholesaleprice = "wholesaleprice";
    public static final String localDB_id = "localDB_id";
    public static final String appmarkuppercentage = "appmarkuppercentage";
    public static final String tmcpriceperkgWithMarkupValue = "tmcpriceperkgWithMarkupValue";
    public static final String tmcpriceWithMarkupValue = "tmcpriceWithMarkupValue";
    public static final String barcode_AvlDetails = "barcode_AvlDetails";
    public static final String itemavailability_AvlDetails = "itemavailability_AvlDetails";
    public static final String key_AvlDetails = "key_AvlDetails";
    public static final String lastupdatedtime_AvlDetails = "lastupdatedtime_AvlDetails";
    public static final String menuItemId_AvlDetails = "menuItemId_AvlDetails";
    public static final String receivedStock_AvlDetails = "receivedStock_AvlDetails";
    public static final String stockBalance_AvlDetails = "stockBalance_AvlDetails";
    public static final String stockIncomingKey_AvlDetails = "stockIncomingKey_AvlDetails";
    public static final String vendorkey_AvlDetails = "vendorkey_AvlDetails";
    public static final String allowNegativeStock_AvlDetails = "allowNegativeStock_AvlDetails";
    public static final String inventoryDetails = "inventoryDetails";


    public static String CRETAE_08_QUERY = "CREATE TABLE " + database_Table +" ( "
            + localDB_id + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + itemName                        +" TEXT NOT NULL,"
            + applieddiscountpercentage       +" TEXT NOT NULL,"
            + barcode                         +" TEXT NOT NULL,"
            + checkoutimageurl                +" TEXT NOT NULL,"
            + displayno                       +" TEXT NOT NULL,"
            + grossweight                     +" TEXT NOT NULL,"
            + gstpercentage                   +" TEXT NOT NULL,"
            + imageurl                        +" TEXT NOT NULL,"
            + itemavailability                +" TEXT NOT NULL,"
            + itemcalories                    +" TEXT NOT NULL,"
            + itemuniquecode                  +" TEXT NOT NULL,"
            + marinadelinkedcodes             +" TEXT NOT NULL,"
            + menuboarddisplayname            +" TEXT NOT NULL,"
            + menutype                        +" TEXT NOT NULL,"
            + netweight                       +" TEXT NOT NULL,"
            + portionsize                     +" TEXT NOT NULL,"
            + pricetypeforpos                 +" TEXT NOT NULL,"
            + showinmenuboard                 +" TEXT NOT NULL,"
            + tmcctgykey                      +" TEXT NOT NULL,"
            + tmcpriceperkg                   +" TEXT NOT NULL,"
            + tmcPrice                        +" TEXT NOT NULL,"
            + tmcsubctgykey                   +" TEXT NOT NULL,"
            + vendorkey                       +" TEXT NOT NULL,"
            + vendorname                      +" TEXT NOT NULL,"
            + menuItemId                      +" TEXT NOT NULL,"
            + grossweightingrams              +" TEXT NOT NULL,"
            + reportname                      +" TEXT NOT NULL,"
            + swiggyprice                     +" TEXT NOT NULL,"
            + dunzoprice                      +" TEXT NOT NULL,"
            + bigbasketprice                  +" TEXT NOT NULL,"
            + wholesaleprice                  +" TEXT NOT NULL,"
            + appmarkuppercentage             +" TEXT NOT NULL,"
            + tmcpriceperkgWithMarkupValue    +" TEXT NOT NULL,"
            + tmcpriceWithMarkupValue         +" TEXT NOT NULL,"
            + barcode_AvlDetails              +" TEXT NOT NULL,"
            + itemavailability_AvlDetails     +" TEXT NOT NULL,"
            + key_AvlDetails                  +" TEXT NOT NULL,"
            + lastupdatedtime_AvlDetails      +" TEXT NOT NULL,"
            + menuItemId_AvlDetails           +" TEXT NOT NULL,"
            + receivedStock_AvlDetails        +" TEXT NOT NULL,"
            + stockBalance_AvlDetails         +" TEXT NOT NULL,"
            + stockIncomingKey_AvlDetails     +" TEXT NOT NULL,"
            + vendorkey_AvlDetails            +" TEXT NOT NULL,"
            + inventoryDetails                +" TEXT NOT NULL,"
            + allowNegativeStock_AvlDetails   +" TEXT NOT NULL );";





    public TMCMenuItemSQL_DB_Manager(Context context) {
        this.context = context;
    }






    public TMCMenuItemSQL_DB_Manager open() throws SQLException {

        dbHelper = new DataBaseHelper(context, CRETAE_08_QUERY , database_Table );
        sqLiteDatabase = dbHelper.getWritableDatabase();
        dbHelper.onCreate(sqLiteDatabase);

        return this;
    }


    public void close(){
        dbHelper.close(sqLiteDatabase);
    }

    public long insert(String menuItemKey, String itemName , String itemPrice ){
        ContentValues contentValues = new ContentValues();
        contentValues.put(menuItemId,menuItemKey);
        contentValues.put(itemName,itemName);
        contentValues.put(tmcpriceperkg,itemPrice);
        long id = sqLiteDatabase.insert(database_Table,null,contentValues);
        return id ;
    }


    public Cursor FetchSingleItem(String fieldtoQuery,String valuetoQuery) {
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

    public Cursor FetchAllTable() {
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);

        if(cursor != null ){

            cursor.moveToFirst();

        }
        return cursor;
    }

    public int update(long ID , String  menuItemKey , String itemName , String  itemPrice) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(menuItemId, menuItemKey);
        contentValues.put(itemName, itemName);
        contentValues.put(tmcpriceperkg, itemPrice);
        int response = sqLiteDatabase.update(database_Table, contentValues, localDB_id + "=" + ID, null);
        return response;

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




    public int update(Modal_MenuItem modal_menuItem) {
        ContentValues contentValues = new ContentValues();

        try{
            if(!modal_menuItem.getItemname().equals("") && !(String.valueOf(modal_menuItem.getItemname()).toUpperCase().equals("NULL"))){
                if(String.valueOf(modal_menuItem.getItemname()).toUpperCase().equals(Constants.Empty_Text)){

                    contentValues.put(itemName                    , "");

                }
                else{

                    contentValues.put(itemName                    , modal_menuItem.getItemname());


                }
            }

        }
        catch (Exception e){

            e.printStackTrace();
        }



        try{
            if(!modal_menuItem.getApplieddiscountpercentage().equals("") && !String.valueOf(modal_menuItem.getApplieddiscountpercentage()).toUpperCase().equals("NULL")){
                if(String.valueOf(modal_menuItem.getApplieddiscountpercentage()).toUpperCase().equals(Constants.Empty_Text)){

                    contentValues.put(applieddiscountpercentage                    , "");

                }
                else{

                    contentValues.put(applieddiscountpercentage   , modal_menuItem.getApplieddiscountpercentage());


                }
            }

        }
        catch (Exception e){
            e.printStackTrace();
        }


        try{
            if(!modal_menuItem.getBarcode().equals("") && !String.valueOf(modal_menuItem.getBarcode()).toUpperCase().equals("NULL")){
                if(String.valueOf(modal_menuItem.getBarcode()).toUpperCase().equals(Constants.Empty_Text)){

                    contentValues.put(barcode                    , "");

                }
                else{

                    contentValues.put(barcode                     , modal_menuItem.getBarcode());


                }
            }

        }
        catch (Exception e){
            e.printStackTrace();
        }


        try{
            if(!modal_menuItem.getCheckoutimageurl().equals("") && !String.valueOf(modal_menuItem.getCheckoutimageurl()).toUpperCase().equals("NULL")){
                if(String.valueOf(modal_menuItem.getCheckoutimageurl()).toUpperCase().equals(Constants.Empty_Text)){

                    contentValues.put(checkoutimageurl                    , "");

                }
                else{

                    contentValues.put(checkoutimageurl            , modal_menuItem.getCheckoutimageurl());


                }
            }

        }
        catch (Exception e){
            e.printStackTrace();
        }



        try{
            if(!modal_menuItem.getMenuboarddisplayname().equals("") && !String.valueOf(modal_menuItem.getMenuboarddisplayname()).toUpperCase().equals("NULL")){
                if(String.valueOf(modal_menuItem.getMenuboarddisplayname()).toUpperCase().equals(Constants.Empty_Text)){

                    contentValues.put(menuboarddisplayname                    , "");

                }
                else{

                    contentValues.put(menuboarddisplayname        , modal_menuItem.getMenuboarddisplayname());


                }
            }

        }
        catch (Exception e){
            e.printStackTrace();
        }



        try{
            if(!modal_menuItem.getGrossweight().equals("") && !String.valueOf(modal_menuItem.getGrossweight()).toUpperCase().equals("NULL")){
                if(String.valueOf(modal_menuItem.getGrossweight()).toUpperCase().equals(Constants.Empty_Text)){

                    contentValues.put(grossweight                    , "");

                }
                else{

                    contentValues.put(grossweight                 , modal_menuItem.getGrossweight());


                }
            }

        }
        catch (Exception e){
            e.printStackTrace();
        }


        try{
            if(!modal_menuItem.getGstpercentage().equals("") && !String.valueOf(modal_menuItem.getGstpercentage()).toUpperCase().equals("NULL")){
                if(String.valueOf(modal_menuItem.getGstpercentage()).toUpperCase().equals(Constants.Empty_Text)){

                    contentValues.put(gstpercentage                    , "");

                }
                else{

                    contentValues.put(gstpercentage               , modal_menuItem.getGstpercentage());


                }
            }

        }
        catch (Exception e){
            e.printStackTrace();
        }

        try{
            if(!modal_menuItem.getImageurl().equals("") && !String.valueOf(modal_menuItem.getImageurl()).toUpperCase().equals("NULL")){
                if(String.valueOf(modal_menuItem.getImageurl()).toUpperCase().equals(Constants.Empty_Text)){

                    contentValues.put(imageurl                    , "");

                }
                else{

                    contentValues.put(imageurl                    , modal_menuItem.getImageurl());


                }
            }

        }
        catch (Exception e){
            e.printStackTrace();
        }
        try{
            if(!modal_menuItem.getItemavailability().equals("") && !String.valueOf(modal_menuItem.getItemavailability()).toUpperCase().equals("NULL")){
                if(String.valueOf(modal_menuItem.getItemavailability()).toUpperCase().equals(Constants.Empty_Text)){

                    contentValues.put(itemavailability                    , "");

                }
                else{

                    contentValues.put(itemavailability            , modal_menuItem.getItemavailability());


                }
            }

        }
        catch (Exception e){
            e.printStackTrace();
        }  try{
            if(!modal_menuItem.getItemcalories().equals("") && !String.valueOf(modal_menuItem.getItemcalories()).toUpperCase().equals("NULL")){
                if(String.valueOf(modal_menuItem.getItemcalories()).toUpperCase().equals(Constants.Empty_Text)){

                    contentValues.put(itemcalories                    , "");

                }
                else{

                    contentValues.put(itemcalories                , modal_menuItem.getItemcalories());


                }
            }

        }
        catch (Exception e){
            e.printStackTrace();
        }


        try{
            if(!modal_menuItem.getItemuniquecode().equals("") && !String.valueOf(modal_menuItem.getItemuniquecode()).toUpperCase().equals("NULL")){
                if(String.valueOf(modal_menuItem.getItemuniquecode()).toUpperCase().equals(Constants.Empty_Text)){

                    contentValues.put(itemuniquecode                    , "");

                }
                else{

                    contentValues.put(itemuniquecode              , modal_menuItem.getItemuniquecode());


                }
            }

        }
        catch (Exception e){
            e.printStackTrace();
        }


        try{
            if(!modal_menuItem.getMarinadelinkedcodes().equals("") && !String.valueOf(modal_menuItem.getMarinadelinkedcodes()).toUpperCase().equals("NULL")){
                if(String.valueOf(modal_menuItem.getMarinadelinkedcodes()).toUpperCase().equals(Constants.Empty_Text)){

                    contentValues.put(marinadelinkedcodes                    , "");

                }
                else{

                    contentValues.put(marinadelinkedcodes         , modal_menuItem.getMarinadelinkedcodes());


                }
            }

        }
        catch (Exception e){
            e.printStackTrace();
        }


        try{
            if(!modal_menuItem.getMenutype().equals("") && !String.valueOf(modal_menuItem.getMenutype()).toUpperCase().equals("NULL")){
                if(String.valueOf(modal_menuItem.getMenutype()).toUpperCase().equals(Constants.Empty_Text)){

                    contentValues.put(menutype                    , "");

                }
                else{

                    contentValues.put(menutype                    , modal_menuItem.getMenutype());


                }
            }

        }
        catch (Exception e){
            e.printStackTrace();
        }


        try{
            if(!modal_menuItem.getNetweight().equals("") && !String.valueOf(modal_menuItem.getNetweight()).toUpperCase().equals("NULL")){
                if(String.valueOf(modal_menuItem.getNetweight()).toUpperCase().equals(Constants.Empty_Text)){

                    contentValues.put(netweight                    , "");

                }
                else{

                    contentValues.put(netweight                   , modal_menuItem.getNetweight());



                }
            }

        }
        catch (Exception e){
            e.printStackTrace();
        }


        try{
            if(!modal_menuItem.getPortionsize().equals("") && !String.valueOf(modal_menuItem.getPortionsize()).toUpperCase().equals("NULL")){
                if(String.valueOf(modal_menuItem.getPortionsize()).toUpperCase().equals(Constants.Empty_Text)){

                    contentValues.put(portionsize                    , "");

                }
                else{

                    contentValues.put(portionsize                 , modal_menuItem.getPortionsize());


                }
            }

        }
        catch (Exception e){
            e.printStackTrace();
        }


        try{
            if(!modal_menuItem.getPricetypeforpos().equals("") && !String.valueOf(modal_menuItem.getPricetypeforpos()).toUpperCase().equals("NULL")){
                if(String.valueOf(modal_menuItem.getPricetypeforpos()).toUpperCase().equals(Constants.Empty_Text)){

                    contentValues.put(pricetypeforpos                    , "");

                }
                else{

                    contentValues.put(pricetypeforpos             , modal_menuItem.getPricetypeforpos());



                }
            }

        }
        catch (Exception e){
            e.printStackTrace();
        }

        try{
            if(!modal_menuItem.getShowinmenuboard().equals("") && !String.valueOf(modal_menuItem.getShowinmenuboard()).toUpperCase().equals("NULL")){
                if(String.valueOf(modal_menuItem.getShowinmenuboard()).toUpperCase().equals(Constants.Empty_Text)){

                    contentValues.put(showinmenuboard                    , "");

                }
                else{

                    contentValues.put(showinmenuboard             , modal_menuItem.getShowinmenuboard());


                }
            }

        }
        catch (Exception e){
            e.printStackTrace();
        }


        try{
            if(!modal_menuItem.getTmcctgykey().equals("") && !String.valueOf(modal_menuItem.getTmcctgykey()).toUpperCase().equals("NULL")){
                if(String.valueOf(modal_menuItem.getTmcctgykey()).toUpperCase().equals(Constants.Empty_Text)){

                    contentValues.put(tmcctgykey                    , "");

                }
                else{

                    contentValues.put(tmcctgykey                  , modal_menuItem.getTmcctgykey());


                }
            }

        }
        catch (Exception e){
            e.printStackTrace();
        }


        try{
            if(!modal_menuItem.getTmcpriceperkg().equals("") && !String.valueOf(modal_menuItem.getTmcpriceperkg()).toUpperCase().equals("NULL")){
                if(String.valueOf(modal_menuItem.getTmcpriceperkg()).toUpperCase().equals(Constants.Empty_Text)){

                    contentValues.put(tmcpriceperkg                    , "");

                }
                else{

                    contentValues.put(tmcpriceperkg               , modal_menuItem.getTmcpriceperkg());


                }
            }

        }
        catch (Exception e){
            e.printStackTrace();
        }

        try{
            if(!modal_menuItem.getTmcprice().equals("") && !String.valueOf(modal_menuItem.getTmcprice()).toUpperCase().equals("NULL")){
                if(String.valueOf(modal_menuItem.getTmcprice()).toUpperCase().equals(Constants.Empty_Text)){

                    contentValues.put(tmcPrice                    , "");

                }
                else{

                    contentValues.put(tmcPrice                    , modal_menuItem.getTmcprice());


                }
            }

        }
        catch (Exception e){
            e.printStackTrace();
        }

        try{
            if(!modal_menuItem.getTmcsubctgykey().equals("") && !String.valueOf(modal_menuItem.getTmcsubctgykey()).toUpperCase().equals("NULL")){
                if(String.valueOf(modal_menuItem.getTmcsubctgykey()).toUpperCase().equals(Constants.Empty_Text)){

                    contentValues.put(tmcsubctgykey                    , "");

                }
                else{

                    contentValues.put(tmcsubctgykey               , modal_menuItem.getTmcsubctgykey());


                }
            }

        }
        catch (Exception e){
            e.printStackTrace();
        }
        try{
            if(!modal_menuItem.getVendorkey().equals("") && !String.valueOf(modal_menuItem.getVendorkey()).toUpperCase().equals("NULL")){
                if(String.valueOf(modal_menuItem.getVendorkey()).toUpperCase().equals(Constants.Empty_Text)){

                    contentValues.put(vendorkey                    , "");

                }
                else{

                    contentValues.put(vendorkey                   , modal_menuItem.getVendorkey());


                }
            }

        }
        catch (Exception e){
            e.printStackTrace();
        }

        try{
            if(!modal_menuItem.getVendorname().equals("") && !String.valueOf(modal_menuItem.getVendorname()).toUpperCase().equals("NULL")){
                if(String.valueOf(modal_menuItem.getVendorname()).toUpperCase().equals(Constants.Empty_Text)){

                    contentValues.put(vendorname                    , "");

                }
                else{

                    contentValues.put(vendorname                  , modal_menuItem.getVendorname());


                }
            }

        }
        catch (Exception e){
            e.printStackTrace();
        }


        try{
            if(!modal_menuItem.getMenuItemId().equals("") && !String.valueOf(modal_menuItem.getMenuItemId()).toUpperCase().equals("NULL")){
                if(String.valueOf(modal_menuItem.getMenuItemId()).toUpperCase().equals(Constants.Empty_Text)){

                    contentValues.put(menuItemId                    , "");

                }
                else{

                    contentValues.put(menuItemId                  , modal_menuItem.getMenuItemId());


                }
            }

        }
        catch (Exception e){
            e.printStackTrace();
        }

        try{
            if(!modal_menuItem.getGrossweight().equals("") && !String.valueOf(modal_menuItem.getGrossweight()).toUpperCase().equals("NULL")){
                if(String.valueOf(modal_menuItem.getGrossweight()).toUpperCase().equals(Constants.Empty_Text)){

                    contentValues.put(grossweight                    , "");

                }
                else{

                    contentValues.put(grossweight                 , modal_menuItem.getGrossweight());


                }
            }

        }
        catch (Exception e){
            e.printStackTrace();
        }



        try{
            if(!modal_menuItem.getGrossweightingrams().equals("") && !String.valueOf(modal_menuItem.getGrossweightingrams()).toUpperCase().equals("NULL")){
                if(String.valueOf(modal_menuItem.getGrossweightingrams()).toUpperCase().equals(Constants.Empty_Text)){

                    contentValues.put(grossweightingrams                    , "");

                }
                else{

                    contentValues.put(grossweightingrams          , modal_menuItem.getGrossweightingrams());


                }
            }

        }
        catch (Exception e){
            e.printStackTrace();
        }

        try{
            if(!modal_menuItem.getReportname().equals("") && !String.valueOf(modal_menuItem.getReportname()).toUpperCase().equals("NULL")){
                if(String.valueOf(modal_menuItem.getReportname()).toUpperCase().equals(Constants.Empty_Text)){

                    contentValues.put(reportname                    , "");

                }
                else{

                    contentValues.put(reportname                  , modal_menuItem.getReportname());


                }
            }

        }
        catch (Exception e){
            e.printStackTrace();
        }

        try{
            if(!modal_menuItem.getSwiggyprice().equals("") && !String.valueOf(modal_menuItem.getSwiggyprice()).toUpperCase().equals("NULL")){
                if(String.valueOf(modal_menuItem.getSwiggyprice()).toUpperCase().equals(Constants.Empty_Text)){

                    contentValues.put(swiggyprice                    , "");

                }
                else{

                    contentValues.put(swiggyprice                 , modal_menuItem.getSwiggyprice());


                }
            }

        }
        catch (Exception e){
            e.printStackTrace();
        }



        try{
            if(!modal_menuItem.getDunzoprice().equals("") && !String.valueOf(modal_menuItem.getDunzoprice()).toUpperCase().equals("NULL")){
                if(String.valueOf(modal_menuItem.getDunzoprice()).toUpperCase().equals(Constants.Empty_Text)){

                    contentValues.put(dunzoprice                    , "");

                }
                else{

                    contentValues.put(dunzoprice                  , modal_menuItem.getDunzoprice());


                }
            }

        }
        catch (Exception e){
            e.printStackTrace();
        }

        try{
            if(!modal_menuItem.getBigbasketprice().equals("") && !String.valueOf(modal_menuItem.getBigbasketprice()).toUpperCase().equals("NULL")){
                if(String.valueOf(modal_menuItem.getBigbasketprice()).toUpperCase().equals(Constants.Empty_Text)){

                    contentValues.put(bigbasketprice                    , "");

                }
                else{

                    contentValues.put(bigbasketprice              , modal_menuItem.getBigbasketprice());



                }
            }

        }
        catch (Exception e){
            e.printStackTrace();
        }

        try{
            if(!modal_menuItem.getWholesaleprice().equals("") && !String.valueOf(modal_menuItem.getWholesaleprice()).toUpperCase().equals("NULL")){
                if(String.valueOf(modal_menuItem.getWholesaleprice()).toUpperCase().equals(Constants.Empty_Text)){

                    contentValues.put(wholesaleprice                    , "");

                }
                else{

                    contentValues.put(wholesaleprice              , modal_menuItem.getWholesaleprice());


                }
            }

        }
        catch (Exception e){
            e.printStackTrace();
        }



        try{
            if(!modal_menuItem.getAppmarkuppercentage().equals("") && !String.valueOf(modal_menuItem.getAppmarkuppercentage()).toUpperCase().equals("NULL")){
                if(String.valueOf(modal_menuItem.getAppmarkuppercentage()).toUpperCase().equals(Constants.Empty_Text)){

                    contentValues.put(appmarkuppercentage                    , "");

                }
                else{

                    contentValues.put(appmarkuppercentage         , modal_menuItem.getAppmarkuppercentage());


                }
            }

        }
        catch (Exception e){
            e.printStackTrace();
        }


        try{
            if(!modal_menuItem.getTmcpriceperkgWithMarkupValue().equals("") && !String.valueOf(modal_menuItem.getTmcpriceperkgWithMarkupValue()).toUpperCase().equals("NULL")){
                if(String.valueOf(modal_menuItem.getTmcpriceperkgWithMarkupValue()).toUpperCase().equals(Constants.Empty_Text)){

                    contentValues.put(tmcpriceperkgWithMarkupValue                    , "");

                }
                else{

                    contentValues.put(tmcpriceperkgWithMarkupValue, modal_menuItem.getTmcpriceperkgWithMarkupValue());


                }
            }

        }
        catch (Exception e){
            e.printStackTrace();
        }

        try{
            if(!modal_menuItem.getTmcpriceWithMarkupValue().equals("") && !String.valueOf(modal_menuItem.getTmcpriceWithMarkupValue()).toUpperCase().equals("NULL")){
                if(String.valueOf(modal_menuItem.getTmcpriceWithMarkupValue()).toUpperCase().equals(Constants.Empty_Text)){

                    contentValues.put(tmcpriceWithMarkupValue                    , "");

                }
                else{

                    contentValues.put(tmcpriceWithMarkupValue     , modal_menuItem.getTmcpriceWithMarkupValue());


                }
            }

        }
        catch (Exception e){
            e.printStackTrace();
        }
        try{
            if(!modal_menuItem.getItemavailability_AvlDetails().equals("") && !String.valueOf(modal_menuItem.getItemavailability_AvlDetails()).toUpperCase().equals("NULL")){
                if(String.valueOf(modal_menuItem.getItemavailability_AvlDetails()).toUpperCase().equals(Constants.Empty_Text)){

                    contentValues.put(itemavailability_AvlDetails                    , "");

                }
                else{

                    contentValues.put(itemavailability_AvlDetails , modal_menuItem.getItemavailability_AvlDetails());


                }
            }

        }
        catch (Exception e){
            e.printStackTrace();
        }

        try{
            if(!modal_menuItem.getKey_AvlDetails().equals("") && !String.valueOf(modal_menuItem.getKey_AvlDetails()).toUpperCase().equals("NULL")){
                if(String.valueOf(modal_menuItem.getKey_AvlDetails()).toUpperCase().equals(Constants.Empty_Text)){

                    contentValues.put(key_AvlDetails                    , "");

                }
                else{

                    contentValues.put(key_AvlDetails              , modal_menuItem.getKey_AvlDetails());


                }
            }

        }
        catch (Exception e){
            e.printStackTrace();
        }

        try{
            if(!modal_menuItem.getLastupdatedtime_AvlDetails().equals("") && !String.valueOf(modal_menuItem.getLastupdatedtime_AvlDetails()).toUpperCase().equals("NULL")){
                if(String.valueOf(modal_menuItem.getLastupdatedtime_AvlDetails()).toUpperCase().equals(Constants.Empty_Text)){

                    contentValues.put(lastupdatedtime_AvlDetails                    , "");

                }
                else{

                    contentValues.put(lastupdatedtime_AvlDetails  , modal_menuItem.getLastupdatedtime_AvlDetails());


                }
            }

        }
        catch (Exception e){
            e.printStackTrace();
        }




        try{
            if(!modal_menuItem.getMenuitemkey_AvlDetails().equals("") && !String.valueOf(modal_menuItem.getMenuitemkey_AvlDetails()).toUpperCase().equals("NULL")){
                if(String.valueOf(modal_menuItem.getMenuitemkey_AvlDetails()).toUpperCase().equals(Constants.Empty_Text)){

                    contentValues.put(menuItemId_AvlDetails                    , "");

                }
                else{

                    contentValues.put(menuItemId_AvlDetails       , modal_menuItem.getMenuitemkey_AvlDetails());


                }
            }

        }
        catch (Exception e){
            e.printStackTrace();
        }
        try{
            if(!modal_menuItem.getReceivedstock_AvlDetails().equals("") && !String.valueOf(modal_menuItem.getReceivedstock_AvlDetails()).toUpperCase().equals("NULL")){
                if(String.valueOf(modal_menuItem.getReceivedstock_AvlDetails()).toUpperCase().equals(Constants.Empty_Text)){

                    contentValues.put(receivedStock_AvlDetails                    , "");

                }
                else{

                    contentValues.put(receivedStock_AvlDetails    , modal_menuItem.getReceivedstock_AvlDetails());



                }
            }

        }
        catch (Exception e){
            e.printStackTrace();
        }
        try{
            if(!modal_menuItem.getStockbalance_AvlDetails().equals("") && !String.valueOf(modal_menuItem.getStockbalance_AvlDetails()).toUpperCase().equals("NULL")){
                if(String.valueOf(modal_menuItem.getStockbalance_AvlDetails()).toUpperCase().equals(Constants.Empty_Text)){

                    contentValues.put(stockBalance_AvlDetails                    , "");

                }
                else{

                    contentValues.put(stockBalance_AvlDetails     , modal_menuItem.getStockbalance_AvlDetails());



                }
            }

        }
        catch (Exception e){
            e.printStackTrace();
        }
        try{
            if(!modal_menuItem.getStockincomingkey_AvlDetails().equals("") && !String.valueOf(modal_menuItem.getStockincomingkey_AvlDetails()).toUpperCase().equals("NULL")){
                if(String.valueOf(modal_menuItem.getStockincomingkey_AvlDetails()).toUpperCase().equals(Constants.Empty_Text)){

                    contentValues.put(stockIncomingKey_AvlDetails                    , "");

                }
                else{

                    contentValues.put(stockIncomingKey_AvlDetails , modal_menuItem.getStockincomingkey_AvlDetails());


                }
            }

        }
        catch (Exception e){
            e.printStackTrace();
        }


        try{
            if(!modal_menuItem.getVendorkey_AvlDetails().equals("") && !String.valueOf(modal_menuItem.getVendorkey_AvlDetails()).toUpperCase().equals("NULL")){
                if(String.valueOf(modal_menuItem.getVendorkey_AvlDetails()).toUpperCase().equals(Constants.Empty_Text)){

                    contentValues.put(vendorkey_AvlDetails                    , "");

                }
                else{

                    contentValues.put(vendorkey_AvlDetails        , modal_menuItem.getVendorkey_AvlDetails());


                }
            }

        }
        catch (Exception e){
            e.printStackTrace();
        }

        try{
            if(!modal_menuItem.getInventorydetails().equals("") && !String.valueOf(modal_menuItem.getInventorydetails()).toUpperCase().equals("NULL")){
                if(String.valueOf(modal_menuItem.getInventorydetails()).toUpperCase().equals(Constants.Empty_Text)){

                    contentValues.put(inventoryDetails                    , "");

                }
                else{

                    contentValues.put(inventoryDetails            , modal_menuItem.getInventorydetails());


                }
            }

        }
        catch (Exception e){
            e.printStackTrace();
        }

        try{
            if(!modal_menuItem.getAllownegativestock().equals("") && !String.valueOf(modal_menuItem.getAllownegativestock()).toUpperCase().equals("NULL")){
                if(String.valueOf(modal_menuItem.getAllownegativestock()).toUpperCase().equals(Constants.Empty_Text)){

                    contentValues.put(allowNegativeStock_AvlDetails                    , "");

                }
                else{

                    contentValues.put(allowNegativeStock_AvlDetails , modal_menuItem.getAllownegativestock());


                }
            }

        }
        catch (Exception e){
            e.printStackTrace();
        }


        int response = sqLiteDatabase.update(database_Table, contentValues, localDB_id + "=" + modal_menuItem.getLocalDB_id(), null);
        return response;



    }



    public long insert(Modal_MenuItem modal_menuItem) {
        long id =0;
        try {
            ContentValues contentValues = new ContentValues();
            try {
                contentValues.put(itemName, String.valueOf(modal_menuItem.getItemname()));
            }
            catch (Exception e){
                e.printStackTrace();
            }
            try {
                contentValues.put(applieddiscountpercentage, String.valueOf(modal_menuItem.getApplieddiscountpercentage()));

            }
            catch (Exception e){
                e.printStackTrace();
            }
            try {
                contentValues.put(barcode, String.valueOf(modal_menuItem.getBarcode()));

            }
            catch (Exception e){
                e.printStackTrace();
            }
            try {
                contentValues.put(checkoutimageurl, String.valueOf(modal_menuItem.getCheckoutimageurl()));

            }
            catch (Exception e){
                e.printStackTrace();
            }
            try {
                contentValues.put(displayno, String.valueOf(modal_menuItem.getDisplayno()));

            }
            catch (Exception e){
                e.printStackTrace();
            }
            try {
                contentValues.put(grossweight, String.valueOf(modal_menuItem.getGrossweight()));

            }
            catch (Exception e){
                e.printStackTrace();
            }
            try {
                contentValues.put(gstpercentage, String.valueOf(modal_menuItem.getGstpercentage()));

            }
            catch (Exception e){
                e.printStackTrace();
            }
            try {
                contentValues.put(imageurl, String.valueOf(modal_menuItem.getImageurl()));

            }
            catch (Exception e){
                e.printStackTrace();
            }
            try {
                contentValues.put(itemavailability, String.valueOf(modal_menuItem.getItemavailability()));

            }
            catch (Exception e){
                e.printStackTrace();
            }
            try {
                contentValues.put(itemcalories, String.valueOf(modal_menuItem.getItemcalories()));

            }
            catch (Exception e){
                e.printStackTrace();
            }
            try {
                contentValues.put(itemuniquecode, String.valueOf(modal_menuItem.getItemuniquecode()));

            }
            catch (Exception e){
                e.printStackTrace();
            }
            try {
                contentValues.put(marinadelinkedcodes, String.valueOf(modal_menuItem.getMarinadelinkedcodes()));

            }
            catch (Exception e){
                e.printStackTrace();
            }
            try {
                contentValues.put(menuboarddisplayname, String.valueOf(modal_menuItem.getMenuboarddisplayname()));

            }
            catch (Exception e){
                e.printStackTrace();
            }
            try {
                contentValues.put(menutype, String.valueOf(modal_menuItem.getMenutype()));

            }
            catch (Exception e){
                e.printStackTrace();
            }
            try {
                contentValues.put(netweight, String.valueOf(modal_menuItem.getNetweight()));

            }
            catch (Exception e){
                e.printStackTrace();
            }
            try {
                contentValues.put(portionsize, String.valueOf(modal_menuItem.getPortionsize()));

            }
            catch (Exception e){
                e.printStackTrace();
            }
            try {
                contentValues.put(pricetypeforpos, String.valueOf(modal_menuItem.getPricetypeforpos()));

            }
            catch (Exception e){
                e.printStackTrace();
            }
            try {
                contentValues.put(showinmenuboard, String.valueOf(modal_menuItem.getShowinmenuboard()));

            }
            catch (Exception e){
                e.printStackTrace();
            }
            try {
                contentValues.put(tmcctgykey, String.valueOf(modal_menuItem.getTmcctgykey()));

            }
            catch (Exception e){
                e.printStackTrace();
            }
            try {
                contentValues.put(tmcpriceperkg, String.valueOf(modal_menuItem.getTmcpriceperkg()));

            }
            catch (Exception e){
                e.printStackTrace();
            }
            try {
                contentValues.put(tmcPrice, String.valueOf(modal_menuItem.getTmcprice()));

            }
            catch (Exception e){
                e.printStackTrace();
            }
            try {
                contentValues.put(tmcsubctgykey, String.valueOf(modal_menuItem.getTmcsubctgykey()));

            }
            catch (Exception e){
                e.printStackTrace();
            }
            try {
                contentValues.put(vendorkey, String.valueOf(modal_menuItem.getVendorkey()));

            }
            catch (Exception e){
                e.printStackTrace();
            }
            try {
                contentValues.put(vendorname, String.valueOf(modal_menuItem.getVendorname()));

            }
            catch (Exception e){
                e.printStackTrace();
            }
            try {
                contentValues.put(menuItemId, String.valueOf(modal_menuItem.getMenuItemId()));

            }
            catch (Exception e){
                e.printStackTrace();
            }

            try {
                contentValues.put(grossweightingrams, String.valueOf(modal_menuItem.getGrossweightingrams()));

            }
            catch (Exception e){
                e.printStackTrace();
            }
            try {
                contentValues.put(reportname, String.valueOf(modal_menuItem.getReportname()));

            }
            catch (Exception e){
                e.printStackTrace();
            }
            try {
                contentValues.put(swiggyprice, String.valueOf(modal_menuItem.getSwiggyprice()));

            }
            catch (Exception e){
                e.printStackTrace();
            }
            try {
                contentValues.put(dunzoprice, String.valueOf(modal_menuItem.getDunzoprice()));

            }
            catch (Exception e){
                e.printStackTrace();
            }
            try {
                contentValues.put(bigbasketprice, String.valueOf(modal_menuItem.getBigbasketprice()));

            }
            catch (Exception e){
                e.printStackTrace();
            }
            try {
                contentValues.put(wholesaleprice, String.valueOf(modal_menuItem.getWholesaleprice()));

            }
            catch (Exception e){
                e.printStackTrace();
            }
            try {
                contentValues.put(appmarkuppercentage, String.valueOf(modal_menuItem.getAppmarkuppercentage()));

            }
            catch (Exception e){
                e.printStackTrace();
            }
            try {
                contentValues.put(tmcpriceperkgWithMarkupValue, String.valueOf(modal_menuItem.getTmcpriceperkgWithMarkupValue()));

            }
            catch (Exception e){
                e.printStackTrace();
            }
            try {
                contentValues.put(tmcpriceWithMarkupValue, String.valueOf(modal_menuItem.getTmcpriceWithMarkupValue()));

            }
            catch (Exception e){
                e.printStackTrace();
            }
////////////////////////////////////////////////////////////////////
            try {
                contentValues.put(barcode_AvlDetails, String.valueOf(modal_menuItem.getBarcode_AvlDetails()));

            }
            catch (Exception e){
                e.printStackTrace();
            }

            try {
                contentValues.put(itemavailability_AvlDetails, String.valueOf(modal_menuItem.getItemavailability_AvlDetails()));

            }
            catch (Exception e){
                e.printStackTrace();
            }

            try {
                contentValues.put(key_AvlDetails, String.valueOf(modal_menuItem.getKey_AvlDetails()));

            }
            catch (Exception e){
                e.printStackTrace();
            }

            try {
                contentValues.put(lastupdatedtime_AvlDetails, String.valueOf(modal_menuItem.getLastupdatedtime_AvlDetails()));

            }
            catch (Exception e){
                e.printStackTrace();
            }


            try {
                contentValues.put(menuItemId_AvlDetails, String.valueOf(modal_menuItem.getMenuitemkey_AvlDetails()));

            }
            catch (Exception e){
                e.printStackTrace();
            }

            try {
                contentValues.put(receivedStock_AvlDetails, String.valueOf(modal_menuItem.getReceivedstock_AvlDetails()));

            }
            catch (Exception e){
                e.printStackTrace();
            }
            try {
                contentValues.put(stockBalance_AvlDetails, String.valueOf(modal_menuItem.getStockbalance_AvlDetails()));

            }
            catch (Exception e){
                e.printStackTrace();
            }

            try {
                contentValues.put(stockIncomingKey_AvlDetails, String.valueOf(modal_menuItem.getStockincomingkey_AvlDetails()));

            }
            catch (Exception e){
                e.printStackTrace();
            }

            try {
                contentValues.put(vendorkey_AvlDetails, String.valueOf(modal_menuItem.getVendorkey_AvlDetails()));

            }
            catch (Exception e){
                e.printStackTrace();
            }

            try {
                contentValues.put(allowNegativeStock_AvlDetails, String.valueOf(modal_menuItem.getAllownegativestock()));

            }
            catch (Exception e){
                e.printStackTrace();
            }

            try {
                contentValues.put(inventoryDetails, String.valueOf(modal_menuItem.getInventorydetails()));

            }
            catch (Exception e){
                e.printStackTrace();
            }


            try {
                id = sqLiteDatabase.insert(database_Table, null, contentValues);

            }
            catch (Exception e){
                e.printStackTrace();
            }



        }
        catch (Exception e){
            e.printStackTrace();
        }
        return id;
    }

}
