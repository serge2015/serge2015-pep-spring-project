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

    public Message findByMessageId(int messageId){
        Optional<Message> messageFound = messageRepository.findById(messageId);
        Message message = new Message();
        if (messageFound.isPresent()){
            message = messageFound.get();
        } 
        return message;
    }

    public List<Message> getMessagesByAccountList(int accountId){
        List<Message> accountMessageList = messageRepository.findByPostedBy(accountId);
        return accountMessageList;
    }

    public boolean deleteMessage(int messageId){
        Optional<Message> messageToDelete = messageRepository.findById(messageId);
        if (messageToDelete.isPresent()){
            messageRepository.deleteById(messageId);
            return true;
        }
        return false;
    }

    public void patchMessage(int messageId, String messageText){
        Optional<Message> patchedMessage  = messageRepository.findById(messageId);
        if (patchedMessage.isPresent()){
            Message message = patchedMessage.get();
            message.setMessageText(messageText);
            messageRepository.save(message);
        }
        return;
    }

    public void addNewMessage(Message newMessage) { messageRepository.save(newMessage); }
}
