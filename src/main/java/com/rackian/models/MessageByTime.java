package com.rackian.models;

import java.util.Comparator;

public class MessageByTime implements Comparator<Message> {

    @Override
    public int compare(Message o1, Message o2) {
        return o1.getTime().compareTo(o2.getTime());
    }

}
