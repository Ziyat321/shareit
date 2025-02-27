package com.practice.shareitziyat.server.comparators;

import com.practice.shareitziyat.server.request.Request;

import java.util.Comparator;

public class RequestCreatedDateComparator implements Comparator<Request> {
    @Override
    public int compare(Request o1, Request o2) {
        if (o1.getCreated().isAfter(o2.getCreated())) return -1;
        else if (o1.getCreated().isBefore(o2.getCreated())) return 1;
        else return 0;
    }
}
