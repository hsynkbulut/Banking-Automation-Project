/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import Helper.DBConnection;
import Helper.ILoginView;
import View.MusteriView;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import static View.MusteriKrediBasvuru.jTableMusteriKredi;
import static View.MusteriKrediBasvuru.txtTcMusteriKredi;
import View.MusteriLogin;
import static View.MusteriLogin.txtmüsteritc;
import static View.MusterihesapView.*;

import java.sql.SQLException;
/**
 *
 * @author Excalibur
 */
public class Customer extends User implements ILoginView {

    public Customer() {}

    public Customer(String tc, String password) {
        super(tc, password);
    }

    public Customer(String tc, String password, String name, String surname) {
        super(tc, password, name, surname);
    }

    public Customer(String tc, String password, String name, String surname, String id, String miktar) {
        super(tc, password, name, surname, id, miktar);
    }
    
    @Override
    public void selectLogin(String username, String password) {
        DBConnection conn = new DBConnection();
        Connection con = conn.connDb();

        String sqlquery = "SELECT * FROM customer WHERE customerTc='" + username + "' AND customerPass='" + password + "'";
        try {
            PreparedStatement pst = con.prepareStatement(sqlquery);
            ResultSet rs = pst.executeQuery();
            if (!rs.next()) {
                JOptionPane.showMessageDialog(null, "TC veya Şifre Hatalı");
                new MusteriLogin().show();
            } else {
                JOptionPane.showMessageDialog(null, "Giriş Başarılı");
                new MusteriView().show();
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    public void paraYatırma(String username, String miktar) {

        int intmiktar1 = Integer.parseInt(miktar);
        String sqlquery = "SELECT * FROM customeraccount WHERE accountNo='" + username + "'";
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
                String sqlquery2 = "UPDATE customeraccount SET customerBalance='" + toplambakiye + "'WHERE accountNo='" + username + "'";
                PreparedStatement pst1 = con.prepareStatement(sqlquery2);
                pst1.execute();

                JOptionPane.showMessageDialog(null, "Para Yatırıldı");
            }
            con.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    public void paraCekme(String username, String miktar) {

        int intmiktar1 = Integer.parseInt(miktar);
        String sqlquery = "SELECT * FROM customeraccount WHERE accountNo='" + username + "'";
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
                    String sqlquery2 = "UPDATE customeraccount SET customerBalance='" + toplambakiye + "'WHERE accountNo='" + username + "'";
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

    public void tablo_updateMusteriKredi() {
        int c;

        String sqlquery = "SELECT * FROM customeraccount WHERE customerTc='" + txtTcMusteriKredi.getText() + "'";
        try {
            DBConnection conn = new DBConnection();
            Connection con = conn.connDb();
            Statement stm = con.createStatement();
            PreparedStatement pst = con.prepareStatement(sqlquery);

            ResultSet rs = pst.executeQuery();
            ResultSetMetaData rss = rs.getMetaData();
            c = rss.getColumnCount();
            DefaultTableModel df = (DefaultTableModel) jTableMusteriKredi.getModel();
            df.setRowCount(0);
            while (rs.next()) {

                Vector v2 = new Vector();

                for (int a = 1; a <= c; a++) {
                    v2.add(rs.getString("accountNo"));
                    v2.add(rs.getString("customerTc"));
                    v2.add(rs.getString("customerCreditResult"));

                }
                df.addRow(v2);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

    public void KrediSonucGoster(String username) {
        String sqlquery = "SELECT * FROM customeraccount WHERE customerTc='" + username + "'";

        try {
            DBConnection conn = new DBConnection();
            Connection con = conn.connDb();
            PreparedStatement pst = con.prepareStatement(sqlquery);

            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                tablo_updateMusteriKredi();

            } else {
                JOptionPane.showMessageDialog(null, "TC Hatalı");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    public void MusteriKrediBasvurusu(String username1) throws SQLException, ClassNotFoundException {
        String sqlquery = "SELECT * FROM customeraccount WHERE accountNo='" + username1 + "' ";

        try {
            DBConnection conn = new DBConnection();
            Connection con = conn.connDb();
            PreparedStatement pst = con.prepareStatement(sqlquery);

            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                String sqlquery2 = "UPDATE customeraccount SET customerCreditStatus = (case customerCreditStatus "
                        + "                                                      when '0' then '1'"
                        + "                                                          end)WHERE accountNo='" + username1 + "' ";
                PreparedStatement pst1 = con.prepareStatement(sqlquery2);
                pst1.execute();
                JOptionPane.showMessageDialog(null, "Başvuru Yapıldı");

            } else {
                JOptionPane.showMessageDialog(null, "Hesap No Hatalı");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Zaten başvuru yaptınız!");
        }
    }

    public void tabloUpdateCustomerAccount() {
        int c;
        String sqlquery = "SELECT * FROM customeraccount WHERE customerTc='" + txtmüsteritc.getText() + "'";
        try {
            DBConnection conn = new DBConnection();
            Connection con = conn.connDb();
            Statement stm = con.createStatement();
            PreparedStatement pst = con.prepareStatement(sqlquery);

            ResultSet rs = pst.executeQuery();
            ResultSetMetaData rss = rs.getMetaData();
            c = rss.getColumnCount();
            DefaultTableModel df = (DefaultTableModel) jTableMusteriHesabi.getModel();
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

}
