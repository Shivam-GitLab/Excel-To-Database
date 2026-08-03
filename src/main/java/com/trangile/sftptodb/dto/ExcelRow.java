package com.trangile.sftptodb.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ExcelRow {

    // Order Header
    private String warehouseId;
    private String lnOrderNumber;
    private String orderType;
    private String consigneeKey;
    private LocalDate orderDate;
    private String workCenter;

    // Order Line
    private String sku;
    private String lineNumber;
    private Integer externLineNo;

    // Quantity
    private Integer openQty;
    private Integer orderQty;

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