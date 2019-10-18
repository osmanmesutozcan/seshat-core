package io.seshat.slackapp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;

@Data
@Entity
public class Question {

  /**
   * Uniquely generated callback answerId.
   */
  @Id
  private String id;

  @NotBlank
  @JsonIgnore
  private String ts;

  @ElementCollection(targetClass = String.class)
  private List<String> tags = new ArrayList<>();

  @NotBlank
  @JsonIgnore
  private String responseUrl;

  @NotBlank
  @JsonIgnore
  private String fromUserId;

  @NotBlank
  private String channelId;

  @NotBlank
  private String question;

  @CreatedDate
  private Date createdAt;

  @PrePersist
  private void saveCreatedAt() {
    this.createdAt = Date.from(Instant.now());
  }
}
