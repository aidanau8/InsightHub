package com.internship.insighthub.service;

import com.internship.insighthub.dto.ConversationDetailDto;
import com.internship.insighthub.dto.ConversationSummaryDto;
import com.internship.insighthub.dto.MessageDto;
import com.internship.insighthub.entity.Conversation;
import com.internship.insighthub.entity.Message;
import com.internship.insighthub.entity.MessageRole;
import com.internship.insighthub.entity.User;
import com.internship.insighthub.repository.ConversationRepository;
import com.internship.insighthub.repository.MessageRepository;
import com.internship.insighthub.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ConversationServiceImpl implements ConversationService {

    private final ConversationRepository conversationRepository;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;


    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User not found"
                ));
    }

    @Override
    public List<ConversationSummaryDto> getUserConversations(String userEmail) {
        User user = getUserByEmail(userEmail);

        List<Conversation> conversations =
                conversationRepository.findByUserIdOrderByUpdatedAtDesc(user.getId());

        return conversations.stream()
                .map(conversation -> {
                    // все сообщения диалога (для счётчика и превью)
                    List<Message> messages =
                            messageRepository.findByConversationIdOrderByCreatedAtAsc(conversation.getId());

                    int count = messages.size();
                    String lastPreview = null;
                    if (!messages.isEmpty()) {
                        String content = messages.get(messages.size() - 1).getContent();
                        // короткое превью
                        lastPreview = content.length() > 50
                                ? content.substring(0, 50) + "..."
                                : content;
                    }

                    return ConversationSummaryDto.builder()
                            .id(conversation.getId())
                            .title(conversation.getTitle())
                            .modelName(conversation.getModelName())
                            .createdAt(conversation.getCreatedAt())
                            .updatedAt(conversation.getUpdatedAt())
                            .messageCount(count)
                            .lastMessagePreview(lastPreview)
                            .build();
                })
                .collect(Collectors.toList());
    }

    @Override
    public ConversationDetailDto getConversationDetail(String userEmail, Long conversationId) {
        User user = getUserByEmail(userEmail);

        Conversation conversation = conversationRepository
                .findByIdAndUserId(conversationId, user.getId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Conversation not found"
                ));

        List<Message> messages =
                messageRepository.findByConversationIdOrderByCreatedAtAsc(conversationId);

        List<MessageDto> messageDtos = messages.stream()
                .map(this::toMessageDto)
                .collect(Collectors.toList());

        return ConversationDetailDto.builder()
                .id(conversation.getId())
                .title(conversation.getTitle())
                .modelName(conversation.getModelName())
                .createdAt(conversation.getCreatedAt())
                .updatedAt(conversation.getUpdatedAt())
                .messages(messageDtos)
                .build();
    }

    @Override
    public void deleteConversation(String userEmail, Long conversationId) {
        User user = getUserByEmail(userEmail);

        Conversation conversation = conversationRepository
                .findByIdAndUserId(conversationId, user.getId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Conversation not found"
                ));

        conversationRepository.delete(conversation);

    }


    private MessageDto toMessageDto(Message message) {
        return new MessageDto(
                message.getId(),          // id
                message.getRole(),        // уже MessageRole, просто передаём
                message.getContent(),     // текст
                message.getCreatedAt()    // время создания
        );
    }
}

