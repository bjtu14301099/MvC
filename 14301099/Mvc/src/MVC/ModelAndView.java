package MVC;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.sun.xml.internal.ws.config.management.policy.ManagementPrefixMapper;

public class ModelAndView {
	
	Map<String,Object> map = new HashMap<String,Object>();
	
	String viewName;


	public void setViewName(String string) {
		// TODO Auto-generated method stub
		viewName = string;
		
	}
	
	public String getViewName(){
		return viewName;
	}

	public Object getMap(String string) {
		// TODO Auto-generated method stub
		return map.get(string);
	}

	public void addObject(String string, Object map) {
		// TODO Auto-generated method stub
		this.map.put(string, map);
	}
	
	public Set<String> getParameters(){
		return map.keySet();
	}

}
