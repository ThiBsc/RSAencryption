package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

import client.ChatClient;
import client.ConsoleColors;
import decrypt.Decryptor;

import key.PublicKey;

public class ChatServer extends Thread {

	// the port is the date of the RSA MIT brevet
	public static final int PORT = 1983;
	private ServerSocket ss;
	private ChatClient cc;
	
	public ChatServer() {
		cc = new ChatClient();
	}

	@Override
	public void run() {
		try {
			System.out.println("Secure server RSA started");
			ss = new ServerSocket(PORT);
			boolean isRunning = true;
			cc.start();
			while (isRunning) {
				Socket s = ss.accept();
				// you are receiving message
				ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
				Object message;
				try {
					while ( (message = ois.readObject()) != null){
						if (message instanceof String){
							if (((String) message).matches("(^/\\w+$|^/\\w+\\s.*$)")){
								// the message is a command
								if (((String) message).startsWith("/pubk")){
									String[] cmd_publick = ((String) message).split(" ");
									if (cc.getInterlocutor_pubk() == null){
										cc.setInterlocutor_pubk(new PublicKey(new BigInteger(cmd_publick[1]), new BigInteger(cmd_publick[2])));
										cc.autoConnectAfterConnectionReceive(s.getInetAddress().getHostAddress());
									}
								}
							}
						} else if (message instanceof Vector<?>) {
							@SuppressWarnings("unchecked")
							Vector<BigInteger> crypted = (Vector<BigInteger>) message;
							String decrypted = Decryptor.decrypt(crypted, cc.getPrivk());
							System.out.println(ConsoleColors.RED + "> " + ConsoleColors.RESET + decrypted);
						}
					}
				} catch (ClassNotFoundException e) {
					System.err.println(e.getMessage());
				}
			}
		} catch (IOException e) {
			System.err.println(e.getMessage());
		} finally {
			System.out.println("Secure server RSA goodbye.");
			System.exit(0);
		}
	}
	
	public static void main(String[] args) {
		System.out.println(ConsoleColors.CYAN + "#" + 
				ConsoleColors.BLUE_BRIGHT + " Peer to Peer " +
				ConsoleColors.WHITE + ConsoleColors.RED_BACKGROUND + "RSA" +
				ConsoleColors.BLUE_BRIGHT + " secure chat ! " + 
				ConsoleColors.CYAN + "#" + ConsoleColors.RESET);
		ChatServer cs = new ChatServer();
		cs.start();
	}

}
