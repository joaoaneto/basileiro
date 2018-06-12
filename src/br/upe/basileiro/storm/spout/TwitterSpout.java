package br.upe.basileiro.storm.spout;

import java.io.IOException;
import java.util.Map;

import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.IRichSpout;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import br.upe.basileiro.data.RabbitConnection;
import br.upe.basileiro.data.TwitterConnection;
import br.upe.basileiro.data.TwitterStreamingGateway;

public class TwitterSpout implements IRichSpout {
	
	private SpoutOutputCollector collector;
	private Channel channel;
	
	@Override
	public void open(Map arg0, TopologyContext arg1, SpoutOutputCollector arg2) {
		this.collector = arg2;
		
		RabbitConnection rabbit = new RabbitConnection("localhost");
		
		this.channel = rabbit.getChannel("twitter-copa");
		
		TwitterConnection twitterConnection = new TwitterConnection();
		
		TwitterStreamingGateway tsg = new TwitterStreamingGateway(
				twitterConnection.getTwitterStreamConnection(),
				this.channel,
				"twitter-copa");

		tsg.getStreamTweets(new double[][]{new double[]{-126.562500,30.448674}, new double[]{-61.171875,44.087585}}, new String[]{"pt"}, new String[]{"Neymar", "Brasil", "Hexa"});
	}

	@Override
	public void nextTuple() {
		Consumer consumer = new DefaultConsumer(channel) {
			  @Override
			  public void handleDelivery(String consumerTag, Envelope envelope,
			                             AMQP.BasicProperties properties, byte[] body)
			      throws IOException {
			    String message = new String(body, "UTF-8");
			    
			    collector.emit(new Values(message));
			  }
			};
			try {
				channel.basicConsume("twitter-copa", true, consumer);
			} catch (IOException e) {
				e.printStackTrace();
			}
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("tweet"));
	}
	
	@Override
	public void ack(Object arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void activate() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deactivate() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void fail(Object arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public Map<String, Object> getComponentConfiguration() {
		// TODO Auto-generated method stub
		return null;
	}

}
