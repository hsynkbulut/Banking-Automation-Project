/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import Helper.DBConnection;
import Helper.ILoginView;
import View.BankaciLogin;
import View.BankaciView;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import static View.BankaciUpdate.*;
//import static View.BankacihesapCreate.jTableBankerAccount;
import static View.BankacihesapCreate.*;

/**
 *
 * @author Excalibur
 */
public class Banker extends User implements ILoginView {

    public Banker() {}

    public Banker(String tc, String password) {
        super(tc, password);
    }

    public Banker(String tc, String password, String name, String surname) {
        super(tc, password, name, surname);
    }
    
    public Banker(String tc, String password, String name, String surname, String id, String miktar) {
        super(tc, password, name, surname, id, miktar);
    }
    
    @Override
    public void selectLogin(String username, String password) {
        DBConnection conn = new DBConnection();
        Connection con = conn.connDb();

        String sqlquery = "SELECT * FROM banker WHERE bankerTc='" + username + "' AND bankerPass='" + password + "'";
        try {
            PreparedStatement pst = con.prepareStatement(sqlquery);
            ResultSet rs = pst.executeQuery();
            if (!rs.next()) {
                JOptionPane.showMessageDialog(null, "TC veya Şifre Hatalı");
                new BankaciLogin().show();
            } else {
                JOptionPane.showMessageDialog(null, "Giriş Başarılı");
                new BankaciView().show();
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    public void paraYatırma(String hesapno, String miktar) {

        int intmiktar1 = Integer.parseInt(miktar);
        String sqlquery = "SELECT * FROM customeraccount WHERE accountNo='" + hesapno + "'";
        try {
            DBConnection conn = new DBConnection();
            Connection con = conn.connDb();
            PreparedStatement pst = con.prepareStatement(sqlquery);

            ResultSet rs = pst.executeQuery();
            if (!rs.next()) {
                JOptionPane.showMessageDialog(null, "HesapNo Hatalı");
            } else {
                String bakiye = rs.getString("customerBalance");
                int toplambakiye = Integer.parseInt(bakiye) + intmiktar1;
                String sqlquery2 = "UPDATE customeraccount SET customerBalance='" + toplambakiye + "'WHERE accountNo='" + hesapno + "'";
                PreparedStatement pst1 = con.prepareStatement(sqlquery2);
                pst1.execute();

                JOptionPane.showMessageDialog(null, "Para Yatırıldı");
            }
            con.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    public void paraCekme(String hesapno, String miktar) {

        int intmiktar1 = Integer.parseInt(miktar);
        String sqlquery = "SELECT * FROM customeraccount WHERE accountNo='" + hesapno + "'";
        try {
            DBConnection conn = new DBConnection();
            Connection con = conn.connDb();
            PreparedStatement pst = con.prepareStatement(sqlquery);

            ResultSet rs = pst.executeQuery();
            if (!rs.next()) {
                JOptionPane.showMessageDialog(null, "HesapNo Hatalı");
            } else {
                String bakiye = rs.getString("customerBalance");
                int toplambakiye = Integer.parseInt(bakiye) - intmiktar1;
                if (intmiktar1 > Integer.parseInt(bakiye)) {
                    JOptionPane.showMessageDialog(null, "Hesaptaki Miktar Yetersiz");
                } else {
                    String sqlquery2 = "UPDATE customeraccount SET customerBalance='" + toplambakiye + "'WHERE accountNo='" + hesapno + "'";
                    PreparedStatement pst1 = con.prepareStatement(sqlquery2);
                    pst1.execute();

                    JOptionPane.showMessageDialog(null, "Para Çekildi");
                }
            }
            con.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    public void tablo_update() {
        int c;
        String sqlquery = "SELECT * FROM customer";
        try {
            DBConnection conn = new DBConnection();
            Connection con = conn.connDb();
            Statement stm = con.createStatement();
            PreparedStatement pst = con.prepareStatement(sqlquery);

            ResultSet rs = pst.executeQuery();
            ResultSetMetaData rss = rs.getMetaData();
            c = rss.getColumnCount();
            DefaultTableModel df = (DefaultTableModel) jTable1.getModel();
            df.setRowCount(0);
            while (rs.next()) {
                Vector v2 = new Vector();

                for (int a = 1; a <= c; a++) {
                    v2.add(rs.getString("customerTc"));
                    v2.add(rs.getString("customerName"));
                    v2.add(rs.getString("customerSurname"));
                    v2.add(rs.getString("customerPass"));
                }
                df.addRow(v2);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    public void Insert(String tcduzen, String isimduzen, String soyisimduzen, String sifreduzen) {
        String sqlquery = "INSERT INTO customer VALUES('" + tcduzen + "','" + isimduzen + "','" + soyisimduzen + "','" + sifreduzen + "')";

        try {
            if (txttcduzen.getText().equals("")) {
                JOptionPane.showMessageDialog(null, "Lütfen TC Girin");
            }
            if (txtisimduzen.getText().equals("")) {
                JOptionPane.showMessageDialog(null, "Lütfen İsim Girin");
            }
            if (txtsoyisimduzen.getText().equals("")) {
                JOptionPane.showMessageDialog(null, "Lütfen Soyisim Girin");
            } else if (txtsifreduzen.getText().equals("")) {
                JOptionPane.showMessageDialog(null, "Lütfen Şifre Girin");
            } else {
                DBConnection conn = new DBConnection();
                Connection con = conn.connDb();
                Statement stm = con.createStatement();
                PreparedStatement pst = con.prepareStatement(sqlquery);

                stm.executeUpdate(sqlquery);
                JOptionPane.showMessageDialog(null, "Kayıt Edildi");
                tablo_update();
                txttcduzen.setText("");
                txtisimduzen.setText("");
                txtsoyisimduzen.setText("");
                txtsifreduzen.setText("");
                txttcduzen.requestFocus();
                con.close();
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    public void Edit(String id) {
        String sqlquery = "UPDATE customer SET customerTc='" + txttcduzen.getText() + "',customerName='" + txtisimduzen.getText() + "',customerSurname='" + txtsoyisimduzen.getText() + "',customerPass='" + txtsifreduzen.getText() + "'WHERE customerTc='" + id + "'";

        try {
            DBConnection conn = new DBConnection();
            Connection con = conn.connDb();
            Statement stm = con.createStatement();
            PreparedStatement pst = con.prepareStatement(sqlquery);

            stm.executeUpdate(sqlquery);
            JOptionPane.showMessageDialog(null, "Kayıt Düzenlendi");
            tablo_update();
            txttcduzen.setText("");
            txtisimduzen.setText("");
            txtsoyisimduzen.setText("");
            txtsifreduzen.setText("");
            txttcduzen.requestFocus();
            con.close();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    public void Delete(String id) {
        String sqlquery = "DELETE FROM customer WHERE customerTc='" + id + "'";

        try {
            DBConnection conn = new DBConnection();
            Connection con = conn.connDb();
            Statement stm = con.createStatement();
            PreparedStatement pst = con.prepareStatement(sqlquery);

            stm.executeUpdate(sqlquery);
            JOptionPane.showMessageDialog(null, "Kayıt Silindi");
            tablo_update();
            txttcduzen.setText("");
            txtisimduzen.setText("");
            txtsoyisimduzen.setText("");
            txtsifreduzen.setText("");
            txttcduzen.requestFocus();
            con.close();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    public void TabloBankHesapUpdate() {
        int c;
        String sqlquery = "SELECT * FROM customeraccount";
        try {
            DBConnection conn = new DBConnection();
            Connection con = conn.connDb();
            Statement stm = con.createStatement();
            PreparedStatement pst = con.prepareStatement(sqlquery);

            ResultSet rs = pst.executeQuery();
            ResultSetMetaData rss = rs.getMetaData();
            c = rss.getColumnCount();
            DefaultTableModel df = (DefaultTableModel) jTableBankerAccount.getModel();
            df.setRowCount(0);
            while (rs.next()) {
                Vector v2 = new Vector();

                for (int a = 1; a <= c; a++) {
                    v2.add(rs.getString("accountNo"));
                    v2.add(rs.getString("customerTc"));
                    v2.add(rs.getString("customerName"));
                    v2.add(rs.getString("customerSurname"));
                    v2.add(rs.getString("customerPass"));
                    v2.add(rs.getString("customerBalance"));
                }
                df.addRow(v2);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    public void BankerAccountCreate(String tcduzen, String isimduzen, String soyisimduzen, String sifreduzen) {
        String sqlquery = "INSERT INTO customeraccount(customerTc,customerName,customerSurname,customerPass,customerBalance) VALUES('" + tcduzen + "','" + isimduzen + "','" + soyisimduzen + "','" + sifreduzen + "','" + 0 + "')";

        try {
            if (txtTcHesap.getText().equals("")) {
                JOptionPane.showMessageDialog(null, "Lütfen TC Girin");
            }
            if (txtİsimHesap.getText().equals("")) {
                JOptionPane.showMessageDialog(null, "Lütfen İsim Girin");
            }
            if (txtSoyisimHesap.getText().equals("")) {
                JOptionPane.showMessageDialog(null, "Lütfen Soyisim Girin");
            } else if (txtSifreHesap.getText().equals("")) {
                JOptionPane.showMessageDialog(null, "Lütfen Şifre Girin");
            } else {
                DBConnection conn = new DBConnection();
                Connection con = conn.connDb();
                Statement stm = con.createStatement();
                PreparedStatement pst = con.prepareStatement(sqlquery);

                stm.executeUpdate(sqlquery);
                JOptionPane.showMessageDialog(null, "Kayıt Edildi");
                txtTcHesap.setText("");
                txtİsimHesap.setText("");
                txtSoyisimHesap.setText("");
                txtSifreHesap.setText("");
                txtTcHesap.requestFocus();
                TabloBankHesapUpdate();
                con.close();
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
    
    public void BankerAccountDelete(String id) {
        String sqlquery = "DELETE FROM customeraccount WHERE accountNo='" + id + "'";

        try {
            DBConnection conn = new DBConnection();
            Connection con = conn.connDb();
            Statement stm = con.createStatement();
            PreparedStatement pst = con.prepareStatement(sqlquery);

            stm.executeUpdate(sqlquery);
            JOptionPane.showMessageDialog(null, "Kayıt Silindi");
            TabloBankHesapUpdate();
            txtTcHesap.setText("");
            txtİsimHesap.setText("");
            txtSoyisimHesap.setText("");
            txtSifreHesap.setText("");
            txtTcHesap.requestFocus();
            con.close();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
    
}
