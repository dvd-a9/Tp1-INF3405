package gitTp1;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class writeJson{
	public static String pathJson = System.getProperty("user.dir")+"/src/gitTp1/idInfo.json"; 
	
	public JSONObject getDb(String filePath) {
		File jsonFile = new File(filePath);
		JSONObject jsonObject = new JSONObject();

		if (jsonFile.exists() && !jsonFile.isDirectory()) {
			try {
				JSONParser jsonParser = new JSONParser();
				FileReader fileReader = new FileReader(filePath);
				Object objectJson = jsonParser.parse(fileReader);
				jsonObject = (JSONObject) objectJson;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return jsonObject;
	}
	
	public void addInfo(JSONObject jsonObject) {
	    try (FileWriter file = new FileWriter(pathJson)) {
			file.write(jsonObject.toString());
			file.flush();
			file.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void addUser(String username, String password) {
		Boolean isUserCreated = true;
		JSONObject jsonObject = this.getDb(pathJson);
		HashMap usersList = jsonObject.get("users") != null ? (HashMap) jsonObject.get("users") : new HashMap();

		usersList.put(username, password);
		jsonObject.put("users", usersList);

		addInfo(jsonObject);
		return;
	}
	
	public HashMap getHasmapJson() {
		JSONObject db = this.getDb(pathJson);
		HashMap userList = (HashMap) db.get("users");
		
		return userList;
	}
	
}
