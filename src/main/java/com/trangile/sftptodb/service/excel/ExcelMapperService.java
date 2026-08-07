package com.trangile.sftptodb.service.excel;

import com.trangile.sftptodb.dto.ExcelRow;
import com.trangile.sftptodb.enums.SkuType;
import com.trangile.sftptodb.exception.FileValidationException;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;

@Service
public class ExcelMapperService {

    private final DataFormatter formatter = new DataFormatter();

    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("dd-MM-yyyy");

    // Excel Column Index
    private static final int WAREHOUSE_ID = 0;
    private static final int LN_ORDER_NUMBER = 1;
    private static final int ORDER_TYPE = 2;
    private static final int CONSIGNEE_KEY = 3;
    private static final int ORDER_DATE = 4;
    private static final int WORK_CENTER = 5;
    private static final int SKU = 6;
    private static final int LINE_NUMBER = 7;
    private static final int EXTERN_LINE_NO = 8;
    private static final int OPEN_QTY = 9;
    private static final int ORDER_QTY = 10;
    private static final int ALLOCATION_STRATEGY = 11;
    private static final int PLANT_CODE = 12;
    private static final int PROJECT_NUMBER = 13;
    private static final int MRIR = 14;
    private static final int LOT_NUMBER = 15;
    private static final int INSTRUCTION_FOR_CUTTING = 16;
    private static final int SKU_TYPE = 17;

    public ExcelRow map(Row row, int rowNumber) {

        ExcelRow dto = new ExcelRow();

        // Order Header
        dto.setWarehouseId(getStringValue(getCell(row, WAREHOUSE_ID)));
        dto.setLnOrderNumber(getStringValue(getCell(row, LN_ORDER_NUMBER)));
        dto.setOrderType(getStringValue(getCell(row, ORDER_TYPE)));
        dto.setConsigneeKey(getStringValue(getCell(row, CONSIGNEE_KEY)));
        dto.setOrderDate(getDateValue(getCell(row, ORDER_DATE), rowNumber));
        dto.setWorkCenter(getStringValue(getCell(row, WORK_CENTER)));

        // Order Line
        dto.setSku(getStringValue(getCell(row, SKU)));
        dto.setLineNumber(getStringValue(getCell(row, LINE_NUMBER)));
        dto.setExternLineNo(getStringValue(getCell(row, EXTERN_LINE_NO)));

        // Quantity
        dto.setOpenQty(getDecimalValue(getCell(row, OPEN_QTY), rowNumber));
        dto.setOrderQty(getDecimalValue(getCell(row, ORDER_QTY), rowNumber));

        // Allocation
        dto.setAllocationStrategy(getStringValue(getCell(row, ALLOCATION_STRATEGY)));

        // Lot Information
        dto.setPlantCode(getStringValue(getCell(row, PLANT_CODE)));
        dto.setProjectNumber(getStringValue(getCell(row, PROJECT_NUMBER)));
        dto.setMrir(getStringValue(getCell(row, MRIR)));
        dto.setLotNumber(getStringValue(getCell(row, LOT_NUMBER)));

        // Cutting
        dto.setInstructionForCutting(getStringValue(getCell(row, INSTRUCTION_FOR_CUTTING)));

        // SKU Type
        dto.setSkuType(getSkuType(getStringValue(getCell(row, SKU_TYPE)), rowNumber));

        return dto;
    }

    private Cell getCell(Row row, int index) {
        return row.getCell(index, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
    }

    private String getStringValue(Cell cell) {
        return formatter.formatCellValue(cell).trim();
    }

    private BigDecimal getDecimalValue(Cell cell, int rowNumber) {

        if (cell == null || cell.getCellType() == CellType.BLANK) {
            return BigDecimal.ZERO;
        }

        String value = formatter.formatCellValue(cell).trim();

        if (value.isBlank()) {
            return BigDecimal.ZERO;
        }

        value = value.replace(",", "");

        try {
            return new BigDecimal(value);

        } catch (NumberFormatException ex) {
            throw new FileValidationException(
                    "Invalid numeric value '" + value +
                            "' at Excel row " + rowNumber
            );
        }
    }

    private LocalDateTime getDateValue(Cell cell, int rowNumber) {

        if (cell == null || cell.getCellType() == CellType.BLANK) {
            return null;
        }

        if (cell.getCellType() == CellType.NUMERIC &&
                DateUtil.isCellDateFormatted(cell)) {

            return cell.getLocalDateTimeCellValue();
        }

        String value = formatter.formatCellValue(cell).trim();

        if (value.isBlank()) {
            return null;
        }

        try {

            LocalDate date = LocalDate.parse(value, DATE_FORMATTER);
            return date.atStartOfDay();

        } catch (DateTimeParseException ex) {

            throw new FileValidationException(
                    "Invalid date '" + value +
                            "' at Excel row " + rowNumber +
                            ". Expected format: dd-MM-yyyy"
            );
        }
    }

    private String getSkuType(String skuType, int rowNumber) {

        if (skuType.isBlank()) {
            return SkuType.NORMAL.name();
        }

        try {

            return SkuType.valueOf(skuType.toUpperCase()).name();

        } catch (IllegalArgumentException ex) {

            throw new FileValidationException(
                    "Invalid SKU Type '" + skuType +
                            "' at Excel row " + rowNumber +
                            ". Allowed values are: " +
                            Arrays.toString(SkuType.values())
            );
        }
    }
}