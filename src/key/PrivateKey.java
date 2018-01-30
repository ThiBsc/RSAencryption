package key;

import java.math.BigInteger;

public class PrivateKey {
	
	/**
	 * Pour créer une clé privé, il faut calculer un nombre u vérifiant l'équation
	 * "diophantienne" e*u + m*v = PGCD(e, m) = 1 & 2 < u < m
	 * à l'aide de l'algorithme d'euclide étendu (e & m ont déjà été calculé dans la clé publique)
	 * u va permettre d'inverser la fonction de chiffrement très facilement
	 * (u est ainsi utilisé pour le déchiffrement). Ce nombre est important et doit rester secret.
	 */
	class Triplet {
		public BigInteger r, u, v;
	}
	
	public class KeyPair {
		public BigInteger n, u;
		
		public KeyPair(BigInteger n, BigInteger u) {
			this.n = n;
			this.u = u;
		}
	}
	
	private Triplet triplet;
	private BigInteger n;
	
	public PrivateKey(PublicKey pubk) {
		n = pubk.getPublicPair().n;
		triplet = euclide_extended(pubk.getE(), pubk.getM());
	}
	
	public Triplet getTriplet(){
		return triplet;
	}
	
	public String toString(){
		//String ret = "("+n.toString()+", "+triplet.r.toString()+", "+triplet.u.toString()+", "+triplet.v.toString()+")";
		String ret = "("+n.toString()+", "+triplet.u.toString()+")";
		return ret;
	}
	
	public KeyPair getPrivatePair(){
		KeyPair key = new KeyPair(n, triplet.u);
		//KeyPair key = new KeyPair(new BigInteger("5141"), new BigInteger("4279"));
		return key;
	}
	
	/*
	 * Entrée : a, b entiers (naturels)
	 * Sortie : r entier (naturel) et  u, v entiers relatifs tels que r = pgcd(a, b) et r = a*u+b*v
	 * 	
	 * Initialisation : r := a, r' := b, u := 1, v := 0, u' := 0, v' := 1
	 *	                  q  quotient entier
	 *	                  rs, us, vs  variables de stockage intermédiaires
	 *	
	 * les égalités r = a*u+b*v et r' = a*u'+b*v' sont des invariants de boucle
	 *	
	 * tant que (r' ≠ 0) faire
	 *	    q := r÷r'
	 *	    rs := r, us := u, vs := v,
	 *	    r := r', u := u', v := v',
	 *	    r' := rs - q*r', u' = us - q*u', v' = vs - q*v'
	 *	   fait
	 *	renvoyer (r, u, v)
	 */
	private Triplet euclide_extended(BigInteger e, BigInteger m){
		Triplet triple = new Triplet();
		Triplet triple_prim = new Triplet();
		Triplet triple_tmp = new Triplet();
		BigInteger quotient;
		
		triple.r = e;
		triple.u = BigInteger.ONE;
		triple.v = BigInteger.ZERO;
		triple_prim.r = m;
		triple_prim.u = BigInteger.ZERO;
		triple_prim.v = BigInteger.ONE;
		
		while (triple_prim.r != BigInteger.ZERO) {
			quotient = triple.r.divide(triple_prim.r);
			triple_tmp.r = triple.r;
			triple_tmp.u = triple.u;
			triple_tmp.v = triple.v;
			
			triple.r = triple_prim.r;
			triple.u = triple_prim.u;
			triple.v = triple_prim.v;
			
			triple_prim.r = triple_tmp.r.subtract(quotient.multiply(triple_prim.r));
			triple_prim.u = triple_tmp.u.subtract(quotient.multiply(triple_prim.u));
			triple_prim.v = triple_tmp.v.subtract(quotient.multiply(triple_prim.v));
		}
		if ( !(triple.u.compareTo(new BigInteger("2")) == 1 && triple.u.compareTo(m) == -1) ){
			BigInteger k = new BigInteger("-1");
			BigInteger tmp = triple.u;
			do {
				tmp = triple.u.subtract(k.multiply(m));
				k = k.subtract(BigInteger.ONE);
				// System.out.println("2 < "+tmp.toString()+" < "+m.toString()+" = "+(tmp.compareTo(new BigInteger("2")) == 1 && tmp.compareTo(m) == -1));
			} while ( !(tmp.compareTo(new BigInteger("2")) == 1 && tmp.compareTo(m) == -1) && tmp.compareTo(m) == -1);
			triple.u = tmp;
		}
		System.err.println(n + " " + triple.u);
		return triple;
	}

}
