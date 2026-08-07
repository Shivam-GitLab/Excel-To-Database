package com.trangile.sftptodb.service.order;

import com.trangile.sftptodb.entity.ExcelStaging;
import com.trangile.sftptodb.entity.Orders;
import com.trangile.sftptodb.repository.OrdersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrdersRepository ordersRepository;

    public void createOrder(ExcelStaging staging, String generatedOrderKey) {

        if (ordersRepository.existsById(generatedOrderKey)) {
            return;
        }

        Orders order = Orders.builder()
                .whseId(staging.getWarehouseId())
                .storerKey("GA")
                .orderKey(generatedOrderKey)
                .type(staging.getOrderType())
                .orderDate(staging.getOrderDate())
                .externalOrderKey2(staging.getLnOrderNumber())
                .consigneeKey(staging.getConsigneeKey())
                .status("02")
                .susr1(staging.getWorkCenter())
                .build();

        ordersRepository.save(order);
    }
}