
package com.hexaware.lms.dto;

import com.hexaware.lms.entity.User;
import com.hexaware.lms.utils.NotificationType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.AssertFalse;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotificationResponseDTO {
    private Long id;
    @Size(max = 100, min = 5, message = "Invalid message. Size should be between 3 to 30.")
    private String message;
    private boolean seen;
    @Enumerated(value = EnumType.STRING)
    private NotificationType type;
    private Long userID;
}
