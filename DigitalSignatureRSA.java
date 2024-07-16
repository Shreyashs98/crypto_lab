import java.security.*;
import java.util.Base64;
import java.util.Scanner;

public class DigitalSignatureRSA {
	public static void main(String[] args) throws Exception {
		Scanner scanner = new Scanner(System.in);
		KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
		keyGen.initialize(2048);
		KeyPair keyPair = keyGen.generateKeyPair();
		PrivateKey privateKey = keyPair.getPrivate();
		PublicKey publicKey = keyPair.getPublic();

		String base64PrivateKey = Base64.getEncoder().encodeToString(privateKey.getEncoded());
		String base64PublicKey = Base64.getEncoder().encodeToString(publicKey.getEncoded());
		System.out.println("Private key (Base64 encoded): "+base64PrivateKey);
		System.out.println("Public key (Base64 encoded): "+base64PublicKey);

		Signature signature = Signature.getInstance("SHA256withRSA");

		signature.initSign(privateKey);

		System.out.println("Enter text to sign:");
		String data = scanner.nextLine();
		signature.update(data.getBytes());

		byte[] digitalSignature = signature.sign();

		String base64Signature = Base64.getEncoder().encodeToString(digitalSignature);
		System.out.println("Digital Signature (Base64 encoded) : " +base64Signature);
		System.out.println("Digital Signature (raw bytes): " + new String(digitalSignature));
		
		Signature verifier = Signature.getInstance("SHA256withRSA");
		verifier.initVerify(publicKey);
		verifier.update(data.getBytes());

		boolean verified = verifier.verify(digitalSignature);
		System.out.println("Signature verified: "+verified);

		String tamperedData = data + "tampered";
		verifier.update(tamperedData.getBytes());
		boolean tamperedVerified = verifier.verify(digitalSignature);
		System.out.print("Tempered signature verified: "+tamperedVerified);
	}
}
