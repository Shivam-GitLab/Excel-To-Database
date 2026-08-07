package com.trangile.sftptodb.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "orderdetail")
@IdClass(OrderDetailId.class)
public class OrderDetails {

    @Id
    @Column(name = "ORDERKEY", nullable = false)
    private String orderKey;

    @Id
    @Column(name = "ORDERLINENUMBER", nullable = false)
    private String orderLineNumber;

    @Column(name = "WHSEID")
    private String whseId;

    @Column(name = "STORERKEY", nullable = false)
    private String storerKey;

    @Column(name = "EXTERNLINENO", nullable = false)
    private String externLineNo;

    @Column(name = "SKU", nullable = false)
    private String sku;

    @Column(name = "OPENQTY", nullable = false)
    private BigDecimal openQty;

    @Column(name = "ORIGINALQTY", nullable = false)
    private BigDecimal originalQty;

    @Column(name = "UOM", nullable = false)
    private String uom;

    @Column(name = "NEWALLOCATIONSTRATEGY")
    private String newAllocationStrategy;

    @Column(name = "STATUS", nullable = false)
    private String status;

    @Column(name = "LOTTABLE01", nullable = false)
    private String lotTable01;

    @Column(name = "LOTTABLE02", nullable = false)
    private String lotTable02;

    @Column(name = "LOTTABLE03", nullable = false)
    private String lotTable03;

    @Column(name = "LOTTABLE07")
    private String lotTable07;

    @Column(name = "PICKINGINSTRUCTIONS")
    private String pickingInstructions;

    @Transient
    private String skuType;
}