package gitTp1;

import java.net.*;
import java.util.Map;
import java.io.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.util.Scanner;

public class Client {
	private static Socket socket;
	
	public static void main(String[] args) throws Exception {
		InputConnection inputInfo = new InputConnection();
		Map<String,String> connectionInfo = inputInfo.getInfo();
		
		String serverAddress = connectionInfo.get("ipAdress");
		int serverPort = Integer.parseInt(connectionInfo.get("portNumber"));
		
		socket = new Socket(serverAddress, serverPort);
		System.out.format("The client is running %s: %d%n",serverAddress,serverPort);
		
		Scanner inputUserName = new Scanner(System.in);
        System.out.println("Username : ");
        String userName = "";
        userName = inputUserName.nextLine();
        
        Scanner inputImageName = new Scanner(System.in);
		System.out.println("Entrer le nom de l'image à traiter (avec le .png) : ");
		String imageName = "";
		imageName = inputImageName.nextLine();
		System.out.println(imageName);
		String pathToImage = System.getProperty("user.dir")+"/src/gitTp1/";
		String path = pathToImage+imageName;
		System.out.println(path);
		//chargement de l'image de base
		BufferedImage img = ImageIO.read(new File(path));
		
		DataInputStream in = new DataInputStream(socket.getInputStream());
		String helloMessageFromServer = in.readUTF();
		System.out.println(helloMessageFromServer);
		
		DataOutputStream out = new DataOutputStream(socket.getOutputStream());
		out.writeUTF(userName);
		
		ImageIO.write(img, "PNG", out); //envoi l'image
		System.out.println("image envoyée au serveur");
		out.writeUTF("Image envoyée par le client");
		
		//Save image received
		
		BufferedImage fImage = ImageIO.read(in);
		System.out.println("image reçue");
		
		try {
		    // retrieve image
			File outputfile = new File(pathToImage+"/imageFiltree" + userName + ".png");
		    ImageIO.write(fImage, "png", outputfile);
		} catch (IOException e) {
		}
		System.out.println("image enregistrée dans "+pathToImage);
		
		socket.close();
	}
}
