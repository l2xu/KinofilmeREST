package de.medieninformatik.common;

/**
 * @author Lukas Weihrauch, m29202
 * @date 05.02.2022
 * @version 1.0
 *
 * Die Movie Klasse dient der Darstellung der Filme im Programm.
 * Die Klasse wird sowohl vom Client als auch vom Server verwendet.
 */
public class Movie {

    int id;
    String title;
    String regi;
    int bewertung;
    String genre;
    int laeng;

    //Getter und Setter der Movie Klasse
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRegi() {
        return regi;
    }

    public void setRegi(String regi) {
        this.regi = regi;
    }

    public int getBewertung() {
        return bewertung;
    }

    public void setBewertung(int bewertung) {
        this.bewertung = bewertung;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getLaeng() {
        return laeng;
    }

    public void setLaeng(int laeng) {
        this.laeng = laeng;
    }



    /**
     * Konstruktor der Movie Klasse
     * @param id Filmid
     * @param title Filmtitel
     * @param regi Filmregiseure
     * @param bewertung Filmbewertung
     * @param genre Filmgenre
     * @param laeng Filmlänge
     */
    public Movie(int id, String title, String regi, int bewertung, String genre, int laeng) {
        this.id = id;
        this.title = title;
        this.regi = regi;
        this.bewertung = bewertung;
        this.genre = genre;
        this.laeng = laeng;
    }
}
