package gitTp1;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class writeJson {
	public void writeJson(){
	String jsonContent = "{\n\"identifier\":[{\"username\": \"Hack\",\"password\": \"Track\"}]\n}";
    String pathJson = System.getProperty("user.dir")+"/gitTp1/idInfo.json";
    File file = new File(pathJson) {};

    try {
        if (!file.exists())
            file.createNewFile();
        FileWriter writer = new FileWriter(file);
        writer.write(jsonContent);
        writer.flush();
        writer.close();
    } catch (IOException e) {
        System.out.println("Erreur: impossible de créer le fichier '" + pathJson + "'");
    }
  }
}
