package com.trangile.sftptodb.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ExcelData {
    // Order Header
    private String warehouseId;
    private String lnOrderNumber;
    private String orderType;
    private String consigneeKey;
    private LocalDateTime orderDate;
    private String workCenter;

    // Order Line
    private String sku;
    private String lineNumber;
    private String externLineNo;

    // Quantity
    private BigDecimal openQty;
    private BigDecimal orderQty;

    // Allocation
    private String allocationStrategy;

    // Lot Information
    private String plantCode;
    private String projectNumber;
    private String mrir;
    private String lotNumber;

    // Cutting
    private String instructionForCutting;

    // Used only for Order Splitting (Not Stored in DB)
    private String skuType;
}
