import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class KafkaProducerThread extends Thread {

    // 主題
    private String topic;

    public KafkaProducerThread(String topic) {
        this.topic = topic;
    }

    @Override
    public void run() {
        // 配置生产者信息
        KafkaProducer producer = createProducer();

        // 模拟生产数据
        int count = 50;
        while (count++ < 100) {
            // 生产消息
            String msg = "hello:" + count;
            producer.send(new ProducerRecord<String, String>(topic, msg));
            // 休眠1秒
            try {
                TimeUnit.MILLISECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("消息生产完毕");
        producer.close();
    }

    /**
     * 配置生产者信息
     */
    private KafkaProducer createProducer() {
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.60.167:9092,192.168.60.169:9092,192.168.60.169:9092");
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        return new KafkaProducer(properties);
    }

    public static void main(String[] args) {
        String topic = "movepoint";
        // 模拟生产
        new KafkaProducerThread(topic).start();
    }
}
