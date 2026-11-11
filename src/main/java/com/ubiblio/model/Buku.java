package com.ubiblio.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;

@Entity
@Table(name = "buku")
public class Buku {

    @Id
    @Column(length = 20)
    private String isbn;

    @Column(nullable = false, length = 150)
    private String judul;

    @Column(nullable = false, length = 100)
    private String penulis;

    @Column(nullable = false, length = 50)
    private String genre;

    public Buku() {
    }

    public Buku(String isbn, String judul,
                String penulis, String genre) {
        this.isbn = isbn;
        this.judul = judul;
        this.penulis = penulis;
        this.genre = genre;
    }
    public String getIsbn() {
        return isbn;
    }
    public String getJudul() {
        return judul;
    }
    public String getPenulis() {
        return penulis;
    }
    public String getGenre() {
        return genre;
    }
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }
    public void setJudul(String judul) {
        this.judul = judul;
    }
    public void setPenulis(String penulis) {
        this.penulis = penulis;
    }
    public void setGenre(String genre) {
        this.genre = genre;
    }
}
