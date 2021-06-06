package df.rabbitmq;

import java.util.Map;

public class RabbitMQConfig {
    private String HOST;
    private int PORT;
    private String USERNAME;
    private String PASSWORD;
    private String VIRTUALHOST;
    private String EXCHANGE_NAME;
    private String QUEUE_NAME;
    
    public void read(Map<String, Object> map){
        map.forEach((k,v) -> {
            switch (k)
            {
                case "HOST":
                    HOST = (String) v;
                    break;
                case "PORT":
                    PORT = (Integer) v;
                    break;
                case "USERNAME":
                    USERNAME = (String) v;
                    break;
                case "PASSWORD":
                    PASSWORD = (String) v;
                    break;
                case "VIRTUALHOST":
                    VIRTUALHOST = (String) v;
                    break;
                case "EXCHANGE_NAME":
                    EXCHANGE_NAME = (String) v;
                    break;
                case "QUEUE_NAME":
                    QUEUE_NAME = (String) v;
                    break;
                default:
                    System.out.println("error: key not found.  key = " + k );
            };
        });
        
        
    }
    
    
    // getter & setter
    
    public String getHOST()
    {
        return HOST;
    }
    
    public void setHOST(String HOST)
    {
        this.HOST = HOST;
    }
    
    public int getPORT()
    {
        return PORT;
    }
    
    public void setPORT(int PORT)
    {
        this.PORT = PORT;
    }
    
    public String getUSERNAME()
    {
        return USERNAME;
    }
    
    public void setUSERNAME(String USERNAME)
    {
        this.USERNAME = USERNAME;
    }
    
    public String getPASSWORD()
    {
        return PASSWORD;
    }
    
    public void setPASSWORD(String PASSWORD)
    {
        this.PASSWORD = PASSWORD;
    }
    
    public String getVIRTUALHOST()
    {
        return VIRTUALHOST;
    }
    
    public void setVIRTUALHOST(String VIRTUALHOST)
    {
        this.VIRTUALHOST = VIRTUALHOST;
    }
    
    public String getEXCHANGE_NAME()
    {
        return EXCHANGE_NAME;
    }
    
    public void setEXCHANGE_NAME(String EXCHANGE_NAME)
    {
        this.EXCHANGE_NAME = EXCHANGE_NAME;
    }
    
    public String getQUEUE_NAME()
    {
        return QUEUE_NAME;
    }
    
    public void setQUEUE_NAME(String QUEUE_NAME)
    {
        this.QUEUE_NAME = QUEUE_NAME;
    }
}
