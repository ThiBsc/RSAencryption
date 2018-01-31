package client;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.Vector;

import encrypt.Encryptor;

import server.ChatServer;

import key.KeyGenerator;
import key.PrivateKey;
import key.PublicKey;

public class ChatClient extends Thread {
	
	private Socket server;
	private ObjectOutputStream writer;
	private PublicKey pubk;
	private PublicKey interlocutor_pubk;
	private PrivateKey privk;
	private boolean running;
	
	public ChatClient() {
		server = null;
		writer = null;
		KeyGenerator kg = new KeyGenerator();
		pubk = kg.getPublicKey();
		privk = kg.getPrivateKey();
		interlocutor_pubk = null;
	}
	
	@Override
	public void run() {
		System.out.println("Secure client RSA hello.");
		Scanner sc = new Scanner(System.in);
		running = true;
		while (running) {
			String message = sc.nextLine();
			if (message.matches("(^/\\w+$|^/\\w+\\s.*$)")){
				// the message is a command
				if (message.equals("/quit")){
					running = false;
				} else if (message.equals("/disconnect")){
					try {
						writer.close();
						server.close();
						running = false;
					} catch (IOException e) {
						System.err.println(e.getMessage());
					}
				} else if (message.startsWith("/connect")){
					try {
						server = new Socket(message.split(" ")[1], ChatServer.PORT);
						writer = new ObjectOutputStream(server.getOutputStream());
						writer.writeObject("/pubk "+pubk.getPublicPair().n.toString()+" "+pubk.getPublicPair().e.toString());
						writer.flush();
						System.out.println("You are now in secure communication with "+server.getInetAddress().toString());
					} catch (UnknownHostException e) {
						System.err.println(e.getMessage());
					} catch (IOException e) {
						System.err.println(e.getMessage());
					}
				}
			} else {
				if (server != null){
					Vector<BigInteger> crypted = Encryptor.encrypt(message, interlocutor_pubk);
					try {
						writer.reset();
						writer.writeObject(crypted);
						writer.flush();
					} catch (IOException e) {
						System.err.println(e.getMessage());
					}
				} else {
					System.err.println("You are not connected with an user.");
				}
			}
		}
		sc.close();
		try {
			writer.close();
			server.close();
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
		System.out.println("Secure client RSA goodbye.");
	}

	public PublicKey getPubk() {
		return pubk;
	}

	public void setPubk(PublicKey pubk) {
		this.pubk = pubk;
	}

	public PrivateKey getPrivk() {
		return privk;
	}

	public void setPrivk(PrivateKey privk) {
		this.privk = privk;
	}

	public PublicKey getInterlocutor_pubk() {
		return interlocutor_pubk;
	}

	public void setInterlocutor_pubk(PublicKey interlocutor_pubk) {
		this.interlocutor_pubk = interlocutor_pubk;
	}

	public synchronized boolean isRunning() {
		return running;
	}

	public synchronized void setRunning(boolean running) {
		this.running = running;
	}

}
