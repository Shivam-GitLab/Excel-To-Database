package com.trangile.sftptodb.service.excel;

import com.trangile.sftptodb.config.ExcelProperties;
import com.trangile.sftptodb.exception.FileValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
@RequiredArgsConstructor
public class ExcelValidatorService {

    private final ExcelProperties excelProperties;

    public void validateFile(File file) {

        if (file == null)
            throw new FileValidationException("Uploaded file is null.");

        if (!file.exists())
            throw new FileValidationException("File not found.");

        if (!file.isFile())
            throw new FileValidationException("Invalid file.");

        if (!file.canRead())
            throw new FileValidationException("File cannot be read.");

        if (!file.getName().toLowerCase().endsWith(".xlsx"))
            throw new FileValidationException("Only .xlsx files are allowed.");

        if (file.length() == 0)
            throw new FileValidationException("File is empty.");

        if (file.length() > excelProperties.getMaxFileSize())
            throw new FileValidationException("File size exceeds configured limit.");
    }
}