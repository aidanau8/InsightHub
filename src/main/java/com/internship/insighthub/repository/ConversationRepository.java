package com.internship.insighthub.repository;

import com.internship.insighthub.entity.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ConversationRepository extends JpaRepository<Conversation, Long> {

    List<Conversation> findByUserIdOrderByUpdatedAtDesc(Long userId);

    @Modifying
    @Query("update Conversation c set c.active = false where c.section.id = :sectionId and c.user.id = :userId")
    void deactivateBySectionIdAndUserId(@Param("sectionId") Long sectionId,
                                        @Param("userId") Long userId);
}