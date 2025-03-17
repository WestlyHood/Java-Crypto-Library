import org.bouncycastle.crypto.digests.SHA256Digest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

public class MnemonicGenerator {
    private static List<String> WORD_LIST = new ArrayList<>();

    static {
        try {
            loadWordList();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load BIP-39 word list", e);
        }
    }

    private static void loadWordList() throws IOException {
        InputStream inputStream = MnemonicGenerator.class.getClassLoader().getResourceAsStream("bip39-wordlist.txt");
        if (inputStream == null) {
            throw new IOException("BIP-39 word list file not found!");
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String word;
            while ((word = reader.readLine()) != null) {
                WORD_LIST.add(word.trim());
            }
        }
    }

    public static String generateMnemonic() {
        SecureRandom random = new SecureRandom();
        byte[] entropy = new byte[16]; // 16 bytes = 128 bits = 12 words
        random.nextBytes(entropy);

        byte[] hash = sha256(entropy);
        int checksumBits = entropy.length * 8 / 32;
        int totalBits = entropy.length * 8 + checksumBits;

        List<String> words = new ArrayList<>();
        int bitIndex = 0;
        for (int i = 0; i < totalBits / 11; i++) {
            int index = getNext11Bits(entropy, hash, bitIndex);
            words.add(WORD_LIST.get(index));
            bitIndex += 11;
        }

        return String.join(" ", words);
    }

    private static int getNext11Bits(byte[] entropy, byte[] hash, int bitIndex) {
        int byteIndex = bitIndex / 8;
        int bitOffset = bitIndex % 8;
        int value = ((entropy[byteIndex] & 0xFF) << 16);

        if (byteIndex + 1 < entropy.length) {
            value |= ((entropy[byteIndex + 1] & 0xFF) << 8);
        }
        if (byteIndex + 2 < entropy.length) {
            value |= (entropy[byteIndex + 2] & 0xFF);
        } else {
            value |= (hash[0] & 0xFF);
        }

        value = (value >> (16 - bitOffset)) & 0x7FF; // Ensure only 11 bits are taken
        return value;
    }


    private static byte[] sha256(byte[] data) {
        SHA256Digest digest = new SHA256Digest();
        digest.update(data, 0, data.length);
        byte[] hash = new byte[digest.getDigestSize()];
        digest.doFinal(hash, 0);
        return hash;
    }

    public static void main(String[] args) {
        
        System.out.println("Generated Mnemonic: " + generateMnemonic());
    }
}
