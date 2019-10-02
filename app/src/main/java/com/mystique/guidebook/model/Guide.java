package com.mystique.guidebook.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Guide {
    @SerializedName("startDate")
    @Expose
    public String startDate;
    @SerializedName("objType")
    @Expose
    public String objType;
    @SerializedName("endDate")
    @Expose
    public String endDate;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("loginRequired")
    @Expose
    public Boolean loginRequired;
    @SerializedName("url")
    @Expose
    public String url;
    @SerializedName("venue")
    @Expose
    public Venue venue;
    @SerializedName("icon")
    @Expose
    public String icon;
}

