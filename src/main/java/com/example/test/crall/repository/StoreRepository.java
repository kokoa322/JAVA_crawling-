package com.example.test.crall.repository;

import com.example.test.crall.domain.Notice;
import com.example.test.crall.domain.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {
}