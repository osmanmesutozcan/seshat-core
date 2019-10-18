package io.seshat.slackapp;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SlackappConfiguration {

  @Value("${seshat.slack.token}")
  public String SLACK_TOKEN;
}
