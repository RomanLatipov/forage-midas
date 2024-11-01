package com.jpmc.midascore.component;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;
import org.springframework.stereotype.Component;

import com.jpmc.midascore.entity.TransactionRecord;
import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.repository.TransactionRepository;
import com.jpmc.midascore.repository.UserRepository;
import com.jpmc.midascore.foundation.Incentive;

@Component
public class HandleTransaction {
    private final TransactionRepository transactionRecordInterface;
    private final UserRepository userRepository;
    private final RestTemplate restTemplate;

    public HandleTransaction(TransactionRepository transactionRecordInterface, UserRepository userRepository, RestTemplateBuilder builder) {
        this.transactionRecordInterface = transactionRecordInterface;
        this.userRepository = userRepository;
        this.restTemplate = builder.build();
    }

    public UserRecord getUser(long id) {
        return userRepository.findById(id);
    }
    
    public boolean isValid(Transaction transaction) {
        UserRecord sender = getUser(transaction.getSenderId());
        UserRecord recipient = getUser(transaction.getRecipientId());

        if (sender.getBalance() < transaction.getAmount() && sender != null && recipient != null) {
            return true;
        }
        return false;
    }
    
    public void transactionHandler(Transaction transaction) {
        if (isValid(transaction)) {
            Incentive response = restTemplate.postForObject("http://localhost:8080/incentive/", transaction, Incentive.class);
            transaction.setIncentive(response.getAmount());

            UserRecord sender = getUser(transaction.getSenderId());
            sender.setBalance(sender.getBalance() - transaction.getAmount());
            // userRepository.save(sender);

            UserRecord recipient = getUser(transaction.getRecipientId());
            recipient.setBalance(recipient.getBalance() + transaction.getAmount() + response.getAmount());
            // userRepository.save(recipient);

            TransactionRecord transactionRecord = new TransactionRecord(sender, recipient, transaction.getAmount(), response.getAmount());
            transactionRecordInterface.save(transactionRecord);




        }
        
    }
}
