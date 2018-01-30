package decrypt;

import java.math.BigInteger;
import java.util.Vector;

import key.PrivateKey;

public class Decryptor {
	
	public static String decrypt(Vector<BigInteger> crypted_message, PrivateKey privk){
		String message = "";
		for (BigInteger bi : crypted_message){
			message += String.valueOf( (char) bi.modPow(privk.getPrivatePair().u, privk.getPrivatePair().n).intValue() );
		}
		return message;
	}

}
