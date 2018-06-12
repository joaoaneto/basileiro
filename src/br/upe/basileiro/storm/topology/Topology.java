package br.upe.basileiro.storm.topology;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.topology.TopologyBuilder;

import br.upe.basileiro.storm.bolt.PrintTweetBolt;
import br.upe.basileiro.storm.spout.TwitterSpout;

public class Topology {
	
	public static void main(String[] args) {
		Config config = new Config();
		
		TopologyBuilder builder = new TopologyBuilder();
		
		builder.setSpout("twitter-spout", new TwitterSpout());

		builder.setBolt("twitter-bolt", new PrintTweetBolt()).shuffleGrouping("twitter-spout");
		
		LocalCluster local = new LocalCluster();

		try {
			local.submitTopology("basileiro", config, builder.createTopology());
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
	
}
