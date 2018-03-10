package com.getset.chapter_2_2;

public interface MyEventListener {
    void onNewEvent(MyEventSource.MyEvent event);

    void onEventStopped();
}
