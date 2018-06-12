package br.upe.basileiro.data;

import com.rabbitmq.client.Channel;
import twitter4j.FilterQuery;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterStream;

public class TwitterStreamingGateway {
	
	private static String queueName;
	private TwitterStream twitterStream;
	private Channel rabbitChannel;

	public TwitterStreamingGateway(TwitterStream twitterStream, Channel rabbitChannel, String queueName) {
		this.twitterStream = twitterStream;
		this.rabbitChannel = rabbitChannel;
		TwitterStreamingGateway.queueName = queueName;
		this.addListener();
	}
	
	public void getStreamTweets(double [][]locations, String[] language, String[] keywords) {
		FilterQuery tweetFilterQuery = new FilterQuery();
		tweetFilterQuery.locations(locations);
		tweetFilterQuery.language(language);
		tweetFilterQuery.track(keywords);
		
		this.twitterStream.filter(tweetFilterQuery);
	}
	
	
	public void addListener() {
		StatusListener listener = new StatusListener() {
			
			@Override
			public void onException(Exception arg0) {
				
			}
			
			@Override	
			public void onTrackLimitationNotice(int arg0) {
				
			}
			
			@Override
			public void onStatus(Status status) {
				try {	
					rabbitChannel.basicPublish("", queueName, null, status.getText().getBytes());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			@Override
			public void onStallWarning(StallWarning arg0) {
				
			}
			
			@Override
			public void onScrubGeo(long arg0, long arg1) {
				
			}
			
			@Override
			public void onDeletionNotice(StatusDeletionNotice arg0) {
				
			}
		};
		
		this.twitterStream.addListener(listener);
	}
	
}
