package com.swapnil.techvertica.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;



public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "TechVertica";

    // Contacts table name
    private static final String tbl_product = "ProductData";



    // Product Data Table Columns names
    private static final String pid = "pid";
    private static final String pname = "pname";
    private static final String pprice = "pprice";
    private static final String pdisc = "pdisc";
    private static final String pfinalprice = "pfinalprice";
    private static final String pcompany = "pcompany";
    private static final String psku = "psku";
    private static final String pimage = "pimage";




    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    String create_product_table = "CREATE TABLE " + tbl_product + "("
            + pid + " INTEGER,"
            + pname + " TEXT,"
            + pprice + " TEXT,"
            + pdisc + " TEXT,"
            + pfinalprice + " TEXT,"
            + pcompany + " TEXT,"
            + psku + " TEXT,"
            + pimage + " TEXT" + ")";





    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        try {
            sqLiteDatabase.execSQL(create_product_table);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }




    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {

        try {


            // Drop older table if existed
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + tbl_product);


            // Create tables again
            onCreate(sqLiteDatabase);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    // Adding new product
    public void addProduct(Product product) {

        long result= 0;
        try {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(pid,product.getpId());
            values.put(pname,product.getpName());
            values.put(pprice,product.getpPrice());
            values.put(pdisc,product.getpDisc());
            values.put(pfinalprice,product.getpFinalPrice());
            values.put(pcompany,product.getpCompany());
            values.put(psku,product.getSku());
            values.put(pimage, product.getpImage());


            // Inserting Row
            result = db.insert(tbl_product, null, values);
            db.close(); // Closing database connection


        }
        catch (Exception e)
        {
            e.printStackTrace();
        }


    }


    public Product getOne(int id)
    {

        Product shop = null;

        // Select All Query
        String selectQuery = "SELECT  * FROM " + tbl_product + " where pid = " +  id  ;

        try {


            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    shop = new Product();

                    //shop.setpId(Integer.parseInt(cursor.getInt(0)));
                    shop.setpName(cursor.getString(1));
                    shop.setpPrice(cursor.getString(2));
                    shop.setpDisc(cursor.getString(3));
                    shop.setpFinalPrice(cursor.getString(4));
                    shop.setpCompany(cursor.getString(5));
                    shop.setSku(cursor.getString(6));
                    shop.setpImage(cursor.getString(7));



                } while (cursor.moveToNext());
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }



        return shop;
    }

}
