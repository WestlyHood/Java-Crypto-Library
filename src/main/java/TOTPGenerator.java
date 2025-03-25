import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Scanner;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class TOTPGenerator {
    private static final String SECRET_KEY = "MYSECRETKEY"; // Use a securely generated key
    private static final int TIME_STEP = 30; // 30 seconds
    private static final int CODE_DIGITS = 6;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Generating a TOTP code...");
        String generatedTOTP = generateTOTP(SECRET_KEY);
        System.out.println("Your TOTP: " + generatedTOTP);

        while (true) {
            System.out.print("Enter your TOTP: ");
            String userInput = scanner.nextLine();

            if (userInput.equals(generatedTOTP)) {
                System.out.println("✅ Access Granted!");
                break;
            } else {
                System.out.println("❌ Invalid TOTP. Try again.");
            }
        }

        scanner.close();
    }

    private static String generateTOTP(String secret) {
        long time = System.currentTimeMillis() / 1000 / TIME_STEP;
        return generateHMAC(secret, time);
    }

    private static String generateHMAC(String secret, long counter) {
        try {
            byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
            Key key = new SecretKeySpec(keyBytes, "HmacSHA1");
            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(key);

            byte[] counterBytes = new byte[8];
            for (int i = 7; counter > 0; counter >>= 8) {
                counterBytes[i--] = (byte) (counter & 0xFF);
            }

            byte[] hmac = mac.doFinal(counterBytes);
            int offset = hmac[hmac.length - 1] & 0xF;
            int binary =
                    ((hmac[offset] & 0x7F) << 24) |
                    ((hmac[offset + 1] & 0xFF) << 16) |
                    ((hmac[offset + 2] & 0xFF) << 8) |
                    (hmac[offset + 3] & 0xFF);

            int otp = binary % (int) Math.pow(10, CODE_DIGITS);
            return String.format("%06d", otp);
        } catch (Exception e) {
            throw new RuntimeException("Error generating TOTP", e);
        }
    }
}
