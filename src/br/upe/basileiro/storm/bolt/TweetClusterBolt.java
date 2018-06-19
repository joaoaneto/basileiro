package br.upe.basileiro.storm.bolt;

import java.util.Map;

import org.apache.storm.shade.org.apache.commons.lang.SerializationUtils;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.IRichBolt;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

import com.abinj.twittersentimentanalysis.TweetWithSentiment;
import com.abinj.twittersentimentanalysis.analyzer.SentimentAnalyzer;

import br.upe.basileiro.data.GoogleTranslate;
import br.upe.basileiro.models.Tweet;

public class TweetClusterBolt implements IRichBolt {
	
	private static final long serialVersionUID = 1L;
	private OutputCollector collector;

	@Override
	public void prepare(Map arg0, TopologyContext arg1, OutputCollector arg2) {
		this.collector = arg2;
	}
	
	@Override
	public void execute(Tuple input) {
		Tweet tweet = (Tweet) SerializationUtils.deserialize(input.getBinaryByField("tweet"));

		String type = input.getStringByField("type");
				
		SentimentAnalyzer sentimentAnalyzer = new SentimentAnalyzer();
		
		GoogleTranslate gt = new GoogleTranslate();
		
	    TweetWithSentiment tweetWithSentiment = sentimentAnalyzer.findSentiment(gt.translate(tweet.getMessage()));
		if(tweetWithSentiment != null) {
			tweet.setClassification(tweetWithSentiment.getCssClass());
			this.collector.emit(new Values(SerializationUtils.serialize(tweet), type));
		}
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("tweet", "type"));
	}

	@Override
	public void cleanup() {}

	@Override
	public Map<String, Object> getComponentConfiguration() {
		return null;
	}
	
}
