package io.seshat.slackapp.integration;


import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.util.List;
import java.util.Map;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;

public class SlackIntegration {

  @Data
  @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
  public static class SlackEventPayload<T> {
    private T event;

    private String token;
    private String teamId;
    private String apiAppId;
    private List<String> authedUsers;
  }

  @Data
  @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
  public static class StarEventExt {
    private String type;
    private String user;
    private Item item;

    @Data
    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
    public static class Item {
      private String type;
      private String channel;
      private Message message;
    }

    @Data
    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
    public static class Message {
      private String clientMsgId;
      private String type;
      private String user;
      private String text;
      private String ts;
      private String threadTs;
      private String permalink;
      private Boolean isStarred;
    }
  }

  @Getter
  @ToString
  public static class SlashCommand {

    private final String teamId;
    private final String teamName;

    private final String channelId;
    private final String channelName;

    private final String userId;
    private final String userName;

    private final String triggerId;
    private final String responseUrl;
    private final String text;

    private SlashCommand(Map<String, String> params) {
      this.teamId = params.get("team_id");
      this.teamName = params.get("team_name");

      this.channelId = params.get("channel_id");
      this.channelName = params.get("channel_name");

      this.userId = params.get("user_id");
      this.userName = params.get("user_name");

      this.text = params.get("text");
      this.responseUrl = params.get("response_url");
      this.triggerId = params.get("trigger_id");
    }

    public static SlashCommand fromParams(Map<String, String> params) {
      return new SlashCommand(params);
    }
  }
}
