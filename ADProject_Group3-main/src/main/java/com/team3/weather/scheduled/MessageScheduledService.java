package com.team3.weather.scheduled;

import com.alibaba.fastjson.JSONObject;
import com.team3.weather.model.AccountHolder;
import com.team3.weather.model.Alert;
import com.team3.weather.repository.AlertRepository;
import com.team3.weather.repository.MessageRepository;
import com.team3.weather.service.impl.AdminServiceImpl;
import com.team3.weather.util.MailUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Service
//@EnableScheduling (when open the whole project, enable this annotation)
public class MessageScheduledService {

    private static final String PYTHON_TAINFALL_URL = "?";

    @Autowired
    private RestTemplate restTemplate;

    private final AlertRepository alertRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private AdminServiceImpl adminServiceImpl;

    @Autowired
    private MailUtil mailUtil;

    @Autowired
    public MessageScheduledService(AlertRepository alertRepository) {
        this.alertRepository = alertRepository;
    }

    //@Scheduled(cron = "0 0 8 * * ?") //(when need to import from API, please enable this annotation)
    public void scheduledTasks() {
        // query alert info
        List<Alert> alerts = alertRepository.findActiveAlert(true);
        Set<String> alertNames = alerts.stream().map(Alert::getAlertName).collect(Collectors.toSet());

        // call python
        Map<String, Object> uriVariables = new HashMap<>();
        uriVariables.put("alertNames", alertNames);
        JSONObject result = restTemplate.getForObject(PYTHON_TAINFALL_URL, JSONObject.class, uriVariables);
        // todo if
        String[] emails = adminServiceImpl.findAllAdmins().stream()
                .map(AccountHolder::getEmail).toArray(String[]::new);
        String messageInfo = "The threshold you set has issued a warning.";
        mailUtil.sendSimpleMail(emails, messageInfo);
        /*MessageDO messageDO = new MessageDO();
        // todo
        messageDO.setMessageInfo("this object exceeding threshold");
        messageDO.setTimestamp(LocalDateTime.now());
        messageRepository.saveAndFlush(messageDO);*/
    }
}
