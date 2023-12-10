package com.em.taskmanager.repositories;

import com.em.taskmanager.entities.Member;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepo extends CrudRepository<Member, Long> {
    Optional<Member> findByEmail(String email);
}
