package com.trangile.sftptodb.service.order;

import com.trangile.sftptodb.entity.ExcelStaging;
import com.trangile.sftptodb.entity.OrderDetails;
import com.trangile.sftptodb.repository.OrderDetailsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderDetailService {

    private final OrderDetailsRepository orderDetailsRepository;

    public void saveOrderDetails(List<ExcelStaging> rows,
                                 String generatedOrderKey) {

        List<OrderDetails> details = rows.stream()
                .map(row -> OrderDetails.builder()

                        .orderKey(generatedOrderKey)
                        .orderLineNumber(row.getLineNumber())

                        .whseId(row.getWarehouseId())
                        .storerKey("GA")

                        .externLineNo(row.getExternLineNo())

                        .sku(row.getSku())

                        .openQty(row.getOpenQty())
                        .originalQty(row.getOrderQty())

                        .uom("EA")

                        .newAllocationStrategy(row.getAllocationStrategy())
                        .status("02")
                        .lotTable01(row.getPlantCode())
                        .lotTable02(row.getProjectNumber())
                        .lotTable03(row.getMrir())
                        .lotTable07(row.getLotNumber())

                        .pickingInstructions(row.getInstructionForCutting())

                        .build())

                .toList();

        orderDetailsRepository.saveAll(details);
    }
}