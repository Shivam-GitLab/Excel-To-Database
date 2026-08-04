package com.trangile.sftptodb.service.excel;

import com.trangile.sftptodb.dto.ExcelRow;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ExcelGroupingService {

    public Map<String, List<ExcelRow>> groupByGeneratedOrderKey(List<ExcelRow> rows) {

        return rows.stream().collect(Collectors.groupingBy(
                row -> {

                    String lnOrder = row.getLnOrderNumber();
                    String skuType = row.getSkuType();

                    String suffix = switch (skuType.toUpperCase()) {
                        case "NORMAL" -> "N";
                        case "ROD" -> "R";
                        case "SHEET" -> "S";
                        default -> "O"; // Other
                    };

                    return lnOrder + "_" + suffix;

                },
                LinkedHashMap::new,
                Collectors.toList()
        ));
    }
}