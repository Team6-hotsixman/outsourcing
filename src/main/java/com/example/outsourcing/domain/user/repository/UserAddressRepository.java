package com.example.outsourcing.domain.user.repository;

import com.example.outsourcing.domain.user.entity.User;
import com.example.outsourcing.domain.user.entity.UserAddress;
import com.example.outsourcing.domain.user.enums.AddressStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserAddressRepository extends JpaRepository<UserAddress, Long> {
    Optional<UserAddress> findByUserIdAndAddressStatus(Long userId, AddressStatus addressStatus);

    @Query(value = "select a from UserAddress a join fetch a.user where a.id = :addressId ")
    Optional<UserAddress> findByIdWithUser(Long addressId);

    boolean existsByUserIdAndAddress(Long userId, String address);

    List<UserAddress> findByUser(User user);
}
