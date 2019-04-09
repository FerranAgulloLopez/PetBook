package com.example.PETBook.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.PETBook.Fragments.PetInfoFragment;
import com.example.PETBook.Models.PetModel;
import com.example.PETBook.PetsContainer;

import java.util.List;

public class PetAdapters extends FragmentPagerAdapter {

    private List<PetModel> petList;
    public PetAdapters(FragmentManager fm) {
        super(fm);
    }

    public PetAdapters(FragmentManager fm, List<PetModel> petList) {
        super(fm);
        this.petList = petList;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        //Aqui ficarem el switch amb les pantalles que volguem

        return PetInfoFragment.newInstance(petList.get(position).getId());
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        //Retorna el numero de pantalles que tindra el scroll aquest raro
        if (petList!=null)
            return petList.size();
        return 0;
    }
}
