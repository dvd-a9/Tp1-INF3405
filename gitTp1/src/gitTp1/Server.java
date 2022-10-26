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
		writeJson write = new writeJson();
		write.addUser("defaultUser", "1234");
		
		
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
		writeJson write = new writeJson();
		HashMap<String, String> users =  write.getHasmapJson();
		
	
		
		public ClientHandler(Socket socket, int clientNumber) {
			this.socket = socket;
			this.clientNumber = clientNumber;

			System.out.println(users);
			
			System.out.println("New connection with client#" + clientNumber + " at " + socket);	
		}
		
		public void run() {
			try {
			DataOutputStream out = new DataOutputStream(socket.getOutputStream());
				
			out.writeUTF("Hello from Server - you are client#" + clientNumber);
				

			// début de la réception sur le stream
			DataInputStream in = new DataInputStream(socket.getInputStream());
			System.out.println("début réception");
			Sobel filtre = new Sobel();

			//récupération username
			String userName = in.readUTF();
			if (users.containsKey(userName)){
				out.writeByte(1);
				String mdp = in.readUTF();
				System.out.println(mdp.equals(users.get(userName)));
				while (mdp.equals(users.get(userName))==false) {
					out.writeByte(1);
					mdp = in.readUTF();
				}
				out.writeByte(2);
			}
			if(users.containsKey(userName)==false) {
				out.writeByte(3);
				String Creermdp = "";
				Random rand = new Random();
				for(int i = 0 ; i < 6 ; i++){
				 char c = (char)(rand.nextInt(26) + 97);
				 Creermdp += c;
				}
				out.writeUTF(Creermdp);
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