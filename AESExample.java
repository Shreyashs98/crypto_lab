import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Scanner;


public class AESExample {
	
	public static void main(String[] args) throws Exception {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Enter the text to encrypt:");
		String originalText = scanner.nextLine();

		SecretKey secretKey = generateAESKey();
		System.out.println("Secret Key (Base64): "+Base64.getEncoder().encodeToString(secretKey.getEncoded()));

		String encryptedText = encrypt(originalText, secretKey);
		String decryptedText = decrypt(encryptedText, secretKey);

		System.out.println("Original:"+originalText);
		System.out.println("Encrypted:"+encryptedText);
		System.out.println("Decrypted:"+decryptedText);

		scanner.close();
	}

	private static SecretKey generateAESKey() throws Exception {
		KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
		keyGenerator.init(256);

		return keyGenerator.generateKey();
	}

	private static String encrypt(String data, SecretKey key) throws Exception {
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.ENCRYPT_MODE,key);
		byte[] encrypted = cipher.doFinal(data.getBytes());
		return Base64.getEncoder().encodeToString(encrypted);
	}

	private static String decrypt(String data, SecretKey key) throws Exception {
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.DECRYPT_MODE,key);
		byte[] decoded = Base64.getDecoder().decode(data);
		byte[] decrypted = cipher.doFinal(decoded);
		return new String(decrypted);
	}
}
