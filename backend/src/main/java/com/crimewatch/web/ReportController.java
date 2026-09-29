package com.crimewatch.web;

import com.crimewatch.domain.*;
import com.crimewatch.service.*;
import com.crimewatch.web.dto.ReportDtos.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDate;

@RestController @RequestMapping("/api/reports") @RequiredArgsConstructor
public class ReportController {
    private final ReportService reports;
    private final EvidenceService evidence;

    @PostMapping ResponseEntity<ReportView> create(@Valid @RequestBody ReportRequest request) { return ResponseEntity.status(HttpStatus.CREATED).body(reports.create(request)); }

    @GetMapping Page<ReportView> list(
        @RequestParam(required = false) String search, @RequestParam(required = false) ReportStatus status,
        @RequestParam(required = false) Priority priority, @RequestParam(required = false) Long categoryId,
        @RequestParam(required = false) String city,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
        @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size,
        @RequestParam(defaultValue = "updatedAt,desc") String sort) {
        return reports.list(search, status, priority, categoryId, city, from, to, page, size, sort);
    }

    @GetMapping("/{publicId}") ReportView get(@PathVariable String publicId) { return reports.get(publicId); }
    @PutMapping("/{publicId}/status") ReportView status(@PathVariable String publicId, @Valid @RequestBody StatusRequest request) { return reports.updateStatus(publicId, request); }
    @PutMapping("/{publicId}/assign") ReportView assign(@PathVariable String publicId, @Valid @RequestBody AssignRequest request) { return reports.assign(publicId, request); }
    @PostMapping("/{publicId}/notes") ResponseEntity<NoteView> note(@PathVariable String publicId, @Valid @RequestBody NoteRequest request) { return ResponseEntity.status(HttpStatus.CREATED).body(reports.addNote(publicId, request)); }
    @PostMapping(path = "/{publicId}/evidence", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<EvidenceView> evidence(@PathVariable String publicId, @RequestPart("file") MultipartFile file) { return ResponseEntity.status(HttpStatus.CREATED).body(evidence.upload(publicId, file)); }
    @DeleteMapping("/{publicId}") ResponseEntity<Void> delete(@PathVariable String publicId) { reports.delete(publicId); return ResponseEntity.noContent().build(); }
}

