/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import Helper.DBConnection;
import Helper.ILoginView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import static View.BankaciUpdate.jTable1;
import static View.MudurUpdate.*;
import static View.BankaciUpdate.*;

public class User implements ILoginView {

    String tc;
    String password;
    String name;
    String surname;

    String id; //hesapno
    String miktar;
    String kredisonuc;
    
    public User(){}
    
    public User(String tc, String password){
        this.tc = tc;
        this.password = password;     
    }
    
    public User(String tc, String password, String name, String surname) {
        this.tc = tc;
        this.password = password;
        this.name = name;
        this.surname = surname;
    }

    public User(String tc, String password, String name, String surname, String id, String miktar) {
        this.tc = tc;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.id = id;
        this.miktar = miktar;
    }

    public User(String tc, String password, String name, String surname, String id, String miktar, String kredisonuc) {
        this.tc = tc;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.id = id;
        this.miktar = miktar;
        this.kredisonuc = kredisonuc;
    }
    
    
    public String getTc() {
        return tc;
    }

    public void setTc(String tc) {
        this.tc = tc;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMiktar() {
        return miktar;
    }

    public void setMiktar(String miktar) {
        this.miktar = miktar;
    }

    public String getKredisonuc() {
        return kredisonuc;
    }

    public void setKredisonuc(String kredisonuc) {
        this.kredisonuc = kredisonuc;
    }
    
    
    @Override
    public void selectLogin(String username, String password) {
        DBConnection conn = new DBConnection();
        Connection con = conn.connDb();

        String sqlquery = "";
        try {
            PreparedStatement pst = con.prepareStatement(sqlquery);
            ResultSet rs = pst.executeQuery();
            if (!rs.next()) {
                JOptionPane.showMessageDialog(null, "TC veya Şifre Hatalı");
            } else {
                JOptionPane.showMessageDialog(null, "Giriş Başarılı");
                //new BankaciView().show();
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    public void paraYatırma(String username, String miktar) {
        int intmiktar1 = Integer.parseInt(miktar);
        String sqlquery = "";
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
                String sqlquery2 = "";
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
        String sqlquery = "";
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
                    String sqlquery2 = "";
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
        String sqlquery = "";
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
                    v2.add(rs.getString(""));
                    v2.add(rs.getString(""));
                    v2.add(rs.getString(""));
                    v2.add(rs.getString(""));
                }
                df.addRow(v2);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
    
    public void Insert(String tcduzen, String sifreduzen) {
        String sqlquery = "";

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
                //txttcduzen2.setText("");
                //txtsifreduzen2.setText("");
                //txttcduzen2.requestFocus();
                con.close();
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }    
      
    public void Edit(String id) {
        String sqlquery = "";

        try {
            DBConnection conn = new DBConnection();
            Connection con = conn.connDb();
            Statement stm = con.createStatement();
            PreparedStatement pst = con.prepareStatement(sqlquery);
            stm.executeUpdate(sqlquery);
            JOptionPane.showMessageDialog(null, "Kayıt Düzenlendi");
            tablo_update();
            //txttcduzen2.setText("");
            //txtsifreduzen2.setText("");
            //txttcduzen2.requestFocus();
            con.close();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
    
    public void Delete(String id) {
        String sqlquery = "";

        try {
            DBConnection conn = new DBConnection();
            Connection con = conn.connDb();
            Statement stm = con.createStatement();
            PreparedStatement pst = con.prepareStatement(sqlquery);
            stm.executeUpdate(sqlquery);
            JOptionPane.showMessageDialog(null, "Kayıt Silindi");
            tablo_update();
            //txttcduzen2.setText("");
            //txtsifreduzen2.setText("");
            //txttcduzen2.requestFocus();
            con.close();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
    
}
