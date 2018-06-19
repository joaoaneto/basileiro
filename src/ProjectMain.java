import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;

import com.abinj.twittersentimentanalysis.TweetWithSentiment;
import com.abinj.twittersentimentanalysis.analyzer.SentimentAnalyzer;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.translate.Translate;
import com.google.api.services.translate.model.TranslationsListResponse;
import com.google.api.services.translate.model.TranslationsResource;
import com.gtranslate.Language;
import com.gtranslate.Translator;

import br.upe.basileiro.data.RabbitConnection;
import br.upe.basileiro.data.TwitterConnection;
import br.upe.basileiro.data.TwitterStreamingGateway;
import br.upe.basileiro.data.YoutubeConnection;

public class ProjectMain {

	private static SentimentAnalyzer sentimentAnalyzer = new SentimentAnalyzer();
	
	public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException, GeneralSecurityException {	
		
//		RabbitConnection rabbit = new RabbitConnection("localhost");
		
//		TwitterConnection twitter = new TwitterConnection();
//		YoutubeConnection youtube = new YoutubeConnection("basileiro-dev");
		
//		TwitterStreamingGateway tsg = new TwitterStreamingGateway(
//				twitter.getStreamConnection(),
//				rabbit.getChannel("twitter-copa"),
//				"twitter-copa");

//		YoutubeGateway youtubeGw = new YoutubeGateway(
//				youtube.getConnection(),
//				"AIzaSyC2z31-8V5F2QYk64K6z9QV3HFIKuh2_9s",
//				rabbit.getChannel("youtube-politica"),
//				"youtube-politica");
//	
//		//tsg.getStreamTweets(new double[][]{new double[]{-126.562500,30.448674}, new double[]{-61.171875,44.087585}}, new String[]{"pt"}, new String[]{"Neymar", "Brasil", "Hexa"});
//		//youtubeGw.search("Messi");
//		
//		youtubeGw.getComments("Messi");
		
//		Translator translate = Translator.getInstance();
//		String text = translate.translate("Eu sou programador", Language.PORTUGUESE, Language.ENGLISH);
//		
//		System.out.println(text);

//		
			TweetWithSentiment tweetWithSentiment = sentimentAnalyzer.findSentiment("World cup is total futility");
			if(tweetWithSentiment != null) {
				System.out.println(tweetWithSentiment.toString());
			}

	}
	
}
