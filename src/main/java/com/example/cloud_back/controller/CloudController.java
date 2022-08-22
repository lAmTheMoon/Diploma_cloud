package com.example.cloud_back.controller;

import com.example.cloud_back.dto.StorageFileDto;
import com.example.cloud_back.model.StorageFile;
import com.example.cloud_back.service.StorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/")
@Validated
@RequiredArgsConstructor
public class CloudController {

    private final StorageService storageService;

    @PostMapping(value = "/file", produces = MediaType.APPLICATION_JSON_VALUE, consumes = {"multipart/form-data"})
    public ResponseEntity<?> uploadFile(@RequestHeader("auth-token") String authToken,
                                        @Valid @RequestParam String filename,
                                        @RequestBody MultipartFile file) throws IOException {
        storageService.save(authToken, filename, file);
        log.info("Post file: " + filename);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(value = "/file", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteFile(@RequestHeader("auth-token") String authToken,
                                        @Valid @RequestParam String filename) {
        storageService.deleteFileByFilename(authToken, filename);
        log.info("Delete file: " + filename);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/file", produces = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<byte[]> downloadFile(@RequestHeader("auth-token") String authToken,
                                               @Valid @RequestParam String filename) {
        StorageFile file= storageService.getFileByFilename(authToken, filename);
        log.info("Download file: " + filename);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(file.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFileName() + "\"")
                .body(file.getData());
    }

    @PutMapping(value = "/file", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> renameFile(@RequestHeader("auth-token") String authToken,
                                        @Valid @RequestParam String filename,
                                        @RequestBody Map<String, String> bodyParams) {
        storageService.renameFile(authToken, filename, bodyParams.get("filename"));
        log.info("Rename file: " + filename);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/list")
    public List<StorageFileDto> getFileList(@RequestHeader("auth-token") String authToken,
                                                @RequestParam("limit") int limit) {
        log.info("Get files: " + limit);
        return storageService.getFileList(authToken, limit);
    }
}
