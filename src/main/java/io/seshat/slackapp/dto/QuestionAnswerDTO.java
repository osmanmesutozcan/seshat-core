package io.seshat.slackapp.dto;

import io.seshat.slackapp.model.Answer;
import io.seshat.slackapp.model.Question;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class QuestionAnswerDTO {

  private final Question question;
  private final Answer answer;
}
