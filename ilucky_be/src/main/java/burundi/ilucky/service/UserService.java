package burundi.ilucky.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import burundi.ilucky.model.User;
import burundi.ilucky.model.dto.UserRankingDTO;
import burundi.ilucky.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class UserService {
	@Autowired
	private UserRepository userRepository;

	public User findByUserName(String username) {
		try {
			return userRepository.findByUsername(username);
		} catch (Exception e) {
			return null;
		}
	}

	public User saveUser(User user) {
		return userRepository.save(user);
	}

	@Transactional
	public void addDailyBonusPlays() {

		int updateTotalPlay = userRepository.updateTotalPlayDaily();

		log.info("Đã cộng 5 lượt chơi cho tất cả user.");
		log.info("Số lượng user được cập nhật: {}", updateTotalPlay);

	}

	public List<UserRankingDTO> getAllUserByRanking() {

		List<User> users = userRepository.getUserByRanking();
		return users.stream().map(user -> new UserRankingDTO(user.getUsername(), user.getTotalStar()))
				.collect(Collectors.toList());
	}

}
