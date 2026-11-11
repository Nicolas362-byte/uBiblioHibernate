package com.ubiblio.dao;

import com.ubiblio.config.HibernateUtil;
import com.ubiblio.model.Buku;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class BukuDAO {

    public List<Buku> getAll(String cari) {
        try (Session session =
                HibernateUtil.getSessionFactory().openSession()) {

            if (cari == null || cari.trim().isEmpty()) {
                return session.createQuery(
                    "FROM Buku", Buku.class
                ).list();
            }

            return session.createQuery(
                "FROM Buku b WHERE LOWER(b.judul) LIKE :cari "
                + "OR LOWER(b.penulis) LIKE :cari",
                Buku.class
            ).setParameter("cari",
                "%" + cari.toLowerCase() + "%")
             .list();
        }
    }

    public void tambah(Buku buku) {
        Transaction tx = null;

        try (Session session =
                HibernateUtil.getSessionFactory().openSession()) {

            tx = session.beginTransaction();
            session.persist(buku);
            tx.commit();

        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw new RuntimeException("Gagal menambah buku", e);
        }
    }

    public void edit(Buku buku) {
        Transaction tx = null;

        try (Session session =
                HibernateUtil.getSessionFactory().openSession()) {

            tx = session.beginTransaction();
            session.merge(buku);
            tx.commit();

        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw new RuntimeException("Gagal mengedit buku", e);
        }
    }

    public void hapus(String isbn) {
        Transaction tx = null;

        try (Session session =
                HibernateUtil.getSessionFactory().openSession()) {

            tx = session.beginTransaction();

            Buku buku = session.get(Buku.class, isbn);

            if (buku != null) {
                session.remove(buku);
            }

            tx.commit();

        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw new RuntimeException("Gagal menghapus buku", e);
        }
    }
}
