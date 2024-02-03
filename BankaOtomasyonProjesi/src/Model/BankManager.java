/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import Helper.DBConnection;
import Helper.ILoginView;

import View.BankaciView;
import View.MudurView;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import static View.MudurUpdate.*;
import static View.BankaciUpdate.*;
import static View.MudurKrediOnay.*;
import View.MudurLogin;

/**
 *
 * @author Excalibur
 */
public class BankManager extends User implements ILoginView {

    public BankManager() {}

    public BankManager(String tc, String password) {
        super(tc, password);
    }  

    public BankManager(String tc, String password, String name, String surname, String id, String miktar) {
        super(tc, password, name, surname, id, miktar);
    }

    public BankManager(String tc, String password, String name, String surname, String id, String miktar, String kredisonuc) {
        super(tc, password, name, surname, id, miktar, kredisonuc);
    }
    
    @Override
    public void selectLogin(String username, String password) {
        DBConnection conn = new DBConnection();
        Connection con = conn.connDb();

        String sqlquery = "SELECT * FROM bankmanager WHERE bankManagerTc='" + username + "' AND bankManagerPass='" + password + "'";
        try {
            PreparedStatement pst = con.prepareStatement(sqlquery);
            ResultSet rs = pst.executeQuery();
            if (!rs.next()) {
                JOptionPane.showMessageDialog(null, "TC veya Şifre Hatalı");
                new MudurLogin().show();
            } else {
                JOptionPane.showMessageDialog(null, "Giriş Başarılı");
                new MudurView().show();
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    public void tablo_update() {
        int c;
        String sqlquery = "SELECT * FROM banker";
        try {
            DBConnection conn = new DBConnection();
            Connection con = conn.connDb();
            Statement stm = con.createStatement();
            PreparedStatement pst = con.prepareStatement(sqlquery);
            ResultSet rs = pst.executeQuery();

            ResultSetMetaData rss = rs.getMetaData();
            c = rss.getColumnCount();
            DefaultTableModel df = (DefaultTableModel) jTable2.getModel();
            df.setRowCount(0);
            while (rs.next()) {
                Vector v2 = new Vector();

                for (int a = 1; a <= c; a++) {
                    v2.add(rs.getString("bankerTc"));
                    v2.add(rs.getString("bankerPass"));
                }
                df.addRow(v2);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    public void Insert(String tcduzen, String sifreduzen) {
        String sqlquery = "INSERT INTO banker VALUES('" + tcduzen + "','" + sifreduzen + "')";

        try {
            if (txttcduzen2.getText().equals("")) {
                JOptionPane.showMessageDialog(null, "Lütfen TC Girin");
            } else if (txtsifreduzen2.getText().equals("")) {
                JOptionPane.showMessageDialog(null, "Lütfen Şifre Girin");
            } else {
                DBConnection conn = new DBConnection();
                Connection con = conn.connDb();
                Statement stm = con.createStatement();
                PreparedStatement pst = con.prepareStatement(sqlquery);
                stm.executeUpdate(sqlquery);
                JOptionPane.showMessageDialog(null, "Kayıt Edildi");
                tablo_update();
                txttcduzen2.setText("");
                txtsifreduzen2.setText("");
                txttcduzen2.requestFocus();
                con.close();
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    public void Edit(String id) {
        String sqlquery = "UPDATE banker SET bankerTc='" + txttcduzen2.getText() + "',bankerPass='" + txtsifreduzen2.getText() + "'WHERE bankerTc='" + id + "'";

        try {
            DBConnection conn = new DBConnection();
            Connection con = conn.connDb();
            Statement stm = con.createStatement();
            PreparedStatement pst = con.prepareStatement(sqlquery);
            stm.executeUpdate(sqlquery);
            JOptionPane.showMessageDialog(null, "Kayıt Düzenlendi");
            tablo_update();
            txttcduzen2.setText("");
            txtsifreduzen2.setText("");
            txttcduzen2.requestFocus();
            con.close();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    public void Delete(String id) {
        String sqlquery = "DELETE FROM banker WHERE bankerTc='" + id + "'";

        try {
            DBConnection conn = new DBConnection();
            Connection con = conn.connDb();
            Statement stm = con.createStatement();
            PreparedStatement pst = con.prepareStatement(sqlquery);
            stm.executeUpdate(sqlquery);
            JOptionPane.showMessageDialog(null, "Kayıt Silindi");
            tablo_update();
            txttcduzen2.setText("");
            txtsifreduzen2.setText("");
            txttcduzen2.requestFocus();
            con.close();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    public void tablo_updateKredi() {
        int c;
        String sqlquery = "SELECT * FROM customeraccount WHERE customerCreditStatus = 1";
        try {
            DBConnection conn = new DBConnection();
            Connection con = conn.connDb();
            Statement stm = con.createStatement();
            PreparedStatement pst = con.prepareStatement(sqlquery);

            ResultSet rs = pst.executeQuery();
            ResultSetMetaData rss = rs.getMetaData();
            c = rss.getColumnCount();
            DefaultTableModel df = (DefaultTableModel) jTableMudurKredi.getModel();
            df.setRowCount(0);
            while (rs.next()) {
                Vector v2 = new Vector();

                for (int a = 1; a <= c; a++) {
                    v2.add(rs.getString("accountNo"));
                    v2.add(rs.getString("customerTc"));
                    v2.add(rs.getString("customerName"));
                    v2.add(rs.getString("customerSurname"));
                    v2.add(rs.getString("customerPass"));
                    v2.add(rs.getString("customerCreditStatus"));
                    v2.add(rs.getString("customerCreditResult"));
                }
                df.addRow(v2);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

    public void KrediKabul(String username, String a) {
        String sqlquery = "SELECT * FROM customeraccount WHERE accountNo='" + username + "'";
        try {
            DBConnection conn = new DBConnection();
            Connection con = conn.connDb();
            PreparedStatement pst = con.prepareStatement(sqlquery);
            ResultSet rs = pst.executeQuery();
            if (!rs.next()) {
                JOptionPane.showMessageDialog(null, "HesapNo Hatalı");
            } else {
                String sqlquery2 = "UPDATE customeraccount SET customerCreditResult='" + a + "'WHERE accountNo='" + username + "'";
                PreparedStatement pst1 = con.prepareStatement(sqlquery2);
                pst1.execute();

                JOptionPane.showMessageDialog(null, "Kabul Edildi");

            }
            con.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
        tablo_updateKredi();
    }
    
    public void KrediRed(String username, String a) {
        String sqlquery = "SELECT * FROM customeraccount WHERE accountNo='" + username + "'";
        try {
            DBConnection conn = new DBConnection();
            Connection con = conn.connDb();
            PreparedStatement pst = con.prepareStatement(sqlquery);

            ResultSet rs = pst.executeQuery();
            if (!rs.next()) {
                JOptionPane.showMessageDialog(null, "HesapNo Hatalı");
            } else {
                String sqlquery2 = "UPDATE customeraccount SET customerCreditResult='" + a + "'WHERE accountNo='" + username + "'";
                PreparedStatement pst1 = con.prepareStatement(sqlquery2);
                pst1.execute();

                JOptionPane.showMessageDialog(null, "Red Edildi");

            }
            con.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
        tablo_updateKredi();
    }
    
}
