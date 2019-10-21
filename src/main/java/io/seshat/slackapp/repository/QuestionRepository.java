package io.seshat.slackapp.repository;

import io.seshat.slackapp.dto.QuestionAnswerDTO;
import io.seshat.slackapp.model.Question;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface QuestionRepository extends JpaRepository<Question, String> {

  @Query(
      value = "SELECT new io.seshat.slackapp.dto.QuestionAnswerDTO(q, a)"
          + "FROM Question q "
          + "INNER JOIN Answer a on q.ts = a.questionTs "
          + "ORDER BY q.createdAt DESC"
  )
  List<QuestionAnswerDTO> findAllQuestionAnswers();

  @Query(
      value = "SELECT * "
          + "FROM question q "
          + "INNER JOIN answer a on q.ts = a.question_ts "
          + "WHERE to_tsvector(q.question) @@ to_tsquery('test:*') "
          + "ORDER BY ts_rank_cd(to_tsvector(q.question), to_tsquery('test:*')) DESC",
      nativeQuery = true
  )
  List<Question> searchAllQuestionAnswers(@Param("q_query") String query);

  List<Question> findOneByTs(String ts);
}
