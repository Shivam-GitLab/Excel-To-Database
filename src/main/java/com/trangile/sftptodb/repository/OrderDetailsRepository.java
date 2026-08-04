package com.trangile.sftptodb.repository;

import com.trangile.sftptodb.entity.OrderDetailId;
import com.trangile.sftptodb.entity.OrderDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDetailsRepository
        extends JpaRepository<OrderDetails, OrderDetailId> {
}