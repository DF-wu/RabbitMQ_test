package df.rabbitmq;

import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.Map;

public class ConfigLoader {
    private Yaml yaml;
    private String filename;
    private RabbitMQConfig rabbitMQConfig;
    public ConfigLoader(String filename){
        this.filename = filename;
        this.yaml = new Yaml();
    }
    
    public Map<String,Object> getYAMLmap()
    {

        InputStream InputStream = this.getClass()
                .getClassLoader()
                .getResourceAsStream(filename);
        Map<String, Object> obj = yaml.load(InputStream);
        return obj;
    }
}
