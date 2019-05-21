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
import com.example.PETBook.Models.Mensaje;
import com.example.PETBook.SingletonUsuario;
import com.example.pantallafirstview.R;

import java.util.ArrayList;
import java.util.List;

public class AdapterMensajes extends RecyclerView.Adapter<HolderMensaje> {


    private List<MensajeLogic> listMensaje = new ArrayList<>();
    private Context c;

    public AdapterMensajes(Context c) {
        this.c = c;
    }

    public int addMensaje(MensajeLogic m){
        listMensaje.add(m);
        int posicion = listMensaje.size()-1;
        notifyItemInserted(listMensaje.size());
        return posicion;
    }

    public void actualizarMensaje(int posicion, MensajeLogic mensajeLogic) {
        listMensaje.set(posicion, mensajeLogic);
        notifyItemChanged(posicion);
    }


    @NonNull
    @Override
    public HolderMensaje onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        if(viewType==1){
            view = LayoutInflater.from(c).inflate(R.layout.card_view_mensajes_emisor,parent,false);
        }else{
            view = LayoutInflater.from(c).inflate(R.layout.card_view_mensajes_receptor,parent,false);
        }
        return new HolderMensaje(view);

    }

    @Override
    public void onBindViewHolder(@NonNull HolderMensaje holder, int position) {

        MensajeLogic mensajeLogic = listMensaje.get(position);
        String emailUsuarioEmisor = mensajeLogic.getMensaje().getEmailCreador();

        if (emailUsuarioEmisor != null) {
            holder.getNombre().setText(mensajeLogic.getMensaje().getEmailCreador());
        }

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


    @Override
    public int getItemViewType(int position) {
        if(listMensaje.get(position).getMensaje().getEmailCreador() != null){
            if(listMensaje.get(position).getMensaje().getEmailCreador().equals(SingletonUsuario.getInstance().getEmail())){
                return 1;
            }else{
                return -1;
            }
        }else {
            return -1;
        }
       // return super.getItemViewType(position);
    }
}