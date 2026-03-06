package com.project.finance_api.service;

import com.project.finance_api.dto.FeedbackResponse;
import com.project.finance_api.entity.Feedback;
import com.project.finance_api.enums.FeedbackStatus;
import com.project.finance_api.repository.FeedbackRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FeedbackService {
    private final FeedbackRepository feedbackRepository;
    private final JavaMailSender mailSender;

    public Feedback createFeedback(Feedback feedback) {
        feedback.setFeedbackStatus(FeedbackStatus.ISSUED);
        return feedbackRepository.save(feedback);
    }

    public List<Feedback> getAllFeedbacks() {
        return feedbackRepository.findAll();
    }


    public Feedback resolveFeedback(FeedbackResponse feedbackResponse) {
        Feedback feedback = feedbackRepository.findById(feedbackResponse.getFeedback().getId())
                        .orElseThrow(() -> new EntityNotFoundException("Feedback not found"));
        sendMail(feedback.getUser().getEmail(), feedbackResponse.getResponse());
        feedback.setFeedbackStatus(FeedbackStatus.RESOLVED);
        return feedbackRepository.save(feedback);
    }

    @Transactional
    public void deleteFeedback(Long id) {
        feedbackRepository.deleteById(id);
    }

    @Transactional
    public void deleteResolvedFeedback() {
        feedbackRepository.deleteByFeedbackStatus(FeedbackStatus.RESOLVED);
    }

    private void sendMail(String email, String message) {
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(email);
        mail.setSubject("Thank you for sharing your feedback");
        mail.setText(message);

        mailSender.send(mail);
    }
}
