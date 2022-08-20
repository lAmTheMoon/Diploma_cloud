package com.example.cloud_back.service;

import com.example.cloud_back.exception_handling.IncorrectDataException;
import com.example.cloud_back.model.StorageFile;
import com.example.cloud_back.repository.FileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StorageServiceTest {

    @Mock
    private FileRepository fileRepository;
    @InjectMocks
    private StorageService storageService;

    private static final String FILE_NAME = "file.txt";
    private static final String FILE_TYPE = "text/plain";
    private StorageFile file;

    @BeforeEach
    void setUp() {
        file = StorageFile.builder()
                .fileName(FILE_NAME)
                .fileType(FILE_TYPE)
                .fileSize(20L)
                .build();
    }

    @Test
    void getFileByFilenameTest() {
        when(fileRepository.findByFileName(FILE_NAME)).thenReturn(Optional.ofNullable(file));
        StorageFile savedFile = storageService.getFileByFilename(FILE_NAME);
        assertEquals(file, savedFile);
    }

    @Test
    void getFileByFilenameExceptionTest() {
        Exception exception = assertThrows(IncorrectDataException.class, () -> {
            storageService.getFileByFilename(FILE_NAME);
        });

        String expectedMessage = "Error input data";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}