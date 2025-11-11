/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pbo_2310010226;

import javax.swing.*;
import java.sql.*;
import javax.swing.table.DefaultTableModel;
import java.sql.Connection;

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
    public String var_nama, var_alamat, var_telpon, var_harga, var_durasi, var_unit, var_jenis, var_rekening, var_kasir, var_paket, var_konsumen, var_pembayaran, var_tglTransaksi, var_tglAmbil, var_total, var_keterangan, var_level, var_password = null;
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
            model.addColumn("Unit");
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

public void simpanPaketPRT(String id, String nm, String hrg, String drs, String unit){
    try {
        String sql = "insert into paket (id_paket, nama, harga, durasi, unit) VALUE(?, ?, ?, ?, ?)"; 
        String checkPrimary = "SELECT * FROM paket where id_paket = '"+id+"'";
        Statement checkData = koneksiDB.createStatement();
        ResultSet data = checkData.executeQuery(checkPrimary);
        if (data.next()){
            String isi = 
                    "\n Nama : "+data.getString("nama")+
                    "\n Harga : "+data.getString("harga")+
                    "\n Durasi : "+data.getString("durasi")+
                    "\n Unit : "+data.getString("unit");
            JOptionPane.showMessageDialog(null, "id_paket sudah Terdaftar"+ isi);
            
            this.validasi = true;
            this.var_nama = data.getString("nama");
            this.var_harga = data.getString("harga");
            this.var_durasi = data.getString("durasi");
            this.var_unit = data.getString("unit");
            
            JOptionPane.showMessageDialog(null, userName);
        } else {
            PreparedStatement perintah = koneksiDB.prepareStatement(sql);
            perintah.setString(1, id);
            perintah.setString(2, nm);
            perintah.setString(3, hrg);
            perintah.setString(4, drs);
            perintah.setString(5, unit);
            perintah.executeUpdate();
            
            JOptionPane.showMessageDialog(null, "Berhasil disimpan");
            
            this.validasi = false;
            this.var_nama = null;
            this.var_harga = null;
            this.var_durasi = null;
            this.var_unit = null;
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, e.getMessage());
        System.err.println(e.getMessage());
    }
}

public void ubahPaketPRS(String id, String nm, String hrg, String drs, String unit){
    try {
        String sqlUbah = "UPDATE paket Set Nama = ?, Harga = ?, Durasi = ?, Unit = ? Where id_paket = ?";
        PreparedStatement ubah = koneksiDB.prepareStatement(sqlUbah);
        ubah.setString(1, nm);
        ubah.setString(2, hrg);
        ubah.setString(3, drs);
        ubah.setString(4, unit);
        ubah.setString(5, id);
        
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
            model.addColumn("Rekening Pembayaran");
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
        String sql = "insert into pembayaran (id_pembayaran, jenis_pembayaran, rekening_pembayaran) VALUE(?, ?, ?)"; 
        String checkPrimary = "SELECT * FROM pembayaran where id_pembayaran = '"+id+"'";
        Statement checkData = koneksiDB.createStatement();
        ResultSet data = checkData.executeQuery(checkPrimary);
        if (data.next()){
            String isi = 
                    "\n Jenis Pembayaran : "+data.getString("jenis_pembayaran")+
                    "\n Rekening Pembayaran : "+data.getString("rekening_pembayaran");
            JOptionPane.showMessageDialog(null, "id_rekening sudah Terdaftar"+ isi);
            
            this.validasi = true;
            this.var_jenis = data.getString("jenis_pembayaran");
            this.var_rekening = data.getString("rekening_pembayaran");
            
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
            this.var_rekening = null;
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, e.getMessage());
        System.err.println(e.getMessage());
    }
}

public void ubahPembayaranPRS(String id, String jns, String rek){
    try {
        String sqlUbah = "UPDATE pembayaran Set jenis_pembayaran = ?, rekening_pembayaran = ? Where id_pembayaran = ?";
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

public void loadDataTransaksi(JTable tabel, String sql){
    try {
            Statement perintah = koneksiDB.createStatement();
            ResultSet ds = perintah.executeQuery(sql);
            ResultSetMetaData data = ds.getMetaData();
            int kolom = data.getColumnCount();
            DefaultTableModel model = new DefaultTableModel();
//            for(int i = 1; i < kolom; i++){
//            model.addColumn(data.getColumnName(i));
//        }
            model.addColumn("ID Transaksi");
            model.addColumn("Kasir");
            model.addColumn("Tgl Transaksi");
            model.addColumn("Konsumen");
            model.addColumn("Paket");
            model.addColumn("Keterangan");
            model.addColumn("Pembayaran");
            model.addColumn("Unit");
            model.addColumn("Total");
            model.addColumn("Tgl Ambil");
            
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

public void simpanTransaksiPRT(String id_trs, String ksr, String pkt, String kns, String byr, String tgl_trs, String tgl_amb, String total, String ktrg, String unit){
    try {
        String sql = "insert into transaksi (id_transaksi, kasir, tgl_transaksi, konsumen, paket, keterangan, pembayaran, unit, total, tgl_ambil) VALUE(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"; 
        String checkPrimary = "SELECT * FROM transaksi where id_transaksi = '"+id_trs+"'";
        Statement checkData = koneksiDB.createStatement();
        ResultSet data = checkData.executeQuery(checkPrimary);
        if (data.next()){
            String isi = 
                    "\n Kasir : "+data.getString("id_kasir")+
                    "\n Tanggal Transaksi : "+data.getString("transaksi")+
                    "\n Konsumen : "+data.getString("konsumen")+
                    "\n Paket : "+data.getString("paket")+
                    "\n Pembayaran : "+data.getString("pembataran")+
                    "\n Keterangan : "+data.getString("keterangan")+
                    "\n Unit : "+data.getString("unit")+
                    "\n Total : "+data.getString("total")+
                    "\n Tanggal Ambil : "+data.getString("tgl_ambil");
            JOptionPane.showMessageDialog(null, "ID Transaksi sudah Terdaftar"+ isi);
            
            this.validasi = true;
            this.var_kasir = data.getString("kasir");
            this.var_paket = data.getString("paket");
            this.var_konsumen = data.getString("konsumen");
            this.var_pembayaran = data.getString("pembayaran");
            this.var_tglTransaksi = data.getString("tgl_transaksi");
            this.var_tglAmbil = data.getString("tgl_ambil");
            this.var_total = data.getString("total");
            this.var_keterangan = data.getString("keterangan");
            this.var_unit = data.getString("unit");
            
            
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
            perintah.setString(8, unit);
            perintah.setString(9, total);
            perintah.setString(10, tgl_amb);
            perintah.executeUpdate();
            
            JOptionPane.showMessageDialog(null, "Berhasil disimpan");
            
            this.validasi = false;
            this.var_kasir = null;
            this.var_paket = null;
            this.var_konsumen = null;
            this.var_pembayaran = null;
            this.var_tglTransaksi = null;
            this.var_tglAmbil = null;
            this.var_total = null;
            this.var_keterangan = null;
            this.var_unit = null;
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, e.getMessage());
        System.err.println(e.getMessage());
    }
}

public void ubahTransaksiPRS(String id_trs, String ksr, String pkt, String kns, String byr, String tgl_trs, String tgl_amb, String total, String ktrg, String unit){
    try {
        String sqlUbah = "UPDATE transaksi Set kasir = ?, paket = ?, konsumen = ?, pembayaran = ?, tgl_transaksi = ?, tgl_ambil = ?, total = ?, keterangan = ?, unit = ? Where id_transaksi = ?";
        PreparedStatement ubah = koneksiDB.prepareStatement(sqlUbah);
        ubah.setString(1, ksr);
        ubah.setString(2, pkt);
        ubah.setString(3, kns);
        ubah.setString(4, byr);
        ubah.setString(5, tgl_trs);
        ubah.setString(6, tgl_amb);
        ubah.setString(7, total);
        ubah.setString(8, ktrg);
        ubah.setString(9, unit);
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
}
