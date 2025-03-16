import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.util.Base64;

public class ECDSAUtils {

    public static KeyPair generateKeyPair() throws Exception {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("EC");
        ECGenParameterSpec ecSpec = new ECGenParameterSpec("secp256r1");
        keyPairGen.initialize(ecSpec, new SecureRandom());
        return keyPairGen.generateKeyPair();
    }

    public static String signData(String data, PrivateKey privateKey) throws Exception {
        Signature signature = Signature.getInstance("SHA256withECDSA");
        signature.initSign(privateKey);
        signature.update(data.getBytes());
        byte[] signatureBytes = signature.sign();
        return Base64.getEncoder().encodeToString(signatureBytes);
    }

    public static boolean verifySignature(String data, String signature, PublicKey publicKey) throws Exception {
        Signature sig = Signature.getInstance("SHA256withECDSA");
        sig.initVerify(publicKey);
        sig.update(data.getBytes());
        byte[] decodedSignature = Base64.getDecoder().decode(signature);
        return sig.verify(decodedSignature);
    }

    public static void main(String[] args) {
        try {
            // Generate Key Pair
            KeyPair keyPair = generateKeyPair();

            // Sample Data
            String message = "ECDSA Digital Signature Example";

            // Sign Data
            String signature = signData(message, keyPair.getPrivate());
            System.out.println("Signature: " + signature);

            // Verify Signature
            boolean isVerified = verifySignature(message, signature, keyPair.getPublic());
            System.out.println("Signature Verified: " + isVerified);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
