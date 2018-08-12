package com.mahersoua.bakingapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.JsonArray;

public class RecipeModel implements Parcelable {
    private int id;
    private String name;
    private JsonArray ingredients;
    private JsonArray steps;
    private int servings;

    protected RecipeModel(Parcel in) {
        id = in.readInt();
        name = in.readString();
        servings = in.readInt();
        image = in.readString();
    }

    public static final Creator<RecipeModel> CREATOR = new Creator<RecipeModel>() {
        @Override
        public RecipeModel createFromParcel(Parcel in) {
            return new RecipeModel(in);
        }

        @Override
        public RecipeModel[] newArray(int size) {
            return new RecipeModel[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public JsonArray getIngredients() {
        return ingredients;
    }

    public void setIngredients(JsonArray ingredients) {
        this.ingredients = ingredients;
    }

    public JsonArray getSteps() {
        return steps;
    }

    public void setSteps(JsonArray steps) {
        this.steps = steps;
    }

    public int getServings() {
        return servings;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    private String image;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeInt(servings);
        dest.writeString(image);
    }
}
