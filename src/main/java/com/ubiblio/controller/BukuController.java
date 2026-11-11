package com.ubiblio.controller;

import com.ubiblio.dao.BukuDAO;
import com.ubiblio.model.Buku;
import java.util.List;

public class BukuController {

    private final BukuDAO dao = new BukuDAO();

    public List<Buku> tampilkan(String cari) {
        return dao.getAll(cari);
    }

    public void tambah(Buku buku) {
        dao.tambah(buku);
    }

    public void edit(Buku buku) {
        dao.edit(buku);
    }

    public void hapus(String isbn) {
        dao.hapus(isbn);
    }
}
