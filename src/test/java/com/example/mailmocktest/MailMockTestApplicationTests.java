package com.example.mailmocktest;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.mock.MockEndpoint;
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
  void sendBatchOfEmail() {
    MockEndpoint mockEndpoint = camelContext.getEndpoint(inboxConfig.outputEndpoint, MockEndpoint.class);
    int count = 10;
    sendMessages(count);
    mockEndpoint.expectedMessageCount(1);
    System.out.printf("Done");
  }

  public void sendMessages(int count) {
    ProducerTemplate producerTemplate = camelContext.createProducerTemplate();
    for (int i = 0; i < count; ++i) {
      producerTemplate.sendBody(inboxConfig.sendEndpoint(), String.format("This is a test email#%d", i));
    }
  }
}
