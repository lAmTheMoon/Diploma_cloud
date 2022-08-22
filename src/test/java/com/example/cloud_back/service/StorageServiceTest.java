package com.example.cloud_back.service;

import com.example.cloud_back.exception_handling.IncorrectDataException;
import com.example.cloud_back.model.StorageFile;
import com.example.cloud_back.model.User;
import com.example.cloud_back.repository.FileRepository;
import org.hibernate.type.LocalDateTimeType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@Disabled
class StorageServiceTest {

    @Mock
    private FileRepository fileRepository;
    @InjectMocks
    private StorageService storageService;

    private static final String FILE_NAME = "file.txt";
    private static final String FILE_TYPE = "text/plain";
    private StorageFile file;
    private User user;
    private static String authToken = "Bearer_authToken";

    @BeforeEach
    void setUp() {
        byte[] data = (new LocalDateTimeType()).toString().getBytes();
        user = User.builder()
                .id(2L)
                .firstName("Pups")
                .lastName("Lapups")
                .login("Pups@mail.ru")
                .password("1234")
                .build();
        file = StorageFile.builder()
                .id(1L)
                .fileName(FILE_NAME)
                .fileType(FILE_TYPE)
                .fileSize(20L)
                .data(data)
                .user(user)
                .build();
    }

    @Test
    void getFileByFilenameTest() {
        when(fileRepository.findByUserAndFileName(user, FILE_NAME)).thenReturn(Optional.ofNullable(file));
        StorageFile savedFile = storageService.getFileByFilename(authToken, FILE_NAME);
        assertEquals(file, savedFile);
    }

    @Test
    void getFileByFilenameExceptionTest() {
        Exception exception = assertThrows(IncorrectDataException.class, () -> {
            storageService.getFileByFilename(authToken, FILE_NAME);
        });

        String expectedMessage = "Error input data";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}