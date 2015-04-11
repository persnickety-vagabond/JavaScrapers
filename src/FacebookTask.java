import com.eclipsesource.json.JsonObject;

import facebook4j.Facebook;
import facebook4j.FacebookException;
import facebook4j.FacebookFactory;
import facebook4j.Page;
import facebook4j.ResponseList;
import facebook4j.conf.ConfigurationBuilder;

public class FacebookTask {

	public static void scrape() {
		Facebook facebook = FacebookApiBuilder.getFacebook();

		try {

			// perform search query
			ResponseList<Page> pages = facebook.searchPages("Katy Perry");
			JsonObject pagesObj = new JsonObject();
			
			// loop over results, finding non-community pages
			for (Page page : pages) {

				Page fbPage = facebook.getPage(page.getId());
				if (!fbPage.isCommunityPage()) {
					
					
					System.out.println("Url: " + fbPage.getLink());
					System.out.println("Page: " + fbPage.getName());
					System.out.println("Likes: " + fbPage.getLikes());
					System.out.println("Talking about: "
							+ fbPage.getTalkingAboutCount());
					System.out.println("Were here: "
							+ fbPage.getWereHereCount());
				}

				// Sleep for a second and a half to avoid rate limiting
				try {
					Thread.sleep(1500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		} catch (FacebookException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
