package br.upe.basileiro.storm.bolt;

import java.util.Map;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.IRichBolt;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Tuple;

public class PrintTweetBolt implements IRichBolt {
	
	private static final long serialVersionUID = 1L;
	
	@Override
	public void prepare(Map arg0, TopologyContext arg1, OutputCollector arg2) {}
	
	@Override
	public void execute(Tuple input) {
		String tweet = input.getStringByField("tweet");
		
		System.out.println(tweet);
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer arg0) {}

	@Override
	public void cleanup() {}

	@Override
	public Map<String, Object> getComponentConfiguration() {
		return null;
	}
	
}
