package com.prgrms.ohouse.domain.common.file;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

public interface FileUploader {
    UploadFile upload(MultipartFile multipartFile, String originName, String newName) throws IOException;

    void deleteFile(String fileName) throws IOException;

    boolean existByFileName(String fileName);

}
