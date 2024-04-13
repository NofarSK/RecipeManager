package com.example.recipesapp.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.Navigation;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.recipesapp.adapters.FavoriteRecipesAdapter;
import com.example.recipesapp.R;
import com.example.recipesapp.model.Recipe;
import com.example.recipesapp.adapters.RecipeAdapter;
import com.example.recipesapp.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private String userId;
    private FirebaseDatabase database;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        database = FirebaseDatabase.getInstance();
    }


    public void loginFunc(View view)
    {
        String email = ((EditText)findViewById(R.id.usernameLogin)).getText().toString().trim();
        String password = ((EditText)findViewById(R.id.passwordLogin)).getText().toString().trim();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            currentUser = mAuth.getCurrentUser();
                            userId = currentUser != null ? currentUser.getUid() : null;

                            Toast.makeText(MainActivity.this , getString(R.string.login_successful) , Toast.LENGTH_LONG).show();
                            Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_myRecipesFragment );
                        }
                        else {
                            Toast.makeText(MainActivity.this , getString(R.string.incorrect_username_or_password) , Toast.LENGTH_LONG).show();

                        }
                    }
                });
    }

    public void registerFunc(View view){

        String email = ((EditText)findViewById(R.id.userEmailReg)).getText().toString().trim();
        String password = ((EditText)findViewById(R.id.passwordReg)).getText().toString().trim();

        if (checkEmpty()){
            Toast.makeText(MainActivity.this , getString(R.string.fill_all_the_fields) , Toast.LENGTH_LONG).show();
        }
        else {

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                //           FirebaseUser user = mAuth.getCurrentUser();
                                currentUser = mAuth.getCurrentUser();
                                userId = currentUser != null ? currentUser.getUid() : null;
                                writeUser();
                                //readUser();
                                Toast.makeText(MainActivity.this , getString(R.string.register_successful) , Toast.LENGTH_LONG).show();
                                Navigation.findNavController(view).navigate(R.id.action_registerFragment_to_myRecipesFragment);

                            } else {
                                Toast.makeText(MainActivity.this , getString(R.string.register_fail) , Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }
    }

    public void readUser(){

        DatabaseReference myRef = database.getReference("users").child(userId);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                User value = dataSnapshot.getValue(User.class);
                TextView title = findViewById(R.id.hello_title);
                title.setText(getString(R.string.hello) + value.getName() + "!" );
            }

            @Override
            public void onCancelled(DatabaseError error) {

                Toast.makeText(MainActivity.this, getString(R.string.failed_to_read_value), Toast.LENGTH_LONG).show();
            }
        });

    }
    public void writeUser(){

        if (checkEmpty()){ }
        else {
            EditText email = findViewById(R.id.userEmailReg);
            EditText phone = findViewById(R.id.phoneReg);
            EditText name = findViewById(R.id.usernameReg);


            DatabaseReference myRef = database.getReference("users").child(userId);

            User data = new User( name.getText().toString(),email.getText().toString() ,phone.getText().toString());

            myRef.setValue(data);
        }

    }

    public boolean checkEmpty(){
        String email = ((EditText)findViewById(R.id.userEmailReg)).getText().toString().trim();
        String password = ((EditText)findViewById(R.id.passwordReg)).getText().toString().trim();
        String phone = ((EditText)findViewById(R.id.phoneReg)).getText().toString().trim();

        if (email.isEmpty() || password.isEmpty() || phone.isEmpty()){
            return true;
        }
        else{
            return false;
        }
    }


    public void writeRecipe(String selectedCategory , Uri imageUri ){

        TextInputEditText nameRecipe;
        TextInputEditText timeRecipe;
        TextInputEditText ingredientsRecipe;
        TextInputEditText directionsRecipe;


        nameRecipe = findViewById(R.id.recipe_name);
        timeRecipe = findViewById(R.id.prep_time_input);

        ingredientsRecipe = findViewById(R.id.ingredients_input);
        directionsRecipe = findViewById(R.id.directions_input);


        DatabaseReference myRef = database.getReference("recipes").child(userId).child(selectedCategory).child(nameRecipe.getText().toString());

        Recipe data = new Recipe( nameRecipe.getText().toString(),timeRecipe.getText().toString(),
                selectedCategory , ingredientsRecipe.getText().toString(),directionsRecipe.getText().toString() , imageUri.toString());

        myRef.setValue(data);
    }

    public void writeFavoriteRecipe(Recipe recipeApi ){


        DatabaseReference myRef = database.getReference("favorite").child(userId).child(recipeApi.getRecipeName());

        Recipe data = new Recipe( recipeApi.getRecipeName(),"",
                recipeApi.getCategory() , recipeApi.getIngredients(),recipeApi.getDirections() , recipeApi.getPhoto());

        myRef.setValue(data);
    }


    public void readRecipe(String selectedCategory , ArrayList<Recipe> recipes , RecipeAdapter adapter){


        DatabaseReference myRef = database.getReference("recipes").child(userId).child(selectedCategory);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                recipes.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String key = snapshot.getKey();
                    Recipe value = dataSnapshot.child(key).getValue(Recipe.class);
                    value.setKey(snapshot.getKey());

                    recipes.add(value);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(MainActivity.this, getString(R.string.failed_to_read_value), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void readFavoriteRecipe(ArrayList<Recipe> recipes , FavoriteRecipesAdapter adapter){


        DatabaseReference myRef = database.getReference("favorite").child(userId);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                recipes.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String key = snapshot.getKey();
                    Recipe value = dataSnapshot.child(key).getValue(Recipe.class);
                    value.setKey(snapshot.getKey());

                    recipes.add(value);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(MainActivity.this, getString(R.string.failed_to_read_value), Toast.LENGTH_LONG).show();
            }
        });
    }


    public void readRecipeDetails(String selectedCategory , String recipeName ){

        DatabaseReference myRef = database.getReference("recipes").child(userId).child(selectedCategory).child(recipeName);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Recipe value = dataSnapshot.getValue(Recipe.class);

                TextView nameRecipe = findViewById(R.id.detail_recipe_name);
                TextView timeRecipe = findViewById(R.id.detail_time);
                TextView ingredientsRecipe = findViewById(R.id.detail_ingredients);
                TextView directionsRecipe = findViewById(R.id.detail_directions);

                nameRecipe.setText(value.getRecipeName());
                timeRecipe.setText("      "+value.getPrepTime());
                ingredientsRecipe.setText("\n"+value.getIngredients());
                directionsRecipe.setText("\n"+value.getDirections());

            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(MainActivity.this, getString(R.string.failed_to_read_value), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void readFavoriteRecipeDetails( String recipeName ){

        DatabaseReference myRef = database.getReference("favorite").child(userId).child(recipeName);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Recipe value = dataSnapshot.getValue(Recipe.class);

                TextView nameRecipe = findViewById(R.id.detail_recipe_name);

                TextView ingredientsRecipe = findViewById(R.id.detail_ingredients);
                TextView directionsRecipe = findViewById(R.id.detail_directions);

                nameRecipe.setText(value.getRecipeName());

                ingredientsRecipe.setText(" \n"+value.getIngredients());
                directionsRecipe.setText(" \n"+value.getDirections());

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Toast.makeText(MainActivity.this,getString(R.string.failed_to_read_value), Toast.LENGTH_LONG).show();
            }
        });
    }


    public void deleteRecipe(String selectedCategory, String recipeName) {

        DatabaseReference myRef = database.getReference("recipes").child(userId).child(selectedCategory).child(recipeName);
        myRef.removeValue();
    }

    public void deleteFavoriteRecipe( String recipeName) {

        DatabaseReference myRef = database.getReference("favorite").child(userId).child(recipeName);
        myRef.removeValue();
    }


    public void updateRecipe(Recipe recipe, String oldName, String oldCategory) {
        if (!oldName.equals(recipe.getRecipeName())) {
            deleteRecipe(oldCategory, oldName);
        }
        DatabaseReference myRef = database.getReference("recipes").child(userId).child(recipe.getCategory()).child(recipe.getRecipeName());

        Recipe data = new Recipe(recipe.getRecipeName(), recipe.getPrepTime(),
                recipe.getCategory(), recipe.getIngredients(), recipe.getDirections(), recipe.getPhoto());

        myRef.setValue(data);
    }
}