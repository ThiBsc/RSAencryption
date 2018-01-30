package key;

public class KeyGenerator {

	private PublicKey pub_key;
	private PrivateKey priv_key;
	
	public KeyGenerator() {
		pub_key = new PublicKey();
		priv_key = new PrivateKey(pub_key);
	}
	
	public PublicKey getPublicKey(){
		return pub_key;
	}
	
	public PrivateKey getPrivateKey(){
		return priv_key;
	}

}
