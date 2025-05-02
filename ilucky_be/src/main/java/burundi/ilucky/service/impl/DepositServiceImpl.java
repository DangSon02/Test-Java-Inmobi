package burundi.ilucky.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import burundi.ilucky.Exeption.InvalidDataException;
import burundi.ilucky.Exeption.UserNotAuthenticatedException;
import burundi.ilucky.model.User;
import burundi.ilucky.model.dto.DepositDTO;
import burundi.ilucky.payload.Response;
import burundi.ilucky.service.DepositService;
import burundi.ilucky.service.UserService;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class DepositServiceImpl implements DepositService {

    @Autowired
    private UserService userService;

    @Override
    public Response deposit(UserDetails userDetails, DepositDTO depositDTO) {

        if (depositDTO.getAmount() <= 0) {
            throw new InvalidDataException("Invalid deposit amount.");
        }

        User user = userService.findByUserName(userDetails.getUsername());

        if (user == null) {
            throw new UserNotAuthenticatedException("You need to login to deposit.");
        }

        user.setTotalVnd(user.getTotalVnd() + depositDTO.getAmount());
        userService.saveUser(user);

        Map<String, Object> data = new HashMap<>();

        data.put("totalVnd", user.getTotalVnd());

        return new Response("SUCCESS", "Deposit successful", data);

    }

}
