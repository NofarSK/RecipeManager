package com.example.recipesapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.recipesapp.R;
import com.example.recipesapp.model.Recipe;
import com.example.recipesapp.activity.MainActivity;

import java.util.ArrayList;

public class InternetRecipesAdapter extends RecyclerView.Adapter<InternetRecipesAdapter.MyViewHolder> {

    private ArrayList<Recipe> apiDataSet;
    private ItemClickListener itemClickListener;

    public InternetRecipesAdapter(ArrayList<Recipe> apiDataSet , ItemClickListener itemClickListener){

        this.apiDataSet = apiDataSet;
        this.itemClickListener = itemClickListener;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView recipeName;
        ImageButton favoriteBtn;
        ImageView recipeImage;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            recipeName = itemView.findViewById(R.id.internetRecipeName);
            favoriteBtn = itemView.findViewById(R.id.card_favourite_btn);
            recipeImage = itemView.findViewById(R.id.internetImageView);

        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_internet_recipes , parent , false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Recipe recipe = apiDataSet.get(position);
        holder.recipeName.setText(apiDataSet.get(position).getRecipeName());

        Animation animation = AnimationUtils.loadAnimation(holder.itemView.getContext(),R.anim.anim_one);
        holder.itemView.startAnimation(animation);

        holder.itemView.setOnClickListener(v -> {
            itemClickListener.onItemClick(apiDataSet.get(position));
        });

        holder.favoriteBtn.setOnClickListener(v -> {
            MainActivity mainActivity = (MainActivity) v.getContext();
            mainActivity.writeFavoriteRecipe(recipe);
        });

        if (recipe.getPhoto() != null && !recipe.getPhoto().isEmpty()) {
            Glide.with(holder.itemView.getContext())
                    .load(recipe.getPhoto())
                    .into(holder.recipeImage);
        } else {
            holder.recipeImage.setImageResource(R.mipmap.ic_launcher);
        }
    }

    @Override
    public int getItemCount() {
        return apiDataSet.size();
    }

    public interface ItemClickListener{
        void  onItemClick(Recipe details);
    }

    public void filterList(ArrayList<Recipe> filteredList){
        apiDataSet = filteredList;
        notifyDataSetChanged();
    }
}
