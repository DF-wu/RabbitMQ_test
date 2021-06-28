package df.rabbitmq.Producer;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;
import df.rabbitmq.ConfigLoader;
import df.rabbitmq.RabbitMQConfig;


import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

/*
* for sub/pub mode
*/
public class ProducerP {
    private final RabbitMQConfig rabbitMQConfig;
    private ConnectionFactory connectionFactory;
    private Connection connection;
    private Channel channel;
    
    public ProducerP(String filename) throws IOException, TimeoutException
    {
        ConfigLoader configLoader = new ConfigLoader(filename);
        rabbitMQConfig = new RabbitMQConfig();
        rabbitMQConfig.read(configLoader.getYAMLmap());
        establishConnection();
        channel = connection.createChannel();
        
        /*
         * 宣告exchange(交換機)
         * 引數1：交換機名稱
         * 引數2：交換機型別
         * 引數3：交換機永續性，如果為true則伺服器重啟時不會丟失
         * 引數4：交換機在不被使用時是否刪除
         * 引數5：交換機的其他屬性
         */
        
        
        channel.exchangeDeclare(
                rabbitMQConfig.getEXCHANGE_NAME(),
                "fanout",
                true,
                true,
                null);
    }
    
    public void establishConnection( ) throws IOException, TimeoutException
    {
        // establish connection
        connectionFactory = new ConnectionFactory();
        connectionFactory.setUsername(rabbitMQConfig.getUSERNAME());
        connectionFactory.setPassword(rabbitMQConfig.getPASSWORD());
        connectionFactory.setPort(rabbitMQConfig.getPORT());
        connectionFactory.setVirtualHost(rabbitMQConfig.getVIRTUALHOST());
        connectionFactory.setHost(rabbitMQConfig.getHOST());
        connection = connectionFactory.newConnection();
    }
    
    
    public void send(String msg) throws IOException
    {
        /*
          param 1 = exchange
          param 2 = routingKey
          param 3 = basicProperties
          param 4 = msg in byte[]
          */
        
       channel.basicPublish(
               rabbitMQConfig.getEXCHANGE_NAME(),
               "",
               MessageProperties.PERSISTENT_TEXT_PLAIN,
               msg.getBytes(StandardCharsets.UTF_8)
       );
       
        
    }
    
    public void closeConnectionandChannels() throws IOException, TimeoutException
    {
        channel.close();
        connection.close();
    }
    
    // only for experiment
    public void sendWithNewChannel(String msg) throws IOException
    {
        Channel ch = connection.createChannel();
    
        channel.exchangeDeclare(
                rabbitMQConfig.getEXCHANGE_NAME(),
                "fanout",
                true,
                true,
                null);
    
    
        channel.basicPublish(
                rabbitMQConfig.getEXCHANGE_NAME(),
                "MDFK",
                MessageProperties.PERSISTENT_TEXT_PLAIN,
                msg.getBytes(StandardCharsets.UTF_8)
        );
    
    
    }
    
}
