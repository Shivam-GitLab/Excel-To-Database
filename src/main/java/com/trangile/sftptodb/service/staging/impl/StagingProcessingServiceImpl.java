package com.trangile.sftptodb.service.staging.impl;

import com.trangile.sftptodb.entity.ExcelStaging;
import com.trangile.sftptodb.repository.ExcelStagingRepository;
import com.trangile.sftptodb.service.excel.ExcelGroupingService;
import com.trangile.sftptodb.service.order.OrderDetailService;
import com.trangile.sftptodb.service.order.OrderService;
import com.trangile.sftptodb.service.staging.StagingProcessingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class StagingProcessingServiceImpl implements StagingProcessingService {

    private final ExcelStagingRepository excelStagingRepository;
    private final ExcelGroupingService excelGroupingService;
    private final OrderService orderService;
    private final OrderDetailService orderDetailService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void processPendingRecords() {

        List<ExcelStaging> pendingRecords =
                excelStagingRepository.findByProcessFlag("N");

        if (pendingRecords.isEmpty()) {
            log.info("No pending records found.");
            return;
        }

        Map<String, List<ExcelStaging>> groupedData =
                excelGroupingService.groupByGeneratedOrderKey(pendingRecords);

        for (Map.Entry<String, List<ExcelStaging>> entry : groupedData.entrySet()) {

            String generatedOrderKey = entry.getKey();
            List<ExcelStaging> rows = entry.getValue();

            try {

                ExcelStaging firstRow = rows.getFirst();

                orderService.createOrder(firstRow, generatedOrderKey);

                orderDetailService.saveOrderDetails(rows, generatedOrderKey);

                rows.forEach(row -> {
                    row.setProcessFlag("9");
                });

            } catch (Exception ex) {

                log.error("Failed processing Order Key : {}", generatedOrderKey, ex);

                rows.forEach(row -> {
                    row.setProcessFlag("0");
                });
            }

            excelStagingRepository.saveAll(rows);
        }

        log.info("Staging processing completed successfully.");
    }
}