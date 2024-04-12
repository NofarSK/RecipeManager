package com.example.recipesapp.fragments;

import android.os.Bundle;

import androidx.annotation.DrawableRes;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.recipesapp.R;
import com.example.recipesapp.activity.MainActivity;


public class DetailsRecipeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DetailsRecipeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DetailsRecipeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DetailsRecipeFragment newInstance(String param1, String param2) {
        DetailsRecipeFragment fragment = new DetailsRecipeFragment();
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
        View view = inflater.inflate(R.layout.fragment_details_recipe, container, false);

        String category = getArguments().getString("category");
        String nameRecipe = getArguments().getString("nameRecipe");
        String flag = getArguments().getString("flag");
        String image = getArguments().getString("image");

        ImageView imageRecipe = view.findViewById(R.id.detail_image);
        ImageButton back = view.findViewById(R.id.back);

        if (flag.equals("1")){

            MainActivity mainActivity = (MainActivity) getActivity();
            mainActivity.readRecipeDetails(category,nameRecipe);

            Glide.with(requireContext())
                    .load(image)
                    .into(imageRecipe);

            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putString("category" , category);
                    Navigation.findNavController(view).navigate(R.id.action_detailsRecipeFragment_to_allRecipesFragment,bundle);
                }
            });
        }
        else if (flag.equals("2")){

            String ingredients = getArguments().getString("ingredients");
            String directions = getArguments().getString("directions");


            TextView NameRecipe = view.findViewById(R.id.detail_recipe_name);
            TextView timeRecipe = view.findViewById(R.id.detail_time);
            TextView ingredientsRecipe = view.findViewById(R.id.detail_ingredients);
            TextView directionsRecipe = view.findViewById(R.id.detail_directions);
            ImageView time = view.findViewById(R.id.time);

            time.setVisibility(View.INVISIBLE);
            NameRecipe.setText(nameRecipe);
            timeRecipe.setText("");
            ingredientsRecipe.setText(ingredients);
            directionsRecipe.setText(directions);

            Glide.with(requireContext())
                    .load(image)
                    .into(imageRecipe);

            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Navigation.findNavController(view).navigateUp();
                }
            });
        }
        else {

            MainActivity mainActivity = (MainActivity) getActivity();
            mainActivity.readFavoriteRecipeDetails(nameRecipe);
            Glide.with(requireContext())
                    .load(image)
                    .into(imageRecipe);

            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Navigation.findNavController(view).navigateUp();
                }
            });
        }

        return view;
    }
}