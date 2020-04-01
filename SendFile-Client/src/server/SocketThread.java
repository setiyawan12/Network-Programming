/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.StringTokenizer;

/**
 *
 * @author Setiyawan
 */
public class SocketThread implements Runnable {

    Socket socket;
    MainForm main;
    DataInputStream dis;
    StringTokenizer st;
    String client, filesharing_username;

    private final int BUFFER_SIZE = 100;

    public SocketThread(Socket socket, MainForm main) {
        this.main = main;
        this.socket = socket;

        try {
            dis = new DataInputStream(socket.getInputStream());
        } catch (IOException e) {
            main.appendMessage("[SocketThreadIOException]: " + e.getMessage());
        }
    }

    private void createConnection(String receiver, String sender, String filename) {
        try {
            main.appendMessage("[createConnection]: membuat koneksi berbagi file.");
            Socket s = main.getClientList(receiver);
            if (s != null) {
                main.appendMessage("[createConnection]: Socket OK");
                DataOutputStream dosS = new DataOutputStream(s.getOutputStream());
                main.appendMessage("[createConnection]: DataOutputStream OK");
                String format = "CMD_FILE_XD " + sender + " " + receiver + " " + filename;
                dosS.writeUTF(format);
                main.appendMessage("[createConnection]: " + format);
            } else {
                main.appendMessage("[createConnection]: Klien tidak ditemukan '" + receiver + "'");
                DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
                dos.writeUTF("CMD_SENDFILEERROR " + "Client '" + receiver + "' tidak dapat ditemukan dalam daftar, pastikan pengguna online.");
            }
        } catch (IOException e) {
            main.appendMessage("[createConnection]: " + e.getLocalizedMessage());
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                String data = dis.readUTF();
                st = new StringTokenizer(data);
                String CMD = st.nextToken();

                switch (CMD) {
                    case "CMD_JOIN":
                        String clientUsername = st.nextToken();
                        client = clientUsername;
                        main.setClientList(clientUsername);
                        main.setSocketList(socket);
                        main.appendMessage("[Client]: " + clientUsername + " Bergabunglah dengan ruang obrolan!");
                        break;

                    case "CMD_CHAT":
                        String from = st.nextToken();
                        String sendTo = st.nextToken();
                        String msg = "";
                        while (st.hasMoreTokens()) {
                            msg = msg + " " + st.nextToken();
                        }
                        Socket tsoc = main.getClientList(sendTo);
                        try {
                            DataOutputStream dos = new DataOutputStream(tsoc.getOutputStream());
                            String content = from + ": " + msg;
                            dos.writeUTF("CMD_MESSAGE " + content);
                            main.appendMessage("[Message]: Dari" + from + " Untuk " + sendTo + " : " + msg);
                        } catch (IOException e) {
                            main.appendMessage("[IOException]: Tidak dapat mengirim pesan ke " + sendTo);
                        }
                        break;

                    case "CMD_CHATALL":
                        String chatall_from = st.nextToken();
                        String chatall_msg = "";
                        while (st.hasMoreTokens()) {
                            chatall_msg = chatall_msg + " " + st.nextToken();
                        }
                        String chatall_content = chatall_from + " " + chatall_msg;
                        for (int x = 0; x < main.clientList.size(); x++) {
                            if (!main.clientList.elementAt(x).equals(chatall_from)) {
                                try {
                                    Socket tsoc2 = (Socket) main.socketList.elementAt(x);
                                    DataOutputStream dos2 = new DataOutputStream(tsoc2.getOutputStream());
                                    dos2.writeUTF("CMD_MESSAGE " + chatall_content);
                                } catch (IOException e) {
                                    main.appendMessage("[CMD_CHATALL]: " + e.getMessage());
                                }
                            }
                        }
                        main.appendMessage("[CMD_CHATALL]: " + chatall_content);
                        break;

                    case "CMD_SHARINGSOCKET":
                        main.appendMessage("CMD_SHARINGSOCKET : Klien membuat soket untuk koneksi berbagi file ...");
                        String file_sharing_username = st.nextToken();
                        filesharing_username = file_sharing_username;
                        main.setClientFileSharingUsername(file_sharing_username);
                        main.setClientFileSharingSocket(socket);
                        main.appendMessage("CMD_SHARINGSOCKET : Username: " + file_sharing_username);
                        main.appendMessage("CMD_SHARINGSOCKET : Berbagi File terbuka");
                        break;

                    case "CMD_SENDFILE":
                        main.appendMessage("CMD_SENDFILE : Klien mengirim file ...");
                        String file_name = st.nextToken();
                        String filesize = st.nextToken();
                        String sendto = st.nextToken();
                        String consignee = st.nextToken();
                        main.appendMessage("CMD_SENDFILE : Từ: " + consignee);
                        main.appendMessage("CMD_SENDFILE : Đến: " + sendto);
                        main.appendMessage("CMD_SENDFILE : siap untuk koneksi ..");
                        Socket cSock = main.getClientFileSharingSocket(sendto);

                        if (cSock != null) {

                            try {
                                main.appendMessage("CMD_SENDFILE : Sudah terhubung ..!");
                                main.appendMessage("CMD_SENDFILE : mengirim file ke klien ...");
                                DataOutputStream cDos = new DataOutputStream(cSock.getOutputStream());
                                cDos.writeUTF("CMD_SENDFILE " + file_name + " " + filesize + " " + consignee);
                                InputStream input = socket.getInputStream();
                                OutputStream sendFile = cSock.getOutputStream();
                                byte[] buffer = new byte[BUFFER_SIZE];
                                int cnt;
                                while ((cnt = input.read(buffer)) > 0) {
                                    sendFile.write(buffer, 0, cnt);
                                }
                                sendFile.flush();
                                sendFile.close();
                                main.removeClientFileSharing(sendto);
                                main.removeClientFileSharing(consignee);
                                main.appendMessage("CMD_SENDFILE : File telah dikirim ke klien ...");
                            } catch (IOException e) {
                                main.appendMessage("[CMD_SENDFILE]: " + e.getMessage());
                            }
                        } else {
                            main.removeClientFileSharing(consignee);
                            main.appendMessage("CMD_SENDFILE : Client '" + sendto + "' tidak ditemukan.!");
                            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
                            dos.writeUTF("CMD_SENDFILEERROR " + "Client '" + sendto + "' Tidak dapat menemukan, Berbagi File akan keluar.");
                        }
                        break;

                    case "CMD_SENDFILERESPONSE":
                        String receiver = st.nextToken();
                        String rMsg = "";
                        main.appendMessage("[CMD_SENDFILERESPONSE]: username: " + receiver);
                        while (st.hasMoreTokens()) {
                            rMsg = rMsg + " " + st.nextToken();
                        }
                        try {
                            Socket rSock = (Socket) main.getClientFileSharingSocket(receiver);
                            DataOutputStream rDos = new DataOutputStream(rSock.getOutputStream());
                            rDos.writeUTF("CMD_SENDFILERESPONSE" + " " + receiver + " " + rMsg);
                        } catch (IOException e) {
                            main.appendMessage("[CMD_SENDFILERESPONSE]: " + e.getMessage());
                        }
                        break;

                    case "CMD_SEND_FILE_XD":
                        try {
                            String send_sender = st.nextToken();
                            String send_receiver = st.nextToken();
                            String send_filename = st.nextToken();
                            main.appendMessage("[CMD_SEND_FILE_XD]: Host: " + send_sender);
                            this.createConnection(send_receiver, send_sender, send_filename);
                        } catch (Exception e) {
                            main.appendMessage("[CMD_SEND_FILE_XD]: " + e.getLocalizedMessage());
                        }
                        break;

                    case "CMD_SEND_FILE_ERROR":
                        String eReceiver = st.nextToken();
                        String eMsg = "";
                        while (st.hasMoreTokens()) {
                            eMsg = eMsg + " " + st.nextToken();
                        }
                        try {
                            Socket eSock = main.getClientFileSharingSocket(eReceiver);
                            DataOutputStream eDos = new DataOutputStream(eSock.getOutputStream());
                            eDos.writeUTF("CMD_RECEIVE_FILE_ERROR " + eMsg);
                        } catch (IOException e) {
                            main.appendMessage("[CMD_RECEIVE_FILE_ERROR]: " + e.getMessage());
                        }
                        break;

                    case "CMD_SEND_FILE_ACCEPT": 
                        String aReceiver = st.nextToken();
                        String aMsg = "";
                        while (st.hasMoreTokens()) {
                            aMsg = aMsg + " " + st.nextToken();
                        }
                        try {
                            Socket aSock = main.getClientFileSharingSocket(aReceiver);
                            DataOutputStream aDos = new DataOutputStream(aSock.getOutputStream());
                            aDos.writeUTF("CMD_RECEIVE_FILE_ACCEPT " + aMsg);
                        } catch (IOException e) {
                            main.appendMessage("[CMD_RECEIVE_FILE_ERROR]: " + e.getMessage());
                        }
                        break;

                    default:
                        main.appendMessage("[CMDException]: Perintah tidak dikenal " + CMD);
                        break;
                }
            }
        } catch (IOException e) {
            System.out.println(client);
            System.out.println("File Sharing: " + filesharing_username);
            main.removeFromTheList(client);
            if (filesharing_username != null) {
                main.removeClientFileSharing(filesharing_username);
            }
            main.appendMessage("[SocketThread]: Koneksi klien ditutup ..!");
        }
    }

}



