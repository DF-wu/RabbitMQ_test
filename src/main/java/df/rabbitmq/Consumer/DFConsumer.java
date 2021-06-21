package df.rabbitmq.Consumer;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class DFConsumer extends DefaultConsumer {
    private final Channel channel;
    
    public DFConsumer(Channel channel, String messageString)
    {
        super(channel);
        this.channel = channel;
    }
    
    @Override
    public void handleDelivery ( String consumerTag,
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
}
