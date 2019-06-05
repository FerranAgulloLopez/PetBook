/*
 * Copyright (c) 2012 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.example.PETBook.Calendar;

import com.example.PETBook.Conexion;
import com.example.PETBook.Models.EventModel;
import com.google.api.services.calendar.model.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventAttendee;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.EventReminder;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * Asynchronously insert a new calendar.
 * 
 * @author Yaniv Inbar
 */
class AsyncInsertCalendar extends CalendarAsyncTask {

  private final Calendar entry;
  private final boolean existe;
  private List<EventModel> events;
  CalendarSync calendarSync;

  private String calendarId;
  private String TIME_ZONE = "Europe/Madrid";


  AsyncInsertCalendar(CalendarSync calendarSync, Calendar entry, boolean existe, List<EventModel> events, String googleCalendarID) {
    super(calendarSync);
    this.calendarSync = calendarSync;
    this.entry = entry;
    this.existe = existe;
    this.events = events;

    calendarId = googleCalendarID;
  }

  @Override
  protected void doInBackground() throws IOException {


    System.out.println("\n Existe: " + existe + "\n");

    if(! existe) {
      Calendar calendar = client.calendars().insert(entry).setFields(CalendarInfo.FIELDS).execute();
      calendarId = calendar.getId();

      // [ Hacer conexion para guardar el ID en el server ]
      Conexion con = new Conexion(calendarSync);
      calendarSync.llamada = calendarSync.CONEXION_UpdateCalendarId;
      con.execute("http://10.4.41.146:9999/ServerRESTAPI/events/UpdateCalendarId/" + calendarId, "PUT", null);
    }
    else {
      System.out.println(".\nd\n Existe: " + existe + ".\n,");

      /*    System.out.println("\n Existe: " + existe + "\n");

      try { // POr si acaso el usuario elimino su calendario. Para que no pete
        client.calendars().delete(calendarId);
      } catch (Exception e) {
      }


*/
        // COMO BORRAR UN CALENDARIO ¿?
      try {
        client.calendars().delete(calendarId).execute();
      } catch (Exception e) {
        e.printStackTrace();
        System.out.println(e.getMessage());
      }
      //client.calendarList().delete(calendarId);
      Calendar calendar = client.calendars().insert(entry).setFields(CalendarInfo.FIELDS).execute();
      calendarId = calendar.getId();

      System.out.println(".\nd\n Existe: " + calendarId + ".\n,");

        // Crear i borrar

      //client.calendars().delete(calendarId).execute();

      //AbstractGoogleClientRequest.execute()

      // [ Hacer conexion para guardar el ID en el server ]
      Conexion con = new Conexion(calendarSync);
      calendarSync.llamada = calendarSync.CONEXION_UpdateCalendarId;
      con.execute("http://10.4.41.146:9999/ServerRESTAPI/events/UpdateCalendarId/" + calendarId, "PUT", null);

    }



    for(int i = 0; i < events.size(); ++i) {
      EventModel eventModel = events.get(i);

      String direccion            = eventModel.getDireccion();
      String titulo               = eventModel.getTitulo();
      String descripcion          = eventModel.getDescripcion();
      ArrayList<String> miembros  = eventModel.getMiembros();
      String creador              = eventModel.getCreador();
      Date date                   = eventModel.getDate();



      Event event = new Event()
              .setSummary(titulo)
              .setLocation(direccion)
              .setDescription(descripcion);


      Date startDate = date;
      Date endDate = new Date(startDate.getTime()+1000*3600); // 1000*3600 ms = 3600 s = 60 min = 1 hora

      DateTime start = new DateTime(startDate, TimeZone.getTimeZone(TIME_ZONE));
      event.setStart(new EventDateTime().setDateTime(start).setTimeZone(TIME_ZONE));

      DateTime end = new DateTime(endDate, TimeZone.getTimeZone(TIME_ZONE));
      event.setEnd(new EventDateTime().setDateTime(end).setTimeZone(TIME_ZONE));



      String[] recurrence = new String[]{"RRULE:FREQ=DAILY;COUNT=1"}; // Number or repetitions consecutive
      event.setRecurrence(Arrays.asList(recurrence));

      int numMiembros = miembros.size();
      EventAttendee[] attendees = new EventAttendee[numMiembros];
      for(int j = 0; j < numMiembros; ++j) {
        String emailMiembro = miembros.get(j);
        attendees[j] = new EventAttendee().setEmail(emailMiembro);
      }
      /*
      EventAttendee[] attendees = new EventAttendee[]{
              new EventAttendee().setEmail("abir@aksdj.com2"),
              new EventAttendee().setEmail("asdasd@andlk.com3"),
      };
      */

      event.setAttendees(Arrays.asList(attendees));

      EventReminder[] reminderOverrides = new EventReminder[]{
              new EventReminder().setMethod("email").setMinutes(24 * 60),
              new EventReminder().setMethod("popup").setMinutes(10),
      };
      Event.Reminders reminders = new Event.Reminders()
              .setUseDefault(false)
              .setOverrides(Arrays.asList(reminderOverrides));
      event.setReminders(reminders);



      try {
        System.out.printf("calendarId: %s\n", calendarId);
        event = client.events().insert(calendarId, event).execute(); // QUiza usar path, a ver que sale
      } catch (IOException e) {
        e.printStackTrace();
        System.out.println(e.getMessage());
      }
      System.out.printf("Event created: %s\n", event.getHtmlLink());
    }




  }



}
