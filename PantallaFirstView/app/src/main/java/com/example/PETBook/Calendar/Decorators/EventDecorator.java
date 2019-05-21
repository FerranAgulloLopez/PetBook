package com.example.PETBook.Calendar.Decorators;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.style.BackgroundColorSpan;

import com.google.android.gms.maps.model.Circle;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import java.util.Collection;
import java.util.HashSet;

/**
 * Decorate several days with a dot
 */
public class EventDecorator implements DayViewDecorator {

  private int color;
  private HashSet<CalendarDay> dates;

  public EventDecorator(int color, Collection<CalendarDay> dates) {
    this.color = color;
    this.dates = new HashSet<>(dates);
  }

  @Override
  public boolean shouldDecorate(CalendarDay day) {
    return dates.contains(day);
  }




  @Override
  public void decorate(DayViewFacade view)
  {
    view.addSpan(new DotSpan(10, color)); // Añade un punto debajo de los numero(para indicar que ese dia hay un evento)

    /**  Para que se pinte el Background

      int color2 = Color.parseColor("#228BC34A");
      Drawable highlightDrawable = new ColorDrawable(color2);
      view.setBackgroundDrawable(highlightDrawable);
    */
  }
}
