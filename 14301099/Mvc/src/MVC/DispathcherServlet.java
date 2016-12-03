package MVC;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sun.org.apache.bcel.internal.generic.NEW;

public class DispathcherServlet extends HttpServlet{
	
	private static HashMap<String, Target> map = new HashMap<>();

	public DispathcherServlet(){
		
	}
	
	@Override
	protected void service(HttpServletRequest arg0, HttpServletResponse arg1) throws ServletException, IOException {

		String Tomcat  = System.getProperty("catalina.home");
		
		File dir = new File(Tomcat+"/webapps/Mvc/WEB-INF/classes/test");//ContorllerµÄ²âÊÔ
		
		for(File f:dir.listFiles()){
			String className = f.getName();
			
			Class c = null;
			
			try {
				String basePackage = "test.";
				c = Class.forName(basePackage+className.substring(0,className.length()-6));
				if(c.isAnnotationPresent(Controller.class)){
					Object object = c.newInstance();
					for(Method m:c.getDeclaredMethods()){
						if(m.isAnnotationPresent(RequestMapping.class)){
							Target target = new Target();
							target.setMethod(m);
							target.setTarget(object);
							RequestMapping rm = (RequestMapping)m.getAnnotation(RequestMapping.class);
							map.put(rm.value(), target);
						}
					}
				}
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		// TODO Auto-generated method stub
		Target target = (Target)map.get(arg0.getServletPath());
		if(target==null){
			return;
		}
		ModelAndView mav = new ModelAndView();
		Map m = arg0.getParameterMap();
		for(String key:(Set<String>)m.keySet()){
			String[] values = (String[]) m.get(key);
			for(String value:values){
				mav.addObject(key, value);
			}
		}
		Object result = null;
		try {
			 result= target.getMethod().invoke(target.getTarget(), mav);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(result instanceof ModelAndView){
			mav = (ModelAndView) result;
			for(String p:mav.getParameters()){
				arg0.setAttribute(p, mav.getMap(p));
			}
		}
		arg0.getRequestDispatcher(mav.getViewName()+".jsp").forward(arg0, arg1);
	}
	
	
}
