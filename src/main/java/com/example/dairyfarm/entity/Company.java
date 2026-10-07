package com.example.dairyfarm.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "COMPANY")
public class Company {

    @Id
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "company_seq_generator"
    )
    @SequenceGenerator(
        name = "company_seq_generator",
        sequenceName = "COMPANY_SEQ",
        allocationSize = 1
    )
    @Column(name = "ID")
    private Long id;

    @Column(name = "COMPANY_NAME", nullable = false)
    private String companyName;

    @Column(name = "DESCRIPTION")
    private String description;

    public Company() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}