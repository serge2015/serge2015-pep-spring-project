package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.service.AccountService;
import com.example.service.MessageService;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.http.HttpResponse;
import java.util.*;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 * 
 * 
 * 
 */
@Controller
@RequestMapping()
public class SocialMediaController {

    private MessageService messageService;

    public SocialMediaController(MessageService messageService){
        this.messageService = messageService;
    }

    @GetMapping("messages")
    public @ResponseBody List<Message> getMessages(){
        return messageService.getMessages();
    }

    @GetMapping("messages/{messageId}")
    public @ResponseBody ResponseEntity<String> findByMessageId(@PathVariable int messageId){
        Message messageToFind = messageService.findByMessageId(messageId);
        if (messageToFind.getMessageText() == null){
            return ResponseEntity.status(200).body("");
        } else {
            return ResponseEntity.status(200).body(messageToFind.toString());
        }
    }

    @GetMapping("accounts/{accountId}/messages")
    public @ResponseBody ResponseEntity<List<Message>> getMessagesByAccountList(@PathVariable int accountId){
        List<Message> accountMessageList = messageService.getMessagesByAccountList(accountId);
        return ResponseEntity.status(200).body(accountMessageList);
    }

    @DeleteMapping("messages/{messageId}")
    public @ResponseBody ResponseEntity<String> deleteMessage(@PathVariable int messageId){
        Boolean deleted = messageService.deleteMessage(messageId);
        if (deleted){
            return ResponseEntity.status(200).body("1");
        } else {
            return ResponseEntity.status(200).body(null);
        }
    }

    @PatchMapping("messages/{messageId}")
    public @ResponseBody ResponseEntity<String> patchMessage(@PathVariable int messageId, @RequestBody String messageText){
        Message messageToFind = messageService.findByMessageId(messageId);        
        if (messageText.charAt(17) != '"' && messageText.length() <= 255 && messageToFind.getMessageId() != null){
            messageService.patchMessage(messageId, messageText);
            return ResponseEntity.status(200).body("1");
        } else {
            return ResponseEntity.status(400).body(null);
        }
    }

    @PostMapping("messages")
    public @ResponseBody ResponseEntity<Message> addNewMessage(@RequestBody Message newMessage){
        if (newMessage.getMessageText().length() > 0 && newMessage.getMessageText().length() <= 255 && !messageService.getMessagesByAccountList(newMessage.getPostedBy()).isEmpty()){
            messageService.addNewMessage(newMessage);
            return ResponseEntity.status(200).body(newMessage);
        } else {
            return ResponseEntity.status(400).body(newMessage);
        }
    }

    @RestController
    @RequestMapping()
    public class AccountController {
        private AccountService accountService;

        @Autowired
        public AccountController(AccountService accountService){
            this.accountService = accountService;
        }

        @PostMapping("login")
        public ResponseEntity<String> login(@RequestBody Account account){
            Account loggedInAccount  = accountService.login(account.getUsername(), account.getPassword());
            if (loggedInAccount.getUsername() == null){
                return ResponseEntity.status(401).body("");
            } else {
                return ResponseEntity.status(200).body(loggedInAccount.toString());
            }
        }

        @PostMapping("register")
        public ResponseEntity<String> register(@RequestBody Account account){
            Optional<Account> userAccountFound = accountService.findByUsername(account.getUsername());
            if (account.getUsername().length() > 0 && account.getPassword().length() > 3 && !userAccountFound.isPresent()){
                accountService.register(account);
                return ResponseEntity.status(200).body(account.toString());
            } else if (userAccountFound.isPresent()){
                return ResponseEntity.status(409).body(null);
            } else {
                return ResponseEntity.status(400).body(null);
            }

        }
    }
}
