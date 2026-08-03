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

        // (LN Order + SKU Type) -> Generated Order Key
        Map<String, String> generatedKeys = new LinkedHashMap<>();

        // LN Order -> Counter
        Map<String, Integer> counters = new LinkedHashMap<>();

        return rows.stream()
                .collect(Collectors.groupingBy(row -> {

                    String lnOrder = row.getLnOrderNumber();
                    String skuType = row.getSkuType();

                    String mapKey = lnOrder + "_" + skuType;

                    if (!generatedKeys.containsKey(mapKey)) {

                        int counter = counters.getOrDefault(lnOrder, 0) + 1;

                        counters.put(lnOrder, counter);

                        generatedKeys.put(
                                mapKey,
                                lnOrder + "_" + counter
                        );
                    }

                    return generatedKeys.get(mapKey);
                }));
    }
}