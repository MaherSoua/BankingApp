package com.mahersoua.bakingapp.models;

import com.google.gson.JsonArray;

public class RecipeModel {
    private int id;
    private String name;
    private JsonArray ingredients;
    private JsonArray steps;
    private int servings;

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
}
