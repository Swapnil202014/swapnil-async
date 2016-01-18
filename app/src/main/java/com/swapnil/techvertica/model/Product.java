package com.swapnil.techvertica.model;

/**
 * Created by Swapnil on 1/7/2016.
 */
public class Product
{
    String pId,pName,pPrice,pDisc,pFinalPrice,pCompany,sku,pImage;

    public Product()
    {

    }

    public Product( String pid,String pName,String pPrice,String pDisc,String pFinalPrice,String pCompany,String sku,String pImage)
    {
        this.pId=pid;
        this.pName=pName;
        this.pPrice=pPrice;
        this.pDisc=pDisc;
        this.pFinalPrice=pFinalPrice;
        this.pCompany=pCompany;
        this.sku=sku;
        this.pImage=pImage;




    }

    public String getpCompany() {
        return pCompany;
    }

    public String getpDisc() {
        return pDisc;
    }

    public String getpFinalPrice() {
        return pFinalPrice;
    }

    public String getpId() {
        return pId;
    }

    public String getpImage() {
        return pImage;
    }

    public String getpName() {
        return pName;
    }

    public String getpPrice() {
        return pPrice;
    }

    public String getSku() {
        return sku;
    }


    public void setpCompany(String pCompany) {
        this.pCompany = pCompany;
    }

    public void setpDisc(String pDisc) {
        this.pDisc = pDisc;
    }

    public void setpFinalPrice(String pFinalPrice) {
        this.pFinalPrice = pFinalPrice;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }

    public void setpImage(String pImage) {
        this.pImage = pImage;
    }

    public void setpName(String pName) {
        this.pName = pName;
    }

    public void setpPrice(String pPrice) {
        this.pPrice = pPrice;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

}
