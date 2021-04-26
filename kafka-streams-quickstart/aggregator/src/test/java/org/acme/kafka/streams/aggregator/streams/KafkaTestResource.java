package org.acme.kafka.streams.aggregator.streams;

import java.util.HashMap;
import java.util.Map;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import io.strimzi.StrimziKafkaContainer;

public class KafkaTestResource implements QuarkusTestResourceLifecycleManager {

    private static final StrimziKafkaContainer kafka = new StrimziKafkaContainer();

    public static String getBootstrapServers() {
        return kafka.getBootstrapServers();
    }

    @Override
    public Map<String, String> start() {
        kafka.start();

        Map<String, String> configuration = new HashMap<>();
        configuration.put("quarkus.kafka-streams.bootstrap-servers", kafka.getBootstrapServers());
        configuration.put("kafka.bootstrap.servers", kafka.getBootstrapServers());
        return configuration;
    }

    @Override
    public void stop() {
        kafka.close();
    }
}
