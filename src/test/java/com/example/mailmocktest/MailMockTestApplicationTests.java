package com.example.mailmocktest;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.mock.MockEndpoint;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MailMockTestApplicationTests {

  @Autowired
  private ProducerTemplate producerTemplate;
  @Autowired
  private CamelContext camelContext;

  @Test
  void contextLoads() {
    Assertions.assertNotNull(producerTemplate);
    Assertions.assertNotNull(camelContext);
    MockEndpoint mockEndpoint = camelContext.getEndpoint("mock:output", MockEndpoint.class);
    int count = 10;
    sendMessages(count);
    mockEndpoint.expectedMessageCount(1);
    System.out.printf("Done");
  }

  public void sendMessages(int count) {
    for (int i = 0; i < count; ++i) {
      producerTemplate.sendBody("smtp://james@myhost", String.format("This is a test email#%d", i));
    }
  }
}
