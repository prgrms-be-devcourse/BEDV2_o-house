package com.prgrms.ohouse.domain.common.file;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prgrms.ohouse.domain.common.file.StoredFile;

public interface FileRepository extends JpaRepository<StoredFile, Long> {
}
