package com.prgrms.ohouse.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prgrms.ohouse.domain.common.file.FileRepository;
import com.prgrms.ohouse.domain.common.file.StoredFile;

public interface JpaFileRepository extends JpaRepository<StoredFile, Long>, FileRepository {
}
