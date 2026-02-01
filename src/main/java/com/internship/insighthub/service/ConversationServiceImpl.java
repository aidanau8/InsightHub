package com.internship.insighthub.service;

import com.internship.insighthub.dto.ConversationDto;
import com.internship.insighthub.dto.MessageDto;
import com.internship.insighthub.entity.Conversation;
import com.internship.insighthub.entity.Message;
import com.internship.insighthub.exception.NotFoundException;
import com.internship.insighthub.repository.ConversationRepository;
import com.internship.insighthub.repository.MessageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ConversationServiceImpl implements ConversationService {

    private final ConversationRepository conversationRepository;
    private final MessageRepository messageRepository;

    public ConversationServiceImpl(ConversationRepository conversationRepository,
                                   MessageRepository messageRepository) {
        this.conversationRepository = conversationRepository;
        this.messageRepository = messageRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public List<ConversationDto> getUserConversations(Long userId) {
        return conversationRepository.findByUserIdOrderByUpdatedAtDesc(userId).stream()
                .map(c -> toConversationDto(c, false))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public ConversationDto getConversation(Long conversationId) {
        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new NotFoundException("Conversation not found: " + conversationId));

        return toConversationDto(conversation, true);
    }

    private ConversationDto toConversationDto(Conversation conversation, boolean withHistory) {
        List<MessageDto> history = withHistory
                ? messageRepository.findByConversationIdOrderByCreatedAtAsc(conversation.getId()).stream()
                .map(this::toMessageDto)
                .collect(Collectors.toList())
                : List.of();

        return new ConversationDto(
                conversation.getId(),
                conversation.getSection().getId(),
                conversation.getTitle(),
                conversation.isActive(),
                conversation.getModelName(),
                toLocalDateTime(conversation.getCreatedAt()),
                toLocalDateTime(conversation.getUpdatedAt()),
                history
        );
    }

    private MessageDto toMessageDto(Message message) {
        return new MessageDto(
                message.getId(),
                message.getRole(),
                message.getContent(),
                toLocalDateTime(message.getCreatedAt())
        );
    }

    private LocalDateTime toLocalDateTime(Instant instant) {
        return instant == null ? null : LocalDateTime.ofInstant(instant, ZoneOffset.UTC);
    }
}