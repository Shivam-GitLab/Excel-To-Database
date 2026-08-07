package com.trangile.sftptodb.service.excel;

import com.trangile.sftptodb.dto.ExcelRow;
import com.trangile.sftptodb.exception.FileValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExcelReaderService {

    private final ExcelMapperService excelMapperService;

    private static final DataFormatter FORMATTER = new DataFormatter();

    public List<ExcelRow> readExcel(File file) {

        if (file == null || !file.exists()) {
            throw new FileValidationException("Excel file not found.");
        }

        if (file.length() == 0) {
            throw new FileValidationException("Excel file is empty.");
        }

        List<ExcelRow> excelRows = new ArrayList<>();

        log.info("Reading Excel File : {}", file.getName());

        try (FileInputStream fis = new FileInputStream(file);
             Workbook workbook = WorkbookFactory.create(fis)) {

            if (workbook.getNumberOfSheets() == 0) {
                throw new FileValidationException("Workbook contains no sheets.");
            }

            Sheet sheet = workbook.getSheetAt(0);

            if (sheet.getPhysicalNumberOfRows() <= 1) {
                throw new FileValidationException("Excel contains no data.");
            }

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {

                Row row = sheet.getRow(i);

                if (row == null || isRowEmpty(row)) {
                    continue;
                }

                try {
                    ExcelRow dto = excelMapperService.map(row, i + 1);
                    excelRows.add(dto);

                } catch (Exception ex) {

                    throw new FileValidationException(
                            "Error while reading Excel at Row : " + (i + 1)
                                    + ". " + ex.getMessage()
                    );
                }
            }

            log.info("Total Excel Rows Read : {}", excelRows.size());

            return excelRows;

        } catch (IOException | EncryptedDocumentException ex) {

            log.error("Unable to read Excel file.", ex);

            throw new FileValidationException("Unable to read Excel file.");
        }
    }

    private boolean isRowEmpty(Row row) {

        for (int i = 0; i < row.getLastCellNum(); i++) {

            if (!FORMATTER.formatCellValue(row.getCell(i)).trim().isEmpty()) {
                return false;
            }
        }

        return true;
    }
}