import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Scanner;

public class HMACGenerator {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("Enter a message: ");
            String message = scanner.nextLine();

            System.out.print("Enter a secret key: ");
            String secretKey = scanner.nextLine();

            String hmac = generateHMAC(message, secretKey);
            System.out.println("Generated HMAC: " + hmac);

            // Ask if the user wants to generate another HMAC
            System.out.print("Do you want to generate another HMAC? (yes/no): ");
            String choice = scanner.nextLine().toLowerCase();
            if (!choice.equals("yes")) {
                System.out.println("Exiting HMAC Generator. Stay secure! 🔐");
                break;
            }
        }

        scanner.close();
    }

    public static String generateHMAC(String message, String secretKey) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(), "HmacSHA256");
            mac.init(secretKeySpec);
            byte[] hmacBytes = mac.doFinal(message.getBytes());
            return Base64.getEncoder().encodeToString(hmacBytes);
        } catch (Exception e) {
            throw new RuntimeException("Error generating HMAC", e);
        }
    }
}
