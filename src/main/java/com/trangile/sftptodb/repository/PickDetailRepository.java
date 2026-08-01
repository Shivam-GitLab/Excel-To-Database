package com.trangile.sftptodb.repository;

import com.trangile.sftptodb.entity.WarehouseOrders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PickDetailRepository extends JpaRepository<WarehouseOrders, Long> {
}
