package key;

import java.math.BigInteger;
import java.util.Random;

public class PublicKey {
	
	/**
	 * Pour créer une clé publique, il faut choisir aléatoirement 2 grands
	 * entiers premiers p & q != p
	 * 
	 * n = p*q & m = (p-1)*(q-1)
	 * 
	 * m est l'indicatrice d'euler (nombre d'entiers naturels inférieurs ou égaux à n
	 * qui lui sont premiers
	 * 
	 * Choisir ensuite un "petit" entier (impair) e, appelé exposant public, qui soit
	 * premier avec m (il en existe une infinité)
	 * 
	 * La clé publique est le couple (n, e)
	 */
	private BigInteger p, q, n, m, e;
	
	public class KeyPair {
		public BigInteger n, e;
		
		public KeyPair(BigInteger n, BigInteger e) {
			this.n = n;
			this.e = e;
		}
	}

	public PublicKey() {
		Random r = new Random();
		p = BigInteger.probablePrime(KeySize.BIG, r);
		q = BigInteger.probablePrime(KeySize.BIG, r);
		n = p.multiply(q);
		m = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
		e = null;
		
		BigInteger two = new BigInteger("2");
		do {
			BigInteger tmp = new BigInteger(KeySize.SMALL, r);
			tmp = tmp.multiply(two).add(BigInteger.ONE);
			if (tmp.gcd(m).equals(BigInteger.ONE))
				e = tmp;
		} while (e == null);
	}
	
	public PublicKey(BigInteger n, BigInteger e){
		this.n = n;
		this.e = e;
	}
	
	public BigInteger getE(){
		return e;
	}
	
	public BigInteger getM(){
		return m;
	}
	
	public String toString(){
		String ret = "("+n.toString()+", "+e.toString()+")";
		return ret;
	}
	
	public KeyPair getPublicPair(){
		KeyPair key = new KeyPair(n, e);
		//KeyPair key = new KeyPair(new BigInteger("5141"), new BigInteger("7"));
		return key;
	}

}
