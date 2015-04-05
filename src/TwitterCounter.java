import java.net.*;
import java.io.*;

public class TwitterCounter {

	public static void scrapeTopPages() {
		int numScraped = 0;

		String url = "http://twittercounter.com/pages/100/";
		int page = 0;

		// start scraping at page 0
		while (page < 1000) {

			// open stream to list page
			URL site = null;
			try {
				site = new URL(url + page);
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
			String inputLine;

			try {
				while ((inputLine = in.readLine()) != null) {
					if (inputLine.contains("element: 'Full name' });")) {

						// get twitter handle
						String[] th = inputLine.split("name\" href=\"/");
						th = th[1].split("\" onclick");
						String twitterHandle = th[0];
						System.out.println(twitterHandle);

						// get full name
						String[] fn = inputLine.split("\">");
						fn = fn[1].split("</a>");
						String fullName = fn[0];
						System.out.println(fullName);

						// open stream to detail page
						URL details = new URL("http://twittercounter.com/"
								+ twitterHandle);
						BufferedReader in2 = new BufferedReader(
								new InputStreamReader(details.openStream()));
						String line;
						String twitterId = null;
						while ((line = in2.readLine()) != null) {
							if (line.contains("to_twitter_id")) {
								line = in2.readLine(); // get next line

								// get twitter_id
								String[] id = line.split("=\"");
								id = id[1].split("\"");
								twitterId = id[0];
								System.out.println(twitterId);

								// close the connection to detail page
								in2.close();
								break;
							}
						}

						// save result string
						String result = "@" + twitterHandle + ":"
								+ fullName.toLowerCase() + ":" + twitterId;
						try {
							PrintWriter out = new PrintWriter(
									new BufferedWriter(new FileWriter(
											"twittercounter.txt", true)));
							out.println(result);
							out.close();
							System.out.println("Scraped: " + ++numScraped);
						} catch (IOException e) {
							System.out.println("asdf");
						}

					}
				}
				// close list page connection.
				in.close();

				// increment to next list page
				page = page + 100;
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

	public static void scrapeById(String id) {
		
	}
}
