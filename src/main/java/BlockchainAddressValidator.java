import java.util.Scanner;
import java.util.regex.Pattern;

public class BlockchainAddressValidator {
    private static final Pattern BITCOIN_PATTERN = Pattern.compile("^[13][a-km-zA-HJ-NP-Z1-9]{25,34}$");
    private static final Pattern ETHEREUM_PATTERN = Pattern.compile("^0x[a-fA-F0-9]{40}$");
    private static final Pattern BNB_PATTERN = Pattern.compile("^bnb[a-z0-9]{39}$");
    private static final Pattern SOLANA_PATTERN = Pattern.compile("^[1-9A-HJ-NP-Za-km-z]{32,44}$");

    public static String detectBlockchain(String address) {
        if (BITCOIN_PATTERN.matcher(address).matches()) return "Bitcoin";
        if (ETHEREUM_PATTERN.matcher(address).matches()) return "Ethereum";
        if (BNB_PATTERN.matcher(address).matches()) return "BNB Chain";
        if (SOLANA_PATTERN.matcher(address).matches()) return "Solana";
        return "INVALID ADDRESS";
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("Enter a blockchain address: ");
            String address = scanner.nextLine();
            String result = detectBlockchain(address);

            if (!result.equals("INVALID ADDRESS")) {
                System.out.println("Detected Blockchain: " + result);
            } else {
                System.out.println("Invalid address! Please enter a valid blockchain address.");
            }

            System.out.print("Do you want to check another address? (yes/no): ");
            String response = scanner.nextLine().trim().toLowerCase();
            if (response.equals("no")) {
                System.out.println("Exiting Address Validator...");
                break;
            }
        }
        scanner.close();
    }
}
