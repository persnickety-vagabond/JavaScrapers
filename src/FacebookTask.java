import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;

import facebook4j.Facebook;
import facebook4j.FacebookException;
import facebook4j.Page;
import facebook4j.ResponseList;

public class FacebookTask {

	public static void scrape() {

		// Read in the JSON people file
		JsonObject data = null;
		try {
			data = JsonObject.readFrom(FileUtils.readFileToString(new File(
					"living_celebrities_with_twitter.json")));
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Read in results
		JsonObject finished = null;
		try {
			finished = JsonObject.readFrom(FileUtils.readFileToString(new File(
					"living_celebrities_with_twitter_and_facebook.json")));
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Look each person up in Facebook Graph API
		Facebook fb = FacebookApiBuilder.getFacebook();
		JsonArray people = data.get("people").asArray();

		// Start at Allen Covert
		int startAt = 0;
		for (startAt = 0; startAt < people.size(); startAt++) {
			if (people.get(startAt).asObject().get("fullName").asString()
					.contentEquals("Leo Laporte")) {
				break;
			}
		}

		for (int i = startAt; i < people.size(); i++) {

			// Get a person
			JsonObject person = people.get(i).asObject();

			// Create empty facebook object
			JsonObject facebook = new JsonObject();

			// Store full name
			facebook.add("fullName", person.get("fullName").asString());
			System.out.println("\nNAME:\t" + person.get("fullName").asString()
					+ "\t\t\tPERSON #:\t" + (i + 1) + " of " + people.size());

			// Perform search query on full name
			ResponseList<Page> query = null;
			try {
				query = fb.searchPages(facebook.get("fullName").asString());
			} catch (FacebookException e2) {
				e2.printStackTrace();
			}

			// Loop over search results, storing the non-community pages
			JsonArray pages = new JsonArray();
			for (Page result : query) {

				Page fbPage = null;
				try {
					fbPage = fb.getPage(result.getId());
				} catch (FacebookException e1) {
					e1.printStackTrace();
					continue;
				}
				if (!fbPage.isCommunityPage()
						&& fbPage
								.getName()
								.toLowerCase()
								.contains(
										facebook.get("fullName").asString()
												.toLowerCase())) {

					JsonObject page = new JsonObject();
					page.add("url", fbPage.getLink().toString());
					page.add("pageName", fbPage.getName());
					page.add("likes", fbPage.getLikes());
					page.add("talkingAbout", fbPage.getTalkingAboutCount());
					page.add("wereHere", fbPage.getWereHereCount());
					page.add("id", result.getId());

					pages.add(page);

					System.out.println(pages.size() + "\t"
							+ page.get("pageName").asString());

				}

				// Sleep for a second and a half to avoid rate limiting
				try {
					Thread.sleep(1500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			// add pages object to person's facebook object
			facebook.add("pages", pages);

			// add facebook object onto person
			person.add("facebook", facebook);

			// replace person in people array
			data.get("people").asArray().set(i, person);

			// save new data
			try {
				FileUtils.writeStringToFile(new File(
						"living_celebrities_with_twitter_and_facebook.json"),
						data.toString());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
}
