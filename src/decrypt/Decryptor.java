package decrypt;

import java.math.BigInteger;
import java.util.Vector;

import key.PrivateKey;

public class Decryptor {
	
	public static String decrypt(Vector<BigInteger> crypted_message, PrivateKey privk){
		String message = "";
		BigInteger u = privk.getPrivatePair().u;
		BigInteger n = privk.getPrivatePair().n;
		for (BigInteger bi : crypted_message){
			message += String.valueOf( (char) bi.modPow(u, n).intValue() );
		}
		return message;
	}

}
