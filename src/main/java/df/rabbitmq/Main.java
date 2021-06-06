package df.rabbitmq;

import df.rabbitmq.Consumer.Consumer;
import df.rabbitmq.Producer.Producer;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

public class Main {
    public static void main(String[] args) throws IOException, TimeoutException
    {
        String filename = "setting.yaml";
        //initialize
            // load setting
        ConfigLoader configLoader = new ConfigLoader(filename);
        RabbitMQConfig rabbitMQConfig = new RabbitMQConfig();
        rabbitMQConfig.read(configLoader.getYAMLmap());
        System.out.println(configLoader.getYAMLmap());

        
        String welcomeMessage = "Welcome to DF RabbitMQ tester\n" +
                "consumer mode : c\n" +
                "producer mode : p\n";
    
        System.out.println(welcomeMessage);
        Scanner scanner = new Scanner(System.in);
        String s = scanner.next();
        System.out.println("ur selection: " + s);
        
        if (s.equals("c"))
        {
            //          consumer
            Consumer consumer = new Consumer(filename);
            
            consumer.pollMsg();
            
        }
        else if (s.equals("p"))
        {
            //          producer
            Producer producer = new Producer(filename);
            while(true)
            {
                String str = scanner.next();
                producer.send(str);
            }
        }else if(s.equals("superP")){
            for (int i = 0; i < 10000; i++)
            {
                Producer producer = new Producer(filename);
                producer.send(i+ "  " + UUID.randomUUID().toString());
            }
        }
        else System.out.println("Wrong Input!");

        
      
    }
}
