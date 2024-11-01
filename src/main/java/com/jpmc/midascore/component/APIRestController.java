package com.jpmc.midascore.component;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.foundation.Balance;
import com.jpmc.midascore.repository.UserRepository;

@RestController
public class APIRestController {
    private final UserRepository userRepository;

    public APIRestController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/balance")
    public Balance controller(@RequestParam("userId") long userId) {
        UserRecord user = userRepository.findById(userId);
        float balance = 0;
        if (user != null) {
            balance = user.getBalance();
        }
        return new Balance(balance);
    }
}
