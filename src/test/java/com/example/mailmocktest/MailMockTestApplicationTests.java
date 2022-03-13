package com.example.mailmocktest;

import java.util.concurrent.TimeUnit;
import org.apache.camel.CamelContext;
import org.apache.camel.FluentProducerTemplate;
import org.apache.camel.builder.NotifyBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MailMockTestApplicationTests {
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
    NotifyBuilder notify = new NotifyBuilder(camelContext)
        .from(inboxConfig.outputEndpoint)
        .whenReceived(count)
        .create();
    notify.matches(20, TimeUnit.SECONDS);
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
