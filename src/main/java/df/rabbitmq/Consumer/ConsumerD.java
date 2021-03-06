package df.rabbitmq.Consumer;

import com.rabbitmq.client.*;
import df.rabbitmq.ConfigLoader;
import df.rabbitmq.RabbitMQConfig;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class ConsumerD {
    private RabbitMQConfig rabbitMQConfig;
    private ConnectionFactory connectionFactory;
    private Connection connection;
    private Channel channel;
    
    public ConsumerD(String filename) throws IOException, TimeoutException
    {
        ConfigLoader configLoader = new ConfigLoader(filename);
        rabbitMQConfig = new RabbitMQConfig();
        rabbitMQConfig.read(configLoader.getYAMLmap());
        channel = EstablishConnection().createChannel();
        
        EstablishQueue();
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
    
    public Connection EstablishConnection( ) throws IOException, TimeoutException
    {
        // establish connection
        connectionFactory = new ConnectionFactory();
        connectionFactory.setUsername(rabbitMQConfig.getUSERNAME());
        connectionFactory.setPassword(rabbitMQConfig.getPASSWORD());
        connectionFactory.setPort(rabbitMQConfig.getPORT());
        connectionFactory.setVirtualHost(rabbitMQConfig.getVIRTUALHOST());
        connectionFactory.setHost(rabbitMQConfig.getHOST());
        connection = connectionFactory.newConnection();
        return connection;
    }
    
    private void EstablishQueue() throws IOException
    {
        /*
         * 宣告（建立）佇列
         * 引數1：佇列名稱
         * 引數2：為true時server重啟佇列不會消失
         * 引數3：佇列是否是獨佔的，如果為true只能被一個connection使用，其他連線建立時會丟擲異常
         * 引數4：佇列不再使用時是否自動刪除（沒有連線，並且沒有未處理的訊息)
         * 引數5：建立佇列時的其他引數
         */
        
        channel.queueDeclare(
                rabbitMQConfig.getQUEUE_NAME(),
                true,
                false,
                false,
                null
        );
    
        /*
         * 繫結佇列到交換機(這個交換機的名稱一定要和上面的生產者交換機名稱相同)
         * 引數1：佇列的名稱
         * 引數2：交換機的名稱
         * 引數3：Routing Key
         *
         */
        channel.queueBind(
                rabbitMQConfig.getQUEUE_NAME(),
                rabbitMQConfig.getEXCHANGE_NAME(),
                ""
        );
    
        // 同一時刻伺服器只會發一條訊息給消費者
        channel.basicQos(1);
    }
    
    public void pollMsg() throws IOException
    {
        String consumerTag = "df consumer tag";
        boolean autoAck = false;
        String  msg = "";
        /*
        DefaultConsumer defaultConsumer =  new DefaultConsumer(channel){
            @Override
            public void handleDelivery  (String consumerTag,
                                         Envelope envelope,
                                         AMQP.BasicProperties props,
                                         byte[] body)
                    throws IOException
            {
                String routingKey = envelope.getRoutingKey();
                String contentType = props.getContentType();
                long deliveryTag = envelope.getDeliveryTag();
                // (process the message components here ...)
                System.out.println(String.format("%s, %s, %s", routingKey, consumerTag, deliveryTag));
                String decodeMsg = new String(body, StandardCharsets.UTF_8);
                System.out.println(decodeMsg);
                channel.basicAck(deliveryTag, false);
            }
        };
        */
        
        channel.basicConsume(rabbitMQConfig.getQUEUE_NAME(), autoAck, consumerTag,
                new DFConsumer(channel,msg));
        
        
    }
    
    public void closeConnectionandChannels() throws IOException, TimeoutException
    {
        channel.close();
        connection.close();
    }
    
    
    
    // for Experiment only
    public void pollMsgWithNewChannel() throws IOException
    {
        Channel ch = connection.createChannel();
    
        ch.queueDeclare(
                rabbitMQConfig.getQUEUE_NAME(),
                false,
                false,
                false,
                null
        );
    
        ch.queueBind(
                rabbitMQConfig.getQUEUE_NAME(),
                rabbitMQConfig.getEXCHANGE_NAME(),
                "MDFK");
    
    
        ch.exchangeDeclare(
                rabbitMQConfig.getEXCHANGE_NAME(),
                "fanout",
                false,
                true,
                null);
    
        channel.basicConsume(rabbitMQConfig.getQUEUE_NAME(), false, "consumerTag",
                new DFConsumer(channel,""));
    }
}
