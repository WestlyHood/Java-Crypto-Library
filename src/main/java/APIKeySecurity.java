import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Scanner;

public class APIKeySecurity {
    private static final String ALGORITHM = "AES";
    private static final int GCM_TAG_LENGTH = 128;
    private static SecretKey secretKey;
    private static byte[] iv;

    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(System.in);

            // Generate encryption key
            generateKey();

            // Prompt user for API Key input
            System.out.print("Enter your API Key: ");
            String apiKey = scanner.nextLine();

            // Encrypt API Key
            String encryptedKey = encrypt(apiKey);
            System.out.println("🔒 Encrypted API Key: " + encryptedKey);

            // Ask user if they want to decrypt
            System.out.print("Do you want to decrypt the API Key? (yes/no): ");
            String response = scanner.nextLine().trim().toLowerCase();

            if (response.equals("yes")) {
                String decryptedKey = decrypt(encryptedKey);
                System.out.println("🔓 Decrypted API Key: " + decryptedKey);
            } else {
                System.out.println("Encryption process completed! ✅");
            }

            scanner.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void generateKey() throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHM);
        keyGenerator.init(256);
        secretKey = keyGenerator.generateKey();
        iv = new byte[12]; // GCM standard IV size
        new SecureRandom().nextBytes(iv);
    }

    private static String encrypt(String data) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, new GCMParameterSpec(GCM_TAG_LENGTH, iv));
        byte[] encryptedBytes = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));

        // Encode IV and encrypted data in Base64
        return Base64.getEncoder().encodeToString(iv) + ":" + Base64.getEncoder().encodeToString(encryptedBytes);
    }

    private static String decrypt(String encryptedData) throws Exception {
        String[] parts = encryptedData.split(":");
        byte[] ivDecoded = Base64.getDecoder().decode(parts[0]);
        byte[] encryptedBytes = Base64.getDecoder().decode(parts[1]);

        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        cipher.init(Cipher.DECRYPT_MODE, secretKey, new GCMParameterSpec(GCM_TAG_LENGTH, ivDecoded));
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);

        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }
}
