package io.seshat.slackapp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;

@Data
@Entity
public class Answer {

  /**
   * Uniquely generated callback answerId.
   */
  @Id
  private String id;

  @NotBlank
  @JsonIgnore
  private String questionTs;

  @NotBlank
  private String fromUserId;

  @NotBlank
  private String answer;

  @CreatedDate
  private Date createdAt;

  @PrePersist
  private void saveCreatedAt() {
    this.createdAt = Date.from(Instant.now());
    this.id = UUID.randomUUID().toString();
  }
}
