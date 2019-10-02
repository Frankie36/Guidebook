package com.mystique.guidebook.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GuideResponse {
    @SerializedName("total")
    @Expose
    public String total;

    @SerializedName("data")
    @Expose
    public List<Guide> guideList = null;
}
