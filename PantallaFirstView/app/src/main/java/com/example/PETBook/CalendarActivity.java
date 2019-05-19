package com.example.PETBook;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.example.PETBook.Controllers.AsyncResult;
import com.example.pantallafirstview.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateLongClickListener;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import org.json.JSONObject;
import org.threeten.bp.format.DateTimeFormatter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Shows off the most basic usage
 */
public class CalendarActivity extends AppCompatActivity
        implements OnDateSelectedListener, OnMonthChangedListener, OnDateLongClickListener, AsyncResult {

  private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("EEE, d MMM yyyy");

  //@BindView(R.id.calendarView)
  MaterialCalendarView widget;

  //@BindView(R.id.textView)
  TextView textView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_calendar);
    //ButterKnife.bind(this);

    widget = findViewById(R.id.calendarView);

    textView = findViewById(R.id.textView);

    widget.setOnDateChangedListener(this);
    widget.setOnDateLongClickListener(this);
    widget.setOnMonthChangedListener(this);

    //Setup initial text
    textView.setText("No Selection");
  }

  @Override
  public void onDateSelected(
          @NonNull MaterialCalendarView widget,
          @NonNull CalendarDay date,
          boolean selected) {
    textView.setText(selected ? FORMATTER.format(date.getDate()) : "No Selection");

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
