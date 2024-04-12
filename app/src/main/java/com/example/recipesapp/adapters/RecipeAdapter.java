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

    private ImagePickerListener imagePickerListener;
    private static final int REQUEST_CODE_PICK_IMAGE = 1001;
    private Context context;

    //private static final int REQUEST_CODE_PICK_IMAGE = 1001;
    public RecipeAdapter(ArrayList<Recipe> dataSet , ItemClickListener itemClickListener,Context context ,ImagePickerListener imagePickerListener){

        this.dataSet = dataSet;
        this.itemClickListener = itemClickListener;
        this.context = context;
        this.imagePickerListener = imagePickerListener;
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
            showEditDialog(position , recipe.getPhoto());
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

    public interface ImagePickerListener {
        void onImagePicked(int position, Uri imageUri);
    }

    public void showEditDialog(int position , String image) {
        Recipe recipe = dataSet.get(position);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.fragment_edit_recipe, null);
        builder.setView(dialogView);

        TextInputEditText editNameRecipe = dialogView.findViewById(R.id.edit_recipe_name);
        TextInputEditText editTimeRecipe = dialogView.findViewById(R.id.edit_prep_time_input);
        ImageView editImageRecipe = dialogView.findViewById(R.id.edit_image_food);
        TextInputEditText editIngredientsRecipe = dialogView.findViewById(R.id.edit_ingredients_input);
        TextInputEditText editDirectionsRecipe = dialogView.findViewById(R.id.edit_directions_input);
        TextView category = dialogView.findViewById(R.id.category_edit);

        category.setText("Category: " + recipe.getCategory());
        editNameRecipe.setText(recipe.getRecipeName());
        editTimeRecipe.setText(recipe.getPrepTime());
        editIngredientsRecipe.setText(recipe.getIngredients());
        editDirectionsRecipe.setText(recipe.getDirections());


        Glide.with(context)
                .load(image)
                .into(editImageRecipe);


        String oldName = recipe.getRecipeName();
        String oldCategory = recipe.getCategory();

        Button changeImage = dialogView.findViewById(R.id.change_image);
        Button cancelButton = dialogView.findViewById(R.id.cancel_button);
        Button saveButton = dialogView.findViewById(R.id.update_btn);


        AlertDialog dialog = builder.create();

/*

        changeImage.setOnClickListener(v -> {

            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
            //startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
            //Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            MainActivity mainActivity = (MainActivity) context;
            mainActivity.onEditDialogClosed(position, recipe);
            ((MainActivity) context).startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE, imagePickerListener);


            */
/*Glide.with(context)
                    .load(mainActivity.selectedImageUri)
                    .into(editImageRecipe);*//*

            //Picasso.get().load(recipe.getPhoto()).into(editImageRecipe);

        });
*/



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


