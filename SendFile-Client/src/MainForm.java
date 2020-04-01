
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.HeadlessException;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.Iterator;
import java.util.Vector;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Setiyawan
 */
public class MainForm extends javax.swing.JFrame {

    String username;
    String host;
    int port;
    Socket socket;
    DataOutputStream dos;
    public boolean attachmentOpen = false;
    private boolean isConnected = false;
    private String mydownloadfolder = "D:\\";

    /**
     * Creates new form MainForm
     */
    public MainForm() {
        initComponents();
    }

    public void initFrame(String username, String host, int port) {
        this.username = username;
        this.host = host;
        this.port = port;
        setTitle("Anda saat ini masuk sebagai: " + username);
        connect();
    }

    public void connect() {
        appendMessage(" Menghubungkan...", "Status", Color.GREEN, Color.GREEN);
        try {
            socket = new Socket(host, port);
            dos = new DataOutputStream(socket.getOutputStream());
            dos.writeUTF("CMD_JOIN " + username);
            appendMessage(" Sudah terhubung", "Status", Color.GREEN, Color.GREEN);
            appendMessage("Kirim pesan sekarang.", "Status", Color.GREEN, Color.GREEN);
            new Thread(new ClientThread(socket, this)).start();
            jButton1.setEnabled(true);
            isConnected = true;

        } catch (IOException e) {
            isConnected = false;
            JOptionPane.showMessageDialog(this, "Tidak dapat terhubung ke server, silakan coba lagi nanti.!", "Koneksi gagal", JOptionPane.ERROR_MESSAGE);
            appendMessage("[IOException]: " + e.getMessage(), "Kesalahan", Color.RED, Color.RED);
        }
    }

    public boolean isConnected() {
        return this.isConnected;
    }

    public void appendMessage(String msg, String header, Color headerColor, Color contentColor) {
        jTextPane1.setEditable(true);
        getMsgHeader(header, headerColor);
        getMsgContent(msg, contentColor);
        jTextPane1.setEditable(false);
    }

    public void appendMyMessage(String msg, String header) {
        jTextPane1.setEditable(true);
        getMsgHeader(header, Color.ORANGE);
        getMsgContent(msg, Color.LIGHT_GRAY);
        jTextPane1.setEditable(false);
    }
    public void getMsgHeader(String header, Color color) {
        int len = jTextPane1.getDocument().getLength();
        jTextPane1.setCaretPosition(len);
        jTextPane1.setCharacterAttributes(MessageStyle.styleMessageContent(color, "Impact", 13), false);
        jTextPane1.replaceSelection(header + ":");
    }

    public void getMsgContent(String msg, Color color) {
        int len = jTextPane1.getDocument().getLength();
        jTextPane1.setCaretPosition(len);
        jTextPane1.setCharacterAttributes(MessageStyle.styleMessageContent(color, "Arial", 12), false);
        jTextPane1.replaceSelection(msg + "\n\n");
    }

    public void appendOnlineList(Vector list) {
        sampleOnlineList(list);
    }

    public void showOnLineList(Vector list) {
        try {
            txtpane2.setEditable(true);
            txtpane2.setContentType("text/html");
            StringBuilder sb = new StringBuilder();
            Iterator it = list.iterator();
            sb.append("<html><table>");
            while (it.hasNext()) {
                Object e = it.next();
                URL url = getImageFile();
                Icon icon = new ImageIcon(this.getClass().getResource("/images/online.png"));
                sb.append("<tr><td><b>></b></td><td>").append(e).append("</td></tr>");
                System.out.println("Online: " + e);
            }
            sb.append("</table></body></html>");
            txtpane2.removeAll();
            txtpane2.setText(sb.toString());
            txtpane2.setEditable(false);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    private void sampleOnlineList(Vector list) {
        txtpane2.setEditable(true);
        txtpane2.removeAll();
        txtpane2.setText("");
        Iterator i = list.iterator();
        while (i.hasNext()) {
            Object e = i.next();
            JPanel panel = new JPanel();
            panel.setLayout(new FlowLayout(FlowLayout.LEFT));
            panel.setBackground(Color.white);

            Icon icon = new ImageIcon(this.getClass().getResource("/images/online.png"));
            JLabel label = new JLabel(icon);
            label.setText(" " + e);
            panel.add(label);
            int len = txtpane2.getDocument().getLength();
            txtpane2.setCaretPosition(len);
            txtpane2.insertComponent(panel);
            sampleAppend();
        }
        txtpane2.setEditable(false);
    }

    private void sampleAppend() {
        int len = txtpane2.getDocument().getLength();
        txtpane2.setCaretPosition(len);
        txtpane2.replaceSelection("\n");
    }

    public URL getImageFile() {
        URL url = this.getClass().getResource("/images/online.png");
        return url;
    }
    public void setMyTitle(String s) {
        setTitle(s);
    }

    public String getMyDownloadFolder() {
        return this.mydownloadfolder;
    }
    public String getMyHost() {
        return this.host;
    }

    public int getMyPort() {
        return this.port;
    }

    public String getMyUsername() {
        return this.username;
    }
    public void updateAttachment(boolean b) {
        this.attachmentOpen = b;
    }
    public void openFolder() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int open = chooser.showDialog(this, "Buka Folder");
        if (open == chooser.APPROVE_OPTION) {
            mydownloadfolder = chooser.getSelectedFile().toString() + "\\";
        } else {
            mydownloadfolder = "D:\\";
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        txtpane2 = new javax.swing.JTextPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextPane1 = new javax.swing.JTextPane();
        jTextField1 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu3 = new javax.swing.JMenu();
        sendFileMenu = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        LogoutMenu = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(204, 255, 255));

        jPanel1.setBackground(new java.awt.Color(204, 255, 255));

        txtpane2.setFont(new java.awt.Font("Tahoma", 1, 9)); // NOI18N
        txtpane2.setForeground(new java.awt.Color(120, 14, 3));
        txtpane2.setAutoscrolls(false);
        txtpane2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jScrollPane3.setViewportView(txtpane2);

        jTextPane1.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        jScrollPane1.setViewportView(jTextPane1);

        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });

        jButton1.setBackground(new java.awt.Color(255, 255, 255));
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/send.png"))); // NOI18N
        jButton1.setBorderPainted(false);
        jButton1.setEnabled(false);
        jButton1.setOpaque(false);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Group 2.png"))); // NOI18N

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/welcome.png"))); // NOI18N

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/daftar user.png"))); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(26, Short.MAX_VALUE)
                .addComponent(jLabel2)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 379, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(56, 56, 56)
                                .addComponent(jLabel4)))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(40, 40, 40)
                                .addComponent(jLabel1))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 298, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(41, 41, 41)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane3))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 218, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton1)
                    .addComponent(jTextField1))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 351, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jMenu3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/transfer.png"))); // NOI18N
        jMenu3.setText("Berbagi File");
        jMenu3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenu3ActionPerformed(evt);
            }
        });

        sendFileMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/1.png"))); // NOI18N
        sendFileMenu.setText("Kirim File");
        sendFileMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sendFileMenuActionPerformed(evt);
            }
        });
        jMenu3.add(sendFileMenu);

        jMenuItem3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/undo.png"))); // NOI18N
        jMenuItem3.setText("Unduh itu");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem3);

        jMenuBar1.add(jMenu3);

        jMenu2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/user.png"))); // NOI18N
        jMenu2.setText("Akun");

        LogoutMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/loggoff_1.png"))); // NOI18N
        LogoutMenu.setText("Logout");
        LogoutMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LogoutMenuActionPerformed(evt);
            }
        });
        jMenu2.add(LogoutMenu);

        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void sendFileMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sendFileMenuActionPerformed
        // TODO add your handling code here:
        if (!attachmentOpen) {
            SendFile s = new SendFile();
            if (s.prepare(username, host, port, this)) {
                s.setLocationRelativeTo(null);
                s.setVisible(true);
                attachmentOpen = true;
            } else {
                JOptionPane.showMessageDialog(this, "Tidak dapat mengatur Berbagi File saat ini, silakan coba lagi nanti.!", "Kesalahan", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_sendFileMenuActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        // TODO add your handling code here:
        try {
            JFileChooser chooser = new JFileChooser();
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int browse = chooser.showOpenDialog(this);
            if (browse == chooser.APPROVE_OPTION) {
                this.mydownloadfolder = chooser.getSelectedFile().toString() + "\\";
            }
        } catch (HeadlessException e) {
        }
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jMenu3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenu3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenu3ActionPerformed

    private void LogoutMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LogoutMenuActionPerformed
        // TODO add your handling code here:
        int confirm = JOptionPane.showConfirmDialog(null, "Apakah Anda yakin sudah keluar?");
        if (confirm == 0) {
            try {
                socket.close();
                setVisible(false);
                /**
                 * Login Form *
                 */
                new LoginForm().setVisible(true);
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }//GEN-LAST:event_LogoutMenuActionPerformed

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
        try {
            String content = username + " " + evt.getActionCommand();
            dos.writeUTF("CMD_CHATALL " + content);
            appendMyMessage(" " + evt.getActionCommand(), username);
            jTextField1.setText("");
        } catch (IOException e) {
            appendMessage(" Tidak dapat mengirim pesan sekarang, tidak dapat terhubung ke Server saat ini, silakan coba lagi nanti atau mulai ulang aplikasi ini.!", "Kesalahan", Color.RED, Color.RED);
        }
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        try {
            String content = username + " " + jTextField1.getText();
            dos.writeUTF("CMD_CHATALL " + content);
            appendMyMessage(" " + jTextField1.getText(), username);
            jTextField1.setText("");
        } catch (IOException e) {
            appendMessage(" Tidak dapat mengirim pesan sekarang, tidak dapat terhubung ke Server saat ini, silakan coba lagi nanti atau mulai ulang aplikasi ini.!", "Kesalahan", Color.RED, Color.RED);
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainForm().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem LogoutMenu;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextPane jTextPane1;
    private javax.swing.JMenuItem sendFileMenu;
    private javax.swing.JTextPane txtpane2;
    // End of variables declaration//GEN-END:variables
}
