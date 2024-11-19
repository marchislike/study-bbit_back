package com.jungle.studybbitback.common.file.repository;

import com.jungle.studybbitback.common.file.entity.UserFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileRepository extends JpaRepository<UserFile, Long> {
    List<UserFile> findByRoomId(Long roomId);
}
