package com.police.eom.web.dto;

import com.police.eom.domain.CaseInfo;
import java.util.List;

public class CaseDetailView {
    private CaseInfo caseInfo;
    private List<EvidenceListItem> evidenceList;

    public CaseInfo getCaseInfo() { return caseInfo; }
    public void setCaseInfo(CaseInfo caseInfo) { this.caseInfo = caseInfo; }
    public List<EvidenceListItem> getEvidenceList() { return evidenceList; }
    public void setEvidenceList(List<EvidenceListItem> evidenceList) { this.evidenceList = evidenceList; }
}
