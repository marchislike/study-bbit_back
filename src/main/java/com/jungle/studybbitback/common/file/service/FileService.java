package com.jungle.studybbitback.common.file.service;

import com.jungle.studybbitback.common.file.dto.GetRoomFileResponseDto;
import com.jungle.studybbitback.common.file.entity.UserFile;
import com.jungle.studybbitback.common.file.repository.FileRepository;
import com.jungle.studybbitback.domain.member.entity.Member;
import com.jungle.studybbitback.domain.member.repository.MemberRepository;
import com.jungle.studybbitback.domain.room.entity.Room;
import com.jungle.studybbitback.domain.room.respository.RoomRepository;
import com.jungle.studybbitback.jwt.dto.CustomUserDetails;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.nio.file.AccessDeniedException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class FileService {
    private final S3Client s3Client;
    private final RoomRepository roomRepository;
    private final FileRepository fileRepository;
    private final MemberRepository memberRepository;

    @Value("${spring.cloud.aws.s3.bucketName}")
    private String bucketName;

    @Transactional
    public String uploadFile(MultipartFile file, String type, Long roomId) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("업로드 할 파일이 없습니다.");
        }

        String fileName = file.getOriginalFilename();
        if (!StringUtils.hasText(fileName)) {
            throw new IllegalArgumentException("파일 이름이 없습니다.");
        }

        String folderName = "image".equals(type) ? "image" : "file";

        // UUID v4 추가하여 중복된 파일명 관리
        UUID uuid = UUID.randomUUID();
        String uploadName = folderName + "/" + uuid + "_" + fileName;
        log.info("FileService :: uploadName: ", uploadName);

        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .contentType(file.getContentType())
                .contentLength(file.getSize())
                .key(uploadName)
                .build();

            RequestBody requestBody = RequestBody.fromBytes(file.getBytes());
            s3Client.putObject(putObjectRequest, requestBody);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        GetUrlRequest getUrlRequest = GetUrlRequest.builder()
                .bucket(bucketName)
                .key(uploadName)
                .build();

        String uploadedUrl = s3Client.utilities().getUrl(getUrlRequest).toString();

        if ("file".equals(folderName)) {
            // DB 에 저장한 데이터 적재
            String fileNameOnly = fileName.substring(0, fileName.lastIndexOf("."));
            String fileType = fileName.substring(fileName.lastIndexOf(".")+1);
            Room room = roomRepository.findById(roomId).orElseThrow(
                    () -> new IllegalArgumentException("존재하지 않는 방입니다."));

            UserFile userFile = new UserFile(fileNameOnly, fileType, file.getSize(), uploadedUrl, room);
            fileRepository.save(userFile);
        }

        return uploadedUrl;
    }


    public String deleteFile(String fileUrl) {
        // 파일 URL에서 버킷 이름과 객체 키(object key) 추출
        String[] urlParts = fileUrl.split("/");
        String fileBucket = urlParts[2].split("\\.")[0];

        if (!fileBucket.equals(bucketName)) {
            throw new IllegalArgumentException("Invalid file bucket in URL: " + fileUrl);
        }

        String objectKey = String.join("/", Arrays.copyOfRange(urlParts, 3, urlParts.length));

        // 삭제할 객체가 존재하는지 확인
        try {
            s3Client.headObject(HeadObjectRequest.builder()
                    .bucket(bucketName)
                    .key(objectKey)
                    .build());
        } catch (NoSuchKeyException e) {
            log.error("File does not exist: " + fileUrl);
            throw new IllegalArgumentException("File does not exist: " + fileUrl);
        } catch (S3Exception e) {
            log.error("Error checking file existence: " + e.awsErrorDetails().errorMessage());
            throw new RuntimeException("Failed to check file existence", e);
        }

        // 객체 삭제
        try {
            s3Client.deleteObject(DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(objectKey)
                    .build());
            log.info("File delete complete: " + objectKey);
        } catch (S3Exception e) {
            log.error("File delete fail: " + e.awsErrorDetails().errorMessage());
            throw new RuntimeException("Failed to delete file", e);
        }
        return "success";
    }


    public int deleteRoomFiles(Long roomId) {
        // 파일 목록 조회
        List<UserFile> files = fileRepository.findByRoomId(roomId);

        // 삭제할 파일이 없으면 바로 0 반환
        if (files.isEmpty()) {
            return 0;
        }

        // Stream API를 사용해 삭제 처리
        return files.stream()
                .mapToInt(file -> {
                    try {
                        deleteFile(file.getFileUploadPath());
                        return 1; // 삭제 성공 시 1 반환
                    } catch (Exception e) {
                        log.warn("파일 삭제 중 오류 발생: " + file.getFileUploadPath(), e);
                        return 0; // 삭제 실패 시 0 반환
                    }
                })
                .sum(); // 삭제된 파일 개수 합산
    }

    @Transactional(readOnly = true)
    public Page<GetRoomFileResponseDto> getRoomFile(Pageable pageable, Long roomId) {
        // 파일 데이터를 페이징으로 가져옴.
        Page<UserFile> userFiles = fileRepository.findByRoomId(roomId, pageable);

        // 모든 createdBy ID 추출
        List<Long> createdByIds = userFiles.stream()
                .map(UserFile::getCreatedBy)
                .collect(Collectors.toList());

        // ID로 Member 데이터를 한 번에 조회
        Map<Long, String> memberNicknames = memberRepository.findAllById(createdByIds)
                .stream()
                .collect(Collectors.toMap(Member::getId, Member::getNickname));

        // DTO 매핑 시 닉네임 포함
        return userFiles.map(userFile -> {
            String nickname = memberNicknames.getOrDefault(userFile.getCreatedBy(), "Unknown");
            return new GetRoomFileResponseDto(userFile, nickname);
        });
    }

    @Transactional
    public String deleteUserFile(String fileUrl) throws AccessDeniedException {

        UserFile file = fileRepository.findByFileUploadPath(fileUrl)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 파일입니다."));

        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if(userDetails.getMemberId() != file.getCreatedBy()) {
            throw new AccessDeniedException("생성자만이 삭제 가능합니다.");
        }

        fileRepository.delete(file);

        return deleteFile(fileUrl);
    }
}
