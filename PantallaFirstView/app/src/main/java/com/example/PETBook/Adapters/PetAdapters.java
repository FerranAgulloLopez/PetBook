package com.example.PETBook.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.PETBook.Models.PetModel;
import com.example.pantallafirstview.R;

import java.util.ArrayList;

public class PetAdapters extends BaseAdapter {

    private Context context;
    private ArrayList<PetModel> petList;

    public PetAdapters(Context context, ArrayList<PetModel> petList) {
        this.context = context;
        this.petList = petList;
    }

    @Override
    public Object getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        //Aqui ficarem el switch amb les pantalles que volguem
        return petList.get(position);
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        //Retorna el numero de pantalles que tindra el scroll aquest raro
        if (petList!=null)
            return petList.size();
        return 0;
    }
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.pets_design,null);
        }
        TextView nombre = (TextView) convertView.findViewById(R.id.nombreForo);
        TextView especie = (TextView) convertView.findViewById(R.id.Especie);
        TextView edad = (TextView) convertView.findViewById(R.id.edad);


        nombre.setText(petList.get(position).getNombre());
        especie.setText(petList.get(position).getEspecie());
        edad.setText(String.format("%d",petList.get(position).getEdad()));

        return convertView;
    }
}
