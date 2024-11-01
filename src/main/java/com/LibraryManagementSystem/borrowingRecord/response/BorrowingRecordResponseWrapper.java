package com.LibraryManagementSystem.borrowingRecord.response;

import com.LibraryManagementSystem.utils.response.PaginationResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@Schema
public class BorrowingRecordResponseWrapper {
    private List<BorrowingRecordResponse> borrowingRecordList;
    private PaginationResponse pagination;
}
