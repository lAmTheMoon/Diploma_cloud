package com.example.cloud_back.service;

import com.example.cloud_back.dto.StorageFileListDto;
import com.example.cloud_back.exception_handling.DeleteFileException;
import com.example.cloud_back.exception_handling.IncorrectDataException;
import com.example.cloud_back.model.StorageFile;
import com.example.cloud_back.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StorageService {

    private final FileRepository fileRepository;

    public void save(MultipartFile file) {
        StorageFile storageFile = StorageFile.builder()
                .fileName(file.getOriginalFilename())
                .fileType(file.getContentType())
                .fileSize(file.getSize())
                .build();
        fileRepository.save(storageFile);
    }

    public StorageFile getFileByFilename(String filename) {
        return fileRepository.findByFileName(filename)
                .orElseThrow(IncorrectDataException::new);
    }

    public void deleteFileByFilename(String filename) {
        fileRepository.deleteByFileName(filename)
                .orElseThrow(DeleteFileException::new);
    }

    public void renameFile(String oldName, String newName) {
        StorageFile storageFile = getFileByFilename(oldName);
        deleteFileByFilename(oldName);
        storageFile.setFileName(newName);
        fileRepository.save(storageFile);
    }

    public List<StorageFileListDto> getFileList(int limit) {
        var pageRequest = PageRequest.of(0, limit, Sort.by("filename"));

        return fileRepository.findAll(pageRequest).stream()
                .map(file -> new StorageFileListDto(file.getFileName(), file.getFileSize()))
                .collect(Collectors.toList());
    }
}
