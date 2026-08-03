package com.trangile.sftptodb.service.order;

import com.trangile.sftptodb.entity.Orders;
import com.trangile.sftptodb.repository.OrdersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrdersRepository ordersRepository;

    public Orders createOrder(
            String whseId,
            String generatedOrderKey,
            String orderType,
            LocalDate orderDate,
            String workCenter,
            String consigneeKey,
            String lnOrderNumber) {

        Orders order = new Orders();

        order.setWhseId(whseId);
        order.setStorerKey("GA");
        order.setOrderKey(generatedOrderKey);
        order.setType(orderType);
        order.setOrderDate(orderDate);
        order.setExternOrderKey2(lnOrderNumber);
        order.setConsigneeKey(consigneeKey);
        order.setSusr1(workCenter);

        return ordersRepository.save(order);
    }
}