import org.bouncycastle.crypto.digests.SHA256Digest;
import java.util.ArrayList;
import java.util.List;

public class MerkleTreeGenerator {
    private List<String> transactions;
    private String merkleRoot;

    public MerkleTreeGenerator(List<String> transactions) {
        this.transactions = transactions;
        this.merkleRoot = buildMerkleTree(transactions);
    }

    private String buildMerkleTree(List<String> data) {
        List<String> hashes = new ArrayList<>();
        
        for (String item : data) {
            hashes.add(hash(item));
        }
        
        while (hashes.size() > 1) {
            List<String> newLevel = new ArrayList<>();
            
            for (int i = 0; i < hashes.size(); i += 2) {
                if (i + 1 < hashes.size()) {
                    newLevel.add(hash(hashes.get(i) + hashes.get(i + 1)));
                } else {
                    newLevel.add(hashes.get(i));
                }
            }
            hashes = newLevel;
        }
        
        return hashes.get(0);
    }

    private String hash(String data) {
        SHA256Digest digest = new SHA256Digest();
        byte[] inputBytes = data.getBytes();
        digest.update(inputBytes, 0, inputBytes.length);
        byte[] hash = new byte[digest.getDigestSize()];
        digest.doFinal(hash, 0);
        return bytesToHex(hash);
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            hexString.append(String.format("%02x", b));
        }
        return hexString.toString();
    }

    public String getMerkleRoot() {
        return merkleRoot;
    }

    public static void main(String[] args) {
        List<String> transactions = List.of("tx1", "tx2", "tx3", "tx4");
        MerkleTreeGenerator merkleTree = new MerkleTreeGenerator(transactions);
        System.out.println("Merkle Root: " + merkleTree.getMerkleRoot());
    }
}
