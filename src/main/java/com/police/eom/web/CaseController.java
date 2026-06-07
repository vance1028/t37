package com.police.eom.web;

import com.police.eom.domain.CaseInfo;
import com.police.eom.service.CaseService;
import com.police.eom.web.dto.CaseDetailView;
import com.police.eom.web.dto.CaseListItem;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cases")
public class CaseController {

    private final CaseService service;

    public CaseController(CaseService service) {
        this.service = service;
    }

    @GetMapping
    public List<CaseListItem> list(@RequestParam(required = false) String status) {
        return service.list(status);
    }

    @GetMapping("/{id}")
    public CaseInfo get(@PathVariable Long id) {
        return service.get(id);
    }

    @GetMapping("/{id}/detail")
    public CaseDetailView getDetail(@PathVariable Long id) {
        return service.getCaseDetail(id);
    }

    @PostMapping
    public ResponseEntity<CaseInfo> create(@RequestBody CaseInfo input) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(input));
    }

    @PutMapping("/{id}")
    public CaseInfo update(@PathVariable Long id, @RequestBody CaseInfo input) {
        return service.update(id, input);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
