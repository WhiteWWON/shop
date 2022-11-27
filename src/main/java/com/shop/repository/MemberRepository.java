package com.shop.repository;

import com.shop.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member,Long> {  // Long 인자는 뭘 의미하는건지..
    Member findByEmail(String email);
}
