package br.upe.basileiro.data;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class RabbitConnection {

	private Connection connection;
	private Channel channel;
	
	public RabbitConnection(String host) {
		this.createConnection(host);
	}
	
	public void createConnection(String host) {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(host);
		
		try {
			connection = factory.newConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Channel getChannel(String queueName) {
		try {
			channel = connection.createChannel();
			channel.queueDeclare(queueName, false, false, false, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return channel;
	}
}
