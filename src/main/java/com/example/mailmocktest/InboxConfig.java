package com.example.mailmocktest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InboxConfig {

  @Value("${inboxConfig.inputEndpoint}")
  public String inputEndpoint;


  @Value("${inboxConfig.outputEndpoint}")
  public String outputEndpoint;

  @Bean
  InboxClearDownRouteBuilder inboxClearDownRouteBuilder() {
    return new InboxClearDownRouteBuilder(inputEndpoint, outputEndpoint);
  }
}
