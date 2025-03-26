import java.security.MessageDigest;
import java.time.Instant;
import java.util.Scanner;

public class BlockchainTimestamp {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter data to timestamp: ");
        String data = scanner.nextLine();

        String hash = generateSHA256Hash(data);
        String timestamp = Instant.now().toString();

        System.out.println("\n🔹 Timestamped Data 🔹");
        System.out.println("Data Hash: " + hash);
        System.out.println("Timestamp: " + timestamp);

        // In real implementation, store this on a blockchain
        scanner.close();
    }

    public static String generateSHA256Hash(String data) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(data.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        } catch (Exception e) {
            throw new RuntimeException("Error generating hash", e);
        }
    }
}
