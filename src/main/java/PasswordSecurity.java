import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import java.util.Scanner;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.SecureRandom;

public class PasswordSecurity {
    private static final int ITERATIONS = 65536;
    private static final int KEY_LENGTH = 256;
    
    public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeySpecException {
        Scanner scanner = new Scanner(System.in);

        // Prompt user to enter a password to hash
        System.out.print("Enter password to hash: ");
        String password = scanner.nextLine();
        
        // Generate salt and hash the password
        byte[] salt = generateSalt();
        String hashedPassword = hashPassword(password, salt);
        
        System.out.println("\n🔒 Hashed Password: " + hashedPassword);
        System.out.println("🧂 Salt: " + Base64.getEncoder().encodeToString(salt) + "\n");

        // Loop for verification until the correct password is entered
        while (true) {
            System.out.print("Enter password to verify: ");
            String inputPassword = scanner.nextLine();
            
            // Verify password
            if (verifyPassword(inputPassword, hashedPassword, salt)) {
                System.out.println("✅ Password verified successfully!");
                break; // Exit loop when correct password is entered
            } else {
                System.out.println("❌ Incorrect password! Try again.\n");
            }
        }
        scanner.close();
    }

    // Function to generate a random salt
    private static byte[] generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return salt;
    }

    // Function to hash the password using PBKDF2
    private static String hashPassword(String password, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, ITERATIONS, KEY_LENGTH);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        byte[] hash = factory.generateSecret(spec).getEncoded();
        return Base64.getEncoder().encodeToString(hash);
    }

    // Function to verify input password with stored hash
    private static boolean verifyPassword(String inputPassword, String storedHash, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
        String newHash = hashPassword(inputPassword, salt);
        return storedHash.equals(newHash);
    }
}
