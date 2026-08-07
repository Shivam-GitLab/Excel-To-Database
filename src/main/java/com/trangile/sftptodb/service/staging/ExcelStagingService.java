package com.trangile.sftptodb.service.staging;

import com.trangile.sftptodb.dto.ExcelRow;

import java.util.List;

public interface ExcelStagingService {

    void save(List<ExcelRow> rows, String fileName);

}