package br.upe.basileiro.storm.topology;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.topology.TopologyBuilder;

import br.upe.basileiro.storm.bolt.CommentFilterBolt;
import br.upe.basileiro.storm.bolt.ClusteringBolt;
import br.upe.basileiro.storm.bolt.TweetFilterBolt;
import br.upe.basileiro.storm.spout.TwitterSpout;
import br.upe.basileiro.storm.spout.YoutubeSpout;

public class Topology {
	
	public static void main(String[] args) {
		Config config = new Config();
		
		String[] worldCupKeywordSet = {
				"brasil",
				"selecao",
				"brasileira",
				"hexa",
				"copa",
				"futebol",
				"russia"
		};
		
		TopologyBuilder builder = new TopologyBuilder();
		
		builder.setSpout("twitter-cup-spout", new TwitterSpout(worldCupKeywordSet, "cup-tweets"));
//		builder.setSpout("twitter-politics-spout", new TwitterSpout(politicsKeywordSet, "politics-tweets"));
//
		builder.setSpout("youtube-cup-spout", new YoutubeSpout(worldCupKeywordSet, "cup-comments"));
//		builder.setSpout("youtube-politics-spout", new YoutubeSpout());
//		
		
		builder.setBolt("tweet-filter-bolt", new TweetFilterBolt(worldCupKeywordSet))
			.shuffleGrouping("twitter-cup-spout");

		builder.setBolt("comment-filter-bolt", new CommentFilterBolt())
			.shuffleGrouping("youtube-cup-spout");

		builder.setBolt("printer-bolt", new ClusteringBolt())
			.shuffleGrouping("tweet-filter-bolt")
			.shuffleGrouping("comment-filter-bolt");
		
		LocalCluster local = new LocalCluster();

		try {
			local.submitTopology("basileiro", config, builder.createTopology());
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
	
}
