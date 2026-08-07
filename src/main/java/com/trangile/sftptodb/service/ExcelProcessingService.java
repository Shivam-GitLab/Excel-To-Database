package com.trangile.sftptodb.service;

import com.trangile.sftptodb.dto.ExcelRow;
import com.trangile.sftptodb.exception.FileValidationException;
import com.trangile.sftptodb.service.excel.ExcelReaderService;
import com.trangile.sftptodb.service.excel.ExcelValidatorService;
import com.trangile.sftptodb.service.staging.ExcelStagingService;
import com.trangile.sftptodb.service.staging.StagingProcessingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ExcelProcessingService {

    private final ExcelValidatorService excelValidatorService;
    private final ExcelReaderService excelReaderService;
    private final ExcelStagingService excelStagingService;
    private final StagingProcessingService stagingProcessingService;

    public void processExcelFile(File localFile) {

        // Validate File
        excelValidatorService.validateFile(localFile);

        // Read Excel
        List<ExcelRow> rows = excelReaderService.readExcel(localFile);

        log.info("Processing File : {}", localFile.getName());
        log.info("Total Records : {}", rows.size());

        if (rows.isEmpty()) {
            throw new FileValidationException("No valid records found.");
        }

        // Phase 1 : Save into Staging
        excelStagingService.save(rows, localFile.getName());

        log.info("Excel data successfully saved into excel_staging table.");

        // Phase 2 : Process Staging
        stagingProcessingService.processPendingRecords();

        log.info("Orders and Order Details created successfully.");
    }
}