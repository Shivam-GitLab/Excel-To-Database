package com.trangile.sftptodb.service.excel;

import com.trangile.sftptodb.dto.ExcelRow;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Service
public class ExcelMapperService {

    private final DataFormatter formatter = new DataFormatter();

    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public ExcelRow map(Row row, int rowNumber) {

        ExcelRow excelRow = new ExcelRow();

        excelRow.setWarehouseId(getStringValue(row.getCell(0)));
        excelRow.setLnOrderNumber(getStringValue(row.getCell(1)));
        excelRow.setOrderType(getStringValue(row.getCell(2)));
        excelRow.setConsigneeKey(getStringValue(row.getCell(3)));
        excelRow.setOrderDate(getDateValue(row.getCell(4)));
        excelRow.setWorkCenter(getStringValue(row.getCell(5)));

        excelRow.setSku(getStringValue(row.getCell(6)));
        excelRow.setLineNumber(getStringValue(row.getCell(7)));

        excelRow.setExternLineNo(getStringValue(row.getCell(8)));

        excelRow.setOpenQty(getDecimalValue(row.getCell(9), rowNumber));
        excelRow.setOrderQty(getDecimalValue(row.getCell(10), rowNumber));

        excelRow.setAllocationStrategy(getStringValue(row.getCell(11)));
        excelRow.setPlantCode(getStringValue(row.getCell(12)));
        excelRow.setProjectNumber(getStringValue(row.getCell(13)));
        excelRow.setMrir(getStringValue(row.getCell(14)));
        excelRow.setLotNumber(getStringValue(row.getCell(15)));
        excelRow.setInstructionForCutting(getStringValue(row.getCell(16)));

        excelRow.setSkuType(getStringValue(row.getCell(17)).trim().toUpperCase());

        return excelRow;
    }

    private BigDecimal getDecimalValue(Cell cell, int rowNumber) {

        if (cell == null) {
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
            throw new RuntimeException(
                    "Invalid numeric value '" + value + "' at row " + rowNumber);
        }
    }

    private String getStringValue(Cell cell) {
        return cell == null ? "" : formatter.formatCellValue(cell).trim();
    }

    private LocalDateTime getDateValue(Cell cell) {

        if (cell == null) {
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
            throw new RuntimeException(
                    "Invalid date format: " + value + ". Expected format is dd-MM-yyyy");
        }
    }
}