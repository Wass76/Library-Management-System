package com.LibraryManagementSystem.patron.response;

import com.LibraryManagementSystem.utils.response.PaginationResponse;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PatronInfoResponseWrapper {
    private List<PatronInfoResponse> patronsInfo;
    private PaginationResponse pagination;
}
