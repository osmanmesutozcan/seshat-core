package io.seshat.slackapp.repository;

import io.seshat.slackapp.model.Answer;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerRepository extends JpaRepository<Answer, String> {
  List<Answer> findByQuestionTs(String ts);
}
