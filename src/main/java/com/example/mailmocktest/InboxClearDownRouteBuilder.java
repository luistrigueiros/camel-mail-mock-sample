package com.example.mailmocktest;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class InboxClearDownRouteBuilder extends RouteBuilder {

  @Value("${inboxClearDownRouteBuilder.inboxEndpoint}")
  public String inboxEndpoint;
  @Value("${inboxClearDownRouteBuilder.outputEndpoint}")
  public String outputEndpoint;

  @Override
  public void configure() throws Exception {
    log.info("Configuring ...");
    from(inboxEndpoint)
        .process(this::logger)
        .to(outputEndpoint);
  }

  private void logger(Exchange exchange) {
    Message in = exchange.getIn();
    log.info("Processing [{}]",in.getBody(String.class));
  }
}
