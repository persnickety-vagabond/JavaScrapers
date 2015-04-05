import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import com.eclipsesource.json.JsonObject;

import twitter4j.Twitter;
import twitter4j.TwitterException;

public class Main {

	public static void main(String[] args) throws IOException {

		Timer timer = new Timer();
		timer.begin();

		// get data.json
		JsonObject data = JsonObject.readFrom(FileUtils
				.readFileToString(new File("data.json")));

		// get instance of twitter API
		Twitter twitter = TwitterApiBuilder.getTwitter();

		// iterate over people
		int numPeople = data.get("people").asArray().size();
		for (int i = 0; i < numPeople; i++) {

			// get Twitter handle
			String name = data.get("people").asArray().get(i).asObject()
					.get("twitter").asObject().get("handle").asString()
					.substring(1);

			// get number of followers from API
			int numFollowers = 0;
			try {
				numFollowers = twitter.showUser(name).getFollowersCount();
			} catch (TwitterException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("Could not scrape: " + name);
				timer.end();
				timer.printFormattedExecutionTime();
				return;
			}

			// update person object
			data.get("people").asArray().get(i).asObject().get("twitter")
					.asObject().add("followers_count", numFollowers);

			try {
				System.out.println(i + ". " + name + " has " + numFollowers
						+ " followers.");
				Thread.sleep(5000); // 1000 milliseconds is one second.
			} catch (InterruptedException ex) {
				Thread.currentThread().interrupt();
			}
		}

		FileUtils.writeStringToFile(new File("data2.json"), data.asString());
		timer.end();
		timer.printFormattedExecutionTime();
	}
}
