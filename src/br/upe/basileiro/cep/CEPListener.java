package br.upe.basileiro.cep;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;
import com.espertech.esper.event.map.MapEventBean;

import br.upe.basileiro.models.Tweet;
import br.upe.basileiro.models.UnitData;

public class CEPListener implements UpdateListener {

	public void update(EventBean[] data1, EventBean[] data2) {

		try {
			
			if(data1 == null) {
				return;
			}
			
			System.out.println("Match - data1 = " + data1.length);
			
			for (int i = 0; i < data1.length; i++) {
				Object o = data1[i].getUnderlying();
				
				if(o instanceof Tweet) {
					//Log.log(Metrics.verboseCEP, "New taxi...");
					
					Tweet tt = (Tweet)o;
					System.out.println(tt.getMessage());
					
					
				} else if(o instanceof Map) {
					MapEventBean map = (MapEventBean) data1[i];
					Map<String, Object> events = map.getProperties();
					
					Set<String> keys = events.keySet();
					Iterator<String> iKeys = keys.iterator();
					
					while(iKeys.hasNext()) {
						String key = iKeys.next();
						Object aux = events.get(key);
						System.out.println("Match - " + key + "," + aux.toString());
					}
		
					//System.out.println(events.get("count(*)"));
					//System.out.println(events.get("QTD"));
					
				 } else {
					 System.out.println(o.toString());
				 }
				
			}
			

		} catch (Throwable e) {
			System.err.println(e.getMessage());
		}
	}
}
