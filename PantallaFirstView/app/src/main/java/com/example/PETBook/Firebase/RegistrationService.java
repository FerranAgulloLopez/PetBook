package com.example.PETBook.Firebase;

import android.app.IntentService;
import android.content.Intent;

import com.example.pantallafirstview.R;
import com.google.firebase.iid.FirebaseInstanceId;

public class RegistrationService extends IntentService {


    public RegistrationService() {
        super("RegistrationService");
    }



    @Override
    protected void onHandleIntent(Intent intent) {

        FirebaseInstanceId myID = FirebaseInstanceId.getInstance();


        //String registrationToken = myID.getToken(getString(R.string.gcm_defaultSenderId),)

    }
}
