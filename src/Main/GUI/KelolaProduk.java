package Main.GUI;

import Main.Controller.DataUser;
import Main.Controller.Koneksi;
import Main.Controller.UserSession;

import javax.swing.*;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class KelolaProduk {
    JFrame window 	= new JFrame("Kelola User");
    Koneksi koneksi = new Koneksi();
    ResultSet resultSet;
    Statement statement;


    String[][] datas        = new String[500][4];
    String[] kolom          = {"ID","Nama Kopi","Harga","Stok"};
    JTable tTable           = new JTable(datas, kolom);
    JScrollPane scrollPane  = new JScrollPane(tTable);

    JLabel lId      = new JLabel("ID Kopi");
    JLabel lNama    = new JLabel("Nama");
    JLabel lHarga   = new JLabel("Harga");
    JLabel lStok    = new JLabel("Stok");

    JTextField fId      = new JTextField();
    JTextField fNama    = new JTextField();
    JTextField fHarga   = new JTextField();
    JTextField fStok    = new JTextField();

    JButton bTambah     = new JButton("Tambah");
    JButton bUpdate 	= new JButton("Ubah");
    JButton bHapus   	= new JButton("Hapus");
    JButton bKembali 	= new JButton("Kembali");

    public KelolaProduk(){
        if(UserSession.getRole()!=1){
            JOptionPane.showMessageDialog(null, "Akses tidak diberikan!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            window.setVisible(false);
            new Login();
        }else {
            initComponents();
            loadData();
            window.setLayout(null);
            window.setSize(700, 700);
            window.setVisible(true);
            window.setLocationRelativeTo(null);
            window.setResizable(false);

            tTable.addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    try {
                        int baris = tTable.rowAtPoint(e.getPoint());
                        String id = tTable.getValueAt(baris, 0).toString();
                        fId.setText(id);
                        String nama = tTable.getValueAt(baris, 1).toString();
                        fNama.setText(nama);
                        String harga = tTable.getValueAt(baris, 2).toString();
                        fHarga.setText(harga);
                        String stok = tTable.getValueAt(baris, 3).toString();
                        fStok.setText(stok);


                    } catch (Exception ea) {
                        JOptionPane.showMessageDialog(null, "Mohon Maaf Data " + ea.getMessage());
                    }
                }

                @Override
                public void mousePressed(MouseEvent e) {

                }

                @Override
                public void mouseReleased(MouseEvent e) {

                }

                @Override
                public void mouseEntered(MouseEvent e) {

                }

                @Override
                public void mouseExited(MouseEvent e) {

                }
            });

            bTambah.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        String MD5 = DataUser.getMd5(fStok.getText());
                        statement = koneksi.getConnection().createStatement();
                        String sql = "INSERT INTO user VALUES(default,'" + fNama.getText() + "','" + fHarga.getText() + "','" + fStok.getText() + "'";
                        int disimpan = statement.executeUpdate(sql);
                        if (disimpan == 1) {
                            JOptionPane.showMessageDialog(null, "Berhasil tambah data!", "Peringatan", JOptionPane.WARNING_MESSAGE);
                            statement.close();
                            window.setVisible(false);
                            new KelolaProduk();
                        }
                    } catch (SQLException sqlError) {
                        JOptionPane.showMessageDialog(null, "Gagal mendaftar! error : " + sqlError);
                    } catch (ClassNotFoundException classError) {
                        JOptionPane.showMessageDialog(null, "Driver tidak ditemukan !!");
                    }
                }
            });

            bUpdate.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        statement = koneksi.getConnection().createStatement();
                        String sql = "UPDATE user set nama_kopi='" + fNama.getText() + "',harga='" + fHarga.getText() + "',stok='" + fStok.getText() + "' WHERE id_kopi='" + fId.getText() + "'";
                        int disimpan = statement.executeUpdate(sql);
                        if (disimpan == 1) {
                            JOptionPane.showMessageDialog(null, "Berhasil Diubah!", "Peringatan", JOptionPane.WARNING_MESSAGE);
                            statement.close();
                            window.setVisible(false);
                            new KelolaProduk();
                        }

                    } catch (SQLException sqlError) {
                        JOptionPane.showMessageDialog(null, "Gagal mendaftar! error : " + sqlError);
                    } catch (ClassNotFoundException classError) {
                        JOptionPane.showMessageDialog(null, "Driver tidak ditemukan !!");
                    }
                }
            });

            bHapus.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent ae) {
                    try {
                        statement = koneksi.getConnection().createStatement();
                        String sql = "DELETE FROM user WHERE id='" + fId.getText() + "'";
                        statement.execute(sql);
                        JOptionPane.showMessageDialog(null, "Berhasil Hapus Data!", "Peringatan", JOptionPane.WARNING_MESSAGE);
                        statement.close();
                        window.setVisible(false);
                        new KelolaProduk();
                    } catch (HeadlessException | SQLException | ClassNotFoundException e) {
                        JOptionPane.showMessageDialog(null, e.getMessage());
                    }
                }
            });

            bKembali.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    window.setVisible(false);
                    new MenuAdmin();
                }
            });
        }
    }

    public void initComponents(){
        window.getContentPane().setBackground(new Color(28, 27, 27));

        tTable.setBackground(new Color(247, 252, 255));
        TableColumnModel columnModel = tTable.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(2);
        columnModel.getColumn(1).setPreferredWidth(60);
        columnModel.getColumn(2).setPreferredWidth(80);
        columnModel.getColumn(3).setPreferredWidth(30);
        window.add(scrollPane);
        scrollPane.setBounds(20, 270, 640, 350);
        scrollPane.setBackground(new Color(247, 252, 255));

        window.add(lId);
        lId.setBounds(20, 70, 150, 20);
        lId.setForeground(new Color(255, 255, 255));
        window.add(fId);
        fId.setBounds(170, 70, 170, 20);
        fId.setEditable(false);

        window.add(lNama);
        lNama.setBounds(20, 100, 150, 20);
        lNama.setForeground(new Color(255, 255, 255));
        window.add(fNama);
        fNama.setBounds(170, 100, 170, 20);

        window.add(lHarga);
        lHarga.setBounds(20, 130, 150, 20);
        lHarga.setForeground(new Color(255, 255, 255));
        window.add(fHarga);
        fHarga.setBounds(170, 130, 170, 20);

        window.add(lStok);
        lStok.setBounds(20, 160, 150, 20);
        lStok.setForeground(new Color(255, 255, 255));
        window.add(fStok);
        fStok.setBounds(170, 160, 170, 20);


        window.add(bTambah);
        bTambah.setBounds(20, 230, 120, 30);
        bTambah.setForeground(new Color(255, 255, 255));
        bTambah.setBackground(new Color(58, 133, 86));

        window.add(bUpdate);
        bUpdate.setBounds(190, 230, 120, 30);
        bUpdate.setForeground(new Color(255,255,255));
        bUpdate.setBackground(new Color(82, 77, 64));

        window.add(bHapus);
        bHapus.setBounds(360, 230, 120, 30);
        bHapus.setForeground(new Color(255,255,255));
        bHapus.setBackground(new Color(102, 55, 51));

        window.add(bKembali);
        bKembali.setBounds(540, 230, 120, 30);
        bKembali.setForeground(new Color(255,255,255));
        bKembali.setBackground(new Color(82, 77, 64));
    }

    private void loadData(){
        try{
            statement = koneksi.getConnection().createStatement();
            String sql = "SELECT * FROM produk";
            resultSet = statement.executeQuery(sql);

            int row = 0;
            while (resultSet.next()){
                datas[row][0] = resultSet.getString("id_kopi");
                datas[row][1] = resultSet.getString("nama_kopi");
                datas[row][2] = resultSet.getString("harga");
                datas[row][3] = resultSet.getString("stok");
                row++;
            }
            statement.close();

        } catch (SQLException sqlError) {
            JOptionPane.showMessageDialog(null, "Data Gagal Ditampilkan" + sqlError);
        } catch (ClassNotFoundException classError) {
            JOptionPane.showMessageDialog(null, "Driver tidak ditemukan !!");
        }
    }


}
