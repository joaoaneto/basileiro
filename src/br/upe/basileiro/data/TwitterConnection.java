package br.upe.basileiro.data;

import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterConnection {

	//Variables for OAuthentication
	private String consumerKey = "oYLOkcSOSydAuZp7RtMjQl4Pm";
	private String consumerSecret = "hYiz9TXpoW11Y3swcqkaSHgvJbU7DHVTnLUIGyAnLAIJVfw7jo";
	private String accessToken = "99114543-9KWBa1KcgtwYMU3ZuTlORgc8v6h2n3EiDtgAerx4C";
	private String accessTokenSecret = "ks5ygCuRcjt9bXGY80jbTYqny5oNON5sPEhIbPBur8Mnq";
	
	private Twitter twitter;
	private TwitterStream twitterStream;
	
	public TwitterConnection() {
		this.configureConnection();
	}
	
	public void configureConnection() {
		ConfigurationBuilder cb = new ConfigurationBuilder();
		
		cb.setOAuthConsumerKey(this.consumerKey);
		cb.setOAuthConsumerSecret(this.consumerSecret);
		cb.setOAuthAccessToken(this.accessToken);
		cb.setOAuthAccessTokenSecret(this.accessTokenSecret);
		
		Configuration config = cb.build();
		
		TwitterFactory tf = new TwitterFactory(config);
		TwitterStreamFactory tsf = new TwitterStreamFactory(config);
		
		this.twitter = tf.getInstance();
		this.twitterStream = tsf.getInstance();
	}
	
	public TwitterStream getStreamConnection() {
		return this.twitterStream;
	}

}
