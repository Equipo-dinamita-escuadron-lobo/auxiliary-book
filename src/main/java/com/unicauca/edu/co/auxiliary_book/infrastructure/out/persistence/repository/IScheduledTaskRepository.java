package com.unicauca.edu.co.auxiliary_book.infrastructure.out.persistence.repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.unicauca.edu.co.auxiliary_book.domain.models.scheduledTasks.ScheduledTaskStatus;
import com.unicauca.edu.co.auxiliary_book.domain.models.scheduledTasks.ScheduledTaskType;
import com.unicauca.edu.co.auxiliary_book.infrastructure.out.persistence.entity.ScheduledTaskEntity;

@Repository
public interface IScheduledTaskRepository extends JpaRepository<ScheduledTaskEntity, Long> {

    Optional<ScheduledTaskEntity> findByPublicId(String publicId);

    @Query("""
            SELECT st FROM ScheduledTaskEntity st
             WHERE (:status IS NULL OR st.status = :status)
               AND (:taskType IS NULL OR st.taskType = :taskType)
            """)
    Page<ScheduledTaskEntity> findByFilters(@Param("status") ScheduledTaskStatus status,
                                            @Param("taskType") ScheduledTaskType taskType,
                                            Pageable pageable);

    @Query("""
            SELECT st FROM ScheduledTaskEntity st
             WHERE st.status = :status
               AND st.executeAt <= :reference
               AND (st.lockedUntil IS NULL OR st.lockedUntil < :reference)
             ORDER BY st.executeAt ASC
            """)
    List<ScheduledTaskEntity> findPendingForExecution(@Param("status") ScheduledTaskStatus status,
                                                      @Param("reference") Instant reference,
                                                      Pageable pageable);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
            UPDATE ScheduledTaskEntity st
               SET st.lockedUntil = :lockUntil
             WHERE st.id = :taskId
               AND (st.lockedUntil IS NULL OR st.lockedUntil < :reference)
            """)
    int acquireLock(@Param("taskId") Long taskId,
                    @Param("reference") Instant reference,
                    @Param("lockUntil") Instant lockUntil);
}
