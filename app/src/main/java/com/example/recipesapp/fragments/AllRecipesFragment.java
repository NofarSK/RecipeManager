package com.example.recipesapp.fragments;

import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.recipesapp.R;
import com.example.recipesapp.model.Recipe;
import com.example.recipesapp.adapters.RecipeAdapter;
import com.example.recipesapp.activity.MainActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;


public class AllRecipesFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ArrayList<Recipe> recipeList;
    private RecipeAdapter adapter;

    public AllRecipesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AllRecipesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AllRecipesFragment newInstance(String param1, String param2) {
        AllRecipesFragment fragment = new AllRecipesFragment();
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
        View view = inflater.inflate(R.layout.fragment_all_recipes, container, false);

        TextView title = view.findViewById(R.id.all_recipes_tv);
        String category;
        category = getArguments().getString("category");

        title.setText(category + getString(R.string.recipes));

        RecyclerView recyclerView = view.findViewById(R.id.recycler_recipes);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        recipeList = new ArrayList<>();

        EditText search = view.findViewById(R.id.search);

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });

        adapter = new RecipeAdapter(recipeList, new RecipeAdapter.ItemClickListener() {
            @Override
            public void onItemClick(Recipe details) {
                Bundle bundle = new Bundle();
                bundle.putString("category", details.getCategory());
                bundle.putString("nameRecipe", details.getRecipeName());
                bundle.putString("flag", "1");
                bundle.putString("image", details.getPhoto());
                Navigation.findNavController(view).navigate(R.id.action_allRecipesFragment_to_detailsRecipeFragment, bundle);
            }
        }, requireContext());
        recyclerView.setAdapter(adapter);

        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.readRecipe(category , recipeList , adapter);


        FloatingActionButton favoritePage = view.findViewById(R.id.favourites_page_recipes);
        FloatingActionButton apiRecipe = view.findViewById(R.id.api_page_recipes);
        FloatingActionButton categoryRecipe = view.findViewById(R.id.categories_recipe_btn);

        favoritePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_allRecipesFragment_to_favoriteRecipesFragment);
            }
        });

        apiRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_allRecipesFragment_to_recipesFromInternetFragment2);
            }
        });

        categoryRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_allRecipesFragment_to_myRecipesFragment);
            }
        });

        return view;
    }

    private void filter(String text)
    {
        ArrayList<Recipe> filteredList = new ArrayList<>();

        for (Recipe data : recipeList){
            if (data.getRecipeName().toLowerCase().contains(text.toLowerCase()) ||
                    data.getIngredients().toLowerCase().contains(text.toLowerCase())){

                filteredList.add(data);
            }
        }
        adapter.filterList(filteredList);
    }
}