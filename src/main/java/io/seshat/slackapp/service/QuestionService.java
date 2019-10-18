package io.seshat.slackapp.service;

import io.seshat.slackapp.dto.QuestionAnswerDTO;
import io.seshat.slackapp.integration.SlackIntegration.StarEventExt;
import io.seshat.slackapp.integration.SlackIntegration.SlashCommand;
import io.seshat.slackapp.model.Answer;
import io.seshat.slackapp.model.Question;
import io.seshat.slackapp.repository.AnswerRepository;
import io.seshat.slackapp.repository.QuestionRepository;

import io.seshat.slackapp.util.MessageParserUtils.ParsedMessage;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class QuestionService {

  private final QuestionRepository questionRepository;
  private final AnswerRepository answerRepository;

  public Question newQuestion(String ts, ParsedMessage message, SlashCommand command) {
    var callbackId = UUID.randomUUID().toString();
    var question = new Question();
    question.setId(callbackId);
    question.setTs(ts);

    question.setTags(message.getTags());
    question.setQuestion(message.getContent());

    question.setChannelId(command.getChannelId());
    question.setFromUserId(command.getUserId());
    question.setResponseUrl(command.getResponseUrl());

    return questionRepository.save(question);
  }

  public Map<String, List<QuestionAnswerDTO>> getDashboardData() {
    return questionRepository.findAllQuestionAnswers()
        .stream()
        .collect(Collectors.groupingBy(o -> o.getQuestion().getChannelId()));
  }

  public boolean processEvent(StarEventExt event) {
    var threadTs = event.getItem().getMessage().getThreadTs();
    if (threadTs == null) {
      log.info("This is not a thread message");
      return true;
    }

    var question = questionRepository.findOneByTs(threadTs);
    if (question.size() == 0) {
      log.info("Thread is not a question");
      return true;
    }

    if (event.getType().equals("star_added")) {
      return saveAnswer(event) != null;
    }

    if (event.getType().equals("star_removed")) {
      removeAnswer(event);
    }

    return true;
  }

  private Answer saveAnswer(StarEventExt event) {
    var answer = new Answer();
    answer.setAnswer(event.getItem().getMessage().getText());
    answer.setFromUserId(event.getUser());
    answer.setQuestionTs(event.getItem().getMessage().getThreadTs());
    return answerRepository.save(answer);
  }

  private void removeAnswer(StarEventExt event) {
    var threadTs = event.getItem().getMessage().getThreadTs();
    var answers = answerRepository.findByQuestionTs(threadTs);
    answerRepository.deleteAll(answers);
  }
}
