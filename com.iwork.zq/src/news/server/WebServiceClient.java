
package news.server;

import java.net.MalformedURLException;
import java.util.Collection;
import java.util.HashMap;

import javax.xml.namespace.QName;

import org.codehaus.xfire.XFireRuntimeException;
import org.codehaus.xfire.aegis.AegisBindingProvider;
import org.codehaus.xfire.annotations.AnnotationServiceFactory;
import org.codehaus.xfire.annotations.jsr181.Jsr181WebAnnotations;
import org.codehaus.xfire.client.XFireProxyFactory;
import org.codehaus.xfire.jaxb2.JaxbTypeRegistry;
import org.codehaus.xfire.service.Endpoint;
import org.codehaus.xfire.service.Service;
import org.codehaus.xfire.soap.AbstractSoapBinding;
import org.codehaus.xfire.transport.TransportManager;

import com.iwork.app.conf.SystemConfig;

public class WebServiceClient {

    private static XFireProxyFactory proxyFactory = new XFireProxyFactory();
    private HashMap endpoints = new HashMap();
    private Service service0;

    public WebServiceClient() {
    	try{
    		create0();
    		Endpoint WebServicePortTypeLocalEndpointEP = service0 .addEndpoint(new QName("http://service.com", "WebServicePortTypeLocalEndpoint"), new QName("http://service.com", "WebServicePortTypeLocalBinding"), "xfire.local://WebService");
    		endpoints.put(new QName("http://service.com", "WebServicePortTypeLocalEndpoint"), WebServicePortTypeLocalEndpointEP);
    		Endpoint WebServiceHttpPortEP = service0 .addEndpoint(new QName("http://service.com", "WebServiceHttpPort"), new QName("http://service.com", "WebServiceHttpBinding"), SystemConfig._crawlerConf.getUrl());
    		endpoints.put(new QName("http://service.com", "WebServiceHttpPort"), WebServiceHttpPortEP);
    	}catch(Exception e){
    	}
    }

    public Object getEndpoint(Endpoint endpoint) {
        try {
            return proxyFactory.create((endpoint).getBinding(), (endpoint).getUrl());
        } catch (MalformedURLException e) {
            throw new XFireRuntimeException("Invalid URL", e);
        }
    }

    public Object getEndpoint(QName name) {
        Endpoint endpoint = ((Endpoint) endpoints.get((name)));
        if ((endpoint) == null) {
            throw new IllegalStateException("No such endpoint!");
        }
        return getEndpoint((endpoint));
    }

    public Collection getEndpoints() {
        return endpoints.values();
    }

    private void create0() {
        TransportManager tm = (org.codehaus.xfire.XFireFactory.newInstance().getXFire().getTransportManager());
        HashMap props = new HashMap();
        props.put("annotations.allow.interface", true);
        AnnotationServiceFactory asf = new AnnotationServiceFactory(new Jsr181WebAnnotations(), tm, new AegisBindingProvider(new JaxbTypeRegistry()));
        asf.setBindingCreationEnabled(false);
        service0 = asf.create((WebServicePortType.class), props);
        {
            AbstractSoapBinding soapBinding = asf.createSoap11Binding(service0, new QName("http://service.com", "WebServicePortTypeLocalBinding"), "urn:xfire:transport:local");
        }
        {
            AbstractSoapBinding soapBinding = asf.createSoap11Binding(service0, new QName("http://service.com", "WebServiceHttpBinding"), "http://schemas.xmlsoap.org/soap/http");
        }
    }

    public WebServicePortType getWebServicePortTypeLocalEndpoint() {
        return ((WebServicePortType)(this).getEndpoint(new QName("http://service.com", "WebServicePortTypeLocalEndpoint")));
    }

    public WebServicePortType getWebServicePortTypeLocalEndpoint(String url) {
        WebServicePortType var = getWebServicePortTypeLocalEndpoint();
        org.codehaus.xfire.client.Client.getInstance(var).setUrl(url);
        return var;
    }

    public WebServicePortType getWebServiceHttpPort() {
        return ((WebServicePortType)(this).getEndpoint(new QName("http://service.com", "WebServiceHttpPort")));
    }

    public WebServicePortType getWebServiceHttpPort(String url) {
        WebServicePortType var = getWebServiceHttpPort();
        org.codehaus.xfire.client.Client.getInstance(var).setUrl(url);
        return var;
    }

    public static void main(String[] args) {
        

        WebServiceClient client = new WebServiceClient();
        
		//create a default service endpoint
        WebServicePortType service = client.getWebServiceHttpPort();
        
		//TODO: Add custom client code here
        		//
        		//service.yourServiceOperationHere();
        
        		System.exit(0);
    }

}
