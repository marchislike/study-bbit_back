package com.jungle.studybbitback.common.file.entity;

import com.jungle.studybbitback.common.entity.CreatedEntity;
import com.jungle.studybbitback.common.entity.ModifiedTimeEntity;
import com.jungle.studybbitback.domain.room.entity.Room;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
public class UserFile extends CreatedEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "file_id")
    private Long id;

    @Column(name = "file_name", nullable = false)
    private String uploadName;

    @Column(name = "file_type", nullable = false)
    private String fileType;

    @Column(name = "file_size", nullable = false)
    private Long fileSize;

    @Column(name = "file_upload_path")
    private String fileUploadPath;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room room;

    public UserFile(String uploadName, String fileType, Long fileSize, String fileUploadPath, Room room) {
        this.uploadName = uploadName;
        this.fileType = fileType;
        this.fileSize = fileSize;
        this.fileUploadPath = fileUploadPath;
        this.room = room;
    }
}
