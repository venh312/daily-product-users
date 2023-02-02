package com.daily.product.users.controller;

import com.daily.product.users.dto.BookmarkSaveRequestDto;
import com.daily.product.users.service.BookmarkService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user-bookmark")
@Tag(name = "Bookmark", description = "Bookmark API 명세서")
public class BookmarkController {
    private final BookmarkService bookmarkService;

    public BookmarkController(BookmarkService bookmarkService) {
        this.bookmarkService = bookmarkService;
    }

    @Operation(summary = "[Bookmark] 등록", description = "찜/즐겨찾기 등록")
    @Parameters({
        @Parameter(name="targetId", description = "대상 ID", required = true),
        @Parameter(name="userId", description = "사용자 ID", required = true),
    })
    @PostMapping
    public ResponseEntity<Long> save(BookmarkSaveRequestDto saveRequestDto) {
        return ResponseEntity.ok(bookmarkService.save(saveRequestDto));
    }

    @Operation(summary = "[Bookmark] 삭제", description = "찜/즐겨찾기 삭제")
    @Parameters({
        @Parameter(name="id", description = "Bookmark PK", required = true),
    })
    @DeleteMapping("/{id}")
    public ResponseEntity deleteById(@PathVariable Long id) {
        bookmarkService.deleteById(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
