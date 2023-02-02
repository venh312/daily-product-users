package com.daily.product.users.dto;

import com.daily.product.users.domain.Bookmark;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class BookmarkSaveRequestDto {
    private Long targetId;
    private Long userId;

    public Bookmark toEntity() {
        return Bookmark.builder()
            .targetId(targetId)
            .userId(userId)
            .build();
    }
}
