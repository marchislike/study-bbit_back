package com.jungle.studybbitback.domain.posting.entity;

import com.jungle.studybbitback.common.entity.ModifiedEntity;
import com.jungle.studybbitback.domain.posting.dto.posting.AddPostingRequestDto;
import com.jungle.studybbitback.domain.posting.dto.posting.UpdatePostingRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Posting extends ModifiedEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "posting_id")
    private Long id;

    private String title;
    private String content;
    private String author;

    @OneToMany(mappedBy = "posting")
    @OrderBy("createdAt DESC")
    private List<Comment> comments = new ArrayList<>();

    public Posting(AddPostingRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
        this.author = requestDto.getAuthor();
    }

    public void updatePosting(UpdatePostingRequestDto requestDto) {
        if (StringUtils.hasText(requestDto.getTitle())) {
            this.title = requestDto.getTitle();
        }

        if (StringUtils.hasText(requestDto.getContent())) {
            this.content = requestDto.getContent();
        }

        if (StringUtils.hasText(requestDto.getAuthor())) {
            this.author = requestDto.getAuthor();
        }
    }
}
