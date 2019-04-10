package com.example.PETBook.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.pantallafirstview.R;

public class PetInfoFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ID_PET = "id_pet";

    String petId;

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static PetInfoFragment newInstance(String sectionNumber) {
        PetInfoFragment fragment = new PetInfoFragment();
        Bundle args = new Bundle();
        args.putString(ID_PET, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_pets, container, false);
        TextView textView = (TextView) rootView.findViewById(R.id.section_label);

        if (getArguments() != null) {
            petId = getArguments().getString(ID_PET);
            textView.setText("Este es el pet: " + petId);

            //textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
        }
        return rootView;
    }
}
