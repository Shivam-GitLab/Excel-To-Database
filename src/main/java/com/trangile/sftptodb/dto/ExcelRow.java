package com.trangile.sftptodb.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class ExcelRow {

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
    private String uom;
    private String projectNumber;
    private String mrir;
    private String lotNumber;

    // Cutting
    private String instructionForCutting;

    // Used only for Order Splitting (Not Stored in DB)
    private String skuType;
}