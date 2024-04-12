package com.example.recipesapp.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.recipesapp.adapters.FavoriteRecipesAdapter;
import com.example.recipesapp.R;
import com.example.recipesapp.model.Recipe;
import com.example.recipesapp.activity.MainActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FavoriteRecipesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FavoriteRecipesFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FavoriteRecipesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FavoriteRecipesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FavoriteRecipesFragment newInstance(String param1, String param2) {
        FavoriteRecipesFragment fragment = new FavoriteRecipesFragment();
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
        View view = inflater.inflate(R.layout.fragment_favorite_recipes, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.favourites_recipes_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        ArrayList<Recipe> recipeFavoriteList = new ArrayList<>();

        FavoriteRecipesAdapter adapter = new FavoriteRecipesAdapter(recipeFavoriteList, new FavoriteRecipesAdapter.ItemClickListener() {
            @Override
            public void onItemClick(Recipe details) {
                Bundle bundle = new Bundle();
                bundle.putString("nameRecipe", details.getRecipeName());
                bundle.putString("flag" , "3");
                bundle.putString("image" , details.getPhoto());
                Navigation.findNavController(view).navigate(R.id.action_favoriteRecipesFragment_to_detailsRecipeFragment, bundle);
            }
        });
        recyclerView.setAdapter(adapter);

        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.readFavoriteRecipe(recipeFavoriteList , adapter);

        FloatingActionButton apiPage = view.findViewById(R.id.api_page_favorites);
        FloatingActionButton categoryPage = view.findViewById(R.id.categories_btn);

        apiPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_favoriteRecipesFragment_to_recipesFromInternetFragment2);
            }
        });

        categoryPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_favoriteRecipesFragment_to_myRecipesFragment);
            }
        });

        return view;
    }
}