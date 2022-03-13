package com.example.mailmocktest;

import javax.mail.Message;
import org.apache.camel.ProducerTemplate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.jvnet.mock_javamail.Mailbox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MailMockTests {

	@Autowired
	private ProducerTemplate producerTemplate;

	@BeforeEach
	void beforeTest() {
		Assertions.assertNotNull(producerTemplate);
	}

	@Test
	void contextLoads() throws Exception {
		String body = "This is a test email";
		producerTemplate.sendBody("smtp://james@myhost", body);
		Mailbox mailbox = Mailbox.get("james@myhost");
		Integer size = mailbox.size();
		mailbox.forEach(this::printMessage);
		Assertions.assertTrue(size> 0);
		System.out.printf("Done");
	}

	private void printMessage(Message message) {
			try {
				String s = message.getContent().toString();
				System.out.printf(s);
			} catch (Exception e) {
				e.printStackTrace();
			}
	}
}
