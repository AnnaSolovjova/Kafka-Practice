import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;


import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class Consumer {
    public static void main(String [] args){
        Consumer consumer = new Consumer();
        consumer.run();

    }

    public void run(){

        Properties properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,"kafka:9093");
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        //Nt sure why but each consumer needs to e within a group with group ID
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "AppBGroup");
        // none/earliers/latest
        // none - dont start id no offset found
        // earliest - read from very beginning of topic. Good to know here is that if we reconnect with same group ID, the messages that we already read won't be read again (the offsets are remembered)
        // latest - read only from now
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");


        KafkaConsumer<String,String> consumer = new KafkaConsumer<>(properties);
        consumer.subscribe(Collections.singletonList("ApplicaitionAMessage"));
        Producer producer = new Producer();

        while(true) {
            //System.out.println("Polling");
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
            for(ConsumerRecord<String, String> record: records) {
                // Print Received message
                System.out.println("MessageReceived:"+ record.value());
                System.out.println("Topic:"+ record.topic()+", Value: " + record.value());
                //Run something, potentially on another thread
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                //Post message to Kafka
                producer.run();
            }
        }
    }
}
