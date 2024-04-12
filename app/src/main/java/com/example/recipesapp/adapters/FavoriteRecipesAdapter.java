package com.example.recipesapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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

import java.util.ArrayList;

public class FavoriteRecipesAdapter extends RecyclerView.Adapter<FavoriteRecipesAdapter.MyViewHolder> {

    private ArrayList<Recipe> dateSet;
    private ItemClickListener itemClickListener;

    public FavoriteRecipesAdapter(ArrayList<Recipe> dateSet , ItemClickListener itemClickListener){
        this.dateSet = dateSet;
        this.itemClickListener = itemClickListener;
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView recipeName;
        ImageButton removeBtn;
        ImageView recipeImage;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            recipeName = itemView.findViewById(R.id.favoriteRecipeName);
            removeBtn = itemView.findViewById(R.id.favorite_delete_recipe);
            recipeImage = itemView.findViewById(R.id.favoriteImageView);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_favorite_recipes , parent , false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Recipe recipe = dateSet.get(position);
        holder.recipeName.setText(dateSet.get(position).getRecipeName());

        Animation animation = AnimationUtils.loadAnimation(holder.itemView.getContext(),R.anim.anim_one);
        holder.itemView.startAnimation(animation);

        holder.itemView.setOnClickListener(v -> {
            itemClickListener.onItemClick(dateSet.get(position));
        });

        if (recipe.getPhoto() != null && !recipe.getPhoto().isEmpty()) {
            Glide.with(holder.itemView.getContext())
                    .load(recipe.getPhoto())
                    .into(holder.recipeImage);
        } else {
            holder.recipeImage.setImageResource(R.mipmap.ic_launcher);
        }

        holder.removeBtn.setOnClickListener(v -> {

            String recipeName = recipe.getRecipeName();

            MainActivity mainActivity = (MainActivity) holder.itemView.getContext();
            mainActivity.deleteFavoriteRecipe(recipeName);

            dateSet.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, dateSet.size());

            Toast.makeText(holder.itemView.getContext(), holder.itemView.getContext().getString(R.string.recipe_deleted), Toast.LENGTH_SHORT).show();
        });

    }

    @Override
    public int getItemCount() {
        return dateSet.size();
    }

    public interface ItemClickListener{
        void  onItemClick(Recipe details);
    }
}
