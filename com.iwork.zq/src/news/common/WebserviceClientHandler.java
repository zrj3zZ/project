    package news.common;  
      
    import org.codehaus.xfire.MessageContext; 
 
    import org.codehaus.xfire.handler.AbstractHandler;  
    import org.jdom.Element;  
      
      
    /** 
     * ������Ȩ��Ϣ�� 
     *  
     * @author Administrator 
     *  
     */  
    public class WebserviceClientHandler extends AbstractHandler {  
        private String username = null;  
        private String password = null;  
      
        public WebserviceClientHandler() {  
      
        }  
      
        public WebserviceClientHandler(String username, String password) {  
            this.username = username;  
            this.password = password;  
        }  
      
        public void setUsername(String username) {  
            this.username = username;  
        }  
      
        public void setPassword(String password) {  
            this.password = password;  
        }  
      
      
        public void invoke(MessageContext context) throws Exception {  
            Element el = new Element(WebserviceConstant.HEADER);  
            context.getOutMessage().setHeader(el);  
            Element auth = new Element(WebserviceConstant.TOKEN);  
            Element username_el = new Element(WebserviceConstant.USERNAME);  
            username_el.addContent(username);  
            Element password_el = new Element(WebserviceConstant.PASSWORD);  
            password_el.addContent(password);  
            auth.addContent(username_el);  
            auth.addContent(password_el);  
            el.addContent(auth);  
        }  
      
    }  