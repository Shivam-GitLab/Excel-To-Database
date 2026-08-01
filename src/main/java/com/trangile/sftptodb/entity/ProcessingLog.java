package com.trangile.sftptodb.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "processing_logs")
@Getter
@Setter
public class ProcessingLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;
    private String status; // SUCCESS or FAILED

    @Column(columnDefinition = "TEXT")
    private String errorMessage;

    private LocalDateTime processedAt;

}