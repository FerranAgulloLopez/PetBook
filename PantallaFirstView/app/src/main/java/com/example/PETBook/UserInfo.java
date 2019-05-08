package com.example.PETBook;

import android.support.v7.app.AppCompatActivity;
import com.example.PETBook.Models.FriendRequestModel;
import android.widget.TextView;
import android.os.Bundle;
import com.example.pantallafirstview.R;

public class UserInfo extends AppCompatActivity {

    private TextView txtTitle;
    private TextView txtDescription;
    private TextView txtLoc;
    private FriendRequestModel friend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        txtTitle = (TextView) findViewById(R.id.textTitulo);
        txtDescription = (TextView) findViewById(R.id.textDesc);
        txtLoc = (TextView) findViewById(R.id.textLoc);

        recibirDatos();
    }

    private void recibirDatos(){
        Bundle datosRecibidos = this.getIntent().getExtras();
        if(datosRecibidos != null) {
            friend = (FriendRequestModel) datosRecibidos.getSerializable("friendRequestSelected");
            txtTitle.setText(friend.getName());
            txtDescription.setText(friend.getSurnames());
            txtLoc.setText(friend.getEmail());
        }
    }
}
