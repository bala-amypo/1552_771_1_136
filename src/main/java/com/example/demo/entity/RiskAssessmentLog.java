package com.example.loan.entity;

import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
public class RiskLog {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Double dtiRatio;
    private Timestamp createdAt = new Timestamp(System.currentTimeMillis());
}
