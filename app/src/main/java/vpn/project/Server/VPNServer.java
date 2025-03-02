package vpn.project.Server;

import vpn.project.EncryptionUtil;

import javax.crypto.SecretKey;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class VPNServer {
    private static final int PORT = 8080;

    public static void main(String[] args) {
        try {
            // Generate AES key
            SecretKey secretKey = EncryptionUtil.generateKey();
//            String base64Key = EncryptionUtil.keyToBase64(secretKey);


            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("✅ VPN Server is running on port: " + PORT);

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("🔗 Client connected!");
                handleClient(socket, secretKey);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void handleClient(Socket socket, SecretKey secretKey) {
        try (
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true)
        ) {
            String encodedKey = Base64.getEncoder().encodeToString(secretKey.getEncoded());
            out.println(encodedKey);
//            System.out.println("🔑 Share this key with the client: " + base64Key);
            String message;
            while ((message = in.readLine()) != null) {
                String decryptedMessage = EncryptionUtil.decryptFromBase64(message, secretKey);
                System.out.println("📩 Received from Client: " + decryptedMessage);

                String encryptedMessage = EncryptionUtil.encryptToBase64("Echo: " + decryptedMessage, secretKey);
                out.println(encryptedMessage);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
