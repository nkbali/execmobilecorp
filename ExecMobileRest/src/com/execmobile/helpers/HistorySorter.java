package com.execmobile.helpers;

import java.util.Comparator;

import org.joda.time.DateTime;
import org.joda.time.DateTimeComparator;

import com.execmobile.data.Apploginhistory;

public class HistorySorter implements Comparator<Apploginhistory> {
    @Override
    public int compare(Apploginhistory history1, Apploginhistory history2) {
    	DateTime history1Time = DateTime.parse(history1.getLastAccessTime());
    	DateTime history2Time = DateTime.parse(history2.getLastAccessTime());
    	DateTimeComparator comparator = DateTimeComparator.getInstance();
        return comparator.compare(history2Time, history1Time);
    }
}
