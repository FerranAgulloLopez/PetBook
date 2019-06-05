package com.example.PETBook.Calendar;

import com.example.PETBook.Conexion;
import com.example.PETBook.Controllers.AsyncResult;
import com.example.PETBook.Models.EventModel;
import com.example.PETBook.SingletonUsuario;
import com.example.pantallafirstview.R;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Calendar;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import org.json.JSONArray;
import org.json.JSONObject;
import org.threeten.bp.LocalDate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Sample activity for Google Calendar API v3. It demonstrates how to use authorization to list
 * calendars, add a new calendar, and edit or delete an existing calendar with the user's
 * permission.
 *
 * <p>
 * <b>Warning:</b> this sample is for illustrative purposes only. Please instead use the native
 * Calendar APIs like the <a
 * href="http://developer.android.com/guide/topics/providers/calendar-provider.html">Calendar
 * Provider API</a>.
 * </p>
 *
 * @author Yaniv Inbar
 */
public final class CalendarSync extends Activity implements AsyncResult {

    /**
     * Logging level for HTTP requests/responses.
     *
     * <p>
     * To turn on, set to {@link Level#CONFIG} or {@link Level#ALL} and run this from command line:
     * </p>
     *
     * <pre>
     * adb shell setprop log.tag.HttpTransport DEBUG
     * </pre>
     */

    private static final String APP_NAME = "pes-calendario";

    private static final Level LOGGING_LEVEL = Level.OFF;

    private static final String PREF_ACCOUNT_NAME = "mbenalifib@gmail.com";

    static final String TAG = "CalendarSync";

    private static final int CONTEXT_EDIT = 0;

    private static final int CONTEXT_DELETE = 1;

    private static final int CONTEXT_BATCH_ADD = 2;

    static final int REQUEST_GOOGLE_PLAY_SERVICES = 0;

    static final int REQUEST_AUTHORIZATION = 1;

    static final int REQUEST_ACCOUNT_PICKER = 2;

    final HttpTransport transport = AndroidHttp.newCompatibleTransport();

    final JsonFactory jsonFactory = GsonFactory.getDefaultInstance();

    GoogleAccountCredential credential;

    CalendarModel model = new CalendarModel();

    ArrayAdapter<CalendarInfo> adapter;

    com.google.api.services.calendar.Calendar client;

    public static final String SUMMARY = "PETBOOK"; // Nombre del calendario


    int numAsyncTasks;

    public String llamada = null;

    public static final String CONEXION_UpdateCalendarId = "UpdateCalendarId";
    public static final String CONEXION_getUserGoogleCalendarID = "getUserGoogleCalendarID";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // enable logging
        Logger.getLogger("com.google.api.client").setLevel(LOGGING_LEVEL);
        // view and menu


        //setContentView(R.layout.activity_calendar_sync);

        // view and menu
        setContentView(R.layout.calendarlist);

        // Google Accounts
        credential =// QUiza meter aqi getApplicationContext() por el this
                GoogleAccountCredential.usingOAuth2(getApplicationContext(), Collections.singleton(CalendarScopes.CALENDAR))
                        .setBackOff(new ExponentialBackOff()); // Hay que usar CalendarScopes.CALENDAR_EVENTS para tener permisos de escritura sobre eventos
        SharedPreferences settings = getPreferences(Context.MODE_PRIVATE);
        credential.setSelectedAccountName(settings.getString(PREF_ACCOUNT_NAME, null));
        // Calendar client
        client = new com.google.api.services.calendar.Calendar.Builder(
                transport, jsonFactory, credential).setApplicationName(APP_NAME) // No se si este nombre es correcto
                .build();

    }

    void showGooglePlayServicesAvailabilityErrorDialog(final int connectionStatusCode) {
        runOnUiThread(new Runnable() {
            public void run() {
                Dialog dialog = GooglePlayServicesUtil.getErrorDialog(
                        connectionStatusCode, CalendarSync.this, REQUEST_GOOGLE_PLAY_SERVICES);
                dialog.show();
            }
        });
    }
    private boolean llamado = false;

    @Override
    protected void onResume() {
        super.onResume();

        if (checkGooglePlayServicesAvailable() && !llamado) {
            llamado = true;
            haveGooglePlayServices();
        }
    }

    private void createCalendarANDInsertEvents() {

        Conexion con = new Conexion(this);
        llamada = CONEXION_getUserGoogleCalendarID;
        con.execute("http://10.4.41.146:9999/ServerRESTAPI/events/getUserGoogleCalendarID", "GET", null);
        //String su = SingletonUsuario.getInstance().getEmail();
        //con.execute("http://10.4.41.146:9999/ServerRESTAPI/events/GetEventsByParticipant?mail=" + su,"GET", null);
    /*
        String id_existente = "328r59ggesgnonbcd2fa9ufnf4@group.calendar.google.com";

        String id = id_existente;
        boolean exist = true;

        OnprocessFinish2(id, exist);
*/

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_GOOGLE_PLAY_SERVICES:
                if (resultCode == Activity.RESULT_OK) {
                    haveGooglePlayServices();
                } else {
                    checkGooglePlayServicesAvailable();
                }
                break;
            case REQUEST_AUTHORIZATION:
                if (resultCode == Activity.RESULT_OK) {
                    createCalendarANDInsertEvents();
                } else {
                    chooseAccount();
                }
                break;
            case REQUEST_ACCOUNT_PICKER:
                if (resultCode == Activity.RESULT_OK && data != null && data.getExtras() != null) {
                    String accountName = data.getExtras().getString(AccountManager.KEY_ACCOUNT_NAME);
                    if (accountName != null) {
                        //credential.setSelectedAccountName(accountName);
                        credential.setSelectedAccount(new Account(accountName, "com.example.PETBook"));
                        System.out.println("\nACCOUNT: " + credential.getSelectedAccountName());
                        System.out.println("ACCOUNT-: " + credential.getSelectedAccountName());
                        System.out.println("ACCOUNT33-: " + credential.getSelectedAccountName());
                        System.out.println("ACCOUNT323-: " + credential.getSelectedAccountName());
                        Account acc = credential.getSelectedAccount();
                        String aname = acc.name;

                        System.out.println("\nname: " + credential.getSelectedAccountName());
                        System.out.println("name-: " + credential.getSelectedAccountName());
                        System.out.println("name-: " + credential.getSelectedAccountName());
                        System.out.println("name-: " + credential.getSelectedAccountName());

                        //SharedPreferences settings = this.getPreferences(Context.MODE_PRIVATE);
                        SharedPreferences settings = this.getSharedPreferences("com.example.PETBook",Context.MODE_PRIVATE);

                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString(PREF_ACCOUNT_NAME, accountName);
                        //editor.commit();
                        editor.apply();
                        createCalendarANDInsertEvents();
                    }
                }
                break;
        }
    }


    /**
     * Check that Google Play services APK is installed and up to date.
     */
    private boolean checkGooglePlayServicesAvailable() {
        final int connectionStatusCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (GooglePlayServicesUtil.isUserRecoverableError(connectionStatusCode)) {
            showGooglePlayServicesAvailabilityErrorDialog(connectionStatusCode);
            return false;
        }
        return true;
    }


    private void chooseAccount() {
        startActivityForResult(credential.newChooseAccountIntent(), REQUEST_ACCOUNT_PICKER);
    }


    private void haveGooglePlayServices() {
        // check if there is already an account selected
        if (credential.getSelectedAccountName() == null) {
            // ask user to choose account
            chooseAccount();
        } else {
            // load calendars
            createCalendarANDInsertEvents();
        }
    }


    public void refreshView() {
        // Lo que sea, un text view o algo
    }


    @Override
    public void OnprocessFinish(JSONObject json) {
        switch (llamada) {
            case CONEXION_getUserGoogleCalendarID:
                try {
                    if (json.getInt("code") == 200) {
                        String googleCalendarID;
                        boolean existe;
                        List<EventModel> events;

                        googleCalendarID = json.getString("googleCalendarID");
                        existe = json.getBoolean("hasGoogleCalendar");
                        //googleCalendarID = "2";
                        //existe = false;

                        events = new ArrayList<>(); // ArrayList<EventsModel>()
                        JSONArray jsonArray = json.getJSONArray("events");
                        for(int i = 0; i < jsonArray.length(); ++i){
                            JSONObject evento = jsonArray.getJSONObject(i);
                            EventModel e = new EventModel();
                            e.setId(evento.getInt("id"));
                            e.setTitulo(evento.getString("title"));
                            e.setDescripcion(evento.getString("description"));

                            e.setFecha(obtainDateANDHour(evento.getString("date")));

                            String dateStr = evento.getString("date");
                            Date dates =obtainDate(dateStr);
                            e.setDate(dates);

                            JSONObject loc = evento.getJSONObject("localization");
                            e.setDireccion(loc.getString("address"));
                            e.setCoordenadas(loc.getDouble("longitude"),loc.getDouble("latitude"));
                            e.setPublico(evento.getBoolean("public"));
                            JSONArray m = evento.getJSONArray("participants");
                            ArrayList<String> miembros = new ArrayList<String>();
                            for(int j = 0; j < m.length(); ++j){
                                miembros.add(m.getString(j));
                            }
                            e.setMiembros(miembros);
                            e.setCreador(evento.getString("creatorMail"));

                            events.add(e);
                        }

                        /*
                        String id_existente = "328r59ggesgnonbcd2fa9ufnf4@group.calendar.google.com";
                        googleCalendarID = id_existente;
                        existe = true;
                        */

                        Calendar calendar = new Calendar();
                        //if (existe) calendar.setId(googleCalendarID);
                        calendar.setSummary(SUMMARY); // Titulo
                        new AsyncInsertCalendar(this, calendar, existe, events, googleCalendarID).execute();


                        System.out.println(json.getInt("code") + " Id Google Calendar bien conseguido\n");
                    } else {
                        System.out.println("El sistema no logra recojer el id del Calendario, exista o no, del usuario.");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
            case CONEXION_UpdateCalendarId:

                break;
        }


    }
















    // Obtains year-month-dayOfTheYear
    private LocalDate obtainLocalDate(String dateStr) {
        String date = dateStr.substring(0,10);

        String string_year = date.substring(0,4);
        String string_month = date.substring(5,7);
        String string_dayOfTheYear = date.substring(8,10);

        int year = Integer.parseInt(string_year);
        int month = Integer.parseInt(string_month);
        int dayOfTheYear = Integer.parseInt(string_dayOfTheYear);

        LocalDate localDate = LocalDate.of(year, month, dayOfTheYear);
        return localDate;
    }

    // Obtains year-month-dayOfTheYear
    private Date obtainDate(String dateStr) {
        String date = dateStr.substring(0,9+1);

        String string_year = date.substring(0,3+1);
        String string_month = date.substring(5,6+1);
        String string_dayOfTheYear = date.substring(8,9+1);
        String string_hour = dateStr.substring(11,12+1);
        String string_min = dateStr.substring(14,15+1);

        int year = Integer.parseInt(string_year);
        int month = Integer.parseInt(string_month);
        int dayOfTheYear = Integer.parseInt(string_dayOfTheYear);
        int hour = Integer.parseInt(string_hour);
        int min = Integer.parseInt(string_min);


        Date date1 = new Date(year-1900,month -1,dayOfTheYear, hour, min);

        return date1;
    }
    // Obtains year-month-dayOfTheYear
    private String obtainDateANDHour(String dateStr) {
        String year = dateStr.substring(0, 3+1);
        String month = dateStr.substring(5, 6+1);
        String dayOfTheYear = dateStr.substring(8, 9+1);
        String hour = dateStr.substring(11,12+1);
        String minute = dateStr.substring(14,15+1);

        String result = year + "-" + month + "-" + dayOfTheYear + " " + hour + ":" + minute;

        return result;
    }



}
