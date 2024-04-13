package com.example.recipesapp.adapters;


import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.recipesapp.R;
import com.example.recipesapp.model.Recipe;
import com.example.recipesapp.activity.MainActivity;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.MyViewHolder> {

    private ArrayList<Recipe> dataSet;
    private ItemClickListener itemClickListener;

    private Context context;

    public RecipeAdapter(ArrayList<Recipe> dataSet , ItemClickListener itemClickListener,Context context ){

        this.dataSet = dataSet;
        this.itemClickListener = itemClickListener;
        this.context = context;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView recipeName;
        ImageButton removeBtn;
        ImageButton editBtn;
        ImageView imageRecipe;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            recipeName = itemView.findViewById(R.id.textView2);
            removeBtn = itemView.findViewById(R.id.delete_recipe);
            editBtn = itemView.findViewById(R.id.edit_recipe);
            imageRecipe = itemView.findViewById(R.id.imageView);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_recipe , parent , false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Recipe recipe = dataSet.get(position);
        holder.recipeName.setText(dataSet.get(position).getRecipeName());

        Animation animation = AnimationUtils.loadAnimation(holder.itemView.getContext(),R.anim.anim_one);
        holder.itemView.startAnimation(animation);

        holder.itemView.setOnClickListener(v -> {
            itemClickListener.onItemClick(dataSet.get(position));
        });

        holder.editBtn.setOnClickListener(v -> {
            showEditDialog(position);
        });

        if (recipe.getPhoto() != null && !recipe.getPhoto().isEmpty()) {
            Glide.with(context)
                    .load(recipe.getPhoto())
                    .into(holder.imageRecipe);

        } else {
            holder.imageRecipe.setImageResource(R.mipmap.ic_launcher);
        }

        holder.removeBtn.setOnClickListener(v -> {


            String selectedCategory = recipe.getCategory();
            String recipeName = recipe.getRecipeName();

            MainActivity mainActivity = (MainActivity) holder.itemView.getContext();
            mainActivity.deleteRecipe(selectedCategory, recipeName);

            dataSet.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, dataSet.size());
            Toast.makeText(context, context.getString(R.string.recipe_deleted), Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }


    public interface ItemClickListener{
        void  onItemClick(Recipe details);
    }


    public void showEditDialog(int position ) {
        Recipe recipe = dataSet.get(position);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.fragment_edit_recipe, null);
        builder.setView(dialogView);

        TextInputEditText editNameRecipe = dialogView.findViewById(R.id.edit_recipe_name);
        TextInputEditText editTimeRecipe = dialogView.findViewById(R.id.edit_prep_time_input);
        TextInputEditText editIngredientsRecipe = dialogView.findViewById(R.id.edit_ingredients_input);
        TextInputEditText editDirectionsRecipe = dialogView.findViewById(R.id.edit_directions_input);


        editNameRecipe.setText(recipe.getRecipeName());
        editTimeRecipe.setText(recipe.getPrepTime());
        editIngredientsRecipe.setText(recipe.getIngredients());
        editDirectionsRecipe.setText(recipe.getDirections());



        String oldName = recipe.getRecipeName();
        String oldCategory = recipe.getCategory();


        Button cancelButton = dialogView.findViewById(R.id.cancel_button);
        Button saveButton = dialogView.findViewById(R.id.update_btn);


        AlertDialog dialog = builder.create();


        cancelButton.setOnClickListener(v -> dialog.dismiss());
        saveButton.setOnClickListener(v -> {

            String newName = editNameRecipe.getText().toString();
            String newTime = editTimeRecipe.getText().toString();
            String newIngredients = editIngredientsRecipe.getText().toString();
            String newDirections = editDirectionsRecipe.getText().toString();


            recipe.setRecipeName(newName);
            recipe.setPrepTime(newTime);
            recipe.setIngredients(newIngredients);
            recipe.setDirections(newDirections);


            notifyItemChanged(position);


            MainActivity mainActivity = (MainActivity) context;
            mainActivity.updateRecipe(recipe,oldName,oldCategory );

            dialog.dismiss();
        });

        dialog.show();
    }

    public void filterList(ArrayList<Recipe> filteredList){
        dataSet = filteredList;
        notifyDataSetChanged();
    }


}


