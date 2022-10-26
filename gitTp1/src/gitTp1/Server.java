package gitTp1;

import java.net.*;
import java.util.Map;
import java.io.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;


public class Server {
	
	private static ServerSocket listener;
	
	public static void main(String[] args) throws Exception {
		int clientNumber = 0;
		
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
		
		public ClientHandler(Socket socket, int clientNumber) {
			this.socket = socket;
			this.clientNumber = clientNumber;
			
			System.out.println("New connection with client#" + clientNumber + " at " + socket);	
		}
		
		public void run() {
			try {
				DataOutputStream out = new DataOutputStream(socket.getOutputStream());
				
				out.writeUTF("Hello from Server - you are client#" + clientNumber);
				Sobel filtre = new Sobel();
				/*in*/
				// début de la réception sur le stream
				DataInputStream in = new DataInputStream(socket.getInputStream());
				System.out.println("début réception");
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

