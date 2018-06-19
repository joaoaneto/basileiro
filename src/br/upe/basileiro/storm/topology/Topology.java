package br.upe.basileiro.storm.topology;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.topology.TopologyBuilder;

import br.upe.basileiro.storm.bolt.CEPBolt;
import br.upe.basileiro.storm.bolt.CommentClusterBolt;
import br.upe.basileiro.storm.bolt.CommentFilterBolt;
import br.upe.basileiro.storm.bolt.TweetClusterBolt;
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
		
		String[] politicsKeywordSet = {
				"governo",
				"eleicoes",
				"Brasil",
				"politica",
				"deputado",
				"presidente",
				"corrupcao",
				"esquerda",
				"direita",
		};
		
		TopologyBuilder builder = new TopologyBuilder();
		
		builder.setSpout("twitter-politics-spout", new TwitterSpout(politicsKeywordSet, "politics-tweets"));

		builder.setSpout("youtube-cup-spout", new YoutubeSpout(worldCupKeywordSet, "cup-comments"));

		builder.setBolt("tweet-filter-bolt", new TweetFilterBolt(politicsKeywordSet))
			.shuffleGrouping("twitter-politics-spout");

		builder.setBolt("comment-filter-bolt", new CommentFilterBolt())
			.shuffleGrouping("youtube-cup-spout");

		builder.setBolt("tweet-cluster-bolt", new TweetClusterBolt())
			.shuffleGrouping("tweet-filter-bolt");

		builder.setBolt("comment-cluster-bolt", new CommentClusterBolt())
		.shuffleGrouping("comment-filter-bolt");

		builder.setBolt("cep-bolt", new CEPBolt())
			.shuffleGrouping("tweet-cluster-bolt")
			.shuffleGrouping("comment-cluster-bolt");

		LocalCluster local = new LocalCluster();

		try {
			local.submitTopology("basileiro", config, builder.createTopology());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
