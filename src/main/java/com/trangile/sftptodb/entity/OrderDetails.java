package com.trangile.sftptodb.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "orderdetail")
public class OrderDetails {

    @EmbeddedId
    private OrderDetailId id;

    @Column(name = "whseid")
    private String whseId;

    @Column(name = "storerkey")
    private String storerKey;

    @Column(name = "externlineno")
    private Integer externLineNo;

    @Column(name = "sku")
    private String sku;

    @Column(name = "openqty")
    private Integer openQty;

    @Column(name = "originalqty")
    private Integer originalQty;

    @Column(name = "uom")
    private String uom;

    @Column(name = "newallocationstrategy")
    private String newAllocationStrategy;

    @Column(name = "lottable01")
    private String lotTable01;

    @Column(name = "lottable02")
    private String lotTable02;

    @Column(name = "lottable03")
    private String lotTable03;

    @Column(name = "lottable07")
    private String lotTable07;

    @Column(name = "pikinginstruction")
    private String pikingInstruction;

    @Transient
    private String skuType;
}