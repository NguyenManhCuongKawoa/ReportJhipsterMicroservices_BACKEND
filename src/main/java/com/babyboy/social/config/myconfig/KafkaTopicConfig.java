package com.babyboy.social.config.myconfig;

import com.babyboy.social.config.Constants;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic topicReportUser() {
        return TopicBuilder.name(Constants.TOPIC_USER_REPORT).partitions(1).replicas(1).build();
    }
}
