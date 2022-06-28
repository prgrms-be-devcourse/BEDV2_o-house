// package com.prgrms.ohouse.domain.common.file;
//
// import java.io.IOException;
// import java.util.UUID;
//
// import org.springframework.stereotype.Component;
// import org.springframework.web.multipart.MultipartFile;
//
// import lombok.RequiredArgsConstructor;
//
// @RequiredArgsConstructor
// @Component
// public class SimpleFileManager implements FileManager {
//     private final FileUploader fileUploader;
//
//     @Override
//     public UploadFile storeFile(MultipartFile multipartFile) throws IOException {
//         String originName = multipartFile.getOriginalFilename();
//         String newName = createStoreFileName(originName);
//         return fileUploader.upload(multipartFile, originName, newName);
//     }
//
//     private String createStoreFileName(String originalFilename) {
//         String ext = extractExt(originalFilename);
//         String uuid = UUID.randomUUID().toString();
//         return uuid + "." + ext;
//     }
//
//     private String extractExt(String originalFilename) {
//         int pos = originalFilename.lastIndexOf(".");
//         return originalFilename.substring(pos + 1);
//     }
// }
