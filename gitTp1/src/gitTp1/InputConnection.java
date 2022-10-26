package gitTp1;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Pattern;

public class InputConnection {
	
	public static Map<String,String> getInfo(){
		Scanner inputIpAdress= new Scanner(System.in);
        String ipAdress = "";
        while(!validateIpAdress(ipAdress)){
            System.out.print("Entrer l'adresse Ip : ");
            ipAdress = inputIpAdress.nextLine();
        }
        System.out.print("Le numéro de l'adresse Ip est : " + ipAdress + "\n");

        Scanner inputPortNo = new Scanner(System.in);
        System.out.print("Entrer le numéro de port : ");
        String portNumber = inputPortNo.nextLine();
        while(!validatePortNo(portNumber)){
            System.out.print("Entrer le numéro de port : ");
            portNumber = inputPortNo.nextLine();
        }
        System.out.print("Le numéro de port est : " + portNumber + "\n");
		
		Map<String,String> connectionInfo = new HashMap<>();
        connectionInfo.put("ipAdress", ipAdress);
        connectionInfo.put("portNumber", portNumber);
        
        return connectionInfo;
		
	}
	
    private static boolean validateIpAdress(String ipAdress) {
        int ipSize = ipAdress.length();
        boolean ipAdressIsCorrect = false;
        if (ipSize >= 7 && ipSize <= 15){
            String[] splitNumberIp = ipAdress.split(Pattern.quote("."));
            if(splitNumberIp.length != 4) return ipAdressIsCorrect;
            for(String element : splitNumberIp){
                try{
                    int numberIp = Integer.parseInt(element);
                    if(numberIp >= 0 && numberIp <= 255){
                        ipAdressIsCorrect = true;
                    }
                    else {
                        System.out.println("Les octets doivent se situer entre 0 à 255 \n");
                        return false;
                    }
                }catch (NumberFormatException e){
                    System.out.println("Erreur de format: "+e);
                    return false;
                }
            }
        }
        return ipAdressIsCorrect;
    }

    private static boolean validatePortNo(String portNo) {
        boolean portNoIsCorrect = false;
        try {
            int portNumber = Integer.parseInt(portNo);
            if (portNumber >= 5000 && portNumber <= 5050){
                portNoIsCorrect = true;
            }
            else {
                System.out.println("Le port doit se situer entre 5000 et 5050 \n");
                return portNoIsCorrect;
            }
        }catch (NumberFormatException e){
            System.out.println("Erreur de format: " + e);
            return portNoIsCorrect;
        }
        return portNoIsCorrect;
    }
}

