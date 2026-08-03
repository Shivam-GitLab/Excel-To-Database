package com.trangile.sftptodb.service;

import com.trangile.sftptodb.dto.ExcelRow;
import com.trangile.sftptodb.exception.FileValidationException;
import com.trangile.sftptodb.service.excel.ExcelGroupingService;
import com.trangile.sftptodb.service.excel.ExcelReaderService;
import com.trangile.sftptodb.service.excel.ExcelValidatorService;
import com.trangile.sftptodb.service.order.OrderDetailService;
import com.trangile.sftptodb.service.order.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class ExcelProcessingService {

    private final ExcelValidatorService excelValidatorService;
    private final ExcelReaderService excelReaderService;
    private final ExcelGroupingService excelGroupingService;
    private final OrderService orderService;
    private final OrderDetailService orderDetailService;

    @Transactional(rollbackFor = Exception.class)
    public void processExcelFile(File localFile) throws Exception {

        // Validate
        excelValidatorService.validateFile(localFile);

        // Read Excel
        List<ExcelRow> rows = excelReaderService.readExcel(localFile);

        log.info("Processing File : {}", localFile.getName());
        log.info("Total Records : {}", rows.size());

        if (rows.isEmpty()) {
            throw new FileValidationException("No valid records found.");
        }

        // Group by Generated Order Key
        Map<String, List<ExcelRow>> groupedData =
                excelGroupingService.groupByGeneratedOrderKey(rows);

        // Save Orders & Order Details
        for (Map.Entry<String, List<ExcelRow>> entry : groupedData.entrySet()) {

            String generatedOrderKey = entry.getKey();
            List<ExcelRow> groupRows = entry.getValue();

            ExcelRow first = groupRows.get(0);

            orderService.createOrder(
                    first.getWarehouseId(),
                    generatedOrderKey,
                    first.getOrderType(),
                    first.getOrderDate(),
                    first.getWorkCenter(),
                    first.getConsigneeKey(),
                    first.getLnOrderNumber()
            );

            orderDetailService.saveOrderDetails(
                    groupRows,
                    generatedOrderKey
            );
        }

        log.info("File processed successfully : {}", localFile.getName());
    }
}