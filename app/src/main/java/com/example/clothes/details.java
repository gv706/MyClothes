package com.example.clothes;

import android.net.Uri;

public class details {
    String name, phno,id;
    String uri;

    String shirts, tshirts, tracks, towels, shorts, pants, pillowcovers, blankets,boxers,pyjamas,date;

    public details(String id, String C, String name, String phno, String shirts, String pants, String TShirt, String track, String boxer, String towel, String shor, String blanket, String pillowcover, String pyjama, String Uri) {

        this.shirts = shirts;
        this.id=id;
        this.uri=Uri;
        this.tshirts = TShirt;
        this.tracks = track;
        this.towels = towel;
        this.shorts = shor;
        this.pants = pants;
        this.pillowcovers = pillowcover;
        this.name = name;
        this.phno = phno;
        this.boxers=boxer;
        this.pyjamas=pyjama;
        this.blankets = blanket;
        this.date=C;
    }
    public  details(){

    }

    public String getName() {
        return name;
    }

    public String getPhno() {
        return phno;
    }

    public String getShirts() {
        return shirts;
    }

    public String getTshirts() {
        return tshirts;
    }

    public String getTracks() {
        return tracks;
    }

    public String getTowels() {
        return towels;
    }

    public String getShorts() {
        return shorts;
    }

    public String getPants() {
        return pants;
    }

    public String getPillowcovers() {
        return pillowcovers;
    }

    public String getBlankets() {
        return blankets;
    }

    public String getDate() {
        return date;
    }

    public String getUri() {
        return uri;
    }

    public String getId() {
        return id;
    }

    public String getBoxers() {
        return boxers;
    }

    public String getPyjamas() {
        return pyjamas;
    }
}
