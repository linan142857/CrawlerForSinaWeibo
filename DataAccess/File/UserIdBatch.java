package File;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class UserIdBatch {
	public String filePath = "";
	
	public UserIdBatch(){
		
	}
	
	public UserIdBatch(String filePath){
		this.filePath = filePath;
	}
	
	public List<String> getAllUserID(){
		List<String> allUserID = new ArrayList<String>();
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(filePath))));
			String line = "";
			while((line = reader.readLine())!= null){
				if(line.length() != 0){
					allUserID.add(line.split("#")[0]);
				}
			}
			reader.close();
		} catch (FileNotFoundException e) {
			System.out.println("用户id文件读取失败！");
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return allUserID;
	}
}
