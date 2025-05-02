package burundi.ilucky.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import burundi.ilucky.model.Gift;
import burundi.ilucky.model.LuckyHistory;
import burundi.ilucky.model.User;
import burundi.ilucky.model.dto.BuyTurnDTO;
import burundi.ilucky.model.dto.LuckyHistoryDTO;
import burundi.ilucky.payload.LuckyResponse;
import burundi.ilucky.payload.Response;
import burundi.ilucky.service.GiftService;
import burundi.ilucky.service.LuckyService;
import burundi.ilucky.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;

@RestController
@RequestMapping("/api/lucky")
@Log4j2
public class LuckyController {
    @Autowired
    private UserService userService;

    @Autowired
    private LuckyService luckyService;

    @PostMapping("/play")
    public ResponseEntity<?> play(@AuthenticationPrincipal UserDetails userDetails) {
        try {

            if (userDetails != null) {
                User user = userService.findByUserName(userDetails.getUsername());

                if (user.getTotalPlay() <= 0) {
                    return ResponseEntity
                            .ok(new Response("FAILED", "Bạn đã hết lượt chơi. Mua thêm để chơi tiếp", null));
                } else {
                    Gift gift = luckyService.lucky(user);
                    return ResponseEntity.ok(new LuckyResponse("OK", gift, user.getTotalPlay()));
                }
            } else {
                Gift gift = luckyService.lucky();
                return ResponseEntity.ok(new LuckyResponse("OK", gift));
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/history")
    public ResponseEntity<?> history(@AuthenticationPrincipal UserDetails userDetails) {
        try {
            if (userDetails != null) {
                User user = userService.findByUserName(userDetails.getUsername());
                List<LuckyHistory> luckyHistories = luckyService.getHistoriesByUserId(user.getId());
                List<LuckyHistoryDTO> luckyGiftHistoriesDTO = luckyService.convertLuckyHistoriesToDTO(luckyHistories);
                return ResponseEntity.ok(luckyGiftHistoriesDTO);
            } else {
                List<LuckyHistory> luckyHistories = luckyService.getHistoriesByUserId(null);
                List<LuckyHistoryDTO> luckyGiftHistoriesDTO = luckyService.convertLuckyHistoriesToDTO(luckyHistories);
                return ResponseEntity.ok(luckyGiftHistoriesDTO);
            }

        } catch (Exception e) {
            log.warn(e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/get_all_gift")
    public ResponseEntity<?> getAllGift() {
        try {
            return ResponseEntity.ok(GiftService.gifts);
        } catch (Exception e) {
            log.warn(e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/buy-turn")
    public ResponseEntity<Response> logout(@Valid @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody BuyTurnDTO buyTurnDTO) {

        return new ResponseEntity<>(luckyService.buyTurn(userDetails, buyTurnDTO.getTurns()), HttpStatus.OK);
    }

}
