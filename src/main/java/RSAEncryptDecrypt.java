import java.security.*;
import javax.crypto.Cipher;
import java.util.Base64;
import java.util.Scanner;

public class RSAEncryptDecrypt {
    private static PrivateKey privateKey;
    private static PublicKey publicKey;

    public static void main(String[] args) {
        try {
            // Generate RSA key pair
            KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
            keyPairGen.initialize(2048);
            KeyPair keyPair = keyPairGen.generateKeyPair();
            publicKey = keyPair.getPublic();
            privateKey = keyPair.getPrivate();

            Scanner scanner = new Scanner(System.in);
            System.out.println("🔐 RSA Encryption & Decryption System");

            while (true) {
                System.out.print("\nEnter a message to encrypt (or type 'exit' to quit): ");
                String message = scanner.nextLine();
                if (message.equalsIgnoreCase("exit")) break;

                // Encrypt the message
                String encryptedText = encrypt(message, publicKey);
                System.out.println("🔹 Encrypted Message: " + encryptedText);

                // Decrypt the message
                String decryptedText = decrypt(encryptedText, privateKey);
                System.out.println("✅ Decrypted Message: " + decryptedText);
            }
            scanner.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Encrypt method
    public static String encrypt(String message, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] encryptedBytes = cipher.doFinal(message.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    // Decrypt method
    public static String decrypt(String encryptedMessage, PrivateKey privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedMessage));
        return new String(decryptedBytes);
    }
}
