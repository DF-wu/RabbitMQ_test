package df.rabbitmq;

import df.rabbitmq.Consumer.ConsumerD;
import df.rabbitmq.Producer.ProducerP;
import org.yaml.snakeyaml.Yaml;

import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
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
            ConsumerD consumerD = new ConsumerD(filename);
            consumerD.pollMsg();
            
        }
        else if (s.equals("p"))
        {
            //          producer
            ProducerP producerP = new ProducerP(filename);
            while(true)
            {
                String str = scanner.next();
                producerP.send(str);
            }
        }else if(s.equals("pp")){
            long now = System.currentTimeMillis();
            
            for (int i = 0; i < 1000; i++)
            {
                ProducerP producerP = new ProducerP(filename);
                producerP.send(i+ "  " + UUID.randomUUID().toString());
                System.out.println("msg sent: " + i);
                producerP.closeConnectionandChannels();
            }
            
            System.out.println("use time: " + ( System.currentTimeMillis() - now ) );
        }
        else if (s.equals("ppr"))
        {   // test for reuse connection
            long now = System.currentTimeMillis();
            ProducerP producerP = new ProducerP(filename);
            for (int i = 0; i < 1000; i++)
            {
                producerP.send(i + "  " + UUID.randomUUID().toString());
                System.out.println("msg sent: " + i);
            }
            producerP.closeConnectionandChannels();
            System.out.println("use time: " + (System.currentTimeMillis() - now));
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
