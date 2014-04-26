package com.example.moneytracker.app.model;

import android.text.format.DateFormat;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Daniel HB on 25/04/14.
 */
public class Expense
{
    public static SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    Date date;
    String value;
    String [] tags;
    String shortDesc;

    public Expense(Date date, String value, String [] tags, String shortDesc)
    {
        this.date = date;
        this.value = value;
        this.tags = tags;
        this.shortDesc = shortDesc;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String[] getTags() {
        return tags;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }

    @Override
    public String toString()
    {
        return "Date: " + Expense.dateFormat.format(this.date) + " Value: " + this.value;
    }
}
