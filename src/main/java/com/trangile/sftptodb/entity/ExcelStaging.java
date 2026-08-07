package com.trangile.sftptodb.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "EXCEL_STAGING")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExcelStaging {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SERIAL_KEY", nullable = false)
    private Long id;

    // File Information
    @Column(name = "FILE_NAME")
    private String fileName;

    // Order Header
    @Column(name = "WAREHOUSE_ID")
    private String warehouseId;

    @Column(name = "LN_ORDER_NUMBER")
    private String lnOrderNumber;

    @Column(name = "ORDER_TYPE")
    private String orderType;

    @Column(name = "CONSIGNEE_KEY")
    private String consigneeKey;

    @Column(name = "ORDER_DATE")
    private LocalDateTime orderDate;

    @Column(name = "WORK_CENTER")
    private String workCenter;

    // Order Line
    @Column(name = "SKU")
    private String sku;

    @Column(name = "LINE_NUMBER")
    private String lineNumber;

    @Column(name = "EXTERN_LINE_NO")
    private String externLineNo;

    // Quantity
    @Column(name = "OPEN_QTY")
    private BigDecimal openQty;

    @Column(name = "ORDER_QTY")
    private BigDecimal orderQty;

    // Allocation
    @Column(name = "ALLOCATION_STRATEGY")
    private String allocationStrategy;

    // Lot Information
    @Column(name = "PLANT_CODE")
    private String plantCode;

    @Column(name = "PROJECT_NUMBER")
    private String projectNumber;

    @Column(name = "MRIR")
    private String mrir;

    @Column(name = "LOT_NUMBER")
    private String lotNumber;

    // Cutting
    @Column(name = "INSTRUCTION_FOR_CUTTING")
    private String instructionForCutting;

    // Used during processing
    @Column(name = "SKU_TYPE")
    private String skuType;

    @Column(name = "PROCESS_FLAG")
    private String processFlag;

    @CreationTimestamp
    @Column(name = "CREATED_AT", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "UPDATED_AT")
    private LocalDateTime updatedAt;
}