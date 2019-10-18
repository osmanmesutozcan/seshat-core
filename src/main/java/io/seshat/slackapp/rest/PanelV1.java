package io.seshat.slackapp.rest;


import io.seshat.slackapp.dto.QuestionAnswerDTO;

import io.seshat.slackapp.service.QuestionService;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController()
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class PanelV1 {

  private final QuestionService questionService;

  @GetMapping(path = "dashboard")
  public Map<String, List<QuestionAnswerDTO>> getDashboard() {
    return questionService.getDashboardData();
  }
}
