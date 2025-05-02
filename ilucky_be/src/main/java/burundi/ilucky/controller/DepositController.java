package burundi.ilucky.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import burundi.ilucky.model.dto.DepositDTO;
import burundi.ilucky.payload.Response;
import burundi.ilucky.service.DepositService;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;

@RestController
@Log4j2
@RequestMapping("/api/deposit")
public class DepositController {

    @Autowired
    private DepositService depositService;

    @PostMapping("")
    public ResponseEntity<Response> logout(@Valid @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody DepositDTO depositDTO) {

        return new ResponseEntity<>(depositService.deposit(userDetails, depositDTO), HttpStatus.OK);
    }

}
