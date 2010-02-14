package es.sidelab.bugzilla;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpState;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.XmlRpcRequest;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.apache.xmlrpc.client.XmlRpcClientException;
import org.apache.xmlrpc.client.XmlRpcCommonsTransportFactory;
import org.apache.xmlrpc.client.XmlRpcSunHttpTransport;
import org.apache.xmlrpc.client.XmlRpcSunHttpTransportFactory;
import org.apache.xmlrpc.client.XmlRpcTransport;
import org.apache.xmlrpc.client.XmlRpcTransportFactory;

public class Bugzilla {
	public static void main(String[] args) throws MalformedURLException,
			XmlRpcException {
		XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
		config.setServerURL(new URL(
				"http://labs.gavab.es/cgi-bin/bugzilla3/xmlrpc.cgi"));

		final XmlRpcClient client = new XmlRpcClient();
		client.setConfig(config);
		
		final HttpClient httpClient = new HttpClient();
        final XmlRpcCommonsTransportFactory factory = new XmlRpcCommonsTransportFactory(client);
        factory.setHttpClient(httpClient);
        client.setTransportFactory(factory);
        
//		XmlRpcTransportFactory factory = new XmlRpcSunHttpTransportFactory(client) {
//			  public XmlRpcTransport getTransport() {
//			    return new XmlRpcSunHttpTransport(client) {
//			      @Override protected void initHttpHeaders(XmlRpcRequest request) throws XmlRpcClientException {
//			        super.initHttpHeaders(request);
////			        setRequestHeader("Cookie", myLovelyCookieData);
//			      }
//			    };
//			  }
//			};
//			client.setTransportFactory(factory);

		Map map = new HashMap();
		map.put("login", "p.gortaza@gmail.com");
		map.put("password", "shakta1e");

		Map result = (Map) client.execute("User.login", new Object[] { map });
		System.out.println("Result = " + result);
		
        HttpState state = httpClient.getState();
        Cookie[] cookies = state.getCookies();
        for(Cookie c:cookies) {
        	StringBuilder sb = new StringBuilder();
        	sb.append(c.getDomain()+"\n");
        	sb.append(c.getName()+"\n");
        	sb.append(c.getPath()+"\n");
        	sb.append(c.getValue()+"\n");
        }
        
        Map bugGetParams = new HashMap();
        bugGetParams.put("ids", Arrays.asList(100, 101));
        
        result = (Map) client.execute("Bug.get", new Object[]{bugGetParams});
        Object[] bugs = (Object[]) result.get("bugs");
        for(Object o : bugs) {
        	Map bugMap = (Map) o;
        	System.out.println(bugMap);
        	Map a = (Map) bugMap.get("internals");
        	System.out.println(a.get("assigned_to") + ": " + bugMap.get("summary"));
        }
                
//        Map searchParams = new HashMap();
//		searchParams.put("assigned_to", "p.gortaza@gmail.com");
//		
//		Map bugs = (Map) client.execute("Bug.search", new Object[] {searchParams});
//		System.out.println("Result = " + bugs);
	}
}
