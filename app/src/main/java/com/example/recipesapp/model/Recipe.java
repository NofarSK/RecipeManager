package com.example.recipesapp.model;

import android.net.Uri;

public class Recipe {
    private String recipeName;
    private String prepTime;
    private String category;
    private String ingredients;
    private String directions;
    private String photo;
    private String key;

    public Recipe(){}

    public Recipe(String recipeName, String prepTime, String category, String ingredients, String directions, String photo) {
        this.recipeName = recipeName;
        this.prepTime = prepTime;
        this.category = category;
        this.ingredients = ingredients;
        this.directions = directions;
        this.photo = photo;
    }


    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public String getPrepTime() {
        return prepTime;
    }

    public void setPrepTime(String prepTime) {
        this.prepTime = prepTime;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getDirections() {
        return directions;
    }

    public void setDirections(String directions) {
        this.directions = directions;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
