import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterApiBuilderExample {

	public static Twitter getTwitter() {
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true)
				.setOAuthConsumerKey("*****************")
				.setOAuthConsumerSecret(
						"***************************************************")
				.setOAuthAccessToken(
						"***************************************************")
				.setOAuthAccessTokenSecret(
						"*********************************************");
		TwitterFactory tf = new TwitterFactory(cb.build());
		Twitter twitter = tf.getInstance();
		return twitter;
	}

}
