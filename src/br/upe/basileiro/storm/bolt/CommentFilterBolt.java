package br.upe.basileiro.storm.bolt;

import java.text.Normalizer;
import java.util.Map;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.IRichBolt;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

public class CommentFilterBolt implements IRichBolt {
	
	private static final long serialVersionUID = 1L;
	
	private OutputCollector collector;

	@Override
	public void prepare(Map arg0, TopologyContext arg1, OutputCollector arg2) {
		this.collector = arg2;
	}
	
	@Override
	public void execute(Tuple input) {
		String comment = input.getStringByField("comment");
		String filteredComment;

		/* Step 1. Normalize and remove special characters from string  */
		filteredComment = Normalizer
				.normalize(comment, Normalizer.Form.NFD)
				.replaceAll("[^a-zA-Z0-9\\s+]", "");
		
		/* Step 2. Convert all characters to lower case */
		filteredComment = filteredComment.toLowerCase();

		if(!filteredComment.isEmpty()) {
			this.collector.emit(new Values(filteredComment));
		}
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("data"));
	}

	@Override
	public void cleanup() {}

	@Override
	public Map<String, Object> getComponentConfiguration() {
		return null;
	}
	
}
