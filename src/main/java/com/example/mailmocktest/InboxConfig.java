package com.example.mailmocktest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class InboxConfig {
  @Value("${inboxConfig.host}")
  public String host;

  @Value("${inboxConfig.user}")
  public String user;

  @Value("${inboxConfig.outputEndpoint}")
  public String outputEndpoint;

  public String inboxEndpoint() {
    return String.format("imap://%s@%s", user, host);
  }

  public String sendEndpoint() {
    return String.format("smtp://%s@%s", user, host);
  }
}
