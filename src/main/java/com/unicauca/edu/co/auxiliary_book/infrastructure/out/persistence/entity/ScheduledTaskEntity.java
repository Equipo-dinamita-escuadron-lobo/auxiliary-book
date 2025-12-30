package com.unicauca.edu.co.auxiliary_book.infrastructure.out.persistence.entity;

import java.time.Instant;

import com.unicauca.edu.co.auxiliary_book.domain.models.scheduledTasks.ScheduledTaskStatus;
import com.unicauca.edu.co.auxiliary_book.domain.models.scheduledTasks.ScheduledTaskType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "scheduled_tasks")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScheduledTaskEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, updatable = false)
    private String publicId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ScheduledTaskType taskType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ScheduledTaskStatus status;

    @Column(nullable = false)
    private Instant executeAt;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String payloadJson;

    private String artifactPath;
    private Instant lastExecutionAt;
    private String lastError;
    private Integer attempts;
    private Instant lockedUntil;

    @CreationTimestamp
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;
}
