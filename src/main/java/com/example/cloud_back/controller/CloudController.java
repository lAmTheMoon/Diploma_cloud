package com.example.cloud_back.controller;

import com.example.cloud_back.dto.StorageFileDto;
import com.example.cloud_back.dto.StorageFileListDto;
import com.example.cloud_back.model.StorageFile;
import com.example.cloud_back.service.StorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class CloudController {

    private final StorageService storageService;

    @PostMapping("/file")
    public void postFile(@RequestParam("file") MultipartFile file) {
        storageService.save(file);
        log.info("Post file: " + file.getOriginalFilename());
    }

    @DeleteMapping(value = "/file/{filename}", produces = MediaType.APPLICATION_JSON_VALUE)
    public void deleteFile(@PathVariable String filename) {
        storageService.deleteFileByFilename(filename);
    }

    @GetMapping(value = "/file/{filename}", produces = MediaType.APPLICATION_JSON_VALUE)
    public StorageFileDto getFile(@PathVariable String filename) {
        StorageFile storageFile = storageService.getFileByFilename(filename);
        return new StorageFileDto(storageFile.getFileName());
    }

    @PutMapping(value = "/file/{filename}", produces = MediaType.APPLICATION_JSON_VALUE)
    public void renameFile(@PathVariable String filename, @RequestBody StorageFileDto fileDto) {
        storageService.renameFile(filename, fileDto.getFilename());
    }

    @GetMapping("/list")
    public List<StorageFileListDto> getFileList(@RequestParam("limit") int limit) {
        return storageService.getFileList(limit);
    }
}
