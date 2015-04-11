import facebook4j.Facebook;
import facebook4j.FacebookFactory;
import facebook4j.conf.ConfigurationBuilder;


public class FacebookApiBuilderExample {

	public static Facebook getFacebook() {
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true)
				.setOAuthAppId("***************")
				.setOAuthAppSecret("******************")
				.setOAuthAccessToken(
						"********************|********************");
		FacebookFactory ff = new FacebookFactory(cb.build());
		Facebook facebook = ff.getInstance();
		return facebook;
	}
}
