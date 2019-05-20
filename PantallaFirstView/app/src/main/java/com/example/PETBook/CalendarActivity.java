package com.example.PETBook;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.example.PETBook.Calendar.*;
import com.example.PETBook.Controllers.AsyncResult;
import com.example.pantallafirstview.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateLongClickListener;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import org.json.JSONObject;
import org.threeten.bp.format.DateTimeFormatter;


import org.threeten.bp.LocalDate;
import org.threeten.bp.Month;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Shows off the most basic usage
 */
public class CalendarActivity extends AppCompatActivity
        implements OnDateSelectedListener, OnMonthChangedListener, OnDateLongClickListener, AsyncResult {

  private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("EEE, d MMM yyyy");
  private final OneDayDecorator oneDayDecorator = new OneDayDecorator();

  MaterialCalendarView widget;
  TextView textView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_calendar);

    widget = findViewById(R.id.calendarView);
    textView = findViewById(R.id.textView);

    widget.setOnDateChangedListener(this);
    widget.setOnDateLongClickListener(this);
    widget.setOnMonthChangedListener(this);


    // Comença Decorador ----------------------------------------------

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


    // Aqui se hara la llamada a la API para escoger los dias que deben ser marcados como que ha eventos en ellos
    LocalDate temp = LocalDate.now().minusMonths(2);
    final ArrayList<CalendarDay> calendarDays = new ArrayList<>();
    for (int i = 0; i < 30; i++) {
      final CalendarDay day = CalendarDay.from(temp);
      calendarDays.add(day);
      temp = temp.plusDays(5);
    }

    widget.addDecorator(new EventDecorator(Color.RED, calendarDays));

    // Acaba Decorador ----------------------------------------------



    //Setup initial text
    textView.setText("No Selection");
  }

  @Override
  public void onDateSelected(
          @NonNull MaterialCalendarView widget,
          @NonNull CalendarDay date,
          boolean selected) {

    textView.setText(selected ? FORMATTER.format(date.getDate()) : "No Selection");

    /*
     * Aqui se mostrara los eventos del dia correspondiente
     * Quiza usar una lista por si hay mas de un evento el mismo dia
     *
     */

    final String a = "Testeando" + "\n" +
            "Esto seria un campo" + "\n" +
            "Este otro" + "\n" +
            "Probando" + "\n" +
            "grrrrrrrrrrrrr" + "\n" +
            "Puta Barça" + "\n" +
            "Me aburro";
    textView.setText(a);
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
  public void OnprocessFinish(JSONObject output) {

  }
}
