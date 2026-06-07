package com.police.eom.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "cases")
public class CaseInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "case_no", nullable = false, unique = true, length = 48)
    private String caseNo;

    @Column(nullable = false, length = 128)
    private String title;

    @Column(name = "case_type", nullable = false, length = 64)
    private String caseType;

    @Column(name = "handling_unit", nullable = false, length = 128)
    private String handlingUnit;

    @Column(name = "lead_officer_id")
    private Long leadOfficerId;

    @Column(nullable = false, length = 32)
    private String status = "UNDER_INVESTIGATION";

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        if (createdAt == null) createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCaseNo() { return caseNo; }
    public void setCaseNo(String caseNo) { this.caseNo = caseNo; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getCaseType() { return caseType; }
    public void setCaseType(String caseType) { this.caseType = caseType; }
    public String getHandlingUnit() { return handlingUnit; }
    public void setHandlingUnit(String handlingUnit) { this.handlingUnit = handlingUnit; }
    public Long getLeadOfficerId() { return leadOfficerId; }
    public void setLeadOfficerId(Long leadOfficerId) { this.leadOfficerId = leadOfficerId; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
