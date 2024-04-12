package com.example.recipesapp;

import android.os.StrictMode;

import com.example.recipesapp.model.Recipe;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


public class DataService {
    private static ArrayList<Recipe> arrRecipe = new ArrayList<>();
    public static ArrayList<Recipe> getArrRecipe(){

        arrRecipe.clear();

        String sURL = "https://themealdb.com/api/json/v1/1/search.php?s=";

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try {
            URL url = new URL(sURL);

            HttpURLConnection request = (HttpURLConnection) url.openConnection();
            request.connect();

            JsonParser jp = new JsonParser();
            JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent()));


            JsonObject jsonObject = root.getAsJsonObject();
            JsonArray meals = jsonObject.getAsJsonArray("meals");

            for (JsonElement meal : meals){
                JsonObject obj = meal.getAsJsonObject();

                JsonElement entriesname = obj.get("strMeal");
                JsonElement entriesdirections = obj.get("strInstructions");
                JsonElement entriescategory = obj.get("strCategory");
                JsonElement entriesimage = obj.get("strMealThumb");

                String image = entriesimage.toString().replace("\"","");

                String name = entriesname.toString().replace("\"","");
                String directions = entriesdirections.toString().replace("\"","");
                String category = entriescategory.toString().replace("\"","");



                String ingredient ="";
                String amount="";


                String ingredients = "";
                for (int i = 1 ; i <= 20 ; i++){

                    JsonElement entriesingr = obj.get("strIngredient"+i);
                    JsonElement entriesamount = obj.get("strMeasure"+i);

                    ingredient = entriesingr.toString().replace("\"","");
                     amount = entriesamount.toString().replace("\"","");


                    ingredients = ingredients + "\n" +(amount + " " + ingredient);


                    if (ingredient.equals("") || ingredient.equals("null")) {

                        i = 21;
                    }
                }

                Recipe recipe = new Recipe(name , "" ,category , ingredients , directions ,image);
                arrRecipe.add(recipe);
            }

        }
        catch (MalformedURLException e){
            throw new RuntimeException(e);
        }
        catch (IOException e){
            throw new RuntimeException(e);
        }

        return arrRecipe;
    }
}
