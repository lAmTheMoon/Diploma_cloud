package com.example.cloud_back.repository;

import com.example.cloud_back.model.StorageFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
public interface FileRepository extends JpaRepository<StorageFile, Long> {

    Optional<StorageFile> findByFileName(String fileName);

    @Transactional
    Optional<StorageFile> deleteByFileName(String fileName);
}
