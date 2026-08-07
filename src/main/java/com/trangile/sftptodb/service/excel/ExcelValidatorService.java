package com.trangile.sftptodb.service.excel;

import com.trangile.sftptodb.exception.FileValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class ExcelValidatorService {

    private static final String EXCEL_EXTENSION = ".xlsx";

    @Value("${excel.max-file-size}")
    private long maxFileSize;

    public void validateFile(File file) {

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

        if (file.getName().isBlank()) {
            throw new FileValidationException("File name is empty.");
        }

        if (!file.getName().toLowerCase(Locale.ROOT).endsWith(EXCEL_EXTENSION)) {
            throw new FileValidationException("Only .xlsx files are allowed.");
        }

        if (file.length() == 0) {
            throw new FileValidationException("File is empty.");
        }

        if (file.length() > maxFileSize) {
            throw new FileValidationException(
                    "File size exceeds maximum allowed size."
            );
        }
    }
}