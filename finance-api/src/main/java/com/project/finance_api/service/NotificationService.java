package com.project.finance_api.service;

import com.project.finance_api.entity.Notification;
import com.project.finance_api.repository.NotificationRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;

    public Notification createNotification(Notification notification) {
        notification.setIsRead(Boolean.FALSE);
        return notificationRepository.save(notification);
    }

    public Notification getIndividualNotificationByUser(Long id) {
        return notificationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Notification not found"));
    }

    public List<Notification> getAllNotificationsByUser(Long id) {
        return notificationRepository.findByUserId(id);
    }

    public Notification updateNotificationReadStatus(Long id) {
        return notificationRepository.findById(id).map(notification -> {
            notification.setIsRead(Boolean.TRUE);
            return notificationRepository.save(notification);
        }).orElseThrow(() -> new EntityNotFoundException("Notification not found with id: "+id));
    }

    @Transactional
    public void deleteNotification(Long id) {
        notificationRepository.deleteById(id);
    }

    @Transactional
    public void deleteAllNotificationsByUser(Long id) {
        notificationRepository.deleteByUserId(id);
    }
}
