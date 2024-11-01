package com.LibraryManagementSystem.book.response;

import com.LibraryManagementSystem.utils.response.PaginationResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@Schema
@AllArgsConstructor
@NoArgsConstructor
public class BookInfoResponseWrapper {
    private List<BookInfoResponse> bookInfo;
    private PaginationResponse pagination;
}
