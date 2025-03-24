import java.security.SecureRandom;
import java.util.Scanner;

public class SecureRNG {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        SecureRandom secureRandom = new SecureRandom();

        System.out.println("Enter the number of random bytes you want (e.g., 16 for 128-bit security): ");
        int numBytes = scanner.nextInt();

        byte[] randomBytes = new byte[numBytes];
        secureRandom.nextBytes(randomBytes);

        System.out.println("Generated Secure Random Bytes: ");
        for (byte b : randomBytes) {
            System.out.printf("%02x ", b); // Display as hexadecimal
        }
        
        scanner.close();
    }
}
