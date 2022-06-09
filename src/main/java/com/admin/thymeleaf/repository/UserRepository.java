package com.admin.thymeleaf.repository;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.admin.thymeleaf.entity.Users;

@Repository
public interface UserRepository extends CrudRepository<Users, Long>  {
    public Optional<Users> findByUsername(String username);
}