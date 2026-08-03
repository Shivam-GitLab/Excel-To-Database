package com.trangile.sftptodb.service.order;

import com.trangile.sftptodb.dto.ExcelRow;
import com.trangile.sftptodb.entity.OrderDetailId;
import com.trangile.sftptodb.entity.OrderDetails;
import com.trangile.sftptodb.repository.OrderDetailsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderDetailService {

    private final OrderDetailsRepository orderDetailsRepository;

    public void saveOrderDetails(List<ExcelRow> rows, String generatedOrderKey) {

        List<OrderDetails> details = new ArrayList<>();

        for (ExcelRow row : rows) {

            OrderDetails detail = new OrderDetails();

            // Composite Primary Key
            OrderDetailId id = new OrderDetailId();
            id.setOrderKey(generatedOrderKey);
            id.setOrderLineNumber(row.getLineNumber());

            detail.setId(id);

            detail.setWhseId(row.getWarehouseId());
            detail.setStorerKey("GA");

            detail.setExternLineNo(row.getExternLineNo());

            detail.setSku(row.getSku());
            detail.setOpenQty(row.getOpenQty());
            detail.setOriginalQty(row.getOrderQty());

            detail.setUom("EA");

            detail.setNewAllocationStrategy(row.getAllocationStrategy());

            detail.setLotTable01(row.getPlantCode());
            detail.setLotTable02(row.getProjectNumber());
            detail.setLotTable03(row.getMrir());
            detail.setLotTable07(row.getLotNumber());

            detail.setPikingInstruction(row.getInstructionForCutting());

            details.add(detail);
        }

        orderDetailsRepository.saveAll(details);
    }
}