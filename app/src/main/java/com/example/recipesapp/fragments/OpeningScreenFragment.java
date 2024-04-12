package com.example.recipesapp.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.recipesapp.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OpeningScreenFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OpeningScreenFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public OpeningScreenFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OpeningScreenFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OpeningScreenFragment newInstance(String param1, String param2) {
        OpeningScreenFragment fragment = new OpeningScreenFragment();
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
        View view = inflater.inflate(R.layout.fragment_opening_screen, container, false);

        Button login = view.findViewById(R.id.login_button);
        Button signUp = view.findViewById(R.id.sign_up_button);

        TextView title = view.findViewById(R.id.title_app);
        ImageView icon = view.findViewById(R.id.icon_app);
        TextView sentence = view.findViewById(R.id.sentence);

        Animation toBottom = AnimationUtils.loadAnimation(requireContext() ,R.anim.top_to_bottom);
        Animation toTop = AnimationUtils.loadAnimation(requireContext() ,R.anim.btn_to_top);

        title.startAnimation(toBottom);
        icon.startAnimation(toBottom);
        login.startAnimation(toTop);
        signUp.startAnimation(toTop);
        sentence.startAnimation(toTop);


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_openingScreenFragment_to_loginFragment);
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_openingScreenFragment_to_registerFragment);
            }
        });

        return view;
    }
}