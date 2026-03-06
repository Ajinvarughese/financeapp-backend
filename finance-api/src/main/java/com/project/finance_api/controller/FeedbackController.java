package com.project.finance_api.controller;

import com.project.finance_api.component.FileUpload;
import com.project.finance_api.dto.FeedbackResponse;
import com.project.finance_api.entity.Feedback;
import com.project.finance_api.service.FeedbackService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("api/feedback")
@AllArgsConstructor
public class FeedbackController {
    private final FeedbackService feedbackService;
    private final FileUpload fileUpload;

    @PostMapping(
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Feedback> createFeedback(
            @RequestPart("feedback") String feedbackJson,
            @RequestPart(name = "file", required = false) MultipartFile document
    ) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Feedback feedback = mapper.readValue(feedbackJson, Feedback.class);

        if (document != null && !document.isEmpty()) {
            String fileUrl = fileUpload.uploadFile(document);
            feedback.setDocument(fileUrl);
        }

        Feedback savedFeedback = feedbackService.createFeedback(feedback);

        return ResponseEntity.ok(savedFeedback);
    }

    @GetMapping
    public ResponseEntity<List<Feedback>> getAllFeedbacks() {
        return ResponseEntity.ok(feedbackService.getAllFeedbacks());
    }

    @PostMapping
    public ResponseEntity<Feedback> resolveFeedback(@RequestBody FeedbackResponse feedbackResponse) {
        return ResponseEntity.ok(feedbackService.resolveFeedback(feedbackResponse));
    }

    @DeleteMapping("/delete/resolved")
    public void deleteResolvedFeedback() {
        feedbackService.deleteResolvedFeedback();
    }

    @DeleteMapping("/delete/{id}")
    public void deleteFeedbackById(@PathVariable Long id) {
        feedbackService.deleteFeedback(id);
    }

}
