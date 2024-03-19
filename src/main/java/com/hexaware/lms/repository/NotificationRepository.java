package com.hexaware.lms.repository;


import com.hexaware.lms.entity.Notification;
import com.hexaware.lms.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByUser(Optional<User> user);
    //set a notification as read/seen
    @Modifying
    @Transactional
    @Query("UPDATE Notification n SET n.seen = true WHERE n.id = :notificationId")
    void markNotificationAsSeen(Long notificationId);

    //set all notification as read/seen
    @Modifying
    @Transactional
    @Query("UPDATE Notification n SET n.seen = true WHERE n.user.id = :userId")
    void markAllNotificationsAsSeenForUser(Long userId);
}
