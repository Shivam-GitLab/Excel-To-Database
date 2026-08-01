package com.trangile.sftptodb.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "warehouse_orders")
@Getter
@Setter
public class WarehouseOrders {

    @Id
    @Column(name = "warehouse_id", nullable = false)
    private String warehouseId;

    @Column(name = "ln_order_number")
    private String lnOrderNumber;

    @Column(name = "order_type")
    private String orderType;

    @Column(name = "consignee_key")
    private String consigneeKey;

    @Column(name = "ln_order_date")
    private LocalDate lnOrderDate;

    @Column(name = "work_center")
    private String workCenter;

    @Column(name = "sku")
    private String sku;

    @Column(name = "ln_line_number")
    private Long lnLineNumber;

    @Column(name = "open_qty")
    private Long openQty;

    @Column(name = "order_qty")
    private Long orderQty;

    @Column(name = "allocation_strategy")
    private String allocationStrategy;

    @Column(name = "plant_code")
    private String plantCode;

    @Column(name = "project_number")
    private String projectNumber;

    @Column(name = "mrir")
    private String mrir;

    @Column(name = "lot_number")
    private String lotNumber;

    @Column(name = "instructions_for_cutting")
    private String instructionsForCutting;

    @Column(name = "sku_type")
    private String skuType;
}