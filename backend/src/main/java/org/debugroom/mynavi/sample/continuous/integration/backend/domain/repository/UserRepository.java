package org.debugroom.mynavi.sample.continuous.integration.backend.domain.repository;

import org.debugroom.mynavi.sample.continuous.integration.backend.domain.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>,
        JpaSpecificationExecutor<User> {


    public Optional<User> findByLoginId(String loginId);
    public Boolean existsByLoginId(String loginId);

    @Query("select coalesce(max(u.userId), 0) from User u")
    public Long getMaxUserId();

}
