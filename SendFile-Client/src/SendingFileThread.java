import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.text.DecimalFormat;
import javax.swing.JOptionPane;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Setiyawan
 */
public class SendingFileThread implements Runnable {
    protected Socket socket;
    private DataOutputStream dos;
    protected SendFile form;
    protected String file;
    protected String receiver;
    protected String sender;
    protected DecimalFormat df = new DecimalFormat("##,#00");
    private final int BUFFER_SIZE = 100;
    public SendingFileThread(Socket soc, String file, String receiver, String sender, SendFile frm){
        this.socket = soc;
        this.file = file;
        this.receiver = receiver;
        this.sender = sender;
        this.form = frm;
    }

    @Override
    public void run() {
        try {
            form.disableGUI(true);
            System.out.println("kirim file..!");
            dos = new DataOutputStream(socket.getOutputStream());
            File filename = new File(file);
            int len = (int) filename.length();
            int filesize = (int)Math.ceil(len / BUFFER_SIZE);
            String clean_filename = filename.getName();
            dos.writeUTF("CMD_SENDFILE "+ clean_filename.replace(" ", "_") +" "+ filesize +" "+ receiver +" "+ sender);
            System.out.println("Dari: "+ sender);
            System.out.println("Kepada: "+ receiver);
            InputStream input = new FileInputStream(filename);
            OutputStream output = socket.getOutputStream();
            BufferedInputStream bis = new BufferedInputStream(input);
            byte[] buffer = new byte[BUFFER_SIZE];
            int count, percent = 0;
            while((count = bis.read(buffer)) > 0){
                percent = percent + count;
                int p = (percent / filesize);
                form.updateProgress(p);
                output.write(buffer, 0, count);
            }
            form.setMyTitle("File telah dikirim.!");
            form.updateAttachment(false);
            JOptionPane.showMessageDialog(form, "File berhasil dikirim.!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
            form.closeThis();
            output.flush();
            output.close();
            System.out.println("File telah dikirim ..!");
        } catch (IOException e) {
            form.updateAttachment(false);
            System.out.println("[SendFile]: "+ e.getMessage());
        }
    }
}



