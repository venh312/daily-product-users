package com.daily.product.users.service;

import com.daily.product.users.dto.BookmarkSaveRequestDto;
import com.daily.product.users.repository.BookmarkRepository;
import org.springframework.stereotype.Service;

@Service
public class BookmarkService {
    private final BookmarkRepository bookmarkRepository;

    public BookmarkService(BookmarkRepository bookmarkRepository) {
        this.bookmarkRepository = bookmarkRepository;
    }

    public Long save(BookmarkSaveRequestDto saveRequestDto) {
        return bookmarkRepository.save(saveRequestDto.toEntity()).getId();
    }

    public void deleteById(Long id) {
        bookmarkRepository.deleteById(id);
    }

}
