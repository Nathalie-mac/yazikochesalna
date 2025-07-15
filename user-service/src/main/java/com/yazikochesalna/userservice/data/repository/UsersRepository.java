package com.yazikochesalna.userservice.data.repository;

import com.yazikochesalna.userservice.data.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {

    Optional<Users> findById (long id);

    @Query("SELECT u FROM Users u WHERE u.username LIKE :prefix%")
    List<Users> findUserIdsByUsernameStartingWith(@Param("prefix") String prefix,
                                                 Pageable pageable);

    List<Users> findAllByIdIn(List<Long> ids);

    boolean existsByUsername(String username);

    @Transactional
    @Modifying
    @Query("UPDATE Users u SET u.fileUuid = :fileUuid WHERE u.id = :userId")
    void updateFileUuid(@Param("userId") Long userId, @Param("fileUuid") UUID fileUuid);

}
