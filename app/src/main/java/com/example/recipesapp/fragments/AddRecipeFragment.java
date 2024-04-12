package com.example.recipesapp.fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.recipesapp.R;
import com.example.recipesapp.activity.MainActivity;

public class AddRecipeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int REQUEST_CODE_PICK_IMAGE = 1001;
    private String selectedCategory ;
    private Uri imageUri = null;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AddRecipeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddRecipeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddRecipeFragment newInstance(String param1, String param2) {
        AddRecipeFragment fragment = new AddRecipeFragment();
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
        View view = inflater.inflate(R.layout.fragment_add_recipe, container, false);

        Spinner category = view.findViewById(R.id.category_select);

        String[] categories = {getString(R.string.select_a_category),getString(R.string.chicken), getString(R.string.meat), getString(R.string.fish), getString(R.string.desserts), getString(R.string.soups),
                getString(R.string.salads), getString(R.string.pastries), getString(R.string.pastas), getString(R.string.pizzas)};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item , categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        category.setAdapter(adapter);


        category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                selectedCategory = (String) parentView.getItemAtPosition(position);

                if (!selectedCategory.equals(getString(R.string.select_a_category))) {
                    Toast.makeText(requireContext(), getString(R.string.selected_category) + selectedCategory, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });

        Button image = view.findViewById(R.id.pick_image);

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
            }
        });


        Button addRecipe = view.findViewById(R.id.add_btn);

        addRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity mainActivity = (MainActivity) getActivity();
                mainActivity.writeRecipe(selectedCategory , imageUri);
                Navigation.findNavController(view).navigate(R.id.action_addRecipeFragment_to_myRecipesFragment);
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            imageUri = data.getData();

            ImageView imageView = requireView().findViewById(R.id.image_food);
            imageView.setImageURI(imageUri);
        }
    }
}