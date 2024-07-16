import java.security.*;
import java.util.*;

public class KerberosSimulationTest {
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);

        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048);
        KeyPair asKeyPair = keyGen.generateKeyPair();
        KeyPair tgsKeyPair = keyGen.generateKeyPair();

        PrivateKey asPrivateKey = asKeyPair.getPrivate();
        PublicKey asPublicKey = asKeyPair.getPublic();
        PrivateKey tgsPrivateKey = tgsKeyPair.getPrivate();
        PublicKey tgsPublicKey = tgsKeyPair.getPublic();

        System.out.println("Client: Requesting TGT from AS");
        System.out.print("Enter client name: ");
        String clientName = scanner.nextLine();
        String tgt = requestTGT(clientName, asPrivateKey);
        System.out.println("TGT received: " + tgt);

        System.out.println("\nClient: Received TGT. Requesting ST from TGS");
        System.out.print("Enter service name: ");
        String serviceName = scanner.nextLine();
        String st = requestST(clientName, serviceName, tgt, tgsPrivateKey, asPublicKey);
        System.out.println("ST received: " + st);

        System.out.println("\nClient: Received ST. Authenticating with Service Server");
        boolean authenticated = authenticateWithService(serviceName, st, tgsPublicKey);
        System.out.println("Authentication " + (authenticated ? "successful" : "failed"));

        System.out.println("\nSimulating tampered data verification");
        String tamperedData = clientName + "tampered";
        boolean tamperedVerified = authenticateWithService(serviceName, tamperedData, tgsPublicKey);
        System.out.println("Tampered authentication " + (tamperedVerified ? "successful" : "failed"));
    }

    public static String requestTGT(String clientName, PrivateKey asPrivateKey) throws Exception {
        String tgt = "TGT_for_" + clientName + "_issued_by_AS";
        return encrypt(tgt, asPrivateKey);
    }

    public static String requestST(String clientName, String serviceName, String tgt, PrivateKey tgsPrivateKey, PublicKey asPublicKey) throws Exception {
        if (!decrypt(tgt, asPublicKey).startsWith("TGT_for_" + clientName)) {
            throw new Exception("Invalid TGT");
        }
        String st = "ST_for_" + clientName + "_to_access_" + serviceName;
        return encrypt(st, tgsPrivateKey);
    }

    public static boolean authenticateWithService(String serviceName, String st, PublicKey tgsPublicKey) throws Exception {
        try {
            String decryptedST = decrypt(st, tgsPublicKey);
            return decryptedST.endsWith("_to_access_" + serviceName);
        } catch (Exception e) {
            return false;
        }
    }

    public static String encrypt(String data, PrivateKey privateKey) throws Exception {
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(privateKey);
        signature.update(data.getBytes());
        byte[] digitalSignature = signature.sign();
        return Base64.getEncoder().encodeToString(digitalSignature) + ":" + data;
    }

    public static String decrypt(String encryptedData, PublicKey publicKey) throws Exception {
        String[] parts = encryptedData.split(":");
        byte[] digitalSignature = Base64.getDecoder().decode(parts[0]);
        String data = parts[1];

        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initVerify(publicKey);
        signature.update(data.getBytes());

        if (signature.verify(digitalSignature)) {
            return data;
        } else {
            throw new Exception("Invalid signature");
        }
    }
}