package com.pankratov.prodlist.model;

public class Valuation {
    private Long id=-1l;
    private String reference="";
    private int rating=-1;
    private String timeStamp="";
    private String author="";
    private String overview="";

    public String getReference() {
        return reference;
    }
    public Valuation(){
        
    }
    public Valuation(String author,int rating,String reference,String overview){
        this.author=author!=null?author:"";
        this.rating=rating;
        this.reference=reference!=null?reference:"";
        this.overview=overview!=null?overview:"";
    }
    
     public Valuation(Long id,int rating,String reference,  String overview, String author, String timeStamp){
        this(author,rating,reference,overview);
        this.timeStamp=timeStamp!=null?timeStamp:"";
        this.id=id;
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
