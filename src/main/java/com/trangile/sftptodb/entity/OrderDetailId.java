package com.trangile.sftptodb.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailId implements Serializable {

    private String orderKey;
    private String orderLineNumber;
}