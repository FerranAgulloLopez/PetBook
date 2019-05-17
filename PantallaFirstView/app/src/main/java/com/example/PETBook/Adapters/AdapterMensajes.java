package com.example.PETBook.Adapters;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.PETBook.Holders.HolderMensaje;
import com.example.PETBook.Models.Logic.MensajeLogic;
import com.example.pantallafirstview.R;

import java.util.ArrayList;
import java.util.List;

public class AdapterMensajes extends RecyclerView.Adapter<HolderMensaje> {


    private List<MensajeLogic> listMensaje = new ArrayList<>();
    private Context c;

    public AdapterMensajes(Context c) {
        this.c = c;
    }

    public void addMensaje(MensajeLogic m){
        listMensaje.add(m);
        notifyItemInserted(listMensaje.size());
    }


    @NonNull
    @Override
    public HolderMensaje onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(c).inflate(R.layout.card_view_mensajes,parent,false);
        return new HolderMensaje(v);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderMensaje holder, int position) {

        MensajeLogic mensajeLogic = listMensaje.get(position);

        holder.getNombre().setText(mensajeLogic.getEmailCreador());
        holder.getMensaje().setText(mensajeLogic.getMensaje().getMensaje());

        if (mensajeLogic.getMensaje().isContineFoto()) {
            holder.getFotoMensaje().setVisibility(View.VISIBLE);
            holder.getMensaje().setVisibility(View.VISIBLE);
            Glide.with(c).load(mensajeLogic.getMensaje().getUrlFoto()).into(holder.getFotoMensaje());
        }
        else {
            holder.getFotoMensaje().setVisibility(View.GONE);
            holder.getMensaje().setVisibility(View.VISIBLE);
        }

        /*
        Poner foto de perfil
         */

        //Glide.with(c).load(urlFotoPerfil).into(holder.getFotoMensajePerfil());

        holder.getHora().setText(mensajeLogic.fechaDeCreacionDelMensaje());

    }

    @Override
    public int getItemCount() {
        return listMensaje.size();
    }
}