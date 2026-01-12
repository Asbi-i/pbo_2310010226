/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pbo_2310010226;

//import javax.swing.*;
//import java.sql.*;
//import javax.swing.table.DefaultTableModel;
//import java.sql.Connection;

//import java.sql.DriverManager;
//import java.sql.Driver;
//import java.sql.Connection;
//import java.sql.SQLException;
//import java.sql.Statement;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
import javax.swing.*;
import java.sql.*;
import javax.swing.table.DefaultTableModel;
import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperDesignViewer;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author ACER
 */
public class crud {
    private String dataBase = "pbo_2310010226";
    private String userName = "root";
    private String password = "";
    private String url = "jdbc:mysql://localhost/"+dataBase;
    public Connection koneksiDB;
    public String var_nama, var_alamat, var_telpon, var_harga, var_durasi, var_jenis, var_tipe, var_kasir, var_paket, var_konsumen, var_pembayaran, var_tglTransaksi, var_tglAmbil, var_total, var_keterangan, var_level, var_password = null;
    public boolean validasi = false;
    
   public void loadDataKonsumen(JTable tabel, String sql){
    try {
            Statement perintah = koneksiDB.createStatement();
            ResultSet ds = perintah.executeQuery(sql);
            ResultSetMetaData data = ds.getMetaData();
            int kolom = data.getColumnCount();
            DefaultTableModel model = new DefaultTableModel();
//            for(int i = 1; i < kolom; i++){
//            model.addColumn(data.getColumnName(i));
//        }
            model.addColumn("ID Konsumen");
            model.addColumn("Nama");
            model.addColumn("Alamat");
            model.addColumn("Telpon");
            model.getDataVector().clear();
            model.fireTableDataChanged();
            while(ds.next()){
                Object[] baris = new Object[kolom];
                for(int j = 1; j <= kolom; j++){
                    baris[j - 1] = ds.getObject(j);
                }
                model.addRow(baris);
                tabel.setModel(model);
            }
        } catch (Exception e) {
        }
}

public crud(){
    try {
        Driver driverKoneksi = new com.mysql.jdbc.Driver();
        DriverManager.registerDriver(driverKoneksi);
        koneksiDB = DriverManager.getConnection(url, userName, password);
        System.out.println("Berhasil Koneksi");
    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "terjadi Error:\n"+e.getMessage());
    }
}

public boolean menuLogin(String username, String password) {
    String sql = "SELECT 1 FROM kasir WHERE nama = ? AND password = ? LIMIT 1";
    try (PreparedStatement ps = koneksiDB.prepareStatement(sql)) {
        ps.setString(1, username);
        ps.setString(2, password);

        try (ResultSet rs = ps.executeQuery()) {
            return rs.next();   // true bila ada baris
        }
    } catch (SQLException e) {
        System.err.println("Error saat verifikasi login: " + e.getMessage());
    }
    return false;
}

// CODINGAN KASIR

public void loadDataKasir(JTable tabel, String sql){
    try {
            Statement perintah = koneksiDB.createStatement();
            ResultSet ds = perintah.executeQuery(sql);
            ResultSetMetaData data = ds.getMetaData();
            int kolom = data.getColumnCount();
            DefaultTableModel model = new DefaultTableModel();
//            for(int i = 1; i < kolom; i++){
//            model.addColumn(data.getColumnName(i));
//        }
            model.addColumn("ID Kasir");
            model.addColumn("Level");
            model.addColumn("Nama");
            model.addColumn("Password");
            model.getDataVector().clear();
            model.fireTableDataChanged();
            while(ds.next()){
                Object[] baris = new Object[kolom];
                for(int j = 1; j <= kolom; j++){
                    baris[j - 1] = ds.getObject(j);
                }
                model.addRow(baris);
                tabel.setModel(model);
            }
        } catch (Exception e) {
        }
}

public void simpanKasirPRT(String id, String lvl, String nm, String pass){
    try {
        String sql = "insert into kasir (id_kasir, level, nama, password) VALUE(?, ?, ?, ?)"; 
        String checkPrimary = "SELECT * FROM kasir where id_kasir = '"+id+"'";
        Statement checkData = koneksiDB.createStatement();
        ResultSet data = checkData.executeQuery(checkPrimary);
        if (data.next()){
            String isi = 
                    "\n Level : "+data.getString("level")+
                    "\n Nama : "+data.getString("nama")+
                    "\n Password : "+data.getString("password");
            JOptionPane.showMessageDialog(null, "id_kasir sudah Terdaftar"+ isi);
            
            this.validasi = true;
            this.var_level = data.getString("level");
            this.var_nama = data.getString("nama");
            this.var_password = data.getString("password");
            
            JOptionPane.showMessageDialog(null, userName);
        } else {
            PreparedStatement perintah = koneksiDB.prepareStatement(sql);
            perintah.setString(1, id);
            perintah.setString(2, lvl);
            perintah.setString(3, nm);
            perintah.setString(4, pass);
            perintah.executeUpdate();
            
            JOptionPane.showMessageDialog(null, "Berhasil disimpan");
            
            this.validasi = false;
            this.var_level = null;
            this.var_nama = null;
            this.var_password = null;
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, e.getMessage());
        System.err.println(e.getMessage());
    }
}

public void ubahKasirPRS(String id, String lvl, String nm, String pass){
    try {
        String sqlUbah = "UPDATE kasir Set level = ?, nama = ?, password = ? Where id_kasir = ?";
        PreparedStatement ubah = koneksiDB.prepareStatement(sqlUbah);
        ubah.setString(1, lvl);
        ubah.setString(2, nm);
        ubah.setString(3, pass);
        ubah.setString(4, id);
        
        ubah.executeUpdate();
        JOptionPane.showMessageDialog(null, "Data Berhasil Diubah");
    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, e.getMessage());
    }
}

public void hapusKasirPRT(String id){
    try {
        String sqlHapus = "DELETE FROM kasir WHERE id_kasir = ?";
        PreparedStatement hapus = koneksiDB.prepareStatement(sqlHapus);
        hapus.setString(1, id);
        hapus.executeUpdate();
        
        JOptionPane.showMessageDialog(null, "Data berhasil dihapus");
    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, e.getMessage());
    }
}

// CODINGAN KONSUMEN

public void simpanKonsumenSTS(String id, String nm, String alm, String tlp){
    try {
        String sql = "insert into konsumen (id_konsumen, nama, alamat, telp)"
                + "VALUES ('"+id+"', '"+nm+"', '"+alm+"', '"+tlp+"')";
                
        String checkPrimary = "SELECT * FROM konsumen when id_konsumen = '"+id+"'";
        Statement checkData = koneksiDB.createStatement();
        ResultSet data = checkData.executeQuery(checkPrimary);
        if (data.next()){
            String isi = 
                    "\n Nama : "+data.getString("nama")+
                    "\n Alamat : "+data.getString("alamat")+
                    "\n Telpon : "+data.getString("telpon");
            JOptionPane.showMessageDialog(null, "id_konsumen sudah Terdaftar"+ isi);
            
            this.validasi = true;
            this.var_nama = data.getString("nama");
            this.var_alamat = data.getString("alamat");
            this.var_telpon = data.getString("telpon");
            
            JOptionPane.showMessageDialog(null, userName);
        } else {
            Statement perintah = koneksiDB.createStatement();
            perintah.execute(sql);
            
            JOptionPane.showMessageDialog(null, "Berhasil disimpan");
            
            this.validasi = false;
            this.var_nama = null;
            this.var_alamat = null;
            this.var_telpon = null;
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, e.getMessage());
    }
}

public void simpanKonsumenPRT(String id, String nm, String alm, String tlp){
    try {
        String sql = "insert into konsumen (id_konsumen, nama, alamat, telp) VALUE(?, ?, ?, ?)"; 
        String checkPrimary = "SELECT * FROM konsumen where id_konsumen = '"+id+"'";
        Statement checkData = koneksiDB.createStatement();
        ResultSet data = checkData.executeQuery(checkPrimary);
        if (data.next()){
            String isi = 
                    "\n Nama : "+data.getString("nama")+
                    "\n Alamat : "+data.getString("alamat")+
                    "\n Telpon : "+data.getString("telpon");
            JOptionPane.showMessageDialog(null, "id_konsumen sudah Terdaftar"+ isi);
            
            this.validasi = true;
            this.var_nama = data.getString("nama");
            this.var_alamat = data.getString("alamat");
            this.var_telpon = data.getString("telpon");
            
            JOptionPane.showMessageDialog(null, userName);
        } else {
            PreparedStatement perintah = koneksiDB.prepareStatement(sql);
            perintah.setString(1, id);
            perintah.setString(2, nm);
            perintah.setString(3, alm);
            perintah.setString(4, tlp);
            perintah.executeUpdate();
            
            JOptionPane.showMessageDialog(null, "Berhasil disimpan");
            
            this.validasi = false;
            this.var_nama = null;
            this.var_alamat = null;
            this.var_telpon = null;
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, e.getMessage());
        System.err.println(e.getMessage());
    }
}

public void ubahKonsumenSTS(String id, String nm, String alm, String tlp){
    try {
        String sqlUbah = "Update konsumen Set Nama = '"+nm+"', Alamat = '" +alm+"'"
                + ", Telpon = '" +tlp+"' Where id_konsumen = " +id+"";
        Statement ubah = koneksiDB.createStatement();
        ubah.execute(sqlUbah);
        JOptionPane.showMessageDialog(null, "Data Berhasil Diubah");
    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, e.getMessage());
    }
}

public void ubahKonsumenPRS(String id, String nm, String alm, String tlp){
    try {
        String sqlUbah = "UPDATE konsumen Set Nama = ?, Alamat = ?, Telp = ? Where id_konsumen = ?";
        PreparedStatement ubah = koneksiDB.prepareStatement(sqlUbah);
        ubah.setString(1, nm);
        ubah.setString(2, alm);
        ubah.setString(3, tlp);
        ubah.setString(4, id);
        
        ubah.executeUpdate();
        JOptionPane.showMessageDialog(null, "Data Berhasil Diubah");
    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, e.getMessage());
    }
}

public void hapusKonsumenSTS(String id){
    try {
        String sqlHapus = "DELETE FROM konsumen WHERE id_konsumen = '"+id+"'";
        Statement hapus = koneksiDB.createStatement();
        hapus.execute(sqlHapus);
        
        JOptionPane.showMessageDialog(null, "Data berhasil dihapus");
    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, e.getMessage());
    }
    
}

public void hapusKonsumenPRT(String id){
    try {
        String sqlHapus = "DELETE FROM konsumen WHERE id_konsumen = ?";
        PreparedStatement hapus = koneksiDB.prepareStatement(sqlHapus);
        hapus.setString(1, id);
        hapus.executeUpdate();
        
        JOptionPane.showMessageDialog(null, "Data berhasil dihapus");
    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, e.getMessage());
    }
}

// CODINGAN PAKET

public void loadDataPaket(JTable tabel, String sql){
    try {
            Statement perintah = koneksiDB.createStatement();
            ResultSet ds = perintah.executeQuery(sql);
            ResultSetMetaData data = ds.getMetaData();
            int kolom = data.getColumnCount();
            DefaultTableModel model = new DefaultTableModel();
//            for(int i = 1; i < kolom; i++){
//            model.addColumn(data.getColumnName(i));
//        }
            model.addColumn("ID Paket");
            model.addColumn("Nama");
            model.addColumn("Harga");
            model.addColumn("Durasi");
            model.getDataVector().clear();
            model.fireTableDataChanged();
            while(ds.next()){
                Object[] baris = new Object[kolom];
                for(int j = 1; j <= kolom; j++){
                    baris[j - 1] = ds.getObject(j);
                }
                model.addRow(baris);
                tabel.setModel(model);
            }
        } catch (Exception e) {
        }
}

public void simpanPaketPRT(String id, String nm, String hrg, String drs){
    try {
        String sql = "insert into paket (id_paket, nama, harga, durasi) VALUE(?, ?, ?, ?)"; 
        String checkPrimary = "SELECT * FROM paket where id_paket = '"+id+"'";
        Statement checkData = koneksiDB.createStatement();
        ResultSet data = checkData.executeQuery(checkPrimary);
        if (data.next()){
            String isi = 
                    "\n Nama : "+data.getString("nama")+
                    "\n Harga : "+data.getString("harga")+
                    "\n Durasi : "+data.getString("durasi");
            JOptionPane.showMessageDialog(null, "id_paket sudah Terdaftar"+ isi);
            
            this.validasi = true;
            this.var_nama = data.getString("nama");
            this.var_harga = data.getString("harga");
            this.var_durasi = data.getString("durasi");
            
            JOptionPane.showMessageDialog(null, userName);
        } else {
            PreparedStatement perintah = koneksiDB.prepareStatement(sql);
            perintah.setString(1, id);
            perintah.setString(2, nm);
            perintah.setString(3, hrg);
            perintah.setString(4, drs);
            perintah.executeUpdate();
            
            JOptionPane.showMessageDialog(null, "Berhasil disimpan");
            
            this.validasi = false;
            this.var_nama = null;
            this.var_harga = null;
            this.var_durasi = null;
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, e.getMessage());
        System.err.println(e.getMessage());
    }
}

public void ubahPaketPRS(String id, String nm, String hrg, String drs){
    try {
        String sqlUbah = "UPDATE paket Set Nama = ?, Harga = ?, Durasi = ? Where id_paket = ?";
        PreparedStatement ubah = koneksiDB.prepareStatement(sqlUbah);
        ubah.setString(1, nm);
        ubah.setString(2, hrg);
        ubah.setString(3, drs);
        ubah.setString(4, id);
        
        ubah.executeUpdate();
        JOptionPane.showMessageDialog(null, "Data Berhasil Diubah");
    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, e.getMessage());
    }
}

public void hapusPaketPRT(String id){
    try {
        String sqlHapus = "DELETE FROM paket WHERE id_paket = ?";
        PreparedStatement hapus = koneksiDB.prepareStatement(sqlHapus);
        hapus.setString(1, id);
        hapus.executeUpdate();
        
        JOptionPane.showMessageDialog(null, "Data berhasil dihapus");
    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, e.getMessage());
    }
}

// CODINGAN PEMBAYARAN

public void loadDataPembayaran(JTable tabel, String sql){
    try {
            Statement perintah = koneksiDB.createStatement();
            ResultSet ds = perintah.executeQuery(sql);
            ResultSetMetaData data = ds.getMetaData();
            int kolom = data.getColumnCount();
            DefaultTableModel model = new DefaultTableModel();
//            for(int i = 1; i < kolom; i++){
//            model.addColumn(data.getColumnName(i));
//        }
            model.addColumn("ID Pembayaran");
            model.addColumn("Jenis Pembayaran");
            model.addColumn("tipe Pembayaran");
            model.getDataVector().clear();
            model.fireTableDataChanged();
            while(ds.next()){
                Object[] baris = new Object[kolom];
                for(int j = 1; j <= kolom; j++){
                    baris[j - 1] = ds.getObject(j);
                }
                model.addRow(baris);
                tabel.setModel(model);
            }
        } catch (Exception e) {
        }
}

public void simpanPembayaranPRT(String id, String jns, String rek){
    try {
        String sql = "insert into pembayaran (id_pembayaran, jenis_pembayaran, tipe) VALUE(?, ?, ?)"; 
        String checkPrimary = "SELECT * FROM pembayaran where id_pembayaran = '"+id+"'";
        Statement checkData = koneksiDB.createStatement();
        ResultSet data = checkData.executeQuery(checkPrimary);
        if (data.next()){
            String isi = 
                    "\n Jenis Pembayaran : "+data.getString("jenis_pembayaran")+
                    "\n Tipe Pembayaran : "+data.getString("tipe");
            JOptionPane.showMessageDialog(null, "id_rekening sudah Terdaftar"+ isi);
            
            this.validasi = true;
            this.var_jenis = data.getString("jenis_pembayaran");
            this.var_tipe = data.getString("tipe");
            
            JOptionPane.showMessageDialog(null, userName);
        } else {
            PreparedStatement perintah = koneksiDB.prepareStatement(sql);
            perintah.setString(1, id);
            perintah.setString(2, jns);
            perintah.setString(3, rek);
            perintah.executeUpdate();
            
            JOptionPane.showMessageDialog(null, "Berhasil disimpan");
            
            this.validasi = false;
            this.var_jenis = null;
            this.var_tipe = null;
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, e.getMessage());
        System.err.println(e.getMessage());
    }
}

public void ubahPembayaranPRS(String id, String jns, String rek){
    try {
        String sqlUbah = "UPDATE pembayaran Set jenis_pembayaran = ?, tipe = ? Where id_pembayaran = ?";
        PreparedStatement ubah = koneksiDB.prepareStatement(sqlUbah);
        ubah.setString(1, jns);
        ubah.setString(2, rek);
        ubah.setString(3, id);
        
        ubah.executeUpdate();
        JOptionPane.showMessageDialog(null, "Data Berhasil Diubah");
    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, e.getMessage());
    }
}

public void hapusPembayaranPRT(String id){
    try {
        String sqlHapus = "DELETE FROM pembayaran WHERE id_pembayaran = ?";
        PreparedStatement hapus = koneksiDB.prepareStatement(sqlHapus);
        hapus.setString(1, id);
        hapus.executeUpdate();
        
        JOptionPane.showMessageDialog(null, "Data berhasil dihapus");
    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, e.getMessage());
    }
}

// CODINGAN TRANSAKSI

//public void loadDataTransaksi(JTable tabel, String sql){
//    try {
//            String sqlLoad = """
//            SELECT
//                t.id_transaksi,
//                k.nama_kasir,
//                ks.nama_konsumen,
//                p.nama_paket,
//                m.nama_metode,
//                t.total,
//                t.tgl_transaksi
//            FROM transaksi t
//            JOIN kasir k ON t.kasir = k.id_kasir
//            JOIN konsumen ks ON t.konsumen = ks.id_konsumen
//            JOIN paket p ON t.paket = p.id_paket
//            JOIN metode_pembayaran m ON t.pembayaran = m.id_pembayaran
//            """;
//            Statement perintah = koneksiDB.createStatement();
//            ResultSet ds = perintah.executeQuery(sqlLoad);
//            ResultSetMetaData data = ds.getMetaData();
//            int kolom = data.getColumnCount();
//            DefaultTableModel model = new DefaultTableModel();
////            for(int i = 1; i < kolom; i++){
////            model.addColumn(data.getColumnName(i));
////        }
//            model.addColumn("ID Transaksi");
//            model.addColumn("Kasir");
//            model.addColumn("Tgl Transaksi");
//            model.addColumn("Konsumen");
//            model.addColumn("Paket");
//            model.addColumn("Keterangan");
//            model.addColumn("Pembayaran");
//            model.addColumn("durasi");
//            model.addColumn("Total");
//            model.addColumn("Tgl Ambil");
//            
//            model.getDataVector().clear();
//            model.fireTableDataChanged();
//            while(ds.next()){
//                Object[] baris = new Object[kolom];
//                for(int j = 1; j <= kolom; j++){
//                    baris[j - 1] = ds.getObject(j);
//                }
//                model.addRow(baris);
//                tabel.setModel(model);
//            }
//        } catch (Exception e) {
//        }
//}

public void loadDataTransaksi(JTable tabel) {
    try {
        String sql = """
        SELECT
            t.id_transaksi,
            k.nama AS nama_kasir,
            t.tgl_transaksi,
            ks.nama AS nama_konsumen,
            p.nama AS nama_paket,
            t.keterangan,
            pb.jenis_pembayaran,
            t.durasi,
            t.total,
            t.tgl_ambil
        FROM transaksi t
        LEFT JOIN kasir k ON t.kasir = k.id_kasir
        LEFT JOIN konsumen ks ON t.konsumen = ks.id_konsumen
        LEFT JOIN paket p ON t.paket = p.id_paket
        LEFT JOIN pembayaran pb ON t.pembayaran = pb.id_pembayaran
        """;
        
        Statement st = koneksiDB.createStatement();
        ResultSet rs = st.executeQuery(sql);

        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("ID Transaksi");
        model.addColumn("Kasir");
        model.addColumn("Tgl Transaksi");
        model.addColumn("Konsumen");
        model.addColumn("Paket");
        model.addColumn("Keterangan");
        model.addColumn("Pembayaran");
        model.addColumn("Durasi");
        model.addColumn("Total");
        model.addColumn("Tgl Ambil");

        while (rs.next()) {
            model.addRow(new Object[]{
                rs.getString("id_transaksi"),
                rs.getString("nama_kasir"),
                rs.getString("tgl_transaksi"),
                rs.getString("nama_konsumen"),
                rs.getString("nama_paket"),
                rs.getString("keterangan"),
                rs.getString("jenis_pembayaran"),
                rs.getString("durasi"),
                rs.getString("total"),
                rs.getString("tgl_ambil")
            });
        }

        tabel.setModel(model);

    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, e.getMessage());
    }
}






public void simpanTransaksiPRT(String id_trs, String ksr, String pkt, String kns, String byr, String tgl_trs, String tgl_amb, String total, String ktrg, String durasi){
    try {
        String sql = "insert into transaksi (id_transaksi, kasir, tgl_transaksi, konsumen, paket, keterangan, pembayaran, durasi, total, tgl_ambil) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"; 
        String checkPrimary = "SELECT * FROM transaksi where id_transaksi = '"+id_trs+"'";
        Statement checkData = koneksiDB.createStatement();
        ResultSet data = checkData.executeQuery(checkPrimary);
        if (data.next()){
            String isi = 
                    "\n Kasir : "+data.getString("id_kasir")+
                    "\n Tanggal Transaksi : "+data.getString("transaksi")+
                    "\n Konsumen : "+data.getString("konsumen")+
                    "\n Paket : "+data.getString("paket")+
                    "\n Keterangan : "+data.getString("keterangan")+
                    "\n Pembayaran : "+data.getString("pembayaran")+
                    "\n Durasi : "+data.getString("durasi")+
                    "\n Total : "+data.getString("total")+
                    "\n Tanggal Ambil : "+data.getString("tgl_ambil");
            JOptionPane.showMessageDialog(null, "ID Transaksi sudah Terdaftar"+ isi);
            
            this.validasi = true;
            this.var_kasir = data.getString("kasir");
            this.var_tglTransaksi = data.getString("tgl_transaksi");
            this.var_konsumen = data.getString("konsumen");
            this.var_paket = data.getString("paket");
            this.var_keterangan = data.getString("keterangan");
            this.var_pembayaran = data.getString("pembayaran");
            this.var_durasi = data.getString("durasi");
            this.var_total = data.getString("total");
            this.var_tglAmbil = data.getString("tgl_ambil");
            
            
            JOptionPane.showMessageDialog(null, userName);
        } else {
            PreparedStatement perintah = koneksiDB.prepareStatement(sql);
            perintah.setString(1, id_trs);
            perintah.setString(2, ksr);
            perintah.setString(3, tgl_trs);
            perintah.setString(4, kns);
            perintah.setString(5, pkt);
            perintah.setString(6, ktrg);
            perintah.setString(7, byr);
            perintah.setString(8, durasi);
            perintah.setString(9, total);
            perintah.setString(10, tgl_amb);
            perintah.executeUpdate();
            
            JOptionPane.showMessageDialog(null, "Berhasil disimpan");
            
            this.validasi = false;
            this.var_kasir = null;
            this.var_tglTransaksi = null;
            this.var_konsumen = null;
            this.var_paket = null;
            this.var_keterangan = null;
            this.var_pembayaran = null;
            this.var_durasi = null;
            this.var_total = null;
            this.var_tglAmbil = null;
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, e.getMessage());
        System.err.println(e.getMessage());
    }
}

public void ubahTransaksiPRS(String id_trs, String ksr, String pkt, String kns, String byr, String tgl_trs, String tgl_amb, String total, String ktrg, String durasi){
    try {
        String sqlUbah = "UPDATE transaksi Set kasir = ?, paket = ?, konsumen = ?, pembayaran = ?, tgl_transaksi = ?, tgl_ambil = ?, total = ?, keterangan = ?, durasi = ? Where id_transaksi = ?";
        PreparedStatement ubah = koneksiDB.prepareStatement(sqlUbah);
        ubah.setString(1, ksr);
        ubah.setString(2, pkt);
        ubah.setString(3, kns);
        ubah.setString(4, byr);
        ubah.setString(5, tgl_trs);
        ubah.setString(6, tgl_amb);
        ubah.setString(7, total);
        ubah.setString(8, ktrg);
        ubah.setString(9, durasi);
        ubah.setString(10, id_trs);
        
        ubah.executeUpdate();
        JOptionPane.showMessageDialog(null, "Data Berhasil Diubah");
    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, e.getMessage());
    }
}

public void hapusTransaksiPRT(String id){
    try {
        String sqlHapus = "DELETE FROM transaksi WHERE id_transaksi = ?";
        PreparedStatement hapus = koneksiDB.prepareStatement(sqlHapus);
        hapus.setString(1, id);
        hapus.executeUpdate();
        
        JOptionPane.showMessageDialog(null, "Data berhasil dihapus");
    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, e.getMessage());
    }
}

//public void cetakLaporan(String fileLaporan, String sql){
//    try {
//            // 1. Load jrxml
//            File laporan = new File(fileLaporan);
//            if (!laporan.exists()) {
//                System.err.println("File jrxml tidak ditemukan: " + fileLaporan);
//                return;
//            }
//
//            JasperDesign designLaporan = JRXmlLoader.load(laporan);
//
//            // 2. Override query
//            JRDesignQuery queryLaporan = new JRDesignQuery();
//            queryLaporan.setText(sql);
//            designLaporan.setQuery(queryLaporan);
//
//            // 3. Compile
//            JasperReport objekLaporan = JasperCompileManager.compileReport(designLaporan);
//
//            // 4. Isi parameter (bisa tambah parameter jika perlu)
//            HashMap<String, Object> param = new HashMap<>();
//
//            // 5. Fill report
//            JasperPrint objekPrint = JasperFillManager.fillReport(objekLaporan, param, koneksiDB);
//            
//            JasperReport report = (JasperReport) JRLoader.loadObject(
//                new File(fileLaporan)
//        );
//
//        JasperPrint print = JasperFillManager.fillReport(
//                report,
//                null,
//                koneksiDB
//        );
//
//        JasperViewer.viewReport(print, false);
//
//
//            // 6. Tampilkan viewer
//            JasperViewer.viewReport(objekPrint, false);
//
//        } catch (Exception e) {
//            // JANGAN DITENGKELAM! Tampilkan errornya.
//            e.printStackTrace();
//            javax.swing.JOptionPane.showMessageDialog(null,
//                    "Gagal mencetak laporan:\n" + e.getMessage(),
//                    "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
//        }
//    }
    
public void cetakLaporan(String path, String sql) {
    try {
        InputStream is = getClass().getResourceAsStream(path);
        if (is == null) {
            throw new RuntimeException("File report tidak ditemukan: " + path);
        }

        JasperDesign design = JRXmlLoader.load(is);

        JRDesignQuery query = new JRDesignQuery();
        query.setText(sql);
        design.setQuery(query);

        JasperReport report = JasperCompileManager.compileReport(design);

        JasperPrint print = JasperFillManager.fillReport(
                report,
                null,
                koneksiDB
        );

        JasperViewer.viewReport(print, false);

    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, e.getMessage());
    }
}

    public ResultSet getKasir() {
    try {
        String sql = "SELECT id_kasir, nama FROM kasir";
        PreparedStatement pst = koneksiDB.prepareStatement(sql);
        return pst.executeQuery();
    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, e.getMessage());
        return null;
    }
}
    public ResultSet ambilData(String sql) {
    try {
        Statement st = koneksiDB.createStatement();
        return st.executeQuery(sql);
    } catch (Exception e) {
        System.out.println("Error ambilData: " + e.getMessage());
        return null;
    }
}
    public String getDurasiPaket(String idPaket) {
    try {
        String sql = "SELECT durasi FROM paket WHERE id_paket = ?";
        PreparedStatement ps = koneksiDB.prepareStatement(sql);
        ps.setString(1, idPaket);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            return rs.getString("durasi");
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, e.getMessage());
    }
    return "";
}
    public void cariTransaksi(JTable tabel, String keyword) {
    try {
        String sql =
            "SELECT t.id_transaksi, k.nama AS kasir, ks.nama AS konsumen, " +
            "p.nama AS paket, pb.jenis_pembayaran, t.total, t.tgl_transaksi, t.keterangan, t.durasi, t.tgl_ambil " +
            "FROM transaksi t " +
            "LEFT JOIN kasir k ON t.kasir = k.id_kasir " +
            "LEFT JOIN konsumen ks ON t.konsumen = ks.id_konsumen " +
            "LEFT JOIN paket p ON t.paket = p.id_paket " +
            "LEFT JOIN pembayaran pb ON t.pembayaran = pb.id_pembayaran " +
            "WHERE t.id_transaksi LIKE '%"+keyword+"%' " +
            "OR COALESCE(k.nama,'') LIKE '%"+keyword+"%' " +
            "OR COALESCE(ks.nama,'') LIKE '%"+keyword+"%' " +
            "OR COALESCE(p.nama,'') LIKE '%"+keyword+"%' " +
            "OR COALESCE(pb.jenis_pembayaran,'') LIKE '%"+keyword+"%' " +
            "OR t.tgl_transaksi LIKE '%"+keyword+"%' " +
            "OR t.keterangan LIKE '%"+keyword+"%' " +
            "OR t.durasi LIKE '%"+keyword+"%' " +
            "OR t.tgl_ambil LIKE '%"+keyword+"%'";

        Statement st = koneksiDB.createStatement();
        ResultSet rs = st.executeQuery(sql);

        DefaultTableModel model = buatModelTransaksi();
        isiModel(model, rs);

        tabel.setModel(model);

    } catch (Exception e) {
        e.printStackTrace();
    }
}
    private DefaultTableModel buatModelTransaksi() {
    DefaultTableModel model = new DefaultTableModel();
    model.addColumn("ID Transaksi");
    model.addColumn("Kasir");
    model.addColumn("Tgl Transaksi");
    model.addColumn("Konsumen");
    model.addColumn("Paket");
    model.addColumn("Keterangan");
    model.addColumn("Pembayaran");
    model.addColumn("Durasi");
    model.addColumn("Total");
    model.addColumn("Tgl Ambil");
    return model;
}

    private void isiModel(DefaultTableModel model, ResultSet rs) throws Exception {
    while (rs.next()) {
        model.addRow(new Object[]{
            rs.getString("id_transaksi"),
            rs.getString("kasir"),
            rs.getString("tgl_transaksi"),
            rs.getString("konsumen"),
            rs.getString("paket"),
            rs.getString("keterangan"),
            rs.getString("jenis_pembayaran"),
            rs.getString("durasi"),
            rs.getString("total"),
            rs.getString("tgl_ambil")
        });
    }
}


}
