package com.crimewatch.repository;

import com.crimewatch.domain.InvestigationNote;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface NoteRepository extends JpaRepository<InvestigationNote, Long> {
    List<InvestigationNote> findByReportIdOrderByCreatedAtDesc(Long reportId);
}

