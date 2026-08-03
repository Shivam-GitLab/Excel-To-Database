package com.trangile.sftptodb.service.excel;

import com.trangile.sftptodb.dto.ExcelRow;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Service
public class ExcelMapperService {

    private final DataFormatter formatter = new DataFormatter();
    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public ExcelRow map(Row row, int rowNumber) {

        ExcelRow excelRow = new ExcelRow();

        // Header
        excelRow.setWarehouseId(getStringValue(row.getCell(0)));
        excelRow.setLnOrderNumber(getStringValue(row.getCell(1)));
        excelRow.setOrderType(getStringValue(row.getCell(2)));
        excelRow.setConsigneeKey(getStringValue(row.getCell(3)));
        excelRow.setOrderDate(getDateValue(row.getCell(4)));
        excelRow.setWorkCenter(getStringValue(row.getCell(5)));

        // Detail
        excelRow.setSku(getStringValue(row.getCell(6)));
        excelRow.setLineNumber(getStringValue(row.getCell(7)));

        excelRow.setExternLineNo((int) getLongValue(row.getCell(8), rowNumber));

        // Agar Excel me Extern Line No alag column hai to uska field DTO me add karo.
        excelRow.setOpenQty((int) getLongValue(row.getCell(9), rowNumber));
        excelRow.setOrderQty((int) getLongValue(row.getCell(10), rowNumber));

        excelRow.setAllocationStrategy(getStringValue(row.getCell(11)));
        excelRow.setPlantCode(getStringValue(row.getCell(12)));
        excelRow.setProjectNumber(getStringValue(row.getCell(13)));
        excelRow.setMrir(getStringValue(row.getCell(14)));
        excelRow.setLotNumber(getStringValue(row.getCell(15)));
        excelRow.setInstructionForCutting(getStringValue(row.getCell(16)));

        // Grouping only
        excelRow.setSkuType(getStringValue(row.getCell(18)).trim().toUpperCase());

        return excelRow;
    }

    private long getLongValue(Cell cell, int rowNumber) {

        if (cell == null) return 0L;

        String value = formatter.formatCellValue(cell).trim();

        if (value.isBlank()) return 0L;

        value = value.replace(",", "");

        try {
            return Long.parseLong(value);
        } catch (NumberFormatException ex) {
            throw new RuntimeException(
                    "Invalid numeric value '" + value + "' at row " + rowNumber);
        }
    }

    private String getStringValue(Cell cell) {
        return cell == null ? "" : formatter.formatCellValue(cell).trim();
    }


    private LocalDate getDateValue(Cell cell) {

        if (cell == null) {
            return null;
        }

        // Excel Date Cell
        if (cell.getCellType() == CellType.NUMERIC &&
                DateUtil.isCellDateFormatted(cell)) {

            return cell.getLocalDateTimeCellValue().toLocalDate();
        }

        String value = formatter.formatCellValue(cell).trim();

        if (value.isBlank()) {
            return null;
        }

        try {
            return LocalDate.parse(value, DATE_FORMATTER);
        } catch (DateTimeParseException ex) {
            throw new RuntimeException("Invalid date format: " + value +
                    ". Expected format is dd-MM-yyyy");
        }
    }
}