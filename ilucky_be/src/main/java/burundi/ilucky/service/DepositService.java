package burundi.ilucky.service;

import org.springframework.security.core.userdetails.UserDetails;

import burundi.ilucky.model.dto.DepositDTO;
import burundi.ilucky.payload.Response;

public interface DepositService {

    Response deposit(UserDetails userDetails, DepositDTO depositDTO);

}
