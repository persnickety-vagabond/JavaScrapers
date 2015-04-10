import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

public class Main {

	public static void main(String[] args) throws IOException {

		Timer timer = new Timer();
		timer.begin();

		// get people.json
		JsonObject data = null;
		try {
			data = JsonObject.readFrom(FileUtils
					.readFileToString(new File("people.json")));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		// make hashset of jobs
		HashSet<String> jobs = new HashSet<String>();
		List<String> lines = FileUtils.readLines(new File("jobs_small.txt"));
		for(String line: lines){
			line = line.replaceAll("[=0-9, ]+$", "");	
			jobs.add(line);
		}
			
		
		// Loop over people array
		JsonArray people = data.get("people").asArray();
		int totalPeople = people.size();
		for(int i = 0; i < people.size(); i++){
			
			JsonObject person = people.get(i).asObject();		
			
			// get job from object
			String job = person.get("occupation").asString();
			
			// Keep people with valid jobs
			if(!jobs.contains(job)){ 
				people.remove(i);
			}
		}
		
		System.out.println("Preserved " + people.size() + " of " + totalPeople + ". A difference of "+ (totalPeople-people.size()) + " people.");
		
		// output the people with valid jobs
		JsonObject j = new JsonObject();
		j.add("people", people);
		FileUtils.writeStringToFile(new File("people_final.json"), j.toString());
		
		timer.end();
		timer.printFormattedExecutionTime();
	}
}
