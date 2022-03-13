package com.example.mailmocktest;

import javax.mail.Message;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.FluentProducerTemplate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.jvnet.mock_javamail.Mailbox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
class MailMockTests {

  @Autowired
  private FluentProducerTemplate producerTemplate;

  @BeforeEach
  void beforeTest() {
    Assertions.assertNotNull(producerTemplate);
  }

  @Test
  @DisplayName("Should be able to send and receive email in mock mail box")
  void simpleMailMockTest() throws Exception {
    String body = "This is a test email";
    producerTemplate.to("smtp://james@myhost").withBody(body).send();
    Mailbox mailbox = Mailbox.get("james@myhost");
    Integer size = mailbox.size();
    mailbox.forEach(this::printMessage);
    Assertions.assertTrue(size > 0);
    log.debug("Done");
  }

  @SneakyThrows
  private void printMessage(Message message) {
    String s = message.getContent().toString();
    log.debug(s);
  }
}
