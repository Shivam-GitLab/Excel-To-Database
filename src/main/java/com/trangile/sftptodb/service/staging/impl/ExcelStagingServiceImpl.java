package com.trangile.sftptodb.service.staging.impl;

import com.trangile.sftptodb.dto.ExcelRow;
import com.trangile.sftptodb.entity.ExcelStaging;
import com.trangile.sftptodb.repository.ExcelStagingRepository;
import com.trangile.sftptodb.service.staging.ExcelStagingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExcelStagingServiceImpl implements ExcelStagingService {

    private final ExcelStagingRepository excelStagingRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(List<ExcelRow> rows, String fileName) {

        if (rows == null || rows.isEmpty()) {
            log.warn("No records found to save into staging.");
            return;
        }

        List<ExcelStaging> stagingList = new ArrayList<>();

        for (ExcelRow dto : rows) {

            ExcelStaging entity = ExcelStaging.builder()

                    // File Information
                    .fileName(fileName)

                    // Order Header
                    .warehouseId(dto.getWarehouseId())
                    .lnOrderNumber(dto.getLnOrderNumber())
                    .orderType(dto.getOrderType())
                    .consigneeKey(dto.getConsigneeKey())
                    .orderDate(dto.getOrderDate())
                    .workCenter(dto.getWorkCenter())

                    // Order Line
                    .sku(dto.getSku())
                    .lineNumber(dto.getLineNumber())
                    .externLineNo(dto.getExternLineNo())

                    // Quantity
                    .openQty(dto.getOpenQty())
                    .orderQty(dto.getOrderQty())

                    // Allocation
                    .allocationStrategy(dto.getAllocationStrategy())

                    // Lot Information
                    .plantCode(dto.getPlantCode())
                    .projectNumber(dto.getProjectNumber())
                    .mrir(dto.getMrir())
                    .lotNumber(dto.getLotNumber())

                    // Cutting
                    .instructionForCutting(dto.getInstructionForCutting())

                    // SKU
                    .skuType(dto.getSkuType())
                    .processFlag("N")
                    .build();

            stagingList.add(entity);
        }

        excelStagingRepository.saveAll(stagingList);
        excelStagingRepository.flush();

        log.info("{} records saved into excel_staging table.", stagingList.size());
    }
}