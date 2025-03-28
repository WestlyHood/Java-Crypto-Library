import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;

public class TransactionIntegrityChecker {

    // Method to generate a SHA-256 hash of a transaction
    public static String generateTransactionHash(String transactionData) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(transactionData.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error: SHA-256 Algorithm not found", e);
        }
    }

    // Method to verify if a transaction has been tampered with
    public static boolean verifyTransaction(String originalData, String storedHash) {
        String newHash = generateTransactionHash(originalData);
        return newHash.equals(storedHash);
    }

    // Convert byte array to hexadecimal string
    private static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            hexString.append(String.format("%02x", b));
        }
        return hexString.toString();
    }

    public static void main(String[] args) {
        // Simulated transaction data
        String transactionData = "UserA sends 1.5 ETH to UserB at 12:30 PM";

        // Generate hash for the transaction
        String transactionHash = generateTransactionHash(transactionData);
        System.out.println("Original Transaction Hash: " + transactionHash);

        // Verify transaction integrity
        boolean isValid = verifyTransaction(transactionData, transactionHash);
        System.out.println("Is the transaction valid? " + (isValid ? "Yes ✅" : "No ❌"));

        // Simulate tampering
        String tamperedData = "UserA sends 2.0 ETH to UserB at 12:30 PM"; // Amount changed
        boolean isTampered = verifyTransaction(tamperedData, transactionHash);
        System.out.println("Is the transaction valid after tampering? " + (isTampered ? "Yes ✅" : "No ❌"));
    }
}
