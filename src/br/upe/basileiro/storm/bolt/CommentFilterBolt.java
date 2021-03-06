package br.upe.basileiro.storm.bolt;

import java.text.Normalizer;
import java.util.Map;

import org.apache.storm.shade.org.apache.commons.lang.SerializationUtils;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.IRichBolt;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

import br.upe.basileiro.models.Comment;

public class CommentFilterBolt implements IRichBolt {
	
	private static final long serialVersionUID = 1L;
	
	private OutputCollector collector;

	@Override
	public void prepare(Map arg0, TopologyContext arg1, OutputCollector arg2) {
		this.collector = arg2;
	}
	
	@Override
	public void execute(Tuple input) {
//		String comment = input.getStringByField("comment");
		Comment comment = (Comment) SerializationUtils.deserialize(input.getBinaryByField("comment"));
		String filteredComment;

		/* Step 1. Normalize and remove special characters from string  */
		filteredComment = Normalizer
				.normalize(comment.getMessage(), Normalizer.Form.NFD)
				.replaceAll("[^a-zA-Z0-9\\s+]", "");
		
		/* Step 2. Convert all characters to lower case */
		filteredComment = filteredComment.toLowerCase();

		if(!filteredComment.isEmpty()) {
			comment.setMessage(filteredComment);
			this.collector.emit(new Values(SerializationUtils.serialize(comment), "cup"));
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
