package com.example.cloud_back.service;

import com.example.cloud_back.dto.StorageFileDto;
import com.example.cloud_back.exception_handling.DeleteFileException;
import com.example.cloud_back.exception_handling.GetFileException;
import com.example.cloud_back.exception_handling.IncorrectDataException;
import com.example.cloud_back.model.StorageFile;
import com.example.cloud_back.model.User;
import com.example.cloud_back.repository.FileRepository;
import com.example.cloud_back.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;

@Service
@RequiredArgsConstructor
public class StorageService {

    private final FileRepository fileRepository;
    private final UserRepository userRepository;
    private Map<String, UserDetails> tokenStorage = new ConcurrentHashMap<>();

    public void login(String authToken, UserDetails userPrincipal) {
        tokenStorage.put(authToken, userPrincipal);
    }

    public Optional<UserDetails> logout(String authToken) {
        return ofNullable(tokenStorage.remove(authToken));
    }

    public void save(String authToken, String fileName, MultipartFile file) throws IOException {
        User user = getUser(authToken);
        StorageFile storageFile = StorageFile.builder()
                .fileName(fileName)
                .fileType(file.getContentType())
                .fileSize(file.getSize())
                .data(file.getBytes())
                .user(user)
                .build();
        fileRepository.save(storageFile);
    }

    public StorageFile getFileByFilename(String authToken, String filename) {
        User user = getUser(authToken);
        return fileRepository.findByUserAndFileName(user, filename)
                .orElseThrow(IncorrectDataException::new);
    }

    public void deleteFileByFilename(String authToken, String filename) {
        User user = getUser(authToken);
        fileRepository.deleteByUserAndFileName(user, filename)
                .orElseThrow(DeleteFileException::new);
    }

    public void renameFile(String authToken, String oldName, String newName) {
        StorageFile storageFile = getFileByFilename(authToken, oldName);
        deleteFileByFilename(authToken, oldName);
        storageFile.setFileName(newName);
        fileRepository.save(storageFile);
    }

    public List<StorageFileDto> getFileList(String authToken, int limit) {
        User user = getUser(authToken);
        List<StorageFile> storageFiles = fileRepository.findAllByUserWithLimit(user, limit)
                .orElseThrow(GetFileException::new);
        return storageFiles.stream()
                .map(f -> new StorageFileDto(f.getFileName()))
                .collect(Collectors.toList());
    }

    private User getUser(String authToken) {
       Optional <UserDetails> userDetails = ofNullable(tokenStorage.get(authToken.substring(7)));
       return userRepository.findByLogin(userDetails.get().getUsername())
               .orElseThrow(IncorrectDataException::new);
    }
}
