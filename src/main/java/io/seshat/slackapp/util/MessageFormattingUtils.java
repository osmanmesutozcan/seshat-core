package io.seshat.slackapp.util;

import io.seshat.slackapp.model.Question;
import io.seshat.slackapp.util.MessageParserUtils.ParsedMessage;
import java.util.List;

public class MessageFormattingUtils {

  /**
   * Format a message to the following template
   *
   * ``` {User} saved a question {Tag1, Tag2, ...}
   *
   * > {Question}
   *
   * {Answer} ```
   */
  public static String getNewEntryToChannelMessage(String fromUserId, ParsedMessage message, String answer) {
    StringBuilder payload = new StringBuilder();
    payload.append(String.format("*%s* saved a question ", fromUserId));

    payload.append("\n");
    for (String tag : message.getTags()) {
      payload.append(String.format("`%s` ", tag));
    }

    payload.append(String.format("\n> %s\n", message.getContent()));
    payload.append(String.format("\n%s", answer));
    return payload.toString();
  }

  /**
   * Format a message to the following template
   *
   * ``` {User} asked a question to {ToUser1, ToUser2, ...} {Tag1, Tag2, ...}
   *
   * > {Question} ```
   */
  public static String getNewQuestionToChannelMessage(String fromUserId, ParsedMessage message) {
    StringBuilder payload = new StringBuilder();
    payload.append(String.format("*%s* asked a question ", fromUserId));

    if (message.getUsers().size() > 0) {
      payload.append("to ");
      for (String user : message.getUsers()) {
        payload.append(String.format("%s ", user));
      }
    }

    payload.append("\n");
    for (String tag : message.getTags()) {
      payload.append(String.format("`%s` ", tag));
    }

    payload.append(String.format("\n> %s\n", message.getContent()));
    return payload.toString();
  }

  /**
   * Format a message to the following template
   *
   * Question {Question}
   * {tag1, tag2}
   * --------------------------------
   */
  public static String getQuestionSearchResultMessage(List<Question> questions) {
    StringBuilder payload = new StringBuilder();

    for (Question question : questions) {
      payload.append(String.format("Question %s \n", question.getQuestion()));
      for (String tag : question.getTags()) {
        payload.append(String.format("`%s` ", tag));
      }
      payload.append("\n----------------------------------\n");
    }

    return payload.toString();
  }
}
