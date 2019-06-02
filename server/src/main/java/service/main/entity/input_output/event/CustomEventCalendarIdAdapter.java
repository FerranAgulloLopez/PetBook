package service.main.entity.input_output.event;

import service.main.entity.Event;

import java.io.Serializable;
import java.util.List;

public class CustomEventCalendarIdAdapter implements Serializable {

    private String googleCalendarID;
    private boolean hasGoogleCalendar;
    private List<Event> events;


    public CustomEventCalendarIdAdapter() {

    }

    public CustomEventCalendarIdAdapter(String googleCalendarID, boolean hasGoogleCalendar, List<Event> events) {
        setEvents(events);
        setGoogleCalendarID(googleCalendarID);
        setHasGoogleCalendar(hasGoogleCalendar);
    }


    public String getGoogleCalendarID() {
        return googleCalendarID;
    }

    public void setGoogleCalendarID(String googleCalendarID) {
        googleCalendarID = googleCalendarID;
    }

    public boolean isHasGoogleCalendar() {
        return hasGoogleCalendar;
    }

    public void setHasGoogleCalendar(boolean hasGoogleCalendar) {
        this.hasGoogleCalendar = hasGoogleCalendar;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }
}
