import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class AllPrimitiveRoots {

	static int power(int a, int b, int mod){
		int res = 1;
		a=a%mod;
		while(b>0) {
			if((b&1)==1)
			   res = (res * a) % mod;
			b=b>>1;
			a=(a*a) % mod;
		}
		return res;
	}

	static void findPrimeFactors(Set<Integer> s,int n) {
		while(n%2==0) {
			s.add(2);
			n /=2;
		}
		for(int i=3;i<= Math.sqrt(n); i+=2) {
			while (n % i==0) {
				s.add(i);
				n /=i;
			}
		}
		if(n>2) {
		   s.add(n);
		}
	}

	static List<Integer> findAllPrimitives(int n) {
		Set<Integer> s = new HashSet<>();
		int phi = n - 1;
		findPrimeFactors(s, phi);

		List<Integer> primitiveRoots = new ArrayList<>();
		for(int r=2;r<=phi;r++) {
			boolean flag =false;
			for(Integer a:s) {
			    if(power(r,phi /a,n) ==1) {
				flag=true;
				break;
			     }
			}
			if(!flag) {
			   primitiveRoots.add(r);
			}
		}
		return primitiveRoots;
	}
	
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		System.out.print("Enter a prime number: ");
		int n= scanner.nextInt();
		scanner.close();

		List<Integer> primitiveRoots = findAllPrimitives(n);
		if(primitiveRoots.isEmpty()){
			System.out.println("No primitive roots found.");
		} else {
			System.out.println("All primitive roots of "+n+"are: "+primitiveRoots);
		}
	}
}
