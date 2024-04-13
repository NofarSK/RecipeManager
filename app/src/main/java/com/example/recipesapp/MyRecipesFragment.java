package com.example.recipesapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.recipesapp.activity.MainActivity;
import com.example.recipesapp.adapters.CategoryAdapter;
import com.example.recipesapp.model.CategoryModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;


public class MyRecipesFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;

    public MyRecipesFragment() {
        // Required empty public constructor
    }


    public static MyRecipesFragment newInstance(String param1, String param2) {
        MyRecipesFragment fragment = new MyRecipesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_recipes, container, false);

        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.readUser();

        FloatingActionButton addBtn = view.findViewById(R.id.add_recipe_btn);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Navigation.findNavController(view).navigate(R.id.action_myRecipesFragment_to_addRecipeFragment);
            }
        });

        ArrayList<CategoryModel> dataSet = new ArrayList<>();
        RecyclerView recyclerView = view.findViewById(R.id.recycler_category);
        GridLayoutManager layoutManager = new GridLayoutManager(requireContext(), 2);
        recyclerView.setLayoutManager(layoutManager);


        for (int i = 0; i < CategoryData.categories.length ; i++ ){
            dataSet.add(new CategoryModel(
                    CategoryData.categories[i],
                    CategoryData.drawableArray[i]
            ));
        }


        CategoryAdapter adapter = new CategoryAdapter(dataSet, new CategoryAdapter.ItemClickListener() {
            @Override
            public void onItemClick(CategoryModel details) {
                Bundle bundle = new Bundle();
                bundle.putString("category" , details.getCategoryName());

                Navigation.findNavController(view).navigate(R.id.action_myRecipesFragment_to_allRecipesFragment , bundle);

            }
        });
        recyclerView.setAdapter(adapter);

        FloatingActionButton favorite = view.findViewById(R.id.favourites_btn);
        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_myRecipesFragment_to_favoriteRecipesFragment);
            }
        });

        FloatingActionButton api = view.findViewById(R.id.api_page);
        api.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_myRecipesFragment_to_recipesFromInternetFragment2);
            }
        });
        return view;
    }
}