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
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class KelolaUser {
    private static final SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Timestamp timestamp = new Timestamp(System.currentTimeMillis());

    JFrame window 		= new JFrame("Kelola User");
    DataUser role;
    Koneksi koneksi = new Koneksi();
    List<DataUser> roles=new ArrayList<>();
    ResultSet resultSet;
    Statement statement;


    String[][] datas        = new String[500][7];
    String[] kolom          = {"ID User","Nama User", "Email", "Sandi", "Role","Dibuat","Diubah"};
    JTable tTable           = new JTable(datas, kolom);
    JScrollPane scrollPane  = new JScrollPane(tTable);

    JLabel lId      = new JLabel("ID User");
    JLabel lNama    = new JLabel("Nama");
    JLabel lEmail   = new JLabel("Email");
    JLabel lSandi   = new JLabel("Sandi");
    JLabel lRole    = new JLabel("Role");

    JTextField fId      = new JTextField();
    JTextField fNama    = new JTextField();
    JTextField fEmail   = new JTextField();
    JTextField fSandi   = new JTextField();
    JComboBox<String> cRole     = new JComboBox<>();

    JButton bTambah     = new JButton("Tambah");
    JButton bUpdate 	= new JButton("Ubah");
    JButton bHapus   	= new JButton("Hapus");
    JButton bKembali 	= new JButton("Kembali");


    public KelolaUser(){
        if(UserSession.getId_user() == null){
            JOptionPane.showMessageDialog(null, "Silahkan login terlebih dahulu!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            window.setVisible(false);
            new Login();
        }else if(UserSession.getRole() != 1){
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
                        String email = tTable.getValueAt(baris, 2).toString();
                        fEmail.setText(email);
                        String sandi = tTable.getValueAt(baris, 3).toString();
                        role.setSandi(sandi);
                        String role = tTable.getValueAt(baris, 4).toString();
                        cRole.setSelectedItem(role);

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
                        String MD5 = DataUser.getMd5(fSandi.getText());
                        statement = koneksi.getConnection().createStatement();
                        String sql = "INSERT INTO user VALUES(default,'" + fNama.getText() + "','" + fEmail.getText() + "','" + MD5 + "','" + roles.get(cRole.getSelectedIndex()).getIdRole() + "','" + time.format(timestamp) + "','" + time.format(timestamp) + "' )";
                        int disimpan = statement.executeUpdate(sql);
                        if (disimpan == 1) {
                            JOptionPane.showMessageDialog(null, "Selamat anda berhasil mendaftar!", "Peringatan", JOptionPane.WARNING_MESSAGE);
                            statement.close();
                            window.setVisible(false);
                            new KelolaUser();
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
                        String MD5 = DataUser.getMd5(fSandi.getText());
                        statement = koneksi.getConnection().createStatement();
                        if (fSandi.getText().isEmpty()) {
                            String sql = "UPDATE user set nama='" + fNama.getText() + "',email='" + fEmail.getText() + "',role='" + roles.get(cRole.getSelectedIndex()).getIdRole() + "',diubah='" + time.format(timestamp) + "' WHERE id='" + fId.getText() + "'";
                            int disimpan = statement.executeUpdate(sql);
                            if (disimpan == 1) {
                                JOptionPane.showMessageDialog(null, "Berhasil Diubah!", "Peringatan", JOptionPane.WARNING_MESSAGE);
                                statement.close();
                                window.setVisible(false);
                                new KelolaUser();
                            }
                        } else {
                            String sql = "UPDATE user set nama='" + fNama.getText() + "',email='" + fEmail.getText() + "',sandi='" + MD5 + "',role='" + roles.get(cRole.getSelectedIndex()).getIdRole() + "',diubah='" + time.format(timestamp) + "' WHERE id='" + fId.getText() + "'";
                            int disimpan = statement.executeUpdate(sql);
                            if (disimpan == 1) {
                                JOptionPane.showMessageDialog(null, "Berhasil Diubah!", "Peringatan", JOptionPane.WARNING_MESSAGE);
                                statement.close();
                                window.setVisible(false);
                                new KelolaUser();
                            }
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
                        new KelolaUser();
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
        columnModel.getColumn(4).setPreferredWidth(70);
        columnModel.getColumn(5).setPreferredWidth(70);
        columnModel.getColumn(6).setPreferredWidth(70);
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

        window.add(lEmail);
        lEmail.setBounds(20, 130, 150, 20);
        lEmail.setForeground(new Color(255, 255, 255));
            window.add(fEmail);
            fEmail.setBounds(170, 130, 170, 20);

        window.add(lSandi);
        lSandi.setBounds(20, 160, 150, 20);
        lSandi.setForeground(new Color(255, 255, 255));
            window.add(fSandi);
            fSandi.setBounds(170, 160, 170, 20);

        window.add(lRole);
        lRole.setBounds(20, 190, 150, 20);
        lRole.setForeground(new Color(255, 255, 255));
            window.add(cRole);
            cRole.setBounds(170, 190, 170, 20);

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
        cRole.removeAllItems();
        List<DataUser> roles = getAllRole();
        for (DataUser user : roles) {
            cRole.addItem(user.getNamaRole().toString());
        }

        try{
            statement = koneksi.getConnection().createStatement();
            String sql = "SELECT * FROM user a INNER JOIN role b ON a.role=b.id_role";
            resultSet = statement.executeQuery(sql);

            int row = 0;
            while (resultSet.next()){
                datas[row][0] = resultSet.getString("id");
                datas[row][1] = resultSet.getString("nama");
                datas[row][2] = resultSet.getString("email");
                datas[row][3] = resultSet.getString("sandi");
                datas[row][4] = resultSet.getString("nama_role");
                datas[row][5] = resultSet.getString("dibuat");
                datas[row][6] = resultSet.getString("diubah");
                row++;
            }
            statement.close();

        } catch (SQLException sqlError) {
            JOptionPane.showMessageDialog(null, "Data Gagal Ditampilkan" + sqlError);
        } catch (ClassNotFoundException classError) {
            JOptionPane.showMessageDialog(null, "Driver tidak ditemukan !!");
        }
    }

    public List<DataUser> getAllRole() {
        try {
            statement = koneksi.getConnection().createStatement();
            String sql = "SELECT * FROM role";
            resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                role = new DataUser();
                role.setIdRole(resultSet.getInt("id_role"));
                role.setNamaRole(resultSet.getString("nama_role"));
                roles.add(role);
            }
        } catch (SQLException sqlError) {
            JOptionPane.showMessageDialog(null, "Data Gagal Ditampilkan" + sqlError);
        } catch (ClassNotFoundException classError) {
            JOptionPane.showMessageDialog(null, "Driver tidak ditemukan !!");
        }
        return roles;
    }


}
