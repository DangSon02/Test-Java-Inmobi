package burundi.ilucky.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import burundi.ilucky.model.User;
import jakarta.transaction.Transactional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    User findByUsername(String username);

    @Transactional
    @Query("UPDATE User u SET u.totalPlay = u.totalPlay + 5")
    int updateTotalPlayDaily();

    @Query("SELECT u FROM User u ORDER BY u.totalStar DESC")
    List<User> getUserByRanking();

}
