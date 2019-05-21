package com.example.PETBook.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.PETBook.Models.EventModel;
import com.example.pantallafirstview.R;

import java.util.ArrayList;

public class EventAdapterCalendar extends BaseAdapter {

    private Context context;
    private ArrayList<EventModel> events;

    public EventAdapterCalendar(Context context, ArrayList<EventModel> array){
        this.context = context;
        events = array;
    }

    @Override
    public int getCount() {
        return events.size();
    }

    @Override
    public Object getItem(int position) {
        return events.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.event_design_calendar,null);
        }
        TextView titulo = (TextView) convertView.findViewById(R.id.e_titulo);
        TextView fecha = (TextView) convertView.findViewById(R.id.e_fecha);
        TextView localizacion = (TextView) convertView.findViewById(R.id.e_localizacion);
        TextView miembros = (TextView) convertView.findViewById(R.id.e_miembros);

        titulo.setText(events.get(position).getTitulo());
        fecha.setText(events.get(position).getFecha());
        localizacion.setText(events.get(position).getDireccion());
        miembros.setText(String.format("%d",events.get(position).getMiembros().size()));
        return convertView;
    }


}
