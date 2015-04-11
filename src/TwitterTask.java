import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashSet;

import org.apache.commons.io.FileUtils;

import twitter4j.ResponseList;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;

public class TwitterTask {

	public static void getHandlesFromFullNames() {
		// Get an instance of Twitter
		Twitter twitter = TwitterApiBuilder.getTwitter();

		// Get the people_final.json
		JsonObject data = null;
		try {
			data = JsonObject.readFrom(FileUtils.readFileToString(new File(
					"people_final.json")));
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Create result JSON
		JsonObject people = new JsonObject();
		JsonArray peopleArray = new JsonArray();
		people.add("people", "");

		HashSet<String> handles = new HashSet<String>();

		// Create counter
		int counter = 0;

		// Loop over all people
		for (int i = 0; i < data.get("people").asArray().size(); i++) {

			// Get common properties
			JsonObject json = data.get("people").asArray().get(i).asObject();
			String fullName = json.get("fullName").asString();
			String occupation = json.get("occupation").asString();
			String dob = json.get("dob").asString();

			// URL encode the name
			String encoded = null;
			try {
				encoded = URLEncoder.encode(fullName, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}

			// Perform search query
			ResponseList<User> results = null;
			while (results == null) {
				try {
					results = twitter.searchUsers(encoded, 0);
				} catch (TwitterException e) {
					e.printStackTrace();
				}

				try {
					Thread.sleep(5000);
				} catch (InterruptedException ex) {
					Thread.currentThread().interrupt();
				}
			}

			// Loop over results, find verified account
			for (int j = 0; j < results.size(); j++) {

				if (results.get(j).isVerified()) {

					User u = results.get(j);

					// perform quality check on first verified account
					// names match?
					if (!u.getName()
							.toLowerCase()
							.replace(". ", " ")
							.contains(fullName.toLowerCase().replace(". ", " "))) {
						System.out.println(fullName + " - " + u.getName()
								+ " - ERROR: Names did not match");
						break;
					}
					// handle already in list?
					else if (!handles.add(u.getScreenName())) {
						System.out.println(u.getScreenName()
								+ " - ERROR: Handle already exists.");
						break;
					}

					counter++;

					JsonObject twitterObj = new JsonObject();
					twitterObj.add("fullName", fullName);
					twitterObj.add("handle", u.getScreenName());
					twitterObj.add("twitterId", u.getId());
					twitterObj.add("followers", u.getFollowersCount());
					twitterObj.add("profilePic", u.getProfileImageURL());
					twitterObj.add("url",
							"https://twitter.com/" + u.getScreenName());

					if (u.getProfileBannerURL() == null) {
						twitterObj.add("backgroundPic", "null");
					} else {
						twitterObj.add("backgroundPic", u.getProfileBannerURL()
								.replace("/web", ""));
					}

					JsonObject person = new JsonObject();
					person.add("fullName", fullName);
					person.add("occupation", occupation);
					person.add("dob", dob);
					person.add("twitter", twitterObj);

					peopleArray.add(person);
					people.set("people", peopleArray);
					System.out.println(fullName + " - "
							+ Utils.toTitleCase(u.getName()) + " - "
							+ twitterObj.get("handle") + " - SUCCESS (Total: "
							+ counter + " of " + (i + 1) + ")");

					// build and export final people object
					try {
						FileUtils
								.writeStringToFile(new File(
										"people_final_twitter.json"), people
										.toString());
					} catch (IOException e) {
						e.printStackTrace();
					}

					break;
				}
			}

		}

		// Print total
		System.out.println("Total: " + counter + "/"
				+ data.get("people").asArray().size());
	}
}
