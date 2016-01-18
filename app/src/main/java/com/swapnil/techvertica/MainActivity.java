package com.swapnil.techvertica;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.swapnil.techvertica.adapter.ProductAdapter;
import com.swapnil.techvertica.model.CheckNetworkConnection;
import com.swapnil.techvertica.model.DatabaseHandler;
import com.swapnil.techvertica.model.Product;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    public RecyclerView mRecyclerView;
    public List<Product> allProduct;
    public ProductAdapter mAdapter;
    public RecyclerView.LayoutManager mLayoutManager;
    public ProgressDialog pDialog;
    AlertDialog alertDialog;
    String message;
    DatabaseHandler db;

    InputStream is=null;
    String result=null,line=null;

    public static List<String> cart;
    public static List<String> fav;

    String spcart;
    String spfav;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //sharedpreferences for mainting cart and favorites
        sharedpreferences= getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);

        String key="fav";
        String value=null;


        String key1="cart";
        String value1=null;

        spfav =sharedpreferences.getString(key, value);
        spcart =sharedpreferences.getString(key1,value1);


        if(spfav == null)
        {

            value="NA";
            editor= sharedpreferences.edit();
            editor.putString(key, value);
            editor.apply();


        }
        if(spcart == null)
        {

            value1="NA";
            editor= sharedpreferences.edit();
            editor.putString(key1, value1);
            editor.apply();


        }



        init();

        db = new DatabaseHandler(this);


        cart=new ArrayList<>();
        fav=new ArrayList<>();





        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        // if connection is avialable
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

            new GetProduct().execute();
        }

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
        mRecyclerView= (RecyclerView) findViewById(R.id.load_product);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_favorites) {

            if(fav.size() > 0)
            {
                startActivity(new Intent(MainActivity.this,FavActivity.class));
            }
            else
            {
                Toast.makeText(MainActivity.this, "No item in favorites", Toast.LENGTH_SHORT).show();
            }


        }
        else if (id == R.id.nav_cart)
        {
            if(cart.size() > 0)
            {
                startActivity(new Intent(MainActivity.this,CartActivity.class));
            }
            else
            {
                Toast.makeText(MainActivity.this, "No item in cart", Toast.LENGTH_SHORT).show();
            }

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //get all product from json
    public void getProduct()
    {

        try
        {

            HttpClient httpclient = new DefaultHttpClient();

            HttpPost httppost = new HttpPost("http://www.pocketdrugs.com/api.php?method=Product_filter&category=Orthopedic");

           // httppost.setEntity(new UrlEncodedFormEntity(checkuser));

            HttpResponse response = httpclient.execute(httppost);

            HttpEntity entity = response.getEntity();

            is = entity.getContent();

        }
        catch(Exception e)
        {
            Log.e("try1", e.getMessage());
        }

        try
        {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
            StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null)
            {
                sb.append(line + "\n");
            }
            is.close();
            result = sb.toString();

        }
        catch(Exception e)
        {
            Log.e("try2", e.getMessage());

        }

        try
        {
            JSONArray jObject = null;
            jObject = new JSONArray(result);

            allProduct= new ArrayList<>();

            Log.e("length", ""+ jObject.length());

            for(int i = 0; i < jObject.length(); i++){

                Product product=new Product();

                product.setpId(jObject.getJSONObject(i).getString("id"));

                product.setpName(jObject.getJSONObject(i).getString("name"));
                product.setpImage(jObject.getJSONObject(i).getString("image"));
                product.setpPrice(jObject.getJSONObject(i).getString("price"));
                product.setpDisc(jObject.getJSONObject(i).getString("discount"));
                product.setpFinalPrice(jObject.getJSONObject(i).getString("final_price"));
                product.setSku(jObject.getJSONObject(i).getString("sku"));
                product.setpCompany(jObject.getJSONObject(i).getString("company"));


                //db.addProduct(new Product());

                allProduct.add(product);




            }


        }
        catch(Exception e)
        {

            Log.e("try3", e.getMessage());
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

    private class GetProduct extends AsyncTask<Void,Void,Void>
    {

        @Override
        protected Void doInBackground(Void... params) {



            getProduct();

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showpDialog();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            try {

                for(int i=0;i< allProduct.size();i++)
                {
                    Product product=allProduct.get(i);

                    db.addProduct(new Product(product.getpId(),product.getpName(),product.getpPrice(),product.getpDisc(),product.getpFinalPrice(),product.getpCompany(),product.getSku(),product.getpImage()));


                }

            mRecyclerView.setHasFixedSize(true);

            mAdapter = new ProductAdapter(MainActivity.this,allProduct);
            mRecyclerView.setAdapter(mAdapter);

            //Layout manager for the Recycler View
            mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
            mRecyclerView.setLayoutManager(mLayoutManager);

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            hidepDialog();


        }

    }
}
