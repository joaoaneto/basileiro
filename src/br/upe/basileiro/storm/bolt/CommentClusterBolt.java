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
import br.upe.basileiro.models.Comment;

public class CommentClusterBolt implements IRichBolt {
	
	private static final long serialVersionUID = 1L;
	private OutputCollector collector;

	@Override
	public void prepare(Map arg0, TopologyContext arg1, OutputCollector arg2) {
		this.collector = arg2;
	}
	
	@Override
	public void execute(Tuple input) {
		Comment comment = (Comment) SerializationUtils.deserialize(input.getBinaryByField("comment"));
		String type = input.getStringByField("type");
		
		System.out.println(comment.getMessage());
		
		SentimentAnalyzer sentimentAnalyzer = new SentimentAnalyzer();
		
		GoogleTranslate gt = new GoogleTranslate();
		
	    TweetWithSentiment commentWithSentiment = sentimentAnalyzer.findSentiment(gt.translate(comment.getMessage()));
		if(commentWithSentiment != null) {
			comment.setClassification(commentWithSentiment.getCssClass());
			this.collector.emit(new Values(SerializationUtils.serialize(comment), type));
		}
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("comment", "type"));
	}

	@Override
	public void cleanup() {}

	@Override
	public Map<String, Object> getComponentConfiguration() {
		return null;
	}
	
}
