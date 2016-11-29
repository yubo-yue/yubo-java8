package yubo.v1.java7threadcookbook.daemon;

import java.time.Instant;

public class Event {
    private Instant date;
    private String event;

    public Event(final Instant date, final String event) {
        this.date = date;
        this.event = event;
    }

    public Instant getDate() {
        return date;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public static Event newInstance(final Instant instant, final String message) {
        return new Event(instant, message);
    }
}
