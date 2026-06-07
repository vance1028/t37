package com.police.eom.repo;

import com.police.eom.domain.CaseInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CaseInfoRepository extends JpaRepository<CaseInfo, Long> {
    boolean existsByCaseNo(String caseNo);
    Optional<CaseInfo> findByCaseNo(String caseNo);
    List<CaseInfo> findByStatus(String status);
}
