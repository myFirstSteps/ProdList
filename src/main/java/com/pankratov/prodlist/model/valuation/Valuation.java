package com.pankratov.prodlist.model.valuation;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

public class Valuation implements Comparable<Valuation> {

    private Long id = -1l;
    private String reference = "";
    private int rating = -1;
    private String timeStamp = "";
    private String author = "";
    private String overview = "";
    private Date time=new Date();

    public String getReference() {
        return reference;
    }

    public Valuation() {

    }

    public Valuation(String author, int rating, String reference, String overview) {
        this.author = author != null ? author : "";
        this.rating = rating;
        this.reference = reference != null ? reference : "";
        this.overview = overview != null ? overview : "";
    }

    public Valuation(Long id, int rating, String reference, String overview, String author, String timeStamp) {
        this(author, rating, reference, overview);
        if (timeStamp != null) {
            this.setTimeStamp(timeStamp);
        } else {
            this.timeStamp = "";
        }
        this.id = id;
    }

    @Override
    public int compareTo(Valuation o) {
        if(o==null) return 1;
        return this.time.compareTo(o.time);
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String time) {
        this.timeStamp = time;
        try {
            this.time = java.sql.Timestamp.valueOf(time);
            this.timeStamp = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG,
                    new Locale("ru","RU")).format(this.time);
        } catch (IllegalArgumentException e) {
        }

    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
