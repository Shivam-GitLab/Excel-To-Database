package com.trangile.sftptodb.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "orders")
public class Orders {

    @Column(name = "WHSEID")
    private String whseId;

    @Column(name = "TYPE", nullable = false)
    private String type;

    @Column(name = "STORERKEY", nullable = false)
    private String storerKey;

    @Id
    @Column(name = "ORDERKEY", nullable = false)
    private String orderKey;

    @Column(name = "ORDERDATE", nullable = false)
    private LocalDateTime orderDate;

    @Column(name = "STATUS", nullable = false)
    private String status;

    @Column(name = "EXTERNALORDERKEY2")
    private String externalOrderKey2;

    @Column(name = "CONSIGNEEKEY", nullable = false)
    private String consigneeKey;

    @Column(name = "SUSR1")
    private String susr1;
}