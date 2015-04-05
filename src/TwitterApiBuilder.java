import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterApiBuilder {

	public static Twitter getTwitter() {
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true)
				.setOAuthConsumerKey("FJBhQkiX2u9YktlqZwjwbdPyL")
				.setOAuthConsumerSecret(
						"ursyON7h1sw3oz6E17sPSxe59nE0MVhUyjnD5dvTANjRWV16cQ")
				.setOAuthAccessToken(
						"2659317550-PrW3k7aoK6M6tIRASIrtph5Jbqzgwi5YmZQU4vM")
				.setOAuthAccessTokenSecret(
						"1NQAqS6hxuLisJ6AHYTVPJmmWuhy3Y12iWRhWBdpMZ5bp");
		TwitterFactory tf = new TwitterFactory(cb.build());
		Twitter twitter = tf.getInstance();
		return twitter;
	}

}
