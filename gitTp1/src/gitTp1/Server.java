package gitTp1;

import java.net.*;
import java.util.Map;
import java.io.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.util.HashMap; 
import java.util.Random;


public class Server {
	private static ServerSocket listener;
	
	public static void main(String[] args) throws Exception {
		int clientNumber = 0;
		
		//INFORMATION SERVEUR
		InputConnection inputInfo = new InputConnection();
		Map<String,String> connectionInfo = inputInfo.getInfo();
		
		String serverAddress = connectionInfo.get("ipAdress");
		int serverPort = Integer.parseInt(connectionInfo.get("portNumber"));
		
		listener = new ServerSocket();
		listener.setReuseAddress(true);
		InetAddress serverIp = InetAddress.getByName(serverAddress);
		
		listener.bind(new InetSocketAddress(serverIp, serverPort));
		System.out.format("The server is running %s: %d%n",serverAddress,serverPort);
		
		
		try {
			while(true) {
				new ClientHandler(listener.accept(), clientNumber++).start();
			}
		}
		finally {
			listener.close();
		}

	}

	private static class ClientHandler extends Thread{
		private Socket socket;
		private int clientNumber;
		
		//RECUPERATION DE LA HASHMAP CONTENANT TOUS LES USERS
		writeJson write = new writeJson();
		HashMap<String, String> users =  write.getHasmapJson();
		
	
		
		public ClientHandler(Socket socket, int clientNumber) {
			this.socket = socket;
			this.clientNumber = clientNumber;
			//affichage dans la console serveur des utilisateurs (pour correction)
			System.out.println(users);
			
			System.out.println("New connection with client#" + clientNumber + " at " + socket);	
		}
		
		public void run() {
			try {
				
			DataOutputStream out = new DataOutputStream(socket.getOutputStream());
				
			out.writeUTF("Hello from Server - you are client#" + clientNumber);
				
			DataInputStream in = new DataInputStream(socket.getInputStream());
			System.out.println("début réception");
			Sobel filtre = new Sobel();

			//récupération userName du client
			String userName = in.readUTF();
			//vérifications si le client s'est déjà connecté
				//client connu
			if (users.containsKey(userName)){
				out.writeByte(1);
				//récupération mot de passe
				String mdp = in.readUTF();
				//vérification du mot de passe
				while (mdp.equals(users.get(userName))==false) {
					out.writeByte(1);
					mdp = in.readUTF();
				}
				out.writeByte(2);
			}
				//client inconnu
			if(users.containsKey(userName)==false) {
				out.writeByte(3);
				//création d'un mot de passe au hasard
				String Creermdp = "";
				Random rand = new Random();
				for(int i = 0 ; i < 6 ; i++){
				 char c = (char)(rand.nextInt(26) + 97);
				 Creermdp += c;
				}
				out.writeUTF(Creermdp);
				//ajout du nouvel utilisateur
				write.addUser( userName , Creermdp );
				
				out.writeByte(2);
			}


			//récupération de l'image envoyée
			BufferedImage image = ImageIO.read(in);
			System.out.println("Image reçue par serveur");
			BufferedImage fImage = filtre.process(image);
			System.out.println("filtre appliqué sur l'image");

			ImageIO.write(fImage, "PNG", out);
			System.out.println("image renvoyée au client");	
			}
			catch(IOException e) {
				System.out.println("Error handling client#" + clientNumber + " : " + e);
			}
			finally {
				try {
					socket.close();
				}
				catch(IOException e) {
					System.out.println("Could not close socket");
				}
				System.out.println("Connection with client#" + clientNumber + " closed");
			}
		}
	}
}
