package io.seshat.slackapp.repository;

import io.seshat.slackapp.dto.QuestionAnswerDTO;
import io.seshat.slackapp.model.Question;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface QuestionRepository extends JpaRepository<Question, String> {

  @Query(
      value = "SELECT new io.seshat.slackapp.dto.QuestionAnswerDTO(q, a)"
          + "FROM Question q "
          + "INNER JOIN Answer a on q.ts = a.questionTs "
          + "ORDER BY q.createdAt DESC"
  )
  List<QuestionAnswerDTO> findAllQuestionAnswers();

  List<Question> findOneByTs(String ts);
}
