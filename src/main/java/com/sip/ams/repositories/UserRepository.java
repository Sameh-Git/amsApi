package com.sip.ams.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.sip.ams.entities.Provider;
import com.sip.ams.entities.User;
@Repository("userRepository")
public interface UserRepository extends CrudRepository<User, Integer>{
    User findByEmail(String email);
}