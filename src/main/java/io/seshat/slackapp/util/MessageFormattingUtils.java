package io.seshat.slackapp.util;

import io.seshat.slackapp.util.MessageParserUtils.ParsedMessage;

public class MessageFormattingUtils {

  public static String getNewQuestionToChannelMessage(String fromUserId, ParsedMessage message) {
    StringBuilder payload = new StringBuilder();
    payload.append(String.format("*%s* asked a question ", fromUserId));
    for (String tag: message.getTags()) {
      payload.append(String.format("`%s` ", tag));
    }

    payload.append(String.format("\n> %s\n", message.getContent()));
    return payload.toString();
  }
}
