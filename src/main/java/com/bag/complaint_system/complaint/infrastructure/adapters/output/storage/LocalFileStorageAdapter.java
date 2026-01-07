package com.bag.complaint_system.complaint.infrastructure.adapters.output.storage;

import com.bag.complaint_system.complaint.application.ports.output.FileStoragePort;
import com.bag.complaint_system.shared.exception.StorageException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Component
@Slf4j
public class LocalFileStorageAdapter implements FileStoragePort {

  private final Path fileStorageLocation = Paths.get("uploads/evidence");

  public LocalFileStorageAdapter() {
    try {
      Files.createDirectories(fileStorageLocation);
    } catch (Exception e) {
      throw new StorageException("The directory for uploading files could not be created.", e);
    }
  }

  @Override
  public String addEvidence(MultipartFile file, Long complaintId) {
    if (file.isEmpty()) {
      throw new StorageException("Failed to save empty file.");
    }

    try {
      String originalName = file.getOriginalFilename();
      String extension =
          originalName != null && originalName.contains(".")
              ? originalName.substring(originalName.lastIndexOf("."))
              : "";
      String newFileName = complaintId + "-" + UUID.randomUUID() + extension;

      Path targetLocation = this.fileStorageLocation.resolve(newFileName);
      Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

      log.info("File uploaded successfully in: {}", targetLocation);

      return targetLocation.toString();
    } catch (IOException e) {
      throw new StorageException("Failed to store file " + file.getOriginalFilename(), e);
    }
  }
}
