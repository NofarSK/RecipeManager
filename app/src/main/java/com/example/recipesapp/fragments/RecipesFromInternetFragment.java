package com.example.recipesapp.fragments;

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

import com.example.recipesapp.DataService;
import com.example.recipesapp.adapters.InternetRecipesAdapter;
import com.example.recipesapp.R;
import com.example.recipesapp.model.Recipe;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RecipesFromInternetFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecipesFromInternetFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ArrayList<Recipe> recipesApi;
    private InternetRecipesAdapter adapter;

    public RecipesFromInternetFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RecipesFromInternetFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RecipesFromInternetFragment newInstance(String param1, String param2) {
        RecipesFromInternetFragment fragment = new RecipesFromInternetFragment();
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
        View view = inflater.inflate(R.layout.fragment_recipes_from_internet, container, false);

        recipesApi = new ArrayList<>();
        RecyclerView recyclerView = view.findViewById(R.id.recipes_api_recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        recyclerView.setLayoutManager(layoutManager);


        recipesApi = DataService.getArrRecipe();


        EditText search = view.findViewById(R.id.search_api);

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });

        adapter = new InternetRecipesAdapter(recipesApi, new InternetRecipesAdapter.ItemClickListener() {
            @Override
            public void onItemClick(Recipe details) {
                Bundle bundle = new Bundle();
                bundle.putString("category", details.getCategory());
                bundle.putString("nameRecipe", details.getRecipeName());
                bundle.putString("flag" , "2");
                bundle.putString("ingredients" , details.getIngredients());
                bundle.putString("directions" , details.getDirections());
                bundle.putString("image" , details.getPhoto());
                Navigation.findNavController(view).navigate(R.id.action_recipesFromInternetFragment2_to_detailsRecipeFragment, bundle);
            }
        });
        recyclerView.setAdapter(adapter);

        FloatingActionButton favoritePage = view.findViewById(R.id.favourites_page_api);
        FloatingActionButton categoryPage = view.findViewById(R.id.categories_api_btn);

        favoritePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_recipesFromInternetFragment2_to_favoriteRecipesFragment);
            }
        });

        categoryPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_recipesFromInternetFragment2_to_myRecipesFragment);
            }
        });

        return view;
    }

    private void filter(String text)
    {
        ArrayList<Recipe> filteredList = new ArrayList<>();
        for (Recipe data : recipesApi){
            if (data.getRecipeName().toLowerCase().contains(text.toLowerCase()) ||
                    data.getIngredients().toLowerCase().contains(text.toLowerCase())){

                filteredList.add(data);
            }
        }

        adapter.filterList(filteredList);
    }
}