import java.util.Scanner;

public class DiffieHellmanAlgorithmExample {
	public static void main(String[] args) {
		long P, G, x, a,y ,b, ka, kb;
		Scanner sc = new Scanner(System.in);

		System.out.println("Both the users should be agreed upon the public terms G and P.");
		System.out.print("Enter value for prime P: ");
		P = sc.nextLong();

		System.out.print("Enter value for primitive root G: ");
		G = sc.nextLong();

		System.out.print("Enter value for privite key a selected by User1: ");
		a = sc.nextLong();

		System.out.print("Enter value for privite key b selected by User2: ");
		b = sc.nextLong();

		x = calculatePower(G, a, P);
		y = calculatePower(G, b, P);

		System.out.println("User1's public key is: " +x);
		System.out.println("User2's public key is: " +y);

		ka = calculatePower(y, a, P);
		kb = calculatePower(x, b, P);

		System.out.println("Secret key for User1 is: " +ka);
		System.out.println("Secret key for User2 is: " +kb);

		if(ka == kb) {
			System.out.println("Shared secrets are equal!");
		} else {
			System.out.println("Shared secrets are NOT equal!");
		}

		sc.close();
	}

	private static long calculatePower(long x, long y, long P) {
		if(y==1) {
			return x % P;
		} else {
			return ((long) Math.pow(x,y)) % P;
		}
	}
}
