package com.trangile.sftptodb.service;

import com.trangile.sftptodb.config.ExcelProperties;
import com.trangile.sftptodb.entity.WarehouseOrders;
import com.trangile.sftptodb.exception.FileValidationException;
import com.trangile.sftptodb.repository.PickDetailRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.io.File;
import java.io.FileInputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ExcelProcessingService {

    private final PickDetailRepository pickDetailRepository;
    private final ExcelProperties excelProperties;

    private final DataFormatter formatter = new DataFormatter();

    @Transactional(rollbackFor = Exception.class)
    public void processExcelFile(File localFile) throws Exception {

        validateFile(localFile);

        List<WarehouseOrders> records = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(localFile);
             Workbook workbook = WorkbookFactory.create(fis)) {

            if (workbook.getNumberOfSheets() == 0) {
                throw new FileValidationException("Workbook contains no sheets.");
            }

            Sheet sheet = workbook.getSheetAt(0);

            validateHeader(sheet);

            if (sheet.getLastRowNum() == 0) {
                throw new FileValidationException("Excel file contains no data.");
            }

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {

                Row row = sheet.getRow(i);

                if (row == null || isRowEmpty(row)) {
                    continue;
                }


                WarehouseOrders detail = new WarehouseOrders();

                detail.setWarehouseId(getStringValue(row.getCell(0)));
                detail.setLnOrderNumber(getStringValue(row.getCell(1)));
                detail.setOrderType(getStringValue(row.getCell(2)));
                detail.setConsigneeKey(getStringValue(row.getCell(3)));
                detail.setLnOrderDate(getDateValue(row.getCell(4)));
                detail.setWorkCenter(getStringValue(row.getCell(5)));
                detail.setSku(getStringValue(row.getCell(6)));
                detail.setLnLineNumber(getLongValue(row.getCell(7), i + 1));
                detail.setOpenQty(getLongValue(row.getCell(8), i + 1));
                detail.setOrderQty(getLongValue(row.getCell(9), i + 1));
                detail.setAllocationStrategy(getStringValue(row.getCell(10)));
                detail.setPlantCode(getStringValue(row.getCell(11)));
                detail.setProjectNumber(getStringValue(row.getCell(12)));
                detail.setMrir(getStringValue(row.getCell(13)));
                detail.setLotNumber(getStringValue(row.getCell(14)));
                detail.setInstructionsForCutting(getStringValue(row.getCell(15)));
                detail.setSkuType(getStringValue(row.getCell(16)));

                validateMandatoryFields(detail, i + 1);

                records.add(detail);
            }

            log.info("Processing File : {}", localFile.getName());
            log.info("Total Records : {}", records.size());

            if (records.isEmpty()) {
                throw new FileValidationException("No valid records found in Excel.");
            }

            pickDetailRepository.saveAll(records);

            log.info("File processed successfully : {}", localFile.getName());

        } catch (Exception e) {
            log.error("Error while processing file : {}", localFile.getName(), e);
            throw e;
        }
    }

    private boolean isRowEmpty(Row row) {

        for (Cell cell : row) {

            if (cell != null &&
                    cell.getCellType() != CellType.BLANK &&
                    !formatter.formatCellValue(cell).trim().isEmpty()) {
                return false;
            }
        }

        return true;
    }

    private void validateFile(File file) {

        if (file == null) {
            throw new FileValidationException("Uploaded file is null.");
        }

        if (!file.exists()) {
            throw new FileValidationException("File not found.");
        }

        if (!file.isFile()) {
            throw new FileValidationException("Invalid file.");
        }

        if (!file.canRead()) {
            throw new FileValidationException("File cannot be read.");
        }

        if (!file.getName().toLowerCase().endsWith(".xlsx")) {
            throw new FileValidationException("Only .xlsx files are allowed.");
        }

        if (file.length() == 0) {
            throw new FileValidationException("File is empty.");
        }

        if (file.length() > excelProperties.getMaxFileSize()) {
            throw new FileValidationException("File size exceeds the configured limit.");
        }
    }

    private void validateMandatoryFields(WarehouseOrders detail, int rowNumber) {

        if (detail.getWarehouseId().isBlank()) {
            throw new FileValidationException(
                    "Warehouse ID is mandatory at row " + rowNumber);
        }

        if (detail.getLnOrderNumber().isBlank()) {
            throw new FileValidationException(
                    "LN Order Number is mandatory at row " + rowNumber);
        }
    }

    private void validateHeader(Sheet sheet) {

        Row header = sheet.getRow(0);

        if (header == null) {
            throw new FileValidationException("Header row is missing.");
        }

        List<String> expectedHeaders = excelProperties.getExpectedHeaders();

        if (expectedHeaders == null || expectedHeaders.isEmpty()) {
            throw new FileValidationException("Expected headers are not configured.");
        }

        for (int i = 0; i < expectedHeaders.size(); i++) {

            Cell cell = header.getCell(i);

            String actual = cell == null ? "" : formatter.formatCellValue(cell).trim();

            if (!expectedHeaders.get(i).equalsIgnoreCase(actual)) {
                throw new FileValidationException(
                        "Invalid Excel template. Expected '" +
                                expectedHeaders.get(i) +
                                "' but found '" +
                                actual +
                                "' at column " + (i + 1)
                );
            }
        }
    }

    private long getLongValue(Cell cell, int rowNumber) {

        if (cell == null) {
            return 0L;
        }

        String value = formatter.formatCellValue(cell).trim();

        if (value.isBlank()) {
            return 0L;
        }

        value = value.replace(",", "");

        try {
            return Long.parseLong(value);
        } catch (NumberFormatException ex) {
            throw new FileValidationException(
                    "Invalid numeric value '" + value + "' at row " + rowNumber
            );
        }
    }

    private String getStringValue(Cell cell) {

        if (cell == null) {
            return "";
        }

        return formatter.formatCellValue(cell).trim();
    }

    private LocalDate getDateValue(Cell cell) {

        if (cell == null) {
            return null;
        }

        if (DateUtil.isCellDateFormatted(cell)) {
            return cell.getLocalDateTimeCellValue().toLocalDate();
        }

        String value = formatter.formatCellValue(cell).trim();

        if (value.isBlank()) {
            return null;
        }

        return LocalDate.parse(value);
    }
}