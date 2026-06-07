package com.police.eom.web.dto;

import com.police.eom.domain.CaseInfo;

public class CaseListItem {
    private CaseInfo caseInfo;
    private int evidenceCount;

    public CaseInfo getCaseInfo() { return caseInfo; }
    public void setCaseInfo(CaseInfo caseInfo) { this.caseInfo = caseInfo; }
    public int getEvidenceCount() { return evidenceCount; }
    public void setEvidenceCount(int evidenceCount) { this.evidenceCount = evidenceCount; }
}
