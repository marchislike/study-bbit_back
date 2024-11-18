package com.jungle.studybbitback.common.file.repository;

import com.jungle.studybbitback.common.file.entity.UserFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<UserFile, Long> {
}
