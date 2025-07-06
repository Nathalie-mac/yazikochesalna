package com.yazikochesalna.messagingservice.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties(prefix = "spring.kafka")
public class KafkaProperties {
    private String bootstrapServers;
    private Producer producer;
    private Consumer consumer;
    private Listener listener;


    @Data
    public static class Producer {
        private String keySerializer;
        private String valueSerializer;

    }

    @Data
    public static class Consumer {
        private String autoOffsetReset;
        private boolean enableAutoCommit;
        private String keyDeserializer;
        private String valueDeserializer;
        private Properties properties;


        @Data
        public static class Properties {
            private Spring spring;

            @Data
            public static class Spring {

                private Json json;

                @Data
                public static class Json {
                    private Trusted trusted;
                    private Type type;

                    @Data
                    public static class Trusted {
                        private String packages;

                    }

                    @Data
                    public static class Type {
                        private String mapping;

                    }
                }
            }


        }
    }

    @Data
    public static class Listener {
        private String ackMode;

    }
}