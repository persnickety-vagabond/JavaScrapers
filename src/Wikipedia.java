import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class Wikipedia {

	public static boolean isPerson(String name) {

		// URL encode the name
		String encoded = null;
		try {
			encoded = URLEncoder.encode(name, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		// Query the Wikipedia API
		URL site = null;
		try {
			site = new URL(
					"http://en.wikipedia.org/w/api.php?format=json&action=query&titles="
							+ encoded + "&prop=revisions&rvprop=content");
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		}
		BufferedReader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(site.openStream()));
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		// Look for personData property
		String inputLine;
		try {
			while ((inputLine = in.readLine()) != null) {
				// System.out.println(inputLine);
				if (inputLine.contains("Persondata")) {
					return true;
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return false;
	}

	public static String getSummary(String name) {
		// URL encode the name
		String encoded = null;
		try {
			encoded = URLEncoder.encode(name, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		// Query the Wikipedia API
		URL site = null;
		try {
			site = new URL(
					"https://en.wikipedia.org/w/api.php?format=json&action=query&prop=extracts&exintro=&explaintext=&titles="
							+ encoded + "&continue=");
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		}
		BufferedReader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(site.openStream()));
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		// Look for personData property
		String inputLine = null;
		String description = null;
		try {
			while ((inputLine = in.readLine()) != null) {
				String[] s = inputLine.split("\"extract\":\"");
				s = s[1].split("\"}}}}");
				description = s[0];
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return description;
	}
}
