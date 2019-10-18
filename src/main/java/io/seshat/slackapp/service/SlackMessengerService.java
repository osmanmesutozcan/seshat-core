package io.seshat.slackapp.service;

import com.github.seratch.jslack.Slack;
import com.github.seratch.jslack.api.methods.response.chat.ChatPostMessageResponse;
import io.seshat.slackapp.SlackappConfiguration;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SlackMessengerService {

  private final SlackappConfiguration configuration;

  public ChatPostMessageResponse sendMessage(MessagePayload payload) {
    try {
       var response = Slack.getInstance()
          .methods(configuration.SLACK_TOKEN)
          .chatPostMessage(chatPostMessageRequestBuilder -> chatPostMessageRequestBuilder
              .mrkdwn(true)
              .channel(payload.channelId)
              .text(payload.content));

       log.info("{}", response);
       return response;

    } catch (Exception ignored) {
      return null;
    }
  }

  @Data
  public static class MessagePayload {
    private final String channelId;
    private final String content;
  }
}
