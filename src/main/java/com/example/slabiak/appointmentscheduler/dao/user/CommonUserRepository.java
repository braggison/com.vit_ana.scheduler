package com.example.slabiak.appointmentscheduler.dao.user;

import com.example.slabiak.appointmentscheduler.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@NoRepositoryBean
public interface CommonUserRepository<T extends User> extends JpaRepository<T, UUID> {

    Optional<T> findByUserName(String userName);

    @Query("select t from #{#entityName} t inner join t.roles r where r.name in :roleName")
    List<T> findByRoleName(@Param("roleName") String roleName);
}
