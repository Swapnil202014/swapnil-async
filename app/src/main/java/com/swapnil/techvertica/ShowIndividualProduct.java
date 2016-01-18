package com.swapnil.techvertica;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.swapnil.techvertica.model.CheckNetworkConnection;
import com.swapnil.techvertica.model.DatabaseHandler;
import com.swapnil.techvertica.model.Product;



import static com.swapnil.techvertica.MainActivity.cart;
import static com.swapnil.techvertica.MainActivity.fav;


import java.io.InputStream;



/**
 * Created by Swapnil on 1/7/2016.
 */
public class ShowIndividualProduct extends AppCompatActivity implements View.OnClickListener {

    DatabaseHandler db;
    Product product;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    TextView pname;
    TextView pprice;
    TextView pcompany;
    TextView psku;
    TextView pideal;
    TextView pcategory,addcart,addpref;
    TextView psub,pid;
    ImageView pimage;
    AlertDialog alertDialog;
    public ProgressDialog pDialog;
    String message;
    InputStream is=null;
    String viwedItem=null,priviousItem=null;

    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_individual);

        sharedpreferences= getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);


        db= new DatabaseHandler(this);
        Intent i=this.getIntent();
        product=new Product();

       // Toast.makeText(ShowIndividualProduct.this, "" + i.getStringExtra("id"), Toast.LENGTH_SHORT).show();

        id= Integer.parseInt(i.getStringExtra("id"));

        init();


        if (!(CheckNetworkConnection.isConnectionAvailable(this)))
        {
            message = getResources().getString(R.string.no_internet_connection);
            showAlertDialog(message, true);

        }
        else
        {
            pDialog = new ProgressDialog(this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);

            new GetSingleProduct().execute();
        }


    }

    public void showAlertDialog(String message, final boolean finish) {
        alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setMessage(message);
        alertDialog.setCancelable(false);

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if (finish)
                            finish();
                    }
                });
        alertDialog.show();
    }

    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    private void init() {

        pname = (TextView) findViewById(R.id.PName);
        pid = (TextView) findViewById(R.id.spid);
        pprice = (TextView) findViewById(R.id.pPrice);
        pcompany = (TextView) findViewById(R.id.pCompany);
        psku = (TextView) findViewById(R.id.pSKU);
        pideal = (TextView) findViewById(R.id.pIdeal);
        pcategory = (TextView) findViewById(R.id.pCategory);
        psub = (TextView) findViewById(R.id.pSub);
        pimage = (ImageView) findViewById(R.id.pImage);

        addcart = (TextView) findViewById(R.id.addCart);
        addpref = (TextView) findViewById(R.id.addPreferred);

        addpref.setOnClickListener(this);
        addcart.setOnClickListener(this);

    }

    public void setData()
    {
       product = db.getOne(id);

       // Toast.makeText(ShowIndividualProduct.this, ""+ product.getpImage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        if(v == findViewById(R.id.addPreferred))
        {
            fav.add(String.valueOf(id));
            Log.e("fav lenth", "" + fav.size());

            String favItems = sharedpreferences.getString("fav", "");

            if (favItems.equals("NA"))
            {
                viwedItem = null;
                viwedItem = String.valueOf(id);

                editor = sharedpreferences.edit();
                editor.putString("fav", viwedItem);
                editor.apply();
                Toast.makeText(this, "Added into favorites!...", Toast.LENGTH_SHORT).show();
                Log.e("fav lenth", sharedpreferences.getString("fav", ""));
            }
            else
            {
                 priviousItem = null;
                priviousItem = sharedpreferences.getString("fav", "");
                viwedItem = null;
                viwedItem = priviousItem + "," + String.valueOf(id);


                editor = sharedpreferences.edit();
                editor.putString("fav", viwedItem);
                editor.apply();

                Toast.makeText(this, "Added into favorites!...", Toast.LENGTH_SHORT).show();

                Log.e("fav lenth", sharedpreferences.getString("fav", ""));

            }


        }
        else
        {
            cart.add(String.valueOf(id));
            Log.e("car lenth", "" + cart.size());

            String favItems = sharedpreferences.getString("cart", "");

            if (favItems.equals("NA"))
            {
                viwedItem = null;
                viwedItem = String.valueOf(id);

                editor = sharedpreferences.edit();
                editor.putString("cart", viwedItem);
                editor.apply();
                Toast.makeText(this, "Added into cart!...", Toast.LENGTH_SHORT).show();
                Log.e("cart lenth", sharedpreferences.getString("cart", ""));
            }
            else
            {
                priviousItem = null;
                priviousItem = sharedpreferences.getString("cart", "");
                viwedItem = null;
                viwedItem = priviousItem + "," + String.valueOf(id);


                editor = sharedpreferences.edit();
                editor.putString("cart", viwedItem);
                editor.apply();

                Toast.makeText(this, "Added into cart!...", Toast.LENGTH_SHORT).show();

                Log.e("cart lenth", sharedpreferences.getString("cart", ""));

            }
        }
    }

    class GetSingleProduct extends AsyncTask<Void,Void,Void>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showpDialog();
        }

        @Override
        protected Void doInBackground(Void... params) {

           setData();


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            //Toast.makeText(ShowIndividualProduct.this, ""+ product.getpImage(), Toast.LENGTH_SHORT).show();


            Picasso.with(getApplicationContext()).load(product.getpImage()).into(pimage);

            pname.setText(product.getpName());
            pprice.setText(product.getpPrice());
            pcompany.setText(product.getpCompany());
            pid.setText(product.getpId());
            psku.setText(product.getSku());
            //pmfg.setText(product.getpCompany());
            //pid.setText(product.getpId());

            hidepDialog();
        }
    }


}
