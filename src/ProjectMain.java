import br.upe.basileiro.data.RabbitConnection;
import br.upe.basileiro.data.TwitterConnection;
import br.upe.basileiro.data.TwitterStreamingGateway;

public class ProjectMain {

	public static void main(String[] args) {	
		
		RabbitConnection rabbit = new RabbitConnection("localhost");
		
		TwitterConnection twitterConnection = new TwitterConnection();
		
		TwitterStreamingGateway tsg = new TwitterStreamingGateway(
				twitterConnection.getTwitterStreamConnection(),
				rabbit.getChannel("twitter-copa"),
				"twitter-copa");

		tsg.getStreamTweets(new double[][]{new double[]{-126.562500,30.448674}, new double[]{-61.171875,44.087585}}, new String[]{"pt"}, new String[]{"Neymar", "Brasil", "Hexa"});
	}
	
}
