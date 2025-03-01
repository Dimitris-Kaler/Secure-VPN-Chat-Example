package vpn.project;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class EncryptionUtil {
    private static final String ALGORITHM = "AES";

    // Generate AES Key
    public static SecretKey generateKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGen = KeyGenerator.getInstance(ALGORITHM);
        keyGen.init(128);
        return keyGen.generateKey();
    }

    // Convert SecretKey to Base64 String
    public static String keyToBase64(SecretKey key) {
        return Base64.getEncoder().withoutPadding().encodeToString(key.getEncoded()); // ✅ FIX: Standard Base64
    }

    // Convert Base64 String back to SecretKey
    public static SecretKey keyFromBase64(String base64Key) {
        byte[] decodedKey = Base64.getDecoder().decode(base64Key.trim()); // ✅ FIX: Trim spaces/newlines
        return new SecretKeySpec(decodedKey, ALGORITHM);
    }

    // Encrypt data and return Base64-encoded string
    public static String encryptToBase64(String data, SecretKey key) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encryptedBytes = cipher.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    // Decrypt a Base64-encoded string
    public static String decryptFromBase64(String encryptedData, SecretKey key) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decodedBytes = Base64.getDecoder().decode(encryptedData.trim()); // ✅ FIX: Trim to remove unwanted characters
        return new String(cipher.doFinal(decodedBytes));
    }
}
