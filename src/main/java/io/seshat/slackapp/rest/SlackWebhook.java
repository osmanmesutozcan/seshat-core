package io.seshat.slackapp.rest;

import io.seshat.slackapp.integration.SlackIntegration.StarEventExt;
import io.seshat.slackapp.integration.SlackIntegration.SlackEventPayload;
import io.seshat.slackapp.integration.SlackIntegration.SlashCommand;
import io.seshat.slackapp.service.QuestionService;
import io.seshat.slackapp.service.SlackMessengerService;
import io.seshat.slackapp.service.SlackMessengerService.MessagePayload;
import io.seshat.slackapp.util.MessageFormattingUtils;
import io.seshat.slackapp.util.MessageParserUtils;

import java.util.Arrays;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController()
@RequestMapping(path = "/slack")
@RequiredArgsConstructor
public class SlackWebhook {

  private final QuestionService questionService;
  private final SlackMessengerService slackMessengerService;

  // Challenge verification for slack events...

//  @PostMapping(path = "/events-ingest")
//  public String eventsIngest(@RequestBody() Map<String, String> body) {
//    return body.get("challenge");
//  }

  @PostMapping(path = "/events-ingest")
  public ResponseEntity<Object> eventsIngest(@RequestBody() SlackEventPayload<StarEventExt> body) {
    if (!questionService.processEvent(body.getEvent())) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    return new ResponseEntity<>(HttpStatus.OK);
  }

  @PostMapping(path = "/slash/save")
  public ResponseEntity<Object> save(@RequestParam() Map<String, String> params) {
    var command = SlashCommand.fromParams(params);

    var message = Arrays.asList(command.getText().split("\\r?\\n"));
    var question = MessageParserUtils.parseSlackMessage(String.join("", message.subList(0, 1)));
    var answer = String.join("\n", message.subList(1, message.size())).strip();

    var messageContent = MessageFormattingUtils
        .getNewEntryToChannelMessage(command.getUserId(), question, answer);
    var messageResponse = this.slackMessengerService
        .sendMessage(new MessagePayload(command.getChannelId(), messageContent));
    if (messageResponse == null) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    questionService.newEntry(messageResponse.getTs(), question, answer, command);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @PostMapping(path = "/slash/ask")
  public ResponseEntity<Object> ask(@RequestParam() Map<String, String> params) {
    var command = SlashCommand.fromParams(params);
    var message = MessageParserUtils.parseSlackMessage(command.getText());

    var messageContent = MessageFormattingUtils
        .getNewQuestionToChannelMessage(command.getUserId(), message);
    var messageResponse = this.slackMessengerService
        .sendMessage(new MessagePayload(command.getChannelId(), messageContent));
    if (messageResponse == null) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    var entity = questionService.newQuestion(messageResponse.getTs(), message, command);
    if (entity == null) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    return new ResponseEntity<>(HttpStatus.OK);
  }

  @PostMapping(path = "/slash/search")
  public ResponseEntity<Object> search(@RequestParam() Map<String, String> params) {
    var command = SlashCommand.fromParams(params);
    var message = MessageParserUtils.parseSlackMessage(command.getText());

    var results = questionService
        .getSearchResults(message.getTags(), message.getContent());

    log.info("{} {}", message.getContent(), results);

    var messageContent = MessageFormattingUtils
        .getQuestionSearchResultMessage(results);
    var messageResponse = this.slackMessengerService
        .sendMessage(new MessagePayload(command.getChannelId(), messageContent));
    if (messageResponse == null) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    return new ResponseEntity<>(HttpStatus.OK);
  }
}
