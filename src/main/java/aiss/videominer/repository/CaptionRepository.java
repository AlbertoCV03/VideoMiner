package aiss.videominer.repository;

import aiss.videominer.model.Caption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Repository
public interface CaptionRepository extends JpaRepository<Caption, String> {
    Page<Caption> findByLanguage(String language, Pageable pageable);
}
