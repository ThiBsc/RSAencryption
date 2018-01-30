package test;

import decrypt.Decryptor;
import encrypt.Encryptor;
import key.PrivateKey;
import key.PublicKey;
import java.math.BigInteger;
import java.util.Vector;

public class Testor {
	
	public static void main(String[] args) {
		PublicKey pubk = new PublicKey();
		PrivateKey privk = new PrivateKey(pubk);
		System.out.println(pubk);
		System.out.println(privk);
		
		Vector<BigInteger> crypted = Encryptor.encrypt("Hello world !", pubk);
		System.out.println(crypted);
		
		String message = Decryptor.decrypt(crypted, privk);
		System.out.println(message);
	}

}
