package com.trangile.sftptodb.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "orders")
public class Orders {

    @Column(name = "whseid", nullable = false)
    private String whseId;

    @Column(name = "type", nullable = false)
    private String type;              // STANDARD

    @Column(name = "storerkey", nullable = false)
    private String storerKey;         // GA

    @Id
    @Column(name = "orderkey", nullable = false, unique = true)
    private String orderKey;          // LN100001_1

    @Column(name = "orderdate")
    private LocalDate orderDate;

    @Column(name = "externorderkey2")
    private String externOrderKey2;   // Original LN Order Number

    @Column(name = "consigneekey")
    private String consigneeKey;

    @Column(name = "susr1")
    private String susr1;             // Work Center
}