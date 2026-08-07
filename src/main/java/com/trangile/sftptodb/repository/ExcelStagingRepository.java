package com.trangile.sftptodb.repository;

import com.trangile.sftptodb.entity.ExcelStaging;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExcelStagingRepository
        extends JpaRepository<ExcelStaging, Long> {

    List<ExcelStaging> findByProcessFlag(String processFlag);
}