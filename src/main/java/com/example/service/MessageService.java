package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.example.entity.Message;
import com.example.repository.MessageRepository;

import java.util.*;

@Service
public class MessageService {

    private MessageRepository messageRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository){
        this.messageRepository = messageRepository;
    }

    public List<Message> getMessages() {return (List<Message>) messageRepository.findAll();}

    public Optional<Message> findByMessageId(int messageId){
        return messageRepository.findById(messageId);
    }

    public List<Message> getMessagesByAccountList(int accountId){
        List<Message> accountMessageList = messageRepository.findByPostedBy(accountId);
        return accountMessageList;
    }

    public void deleteMessage(int messageId){
        messageRepository.deleteById(messageId);
        return;
    }

    public void patchMessage(int messageId, String messageText){
        Message patchedMessage  = messageRepository.findById(messageId).orElseThrow();
        patchedMessage.setMessageText(messageText);
        messageRepository.save(patchedMessage);
        return;
    }

    public void addNewMessage(Message newMessage) { messageRepository.save(newMessage); }
}
