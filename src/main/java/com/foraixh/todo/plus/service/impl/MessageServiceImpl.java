package com.foraixh.todo.plus.service.impl;

import com.foraixh.todo.plus.service.MessageService;
import org.slf4j.Logger;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author myvina
 * @date 2021/03/15 19:59
 * @usage
 */
@Service
public class MessageServiceImpl implements MessageService {
    private static final Logger log = getLogger(MessageServiceImpl.class);

    private final JavaMailSender mailSender;
    private final MailProperties mailProperties;

    public MessageServiceImpl(JavaMailSender mailSender, MailProperties mailProperties) {
        this.mailSender = mailSender;
        this.mailProperties = mailProperties;
    }

    @Override
    public void notify(String userName, String deviceMessage) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(mailProperties.getUsername());
        message.setTo(userName);
        message.setSubject("microsoft login");
        message.setText(deviceMessage);
        mailSender.send(message);

        log.info("已将设备码发送到对应的邮箱：{}", userName);
    }
}
