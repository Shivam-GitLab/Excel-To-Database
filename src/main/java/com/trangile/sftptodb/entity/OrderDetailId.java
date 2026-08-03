package com.trangile.sftptodb.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;

@Data
@Embeddable
public class OrderDetailId implements Serializable {

    @Column(name = "orderkey")
    private String orderKey;

    @Column(name = "orderlinenumber")
    private String orderLineNumber;
}