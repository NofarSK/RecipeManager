package com.example.recipesapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipesapp.model.CategoryModel;
import com.example.recipesapp.R;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder> {

    private ArrayList<CategoryModel> dataSet;
    private ItemClickListener itemClickListener;

    public CategoryAdapter(ArrayList<CategoryModel> dataSet , ItemClickListener itemClickListener){
        this.dataSet = dataSet;
        this.itemClickListener = itemClickListener;
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView categoryTitle;
        ImageView imageView;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryTitle = itemView.findViewById(R.id.category_title);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }

    @NonNull
    @Override
    public CategoryAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_category , parent , false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.MyViewHolder holder, int position) {
        holder.categoryTitle.setText(dataSet.get(position).getCategoryName());
        holder.imageView.setImageResource(dataSet.get(position).getImage());

        Animation animation = AnimationUtils.loadAnimation(holder.itemView.getContext(),R.anim.anim_one);
        holder.itemView.startAnimation(animation);

        holder.itemView.setOnClickListener(v -> {
            itemClickListener.onItemClick(dataSet.get(position));
        });
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public interface ItemClickListener{
        void  onItemClick(CategoryModel details);
    }

}
