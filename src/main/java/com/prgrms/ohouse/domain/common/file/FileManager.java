package com.prgrms.ohouse.domain.common.file;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

public interface FileManager {
    UploadFile storeFile(MultipartFile multipartFile) throws IOException;
}
