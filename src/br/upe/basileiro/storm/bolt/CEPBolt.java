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

import com.espertech.esper.client.Configuration;
import com.espertech.esper.client.EPAdministrator;
import com.espertech.esper.client.EPRuntime;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;

import br.upe.basileiro.cep.CEPListener;
import br.upe.basileiro.models.Comment;
import br.upe.basileiro.models.Tweet;
import br.upe.basileiro.models.UnitData;

public class CEPBolt implements IRichBolt {
	
	private EPRuntime cep;
	
	private static final long serialVersionUID = 1L;

	@Override
	public void prepare(Map arg0, TopologyContext arg1, OutputCollector arg2) {
		this.cep = this.cepConfigure();
	}
	
	@Override
	public void execute(Tuple input) {
		Tweet tweet = null;
		Comment comment = null;
	
		String type = input.getStringByField("type");
		
		if(input.contains("tweet")) {
			byte[] tweetBytes = input.getBinaryByField("tweet");
			tweet = (Tweet) SerializationUtils.deserialize(tweetBytes);
//			System.out.println("---- Type Data = " + type + " ----");
//			System.out.println("---- Classification Data = " + tweet.getClassification() + " ----");
//			System.out.println("---- Favorites = " + tweet.getFavoriteCount() + " ----");
//			System.out.println("---- Date = " + tweet.getDate() + " ----");
			this.cep.sendEvent(tweet);
		}
		
		if(input.contains("comment")) {
			byte[] commentBytes = input.getBinaryByField("comment");
			comment = (Comment) SerializationUtils.deserialize(commentBytes);
			//System.out.println(comment.getMessage());
		}

	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {}

	@Override
	public void cleanup() {}

	@Override
	public Map<String, Object> getComponentConfiguration() {
		return null;
	}
	
	public EPRuntime cepConfigure() {
		EPRuntime cepRT = null;
		
		try {
			Configuration cepConfig = new Configuration();

			cepConfig.addEventType("tweet", Tweet.class.getName());
			cepConfig.addEventType("comment", Comment.class.getName());

			EPServiceProvider cep = EPServiceProviderManager.getProvider("CEPEngine", cepConfig);

			EPAdministrator cepAdm = cep.getEPAdministrator();
			EPStatement cepStatement = cepAdm.createEPL("select * from tweet");

			cepStatement.addListener(new CEPListener());

			cepRT = cep.getEPRuntime();
		} catch (Exception e) {
			e.printStackTrace();
		}	
		
		return cepRT;
	}
	
	
}
