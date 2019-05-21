package com.example.PETBook.Calendar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.PETBook.Adapters.EventAdapterCalendar;
import com.example.PETBook.Calendar.Decorators.EventDecorator;
import com.example.PETBook.Calendar.Decorators.MySelectorDecorator;
import com.example.PETBook.Calendar.Decorators.OneDayDecorator;
import com.example.PETBook.Conexion;
import com.example.PETBook.Controllers.AsyncResult;
import com.example.PETBook.EventInfo;
import com.example.PETBook.Fragments.MyEventsFragment;
import com.example.PETBook.Models.EventModel;
import com.example.PETBook.NewEvent;
import com.example.PETBook.SingletonUsuario;
import com.example.pantallafirstview.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateLongClickListener;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import org.json.JSONArray;
import org.json.JSONObject;
import org.threeten.bp.format.DateTimeFormatter;


import org.threeten.bp.LocalDate;
import org.threeten.bp.Month;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Shows off the most basic usage
 */
public class CalendarActivity extends AppCompatActivity
        implements OnDateSelectedListener, OnMonthChangedListener, OnDateLongClickListener, AsyncResult {

  private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("EEE, d MMM yyyy");
  private final OneDayDecorator oneDayDecorator = new OneDayDecorator();

  // Elementos de dissenyo
  MaterialCalendarView widget;
  //TextView textView;
  private ListView lista;


  //
  private EventAdapterCalendar eventosUser;
  private ArrayList<EventModel> model;
  private Conexion con;
  private SingletonUsuario su;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_calendar);

    widget = findViewById(R.id.calendarView);
    //textView = findViewById(R.id.textView);
    lista = findViewById(R.id.list_eventos);


    widget.setOnDateChangedListener(this);
    widget.setOnDateLongClickListener(this);
    widget.setOnMonthChangedListener(this);


    // Comença Decorador ****************************************************************************************************

    widget.setShowOtherDates(MaterialCalendarView.SHOW_ALL);
    final LocalDate instance = LocalDate.now();
    widget.setSelectedDate(instance);

    final LocalDate min = LocalDate.of(instance.getYear(), Month.JANUARY, 1);
    final LocalDate max = LocalDate.of(instance.getYear(), Month.DECEMBER, 31);

    widget.state().edit().setMinimumDate(min).setMaximumDate(max).commit();

    widget.addDecorators(
            new MySelectorDecorator(this),
            //new HighlightWeekendsDecorator(), // Para que los fines de semana se vean con el fondo distinto
            oneDayDecorator
    );

    // Acaba Decorador ****************************************************************************************************


    // Comença Conexion ****************************************************************************************************

    con = new Conexion(CalendarActivity.this);
    su = SingletonUsuario.getInstance();
    con.execute("http://10.4.41.146:9999/ServerRESTAPI/events/GetEventsByParticipant?mail=" + su.getEmail(),"GET", null);

    // Acaba Conexion ****************************************************************************************************

    // Setup initial text
    //textView.setText("No Selection");
  }

  @Override
  public void onDateSelected(
          @NonNull MaterialCalendarView widget,
          @NonNull CalendarDay date,
          boolean selected) {

    //textView.setText(selected ? FORMATTER.format(date.getDate()) : "No Selection");

    /*
     * Aqui se mostrara los eventos del dia correspondiente
     * Quiza usar una lista por si hay mas de un evento el mismo dia
     *
     */

    // Miro todos los eventos y meto la info del ultimo que encuentre  (ESTO ES SIMPLEMENTE DE PRUEBA, SEGURAMENTE LUEGO USARE UNA LISTA O SIMILAR)
    // IDEA QUE HACER: Mostrar una lista con los eventos(info reducida) de el dia correspondiente, y si el usuario hace click en un evento IR DIRECTAMENTE A LA PANTALLA(ya hecha) DE VER EVENTO
    // Luego quiza se hace alguna clase de mejora para ir mas rapido(quiza ordenar por fecha los eventos desde aqui o desde el server)

    ArrayList<EventModel> eventsOfTheSelectedDay = new ArrayList<>();
    for(int i = 0; i < model.size(); ++i) {
      EventModel event = model.get(i);
      LocalDate fecha = event.getLocalDate();
      if(fecha.getYear()        == date.getYear() &&
         fecha.getMonthValue()  == date.getMonth() &&
         fecha.getDayOfMonth()  == date.getDay())
      {/*
      // Atrbutos provisionales, luego ya vere cuales escojo para mostrar,
        final String info = "Testeando" + "\n" +
                event.getTitulo()       + "\n" +
                event.getCreador()      + "\n" +
                event.getDescripcion()  + "\n" +
                event.getFecha()        + "\n" +
                "Puta Barça" ;
        textView.setText(info);
        */
        eventsOfTheSelectedDay.add(event);
      }

      // Meter los
      eventosUser = new EventAdapterCalendar(this, eventsOfTheSelectedDay);
      lista.setAdapter(eventosUser);

      lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
          EventModel eventoSeleccionado = model.get(position);
          Intent intent = new Intent(getApplicationContext(), EventInfo.class); // Haber si funciona con el context
          intent.putExtra("event", eventoSeleccionado);
          intent.putExtra("eventType", "Participant"); // No se porque participant es requrido  ¿?
          startActivity(intent);
        }
      });
    }


  }

  @Override
  public void onDateLongClick(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date) {
    final String text = String.format("%s is available", FORMATTER.format(date.getDate()));
    Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
  }

  @Override
  public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
    //noinspection ConstantConditions
    getSupportActionBar().setTitle(FORMATTER.format(date.getDate()));
  }





  @Override
  public void OnprocessFinish(JSONObject json) {
    printLines();
    System.out.println("ON PROCESS, FINISH ---------------------------------------------------------------");
    printLines();

    try{
      if(json.getInt("code") == 200){
        model = new ArrayList<>(); // ArrayList<EventsModel>()
        JSONArray jsonArray = json.getJSONArray("array");
        for(int i = 0; i < jsonArray.length(); ++i){
          JSONObject evento = jsonArray.getJSONObject(i);
          EventModel e = new EventModel();
          e.setId(evento.getInt("id"));
          e.setTitulo(evento.getString("title"));
          e.setDescripcion(evento.getString("description"));
          e.setFecha(obtainDateANDHour(evento.getString("date")));

          String dateStr = evento.getString("date");
          LocalDate localdates = obtainLocalDate(dateStr);
          e.setLocalDate(localdates);

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
          model.add(e);
        }


        final ArrayList<CalendarDay> calendarDays = new ArrayList<>();
        for (int i = 0; i < model.size(); i++) {      // Per cada event
          LocalDate date = model.get(i).getLocalDate();
          final CalendarDay day = CalendarDay.from(date);
          calendarDays.add(day);
          //date.plusDays(5); + funciones similares para operar con las fechas
        }

        // Remarcar los dias donde hay eventis
        widget.addDecorator(new EventDecorator(Color.RED, calendarDays));

        System.out.print(json.getInt("code") + " se muestran correctamente la lista de eventos\n");
      }
      else{
        System.out.print("El sistema no logra mostrar la lista de eventos del creador\n");
      }
    } catch (Exception e){
      e.printStackTrace();
    }

  }




  /** FUNCIONES AUXILIARES
 *
 *
 *
 *
 */

  // Obtains year-month-dayOfTheYear
  private LocalDate obtainLocalDate(String dateStr) {
    String date = dateStr.substring(0,10);

    String string_year = date.substring(0,4);
    String string_month = date.substring(5,7);
    String string_dayOfTheYear = date.substring(8,10);

    int year = Integer.parseInt(string_year);
    int month = Integer.parseInt(string_month);
    int dayOfTheYear = Integer.parseInt(string_dayOfTheYear);

    LocalDate localdate = LocalDate.of(year,month,dayOfTheYear);

    return localdate;
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

  private void printLines() {
    System.out.println("\ns \ng \n4g \n. \n4g \ntgt \n n\n o\n");
    System.out.println("\n,\n1\n2\n3\n3\n;\n4\n5\n45");;
  }
}
