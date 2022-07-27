package com.babyboy.social.service.kafka;

import com.babyboy.social.config.Constants;
import com.babyboy.social.domain.User;
import com.babyboy.social.domain.UserReport;
import com.babyboy.social.repository.UserReportRepository;
import com.babyboy.social.repository.UserRepository;
import com.babyboy.social.utils.EmailSenders;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaConsumeListener {

    Logger logger = LoggerFactory.getLogger(KafkaConsumeListener.class);

    @Autowired
    private UserReportRepository userReportRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailSenders emailSenders;

    @KafkaListener(topics = Constants.TOPIC_USER_REPORT, groupId = "babyboy-group")
    void listener(String data) {
        logger.info("Consume User Report: {}", data);
        try {
            UserReport userReport = new ObjectMapper().readValue(data, UserReport.class);
            userReportRepository.save(userReport);

            Optional<User> userOptional = userRepository.findById(userReport.getUserId());
            if (userOptional.isPresent()) {
                emailSenders.sendEmailUserReport(userReport, userOptional.get().getEmail());
            } else {
                logger.error("User Report with id {} is not found", userReport.getUserId());
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            logger.error("Error When Consume User Report: {}", e.getMessage());
        }
    }
}
