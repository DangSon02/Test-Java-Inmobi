package burundi.ilucky.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import burundi.ilucky.Exeption.InvalidDataException;
import burundi.ilucky.Exeption.UserNotAuthenticatedException;
import burundi.ilucky.model.Gift;
import burundi.ilucky.model.LuckyHistory;
import burundi.ilucky.model.User;
import burundi.ilucky.model.dto.LuckyHistoryDTO;
import burundi.ilucky.payload.Response;
import burundi.ilucky.repository.LuckyHistoryRepository;
import burundi.ilucky.repository.UserRepository;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class LuckyService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LuckyHistoryRepository luckyHistoryRepository;

    @Autowired
    private UserService userService;

    private static final int TURN_PRICE = 1000;

    public List<LuckyHistory> getHistoriesByUserId(Long userId) {
        return luckyHistoryRepository.findByUserIdOrderByAddTimeDesc(userId);
    }

    public Gift lucky(User user) {
        Gift gift = GiftService.getRandomGift();

        LuckyHistory luckyHistory = new LuckyHistory();
        if (gift.getType().equals("VND")) {
            user.setTotalVnd(user.getTotalVnd() + gift.getNoItem());
        } else if (gift.getType().equals("STARS")) {
            user.setTotalStar(user.getTotalStar() + gift.getNoItem());
        }

        luckyHistory.setGiftType(gift.getType());
        luckyHistory.setAddTime(new Date());
        luckyHistory.setGiftId(gift.getId());
        luckyHistory.setNoItem(gift.getNoItem());
        luckyHistory.setUser(user);
        luckyHistoryRepository.save(luckyHistory);

        user.setTotalPlay(user.getTotalPlay() - 1);
        userRepository.save(user);

        return gift;
    }

    public Gift lucky() {
        Gift gift = GiftService.getRandomGift();

        LuckyHistory luckyHistory = new LuckyHistory();
        luckyHistory.setGiftType(gift.getType());
        luckyHistory.setAddTime(new Date());
        luckyHistory.setGiftId(gift.getId());
        luckyHistory.setNoItem(gift.getNoItem());
        luckyHistoryRepository.save(luckyHistory);

        return gift;
    }

    public List<LuckyHistoryDTO> convertLuckyHistoriesToDTO(List<LuckyHistory> luckyGiftHistories) {
        List<LuckyHistoryDTO> luckyGiftHistoriesDTO = new ArrayList<>();

        for (LuckyHistory item : luckyGiftHistories) {
            Gift gift = GiftService.gifts.get(item.getGiftId());
            LuckyHistoryDTO luckyHistoryDTO = new LuckyHistoryDTO(item.getAddTime(), gift);
            luckyGiftHistoriesDTO.add(luckyHistoryDTO);
        }

        return luckyGiftHistoriesDTO;
    }

    public Response buyTurn(UserDetails userDetails, int turns) {

        if (userDetails == null) {
            throw new UserNotAuthenticatedException("You need to login to deposit.");
        }

        if (turns <= 0) {
            throw new InvalidDataException("Invalid turn.");
        }

        User user = userService.findByUserName(userDetails.getUsername());
        long totalPrice = turns * TURN_PRICE;

        if (user.getTotalVnd() < totalPrice) {
            throw new InvalidDataException("Not enough balance to buy turn.");
        }

        user.setTotalVnd(user.getTotalVnd() - totalPrice);
        user.setTotalPlay(user.getTotalPlay() + turns);
        userService.saveUser(user);

        Map<String, Object> data = new HashMap<>();
        data.put("totalPlay", user.getTotalPlay());
        data.put("totalVnd", user.getTotalVnd());
        return new Response("SUCCESS", "Mua lượt thành công.", data);
    }

}
