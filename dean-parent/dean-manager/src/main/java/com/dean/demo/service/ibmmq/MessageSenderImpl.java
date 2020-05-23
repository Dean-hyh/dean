package com.dean.demo.service.ibmmq;

import com.dean.demo.service.MessageSender;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

@Component("ibmMessageSender")
public class MessageSenderImpl implements MessageSender {

    @Resource(name="senderJmsTemplate")
    private JmsTemplate jmsTemplate;
    /**
     * createTextMessage方法要求参数必须由final修饰
     */
    @Override
    public void sendMessage(final String message) {
        this.jmsTemplate.send(new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                    return session.createTextMessage(message);
            }
        });
    }
}