package com.trangile.sftptodb.service.excel;

import com.trangile.sftptodb.dto.ExcelRow;
import com.trangile.sftptodb.exception.FileValidationException;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExcelReaderService {

    private final ExcelMapperService excelMapperService;

    public List<ExcelRow> readExcel(File file) throws Exception {

        List<ExcelRow> rows = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(file);
             Workbook workbook = WorkbookFactory.create(fis)) {

            if (workbook.getNumberOfSheets() == 0) {
                throw new FileValidationException("Workbook contains no sheets.");
            }

            Sheet sheet = workbook.getSheetAt(0);

            if (sheet.getLastRowNum() == 0) {
                throw new FileValidationException("Excel contains no data.");
            }

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {

                Row row = sheet.getRow(i);

                if (row == null || isRowEmpty(row)) {
                    continue;
                }

                rows.add(excelMapperService.map(row, i + 1));
            }
        }

        return rows;
    }

    private boolean isRowEmpty(Row row) {

        DataFormatter formatter = new DataFormatter();

        for (int i = 0; i < row.getLastCellNum(); i++) {

            if (!formatter.formatCellValue(row.getCell(i)).trim().isEmpty()) {
                return false;
            }
        }

        return true;
    }
}