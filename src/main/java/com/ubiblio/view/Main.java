package com.ubiblio.view;

import com.ubiblio.config.HibernateUtil;
import com.ubiblio.controller.BukuController;
import com.ubiblio.model.Buku;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Main extends JFrame {

    private final BukuController controller = new BukuController();

    private final DefaultTableModel model = new DefaultTableModel(
        new String[]{"ISBN", "Judul", "Penulis", "Genre"}, 0
    );

    private final JTable tabel = new JTable(model);
    private final JTextField cari = new JTextField(20);

    public Main() {
        setTitle("uBiblio - Library Management");
        setSize(900, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(23, 54, 93));
        header.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        JLabel title = new JLabel("uBiblio - Koleksi Buku");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        header.add(title, BorderLayout.WEST);

        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        toolbar.setBackground(Color.WHITE);

        JButton btnCari = new JButton("Cari");
        JButton btnTambah = new JButton("Tambah");
        JButton btnEdit = new JButton("Edit");
        JButton btnHapus = new JButton("Hapus");

        toolbar.add(new JLabel("Pencarian:"));
        toolbar.add(cari);
        toolbar.add(btnCari);
        toolbar.add(btnTambah);
        toolbar.add(btnEdit);
        toolbar.add(btnHapus);

        tabel.setRowHeight(30);
        tabel.setFont(new Font("Arial", Font.PLAIN, 14));
        tabel.getTableHeader().setFont(
            new Font("Arial", Font.BOLD, 13)
        );

        JPanel content = new JPanel(new BorderLayout(10, 10));
        content.setBackground(new Color(245, 248, 252));
        content.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        content.add(toolbar, BorderLayout.NORTH);
        content.add(new JScrollPane(tabel), BorderLayout.CENTER);

        add(header, BorderLayout.NORTH);
        add(content, BorderLayout.CENTER);

        btnCari.addActionListener(e -> tampilkan());
        btnTambah.addActionListener(e -> tambahBuku());
        btnEdit.addActionListener(e -> editBuku());
        btnHapus.addActionListener(e -> hapusBuku());

        tampilkan();

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                HibernateUtil.shutdown();
            }
        });
    }

    private void tampilkan() {
        try {
            model.setRowCount(0);

            for (Buku b : controller.tampilkan(cari.getText())) {
                model.addRow(new Object[]{
                    b.getIsbn(),
                    b.getJudul(),
                    b.getPenulis(),
                    b.getGenre()
                });
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Gagal mengambil data: " + e.getMessage());
        }
    }

    private void tambahBuku() {
        JTextField isbn = new JTextField();
        JTextField judul = new JTextField();
        JTextField penulis = new JTextField();
        JTextField genre = new JTextField();

        Object[] input = {
            "ISBN:", isbn,
            "Judul:", judul,
            "Penulis:", penulis,
            "Genre:", genre
        };

        int hasil = JOptionPane.showConfirmDialog(
            this, input, "Tambah Buku",
            JOptionPane.OK_CANCEL_OPTION
        );

        if (hasil == JOptionPane.OK_OPTION) {
            try {
                if (isbn.getText().trim().isEmpty()
                    || judul.getText().trim().isEmpty()
                    || penulis.getText().trim().isEmpty()
                    || genre.getText().trim().isEmpty()) {

                    JOptionPane.showMessageDialog(this,
                        "Semua data harus diisi!");
                    return;
                }

                controller.tambah(new Buku(
                    isbn.getText().trim(),
                    judul.getText().trim(),
                    penulis.getText().trim(),
                    genre.getText().trim()
                ));

                tampilkan();

            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                    "Gagal menambah buku: " + e.getMessage());
            }
        }
    }

    private void editBuku() {
        int row = tabel.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(this,
                "Pilih buku yang ingin diedit!");
            return;
        }

        String isbn = model.getValueAt(row, 0).toString();

        JTextField judul = new JTextField(
            model.getValueAt(row, 1).toString());

        JTextField penulis = new JTextField(
            model.getValueAt(row, 2).toString());

        JTextField genre = new JTextField(
            model.getValueAt(row, 3).toString());

        Object[] input = {
            "ISBN: " + isbn,
            "Judul:", judul,
            "Penulis:", penulis,
            "Genre:", genre
        };

        int hasil = JOptionPane.showConfirmDialog(
            this, input, "Edit Buku",
            JOptionPane.OK_CANCEL_OPTION
        );

        if (hasil == JOptionPane.OK_OPTION) {
            try {
                if (judul.getText().trim().isEmpty()
                    || penulis.getText().trim().isEmpty()
                    || genre.getText().trim().isEmpty()) {

                    JOptionPane.showMessageDialog(this,
                        "Semua data harus diisi!");
                    return;
                }

                controller.edit(new Buku(
                    isbn,
                    judul.getText().trim(),
                    penulis.getText().trim(),
                    genre.getText().trim()
                ));

                tampilkan();

            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                    "Gagal mengedit buku: " + e.getMessage());
            }
        }
    }

    private void hapusBuku() {
        int row = tabel.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(this,
                "Pilih buku yang ingin dihapus!");
            return;
        }

        String isbn = model.getValueAt(row, 0).toString();

        int hasil = JOptionPane.showConfirmDialog(
            this,
            "Yakin ingin menghapus buku ini?",
            "Konfirmasi",
            JOptionPane.YES_NO_OPTION
        );

        if (hasil == JOptionPane.YES_OPTION) {
            try {
                controller.hapus(isbn);
                tampilkan();

            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                    "Gagal menghapus buku: " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                new Main().setVisible(true);

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null,
                    "Gagal menjalankan uBiblio: " + e.getMessage());
                HibernateUtil.shutdown();
            }
        });
    }
}