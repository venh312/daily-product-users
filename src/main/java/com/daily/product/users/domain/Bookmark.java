package com.daily.product.users.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@NoArgsConstructor
@Getter
@Entity(name = "bookmark")
public class Bookmark extends BaseTime {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long targetId;
    private Long userId;

    @Builder
    public Bookmark(Long id, Long targetId, Long userId) {
        this.id = id;
        this.targetId = targetId;
        this.userId = userId;
    }
}
