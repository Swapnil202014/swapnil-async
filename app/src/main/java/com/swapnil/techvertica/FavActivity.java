package com.swapnil.techvertica;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.swapnil.techvertica.adapter.SelectedAdapter;
import com.swapnil.techvertica.model.CheckNetworkConnection;
import com.swapnil.techvertica.model.DatabaseHandler;
import com.swapnil.techvertica.model.Product;



import java.util.ArrayList;
import java.util.List;

/**
 * Created by Swapnil on 1/7/2016.
 */
public class FavActivity extends AppCompatActivity {

    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    public RecyclerView mRecyclerView;
    public List<Product> allProduct;
    public SelectedAdapter mAdapter;
    public RecyclerView.LayoutManager mLayoutManager;
    public ProgressDialog pDialog;
    AlertDialog alertDialog;
    String message;
    DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fav);

        sharedpreferences= getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);
        mRecyclerView= (RecyclerView) findViewById(R.id.load_fav);

        db = new DatabaseHandler(this);

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

            new GetFav().execute();
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

    class GetFav extends AsyncTask<Void,Void,Void>
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

            mRecyclerView.setHasFixedSize(true);

            mAdapter = new SelectedAdapter(FavActivity.this,allProduct);
            mRecyclerView.setAdapter(mAdapter);

            //Layout manager for the Recycler View
            mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
            mRecyclerView.setLayoutManager(mLayoutManager);
            hidepDialog();
        }
    }

    public void setData()
    {
        String item=sharedpreferences.getString("fav","");

        String[] items = item.split(",");

        allProduct = new ArrayList<>();

        for (int i =0; i<items.length; i++ )
        {
            Product product = new Product();
            int id= Integer.parseInt(items[i]);
            product = db.getOne(id);

            allProduct.add(product);


        }



        // Toast.makeText(ShowIndividualProduct.this, ""+ product.getpImage(), Toast.LENGTH_SHORT).show();
    }
}
