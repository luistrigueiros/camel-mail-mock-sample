package com.example.mailmocktest;

import java.util.concurrent.TimeUnit;
import org.apache.camel.CamelContext;
import org.apache.camel.FluentProducerTemplate;
import org.apache.camel.builder.NotifyBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MailMockTestApplicationTests {

  @Value("${inboxConfig.sendEndpoint}")
  public String sendEndpoint;

  @Value("${inboxConfig.outputEndpoint}")
  public String outputEndpoint;

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
    assertBatchCompletes(count);
  }

  private void assertBatchCompletes(int count) {
    new NotifyBuilder(camelContext)
        .from(outputEndpoint)
        .whenReceived(count)
        .create()
        .matches(20, TimeUnit.SECONDS);
  }

  public void sendMessages(int count) {
    FluentProducerTemplate producerTemplate = camelContext.createFluentProducerTemplate();
    for (int i = 0; i < count; ++i) {
      producerTemplate.to(sendEndpoint)
          .withBody(String.format("This is a test email#%d", i))
          .send();
    }
  }
}
