package com.example.PETBook;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.PETBook.Adapters.AdapterMensajes;
import com.example.PETBook.Models.Image;
import com.example.PETBook.Models.Logic.MensajeLogic;
import com.example.PETBook.Models.Mensaje;
import com.example.PETBook.Utilidades.Constants;
import com.example.pantallafirstview.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import de.hdodenhof.circleimageview.CircleImageView;

public class Chat extends AppCompatActivity {


    private CircleImageView fotoPerfil;
    private TextView nombre;
    private RecyclerView rvMensajes;
    private EditText txtMensaje;
    private Button btnEnviar;
    private AdapterMensajes adapter;
    private ImageButton btnEnviarFoto;

    private FirebaseDatabase database;
    private DatabaseReference databaseReferenceEmisor;
    private DatabaseReference databaseReferenceReceptor;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private static final int PHOTO_SEND = 1;
    private static final int PHOTO_PERFIL = 2;
    private String fotoPerfilCadena;

    private String emailReceptor;
    private String emailEmisor;
    private String nameReceptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Bitmap fotoPerfilBitmap = null;

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            emailReceptor = getEmailWithoutDotCom(bundle.getString("emailReceptor"));
            nameReceptor = bundle.getString("nameReceptor");
            fotoPerfilBitmap = Image.getInstance().StringToBitMap(bundle.getString("fotoPerfilReceptor"));
        }
        else {
            finish();
        }



        fotoPerfil = (CircleImageView) findViewById(R.id.fotoPerfil);
        //fotoPerfil.setImageBitmap(fotoPerfilBitmap);
        nombre = (TextView) findViewById(R.id.nombre);
        rvMensajes = (RecyclerView) findViewById(R.id.rvMensajes);
        txtMensaje = (EditText) findViewById(R.id.txtMensaje);
        btnEnviar = (Button) findViewById(R.id.btnEnviar);
        btnEnviarFoto = (ImageButton) findViewById(R.id.btnEnviarFoto);
        fotoPerfilCadena = "";

        emailEmisor = getEmailWithoutDotCom(SingletonUsuario.getInstance().getEmail());

        database = FirebaseDatabase.getInstance();
        databaseReferenceEmisor = database.getReference(Constants.NODO_MENSAJES + "/" + emailEmisor + "/" + emailReceptor); //Sala de chat (nombre)
        databaseReferenceReceptor = database.getReference(Constants.NODO_MENSAJES + "/" + emailReceptor + "/" + emailEmisor); //Sala de chat (nombre)
        //databaseReference.orderByChild("createdTimestamp");

        adapter = new AdapterMensajes(this);
        LinearLayoutManager l = new LinearLayoutManager(this);
        rvMensajes.setLayoutManager(l);
        rvMensajes.setAdapter(adapter);
        // Nombre del usuario con quien se está chateando
        nombre.setText(nameReceptor);


        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mensajeToSend = txtMensaje.getText().toString();
                if (!mensajeToSend.isEmpty()) {
                    SingletonUsuario user = SingletonUsuario.getInstance();
                    Mensaje mensaje = new Mensaje();
                    mensaje.setMensaje(mensajeToSend);
                    //mensaje.setUrlFoto();
                    mensaje.setContineFoto(false);
                    mensaje.setEmailCreador(user.getEmail());
                    databaseReferenceEmisor.push().setValue(mensaje);
                    databaseReferenceReceptor.push().setValue(mensaje);
                    txtMensaje.setText("");
                }
            }
        });

        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                setScrollbar();
            }
        });


        FirebaseDatabase.
                getInstance().
                getReference(Constants.NODO_MENSAJES).
                child(emailEmisor).
                child(emailReceptor).addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                final Mensaje mensaje = dataSnapshot.getValue(Mensaje.class);
                final MensajeLogic mensajeLogic = new MensajeLogic(dataSnapshot.getKey(), mensaje);
                final int posicion = adapter.addMensaje(mensajeLogic);


                //adapter.addMensaje(mensajeLogic);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }




    private String getEmailWithoutDotCom(String email) {
        return email.substring(0, email.length() - 4 );
    }


    private void setScrollbar() {
        rvMensajes.scrollToPosition(adapter.getItemCount()-1);
    }

}

