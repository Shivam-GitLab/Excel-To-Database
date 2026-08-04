package com.trangile.sftptodb.service.order;

import com.trangile.sftptodb.entity.Orders;
import com.trangile.sftptodb.repository.OrdersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrdersRepository ordersRepository;

    public void createOrder(
            String whseId,
            String generatedOrderKey,
            String orderType,
            LocalDateTime orderDate,
            String workCenter,
            String consigneeKey,
            String lnOrderNumber) {

        if (ordersRepository.existsById(generatedOrderKey)) {
            return;
        }

        Orders order = new Orders();

        order.setWhseId(whseId);
        order.setStorerKey("GA");
        order.setOrderKey(generatedOrderKey);
        order.setType(orderType);
        order.setOrderDate(orderDate);
        order.setExternalOrderKey2(lnOrderNumber);
        order.setConsigneeKey(consigneeKey);
        order.setSusr1(workCenter);

        ordersRepository.save(order);
    }
}