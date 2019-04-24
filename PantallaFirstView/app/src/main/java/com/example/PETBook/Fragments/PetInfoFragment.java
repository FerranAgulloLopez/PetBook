package com.example.PETBook.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.pantallafirstview.R;

import org.w3c.dom.Text;

public class PetInfoFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ID_PET = "id_pet";
    private static final String NAME_PET = "name_pet";
    private static final String AGE_PET = "age_pet";
    private static final String SEX_PET = "sex_pet";
    private static final String TYPE_PET = "type_pet";
    private static final String COLOR_PET = "color_pet";
    private static final String RACE_PET= "race_pet";
    private static final String DESCRIPTION_PET = "decription_pet";

    String petId;
    String petName;
    String petAge;
    String petSex;
    String petType;
    String petColor;
    String petRace;
    String petDescription;

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


        //TextView textView = (TextView) rootView.findViewById(R.id.section_label);
        TextView name = rootView.findViewById(R.id.petName);
        TextView age = rootView.findViewById(R.id.petAge);
        TextView sex = rootView.findViewById(R.id.petSex);
        TextView type = rootView.findViewById(R.id.petType);
        TextView color = rootView.findViewById(R.id.petColor);
        TextView race = rootView.findViewById(R.id.petRace);
        TextView description = rootView.findViewById(R.id.petDescription);


        if (getArguments() != null) {
            petId = getArguments().getString(ID_PET);
            petName = getArguments().getString(NAME_PET);
            petAge = getArguments().getString(AGE_PET);
            petSex = getArguments().getString(SEX_PET);
            petType = getArguments().getString(TYPE_PET);
            petColor = getArguments().getString(COLOR_PET);
            petRace = getArguments().getString(RACE_PET);
            petDescription = getArguments().getString(DESCRIPTION_PET);
            name.setText( petName);
            age.setText(petAge);
            sex.setText(petSex);
            type.setText(petType);
            color.setText(petColor);
            race.setText(petRace);
            description.setText(petDescription);

            //textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
        }

        return rootView;


    }
}
