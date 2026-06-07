package com.police.eom.web.dto;

import java.time.LocalDateTime;

public class EvidenceListItem {
    private Long id;
    private String evidenceNo;
    private String name;
    private String category;
    private String status;
    private String location;
    private LocalDateTime lastCustodyOccurredAt;
    private String lastCustodyAction;
    private Long lastCustodyOfficerId;
    private String lastCustodyOfficerName;
    private String lastCustodyRemark;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getEvidenceNo() { return evidenceNo; }
    public void setEvidenceNo(String evidenceNo) { this.evidenceNo = evidenceNo; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public LocalDateTime getLastCustodyOccurredAt() { return lastCustodyOccurredAt; }
    public void setLastCustodyOccurredAt(LocalDateTime lastCustodyOccurredAt) { this.lastCustodyOccurredAt = lastCustodyOccurredAt; }
    public String getLastCustodyAction() { return lastCustodyAction; }
    public void setLastCustodyAction(String lastCustodyAction) { this.lastCustodyAction = lastCustodyAction; }
    public Long getLastCustodyOfficerId() { return lastCustodyOfficerId; }
    public void setLastCustodyOfficerId(Long lastCustodyOfficerId) { this.lastCustodyOfficerId = lastCustodyOfficerId; }
    public String getLastCustodyOfficerName() { return lastCustodyOfficerName; }
    public void setLastCustodyOfficerName(String lastCustodyOfficerName) { this.lastCustodyOfficerName = lastCustodyOfficerName; }
    public String getLastCustodyRemark() { return lastCustodyRemark; }
    public void setLastCustodyRemark(String lastCustodyRemark) { this.lastCustodyRemark = lastCustodyRemark; }
}
