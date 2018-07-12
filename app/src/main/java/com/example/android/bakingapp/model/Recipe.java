package com.example.android.bakingapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Recipe implements Parcelable {



    @SerializedName ("id")
    private String mRecipeId;

    @SerializedName ("name")
    private String mRecipeName;

    @SerializedName ("image")
    private String mRecipeImageUrl;

    private int servings;
    private List<Step> steps;
    private List<Ingredients> ingredients;


    public Recipe() {

    }

    protected Recipe(Parcel in) {
        mRecipeId = in.readString();
        mRecipeName = in.readString();
        mRecipeImageUrl = in.readString();
        servings = in.readInt();
    }

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    public int getServings() {
        return servings;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

    public String getRecipeId() {
        return mRecipeId;
    }

    public void setRecipeId(String recipeId) {
        mRecipeId = recipeId;
    }

    public String getRecipeName() {
        return mRecipeName;
    }

    public void setRecipeName(String recipeName) {
        mRecipeName = recipeName;
    }

    public String getRecipeImageUrl() {
        return mRecipeImageUrl;
    }

    public void setRecipeImageUrl(String recipeImageUrl) {
        mRecipeImageUrl = recipeImageUrl;
    }

    public List<Ingredients> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredients> ingredients) {
        this.ingredients = ingredients;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mRecipeId);
        parcel.writeString(mRecipeName);
        parcel.writeString(mRecipeImageUrl);
        parcel.writeInt(servings);
    }


}
