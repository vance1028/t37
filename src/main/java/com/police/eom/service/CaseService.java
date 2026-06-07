package com.police.eom.service;

import com.police.eom.domain.CaseInfo;
import com.police.eom.domain.CustodyRecord;
import com.police.eom.domain.Evidence;
import com.police.eom.domain.Officer;
import com.police.eom.repo.*;
import com.police.eom.web.ApiException;
import com.police.eom.web.dto.CaseDetailView;
import com.police.eom.web.dto.CaseListItem;
import com.police.eom.web.dto.EvidenceListItem;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CaseService {

    private final CaseInfoRepository caseRepo;
    private final EvidenceRepository evidenceRepo;
    private final CustodyRecordRepository custodyRepo;
    private final OfficerRepository officerRepo;

    public CaseService(CaseInfoRepository caseRepo,
                       EvidenceRepository evidenceRepo,
                       CustodyRecordRepository custodyRepo,
                       OfficerRepository officerRepo) {
        this.caseRepo = caseRepo;
        this.evidenceRepo = evidenceRepo;
        this.custodyRepo = custodyRepo;
        this.officerRepo = officerRepo;
    }

    public List<CaseListItem> list(String status) {
        List<CaseInfo> cases;
        if (status != null && !status.isBlank()) {
            cases = caseRepo.findByStatus(status);
        } else {
            cases = caseRepo.findAll();
        }
        List<Long> caseIds = cases.stream().map(CaseInfo::getId).collect(Collectors.toList());
        Map<Long, Long> caseEvidenceCountMap = countEvidenceByCaseId(caseIds);
        return cases.stream().map(c -> {
            CaseListItem item = new CaseListItem();
            item.setCaseInfo(c);
            item.setEvidenceCount(caseEvidenceCountMap.getOrDefault(c.getId(), 0L).intValue());
            return item;
        }).collect(Collectors.toList());
    }

    private Map<Long, Long> countEvidenceByCaseId(List<Long> caseIds) {
        if (caseIds.isEmpty()) return Collections.emptyMap();
        List<CaseInfo> allCases = caseRepo.findAllById(caseIds);
        Map<String, Long> caseNoToIdMap = allCases.stream()
                .collect(Collectors.toMap(CaseInfo::getCaseNo, CaseInfo::getId));
        List<Evidence> allEvidence = evidenceRepo.findAll();
        Map<Long, Long> countMap = new HashMap<>();
        for (Evidence ev : allEvidence) {
            Long caseId = caseNoToIdMap.get(ev.getCaseNo());
            if (caseId != null) {
                countMap.merge(caseId, 1L, Long::sum);
            }
        }
        return countMap;
    }

    public CaseInfo get(Long id) {
        return caseRepo.findById(id)
                .orElseThrow(() -> ApiException.notFound("案件不存在"));
    }

    @Transactional
    public CaseInfo create(CaseInfo input) {
        if (input.getCaseNo() == null || input.getCaseNo().isBlank()) {
            throw ApiException.badRequest("案件编号不能为空");
        }
        if (input.getTitle() == null || input.getTitle().isBlank()) {
            throw ApiException.badRequest("案由不能为空");
        }
        if (input.getCaseType() == null || input.getCaseType().isBlank()) {
            throw ApiException.badRequest("案件类型不能为空");
        }
        if (input.getHandlingUnit() == null || input.getHandlingUnit().isBlank()) {
            throw ApiException.badRequest("办案单位不能为空");
        }
        if (caseRepo.existsByCaseNo(input.getCaseNo())) {
            throw ApiException.conflict("案件编号已存在");
        }
        if (input.getLeadOfficerId() != null && !officerRepo.existsById(input.getLeadOfficerId())) {
            throw ApiException.badRequest("主办民警不存在");
        }
        input.setId(null);
        if (input.getStatus() == null || input.getStatus().isBlank()) {
            input.setStatus("UNDER_INVESTIGATION");
        }
        return caseRepo.save(input);
    }

    @Transactional
    public CaseInfo update(Long id, CaseInfo input) {
        CaseInfo existing = get(id);
        if (input.getCaseNo() != null && !input.getCaseNo().isBlank()) {
            if (!existing.getCaseNo().equals(input.getCaseNo()) && caseRepo.existsByCaseNo(input.getCaseNo())) {
                throw ApiException.conflict("案件编号已存在");
            }
            existing.setCaseNo(input.getCaseNo());
        }
        if (input.getTitle() != null) existing.setTitle(input.getTitle());
        if (input.getCaseType() != null) existing.setCaseType(input.getCaseType());
        if (input.getHandlingUnit() != null) existing.setHandlingUnit(input.getHandlingUnit());
        if (input.getLeadOfficerId() != null) {
            if (!officerRepo.existsById(input.getLeadOfficerId())) {
                throw ApiException.badRequest("主办民警不存在");
            }
            existing.setLeadOfficerId(input.getLeadOfficerId());
        }
        if (input.getStatus() != null && !input.getStatus().isBlank()) {
            existing.setStatus(input.getStatus());
        }
        return caseRepo.save(existing);
    }

    @Transactional
    public void delete(Long id) {
        CaseInfo c = get(id);
        caseRepo.delete(c);
    }

    public CaseDetailView getCaseDetail(Long caseId) {
        CaseInfo caseInfo = get(caseId);
        List<Evidence> evidenceList = evidenceRepo.findByCaseNo(caseInfo.getCaseNo());

        Map<Long, Officer> officerMap = officerRepo.findAll().stream()
                .collect(Collectors.toMap(Officer::getId, o -> o));

        List<EvidenceListItem> evidenceItems = evidenceList.stream().map(ev -> {
            EvidenceListItem item = new EvidenceListItem();
            item.setId(ev.getId());
            item.setEvidenceNo(ev.getEvidenceNo());
            item.setName(ev.getName());
            item.setCategory(ev.getCategory());
            item.setStatus(ev.getStatus());
            item.setLocation(ev.getLocation());

            Optional<CustodyRecord> lastCustody = custodyRepo.findTopByEvidenceIdOrderByOccurredAtDescIdDesc(ev.getId());
            if (lastCustody.isPresent()) {
                CustodyRecord cr = lastCustody.get();
                item.setLastCustodyOccurredAt(cr.getOccurredAt());
                item.setLastCustodyAction(cr.getAction());
                item.setLastCustodyRemark(cr.getRemark());
                Long officerId = cr.getToOfficer() != null ? cr.getToOfficer() : cr.getFromOfficer();
                if (officerId != null) {
                    item.setLastCustodyOfficerId(officerId);
                    Officer officer = officerMap.get(officerId);
                    if (officer != null) {
                        item.setLastCustodyOfficerName(officer.getName());
                    }
                }
            }
            return item;
        }).collect(Collectors.toList());

        CaseDetailView view = new CaseDetailView();
        view.setCaseInfo(caseInfo);
        view.setEvidenceList(evidenceItems);
        return view;
    }
}
