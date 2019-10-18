package io.seshat.slackapp.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MessageParserUtils {

  static public ParsedMessage parseSlackMessage(String text) {
    var tagsMatcher = SlackUtils.TAGS_RGX.matcher(text);
    var usersMatcher = SlackUtils.USERS_RGX.matcher(text);

    var users = new ArrayList<String>();
    while (usersMatcher.find()){
      users.add(usersMatcher.group());
    }

    var tags = new ArrayList<String>();
    while (tagsMatcher.find()){
      tags.add(tagsMatcher.group());
    }

    var content = SlackUtils.TAGS_RGX.matcher(text).replaceAll("");
    content = SlackUtils.USERS_RGX.matcher(content).replaceAll("");
    content = content.strip();

    return new ParsedMessage(users, tags, content);
  }

  static private class SlackUtils {
    static Pattern TAGS_RGX = Pattern.compile("#(\\w+)");
    static Pattern USERS_RGX = Pattern.compile("<(.*)>");
//    static Pattern USERS_RGX = Pattern.compile("<@(.*)\\|(.*)>");
  }

  @Data
  public static class ParsedMessage {
    private final List<String> users;
    private final List<String> tags;
    private final String content;
  }
}
