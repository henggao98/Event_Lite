package uk.ac.man.cs.eventlite.config;

import org.springframework.context.annotation.Configuration;

import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

@Configuration
public class Twitter4j {
	
	public static Twitter configureTwitter4j() {
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true)
		  .setOAuthConsumerKey("530nBLWc1cBN5fDBc4gKfnOll")
		  .setOAuthConsumerSecret("6bHBhjljRzzdV8G4QXirQmPna9lRf1MwUvwWvj58tuI73UvUrk")
		  .setOAuthAccessToken("1253692184097886208-sGe3m0j8PbVFHvJ8LNQVEuyUSdhA1g")
		  .setOAuthAccessTokenSecret("z3JQ23wXWvELddAKQgiDXzA6uHdXCG294XqHAMysFEqAb");
		TwitterFactory tf = new TwitterFactory(cb.build());
		Twitter twitter = tf.getInstance();
		return twitter;
	}
}