package com.project.finance_api.controller;

import com.project.finance_api.entity.Notification;
import com.project.finance_api.entity.User;
import com.project.finance_api.service.NotificationService;
import com.project.finance_api.service.UserService;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("api/notification")
@AllArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<Notification> saveNotification(@RequestBody Notification notification) {
        return ResponseEntity.ok(notificationService.createNotification(notification));
    }

    @GetMapping("/user")
    public ResponseEntity<?> getUserNotification(
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.replace("Bearer ", "").trim();

        try {
            User user = userService.getUserByToken(token);
            return ResponseEntity.ok(notificationService.getAllNotificationsByUser(user.getId()));

        } catch (ExpiredJwtException e) {
            return ResponseEntity.status(401).body(
                Map.of(
                    "status", 401,
                    "error", "UNAUTHORIZED",
                    "message", "Token expired. Please login again."
                )
            );
        }
    }

    @PutMapping
    public ResponseEntity<Notification> markNotificationRead(@RequestBody Notification notification) {
        return ResponseEntity.ok(notificationService.updateNotificationReadStatus(notification.getId()));
    }

    @DeleteMapping("/{id}")
    public void deleteNotification(@PathVariable Long id) {
        notificationService.deleteNotification(id);
    }

    @DeleteMapping("/user")
    public ResponseEntity<?> deleteAllUserNotifications(
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.replace("Bearer ", "").trim();

        try {
            User user = userService.getUserByToken(token);

            notificationService.deleteAllNotificationsByUser(user.getId());

            return ResponseEntity.ok(
                    Map.of(
                            "status", 200,
                            "message", "All notifications deleted successfully"
                    )
            );

        } catch (ExpiredJwtException e) {
            return ResponseEntity.status(401).body(
                    Map.of(
                            "status", 401,
                            "error", "UNAUTHORIZED",
                            "message", "Token expired. Please login again."
                    )
            );
        }
    }
}
