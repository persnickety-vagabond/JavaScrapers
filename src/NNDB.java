import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;

public class NNDB {

	public static void scrapeNames() {
		// get urls.json
		JsonObject data = null;
		try {
			data = JsonObject.readFrom(FileUtils.readFileToString(new File(
					"urls.json")));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Create result
		JsonArray result = new JsonArray();

		// Get HTML from each url
		for (int i = 0; i < 26; i++) {

			// get the url
			String url = data.get("pages").asArray().get(i).asObject()
					.get("urls").asObject().get("href").asString();

			// open stream
			URL site = null;
			try {
				site = new URL(url);
			} catch (MalformedURLException e1) {
				e1.printStackTrace();
			}
			BufferedReader in = null;
			try {
				in = new BufferedReader(
						new InputStreamReader(site.openStream()));
			} catch (IOException e1) {
				e1.printStackTrace();
			}

			// get HTML
			String inputLine;
			String html = "";
			try {
				while ((inputLine = in.readLine()) != null) {
					html = html + inputLine;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// parse out table of people
			String[] body = html
					.split("<table border=0 cellpadding=5 cellspacing=0>");
			body = body[1].split("</table><p>");
			String[] people = body[0].split("</tr>");

			// parse out each person
			for (String person : people) {
				// System.out.println(person);

				if (!person.contains("<tt>-</tt>")) {
					continue;
				}

				person.replace("<font size=-1><i>", "");
				person.replace("</i></font>", "");
				person.replace(
						"<td align=center valign=middle nowrap><tt>-</tt></td>",
						"");

				String name = person.split("\">")[1].split("</a>")[0];
				String job = person.split("<font size=-1>")[1]
						.split("</font></td>")[0];
				String birthday = person.split("<tt>")[1].split("</tt>")[0];

				JsonObject json = new JsonObject();
				json.add("fullName", name);
				json.add("occupation", job);
				json.add("dob", birthday);

				result.add(json);

				System.out.println(result.size() + ". " + name + " " + job
						+ " " + birthday);
			}
		}

		// Finished scraping, save the result array
		JsonObject j = new JsonObject();
		j.add("people", result);
		try {
			FileUtils.writeStringToFile(new File("people.json"), j.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void filterNamesByJob() {

		// get people.json
		JsonObject data = null;
		try {
			data = JsonObject.readFrom(FileUtils.readFileToString(new File(
					"people.json")));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// make hashset of jobs
		HashSet<String> jobs = new HashSet<String>();
		List<String> lines = null;
		try {
			lines = FileUtils.readLines(new File("jobs_small.txt"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (String line : lines) {
			line = line.replaceAll("[=0-9, ]+$", "");
			jobs.add(line);
		}

		// Loop over people array
		JsonArray people = data.get("people").asArray();
		int totalPeople = people.size();
		for (int i = 0; i < people.size(); i++) {

			JsonObject person = people.get(i).asObject();

			// get job from object
			String job = person.get("occupation").asString();

			// Keep people with valid jobs
			if (!jobs.contains(job)) {
				people.remove(i);
			}
		}

		System.out.println("Preserved " + people.size() + " of " + totalPeople
				+ ". A difference of " + (totalPeople - people.size())
				+ " people.");

		// output the people with valid jobs
		JsonObject j = new JsonObject();
		j.add("people", people);
		try {
			FileUtils.writeStringToFile(new File("people_final.json"),
					j.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
