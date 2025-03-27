import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.util.Scanner;

public class SecureKeyStorage {
    private static final String ALGORITHM = "AES";

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter a password to encrypt the key: ");
        String password = scanner.nextLine();

        // Generate a new key
        SecretKey key = generateKey();
        String encryptedKey = encryptKey(key, password);
        
        // Store the key
        saveKeyToFile("secureKey.txt", encryptedKey);
        System.out.println("Key securely stored.");

        // Loop until the correct password is entered
        while (true) {
            System.out.print("Enter password to decrypt the key: ");
            String enteredPassword = scanner.nextLine();
            String decryptedKey = decryptKey(loadKeyFromFile("secureKey.txt"), enteredPassword);
            
            if (decryptedKey != null) {
                System.out.println("✅ Correct Password! Decrypted Key: " + decryptedKey);
                break; // Exit the loop if the password is correct
            } else {
                System.out.println("❌ Incorrect password. Try again.");
            }
        }
        scanner.close();
    }

    private static SecretKey generateKey() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] keyBytes = new byte[16]; // AES-128
        secureRandom.nextBytes(keyBytes);
        return new SecretKeySpec(keyBytes, ALGORITHM);
    }

    private static SecretKey getKeyFromPassword(String password) throws Exception {
        MessageDigest sha = MessageDigest.getInstance("SHA-256");
        byte[] keyBytes = sha.digest(password.getBytes(StandardCharsets.UTF_8));
        return new SecretKeySpec(keyBytes, 0, 16, ALGORITHM); // Use first 16 bytes
    }

    private static String encryptKey(SecretKey key, String password) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, getKeyFromPassword(password));
        byte[] encryptedKey = cipher.doFinal(key.getEncoded());
        return Base64.getEncoder().encodeToString(encryptedKey);
    }

    private static String decryptKey(String encryptedKey, String password) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, getKeyFromPassword(password));
            byte[] decryptedKey = cipher.doFinal(Base64.getDecoder().decode(encryptedKey));
            return Base64.getEncoder().encodeToString(decryptedKey);
        } catch (Exception e) {
            return null; // Return null if the password is incorrect
        }
    }

    private static void saveKeyToFile(String filename, String key) throws IOException {
        try (FileWriter writer = new FileWriter(filename)) {
            writer.write(key);
        }
    }

    private static String loadKeyFromFile(String filename) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            return reader.readLine();
        }
    }
}
