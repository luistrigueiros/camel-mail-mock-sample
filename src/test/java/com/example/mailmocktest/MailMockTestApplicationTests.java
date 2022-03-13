package com.example.mailmocktest;

import org.apache.camel.CamelContext;
import org.apache.camel.ConsumerTemplate;
import org.apache.camel.FluentProducerTemplate;
import org.apache.camel.ProducerTemplate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MailMockTestApplicationTests {
  private static final Logger logger = LoggerFactory.getLogger(MailMockTestApplicationTests.class);
  @Autowired
  private InboxConfig inboxConfig;

  @Autowired
  private CamelContext camelContext;

  @BeforeEach
  void beforeTest() {
    Assertions.assertNotNull(camelContext);
  }


  @Test
  void sendEmailBatch() {
    int count = 10;
    sendMessages(count);
    ConsumerTemplate consumerTemplate = camelContext.createConsumerTemplate();
    int receiveTry = 0;
    while (true) {
      if (count == 0) {
        break;
      }
      if (receiveTry > 20) {
        throw new IllegalStateException("Tried to many times to receive all the messages");
      }
      String msg = consumerTemplate.receiveBody(inboxConfig.outputEndpoint, 300, String.class);
      logger.debug("Got {}", msg);
      ++receiveTry;
      --count;
    }
    logger.info("Done");
  }

  public void sendMessages(int count) {
    FluentProducerTemplate producerTemplate = camelContext.createFluentProducerTemplate();
    for (int i = 0; i < count; ++i) {
      producerTemplate.to(inboxConfig.sendEndpoint())
          .withBody(String.format("This is a test email#%d", i))
          .send();
    }
  }
}
