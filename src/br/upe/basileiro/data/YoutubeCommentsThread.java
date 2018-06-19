package br.upe.basileiro.data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.storm.shade.org.apache.commons.lang.SerializationUtils;

import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.CommentSnippet;
import com.google.api.services.youtube.model.CommentThread;
import com.google.api.services.youtube.model.CommentThreadListResponse;
import com.google.api.services.youtube.model.ResourceId;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.rabbitmq.client.Channel;

import br.upe.basileiro.models.Comment;

public class YoutubeCommentsThread extends Thread {

    private YouTube youtube;
    private String apiKey;
    private Channel rabbitChannel;
    private String queueName;
    private String query;
    
    private ArrayList<Comment> comments = new ArrayList<Comment>();
    
    private static final long NUMBER_OF_VIDEOS_RETURNED = 25;

    public YoutubeCommentsThread(YouTube youtube, String apiKey,  Channel rabbitChannel, String queueName, String query) {
    	this.youtube = youtube;
    	this.apiKey = apiKey;
    	this.rabbitChannel = rabbitChannel;
    	this.queueName = queueName;
    	this.query = query;
    }

	@Override
	public void run() {
		/* Pooling */
		while(true) {			
			this.getComments(this.query);		
		}
	}
	
    public List<SearchResult> search(String query) {
        YouTube.Search.List search = null;
        List<SearchResult> searchResultList = null;
        
		try {
			search = this.youtube.search().list("id,snippet");
			
	        search.setKey(this.apiKey);
	        search.setQ(query);
	        search.setType("video");
	        search.setFields("items(id/kind,id/videoId,snippet/title,snippet/thumbnails/default/url)");
	        search.setMaxResults(NUMBER_OF_VIDEOS_RETURNED);
			
	        SearchListResponse searchResponse = search.execute();
	        searchResultList = searchResponse.getItems();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return searchResultList;
    }
	
    /* Polling getComments() */
    public void getComments(String query) {    	
    	List<SearchResult> searchResultList = search(query);
    	Iterator<SearchResult> iteratorSearchResults = searchResultList.iterator();
    	CommentThreadListResponse videoCommentsListResponse;
		
    	while (iteratorSearchResults.hasNext()) {
            SearchResult singleVideo = iteratorSearchResults.next();
            ResourceId rId = singleVideo.getId();
            
            try {
    			videoCommentsListResponse = this.youtube.commentThreads()
    			        .list("snippet").setVideoId(rId.getVideoId()).setTextFormat("plainText")
    			        .setKey(this.apiKey).execute();
    	        List<CommentThread> videoComments = videoCommentsListResponse.getItems();
    	        
    	        if (videoComments.isEmpty()) {
    	                System.out.println("Can't get video comments.");
	            } else {
	                for (CommentThread videoComment : videoComments) {
	                    CommentSnippet snippet = videoComment.getSnippet().getTopLevelComment()
	                            .getSnippet();
	                    	                    
	                    Comment comment = new Comment(snippet.getTextOriginal(),
	                    							  snippet.getPublishedAt(),
	                    							  snippet.getLikeCount());
	                    
	                    if(!this.comments.contains(comment)) {
	                    	this.comments.add(comment);
							this.rabbitChannel.basicPublish("", this.queueName, null, SerializationUtils.serialize(comment));
	                    }    
	                }
	            }
    		} catch (IOException e) {
    			e.printStackTrace();
    		}           
        }
    }
	
}
