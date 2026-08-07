package com.trangile.sftptodb.service.excel;

import com.trangile.sftptodb.entity.ExcelStaging;
import com.trangile.sftptodb.enums.SkuType;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ExcelGroupingService {

    public Map<String, List<ExcelStaging>> groupByGeneratedOrderKey(List<ExcelStaging> rows) {

        return rows.stream().collect(Collectors.groupingBy(
                row -> {

                    String lnOrder = row.getLnOrderNumber();

                    String skuType = row.getSkuType();

                    if (skuType == null || skuType.isBlank()) {
                        skuType = "NORMAL";
                    }

                    String suffix;

                    try {
                        suffix = switch (SkuType.valueOf(skuType.toUpperCase())) {
                            case NORMAL -> "N";
                            case ROD -> "R";
                            case SHEET -> "S";
                        };
                    } catch (IllegalArgumentException ex) {
                        throw new IllegalArgumentException(
                                "Invalid SKU Type : " + skuType +
                                        ". Allowed values are NORMAL, ROD, SHEET."
                        );
                    }

                    return lnOrder + "_" + suffix;

                },
                LinkedHashMap::new,
                Collectors.toList()
        ));
    }
}