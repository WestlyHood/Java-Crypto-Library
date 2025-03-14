import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHA256Hash {

    /**
     * Generates a SHA-256 hash of the given input string.
     *
     * @param input The string to be hashed
     * @return The hashed output in hexadecimal format
     */
    public static String generateSHA256(String input) {
        try {
            // Get an instance of SHA-256 MessageDigest
            MessageDigest sha256Digest = MessageDigest.getInstance("SHA-256");

            // Compute the hash and get the byte array
            byte[] hashedBytes = sha256Digest.digest(input.getBytes());

            // Convert the byte array into a readable hex format
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashedBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0'); // Ensure 2-digit format
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error: Algorithm not found!", e);
        }
    }

    public static void main(String[] args) {
        // Example input for hashing
        String textToHash = "GM CT";
        String hashedResult = generateSHA256(textToHash);

        // Print the original text and its SHA-256 hash
        System.out.println("Original Text: " + textToHash);
        System.out.println("SHA-256 Hash: " + hashedResult);
    }
}
