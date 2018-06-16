package br.upe.basileiro.storm.bolt;

import java.text.Normalizer;
import java.util.Arrays;
import java.util.Map;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.IRichBolt;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

public class TweetFilterBolt implements IRichBolt {
	
	private static final long serialVersionUID = 1L;
	
	private OutputCollector collector;
	private String[] keywords;

	public TweetFilterBolt(String[] keywords) {
		this.keywords = keywords;
	}

	@Override
	public void prepare(Map arg0, TopologyContext arg1, OutputCollector arg2) {
		this.collector = arg2;
	}
	
	@Override
	public void execute(Tuple input) {
		String tweet = input.getStringByField("tweet");
		String filteredTweet;
		String[] wordsFromTweet;
		int presenceCounter = 0;

		/* Step 1. Normalize and remove special characters from string  */
		filteredTweet = Normalizer
				.normalize(tweet, Normalizer.Form.NFD)
				.replaceAll("[^a-zA-Z0-9\\s+]", "");
		
		/* Step 2. Convert all characters to lower case */
		filteredTweet = filteredTweet.toLowerCase();
		
		/* Step 3. Verify if the majority data is useful for application purpose */
		wordsFromTweet = filteredTweet.split("\\s+");
		for(String word: wordsFromTweet) {
			if(Arrays.stream(this.keywords).anyMatch(word::equals)) {
				presenceCounter++;
			}
		}
		
		if(presenceCounter >= 2) {
			this.collector.emit(new Values(filteredTweet));
			presenceCounter = 0;
		}
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("tweet"));
	}

	@Override
	public void cleanup() {}

	@Override
	public Map<String, Object> getComponentConfiguration() {
		return null;
	}
	
}
