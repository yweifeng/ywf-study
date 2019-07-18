import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.Arrays;
import java.util.Properties;

public class KafkaConsumerThread extends Thread {
    private String topic;

    public KafkaConsumerThread(String topic) {
        this.topic = topic;
    }

    @Override
    public void run() {
        KafkaConsumer consumer = createConsumer();
        consumer.subscribe(Arrays.asList(topic));
        while (true) {
            // 获取纪录
            ConsumerRecords<String, String> records = consumer.poll(100);
            for (ConsumerRecord<String, String> record : records) {
                System.out.printf("offset = %d, key = %s, value = %s", record.offset(), record.key(), record.value()+"\n");
                consumer.commitSync();
            }
        }
    }

    /**
     * 创建消费者
     */
    private KafkaConsumer createConsumer() {
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.60.167:9092,192.168.60.168:9092,192.168.60.169:9092");
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "ywf");
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        return new KafkaConsumer(properties);
    }

    public static void main(String[] args) {
        String topic = "movepoint";
        new KafkaConsumerThread(topic).start();
    }
}
