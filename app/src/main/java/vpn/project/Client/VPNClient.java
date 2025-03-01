package vpn.project.Client;

import vpn.project.EncryptionUtil;

import javax.crypto.SecretKey;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class VPNClient {
    private static final String SERVER_ADDRESS = "127.0.0.1";
    private static final int PORT = 8080;

    public static void main(String[] args) {
        try {
            // 🔴 Copy the key from the server output and paste it here
            String base64Key = "PASTE_SERVER_KEY_HERE";
            SecretKey secretKey = EncryptionUtil.keyFromBase64(base64Key);

            Socket socket = new Socket(SERVER_ADDRESS, PORT);
            System.out.println("✅ Connected to VPN Server");

            handleServerMessages(socket, secretKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void handleServerMessages(Socket socket, SecretKey secretKey) {
        try (
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in))
        ) {
            while (true) {
                System.out.print("💬 Enter message: ");
                String userMessage = userInput.readLine();

                if (userMessage == null || userMessage.equalsIgnoreCase("exit")) {
                    System.out.println("🚪 Disconnecting...");
                    break;
                }

                String encryptedMessage = EncryptionUtil.encryptToBase64(userMessage, secretKey);
                out.println(encryptedMessage);

                String response = in.readLine();
                String decryptedResponse = EncryptionUtil.decryptFromBase64(response, secretKey);
                System.out.println("🔄 Server response: " + decryptedResponse);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
