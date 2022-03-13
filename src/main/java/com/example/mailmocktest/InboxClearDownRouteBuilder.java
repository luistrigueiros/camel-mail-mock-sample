package com.example.mailmocktest;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class InboxClearDownRouteBuilder extends RouteBuilder {

  private final InboxConfig inboxConfig;

  public InboxClearDownRouteBuilder(InboxConfig inboxConfig) {
    this.inboxConfig = inboxConfig;
  }

  @Override
  public void configure() throws Exception {
    from(inboxConfig.inboxEndpoint())
        .id("INBOX_CLEAR_DOWN_ROUTE")
        .process(this::logger)
        .to(inboxConfig.outputEndpoint);
  }

  private void logger(Exchange exchange) {
    Message in = exchange.getIn();
    String to = in.getHeader("To", String.class);
    String from = in.getHeader("from", String.class);
    String id = in.getHeader("Message-ID", String.class);
    log.info("Processing email from=[{}], to=[{}] id=[{}]",from, to, id);
  }
}
