package df.rabbitmq;

import df.rabbitmq.Consumer.Consumer;
import df.rabbitmq.Producer.Producer;
import org.yaml.snakeyaml.Yaml;

import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.TimeoutException;

public class Main {
    public static void main(String[] args) throws IOException, TimeoutException
    {
//        dump();
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
    
    
    public static void dump() throws IOException
    {
        
        Map<String, List<String>> data = new LinkedHashMap<String, List<String>>();
//        Map<String, Object> data = new LinkedHashMap<String, Object>();
    
        for (int i = 0; i < 10000; i++)
        {
            ArrayList<String> ar =  new ArrayList<String >();
    
            for (int j = 0; j < 1000; j++)
            {
                ar.add(UUID.randomUUID().toString());
            }
            
            data.put(String.valueOf(i), ar);
        }
    
        
        Yaml nya = new Yaml();
        FileWriter writer = new FileWriter("dffile.yaml");
        nya.dump(data, writer);
        System.out.println(writer);

//        FileWriter fw = new FileWriter("test.yaml");
    }
}
