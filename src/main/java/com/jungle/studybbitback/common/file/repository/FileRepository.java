package com.jungle.studybbitback.common.file.repository;

import com.jungle.studybbitback.common.file.entity.UserFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FileRepository extends JpaRepository<UserFile, Long> {
    List<UserFile> findByRoomId(Long roomId);

    Page<UserFile> findByRoomId(Long roomId, Pageable pageable);

    Optional<UserFile> findByFileUploadPath(String fileUrl);

    void deleteByFileUploadPath(String fileUrl);
}
