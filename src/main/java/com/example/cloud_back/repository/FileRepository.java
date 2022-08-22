package com.example.cloud_back.repository;

import com.example.cloud_back.model.StorageFile;
import com.example.cloud_back.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface FileRepository extends JpaRepository<StorageFile, Long> {

    Optional<StorageFile> deleteByUserAndFileName(User user, String fileName);

    @Query(value = "select * from FILES f "+
            "join USERS u on u.id = f.user_id " +
            "where f.user_id = ?1 " +
            "order by f.id desc limit ?2", nativeQuery = true)
    Optional<List<StorageFile>> findAllByUserWithLimit(User user, int limit);

    Optional<StorageFile> findByUserAndFileName(User user, String fileName);
}
