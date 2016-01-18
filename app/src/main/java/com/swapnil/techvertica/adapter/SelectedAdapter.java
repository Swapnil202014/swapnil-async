package com.swapnil.techvertica.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.swapnil.techvertica.R;
import com.swapnil.techvertica.ShowIndividualProduct;
import com.swapnil.techvertica.model.Product;

import java.util.Collections;
import java.util.List;

/**
 * Created by Swapnil on 1/7/2016.
 */
public class SelectedAdapter extends RecyclerView.Adapter<SelectedAdapter.ProductHolder> {

    private final LayoutInflater inflater;
    List<Product> products = Collections.EMPTY_LIST;
    Activity context;


    View view;


    public  SelectedAdapter(Activity activity, List<Product> products)
    {
        inflater=LayoutInflater.from(activity);
        context=activity;
        this.products=products;

    }

    @Override
    public ProductHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view=inflater.inflate(R.layout.custom_product,parent,false);
        ProductHolder productHolder=new ProductHolder(view);
        return productHolder;
    }

    @Override
    public void onBindViewHolder(final ProductHolder holder, int position) {

        Product product=products.get(position);

        Picasso.with(context).load(product.getpImage()).error(R.mipmap.ic_launcher).into(holder.pimage);

        holder.pname.setText(product.getpName());
        holder.pmrp.setText(product.getpPrice());
        holder.pmfg.setText(product.getpCompany());
        holder.pid.setText(product.getpId());




    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ProductHolder extends RecyclerView.ViewHolder
    {
        TextView pname;
        TextView pmrp;
        TextView pmfg;
        TextView pid;
        ImageView pimage;
        CardView cardView;

        public ProductHolder(View itemView) {
            super(itemView);

            pname = (TextView) itemView.findViewById(R.id.productName);
            pid = (TextView) itemView.findViewById(R.id.pid);
            pmrp = (TextView) itemView.findViewById(R.id.productMRP);
            pmfg = (TextView) itemView.findViewById(R.id.productMFG);
            pimage = (ImageView) itemView.findViewById(R.id.productImage);
            cardView= (CardView) itemView.findViewById(R.id.pCardView);
        }
    }
}


