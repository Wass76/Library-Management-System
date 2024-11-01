package com.LibraryManagementSystem.book.request;

import com.LibraryManagementSystem.book.Enum.BookCategoryEnum;
import com.LibraryManagementSystem.utils.annotation.ValidEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookRequest {

    @NotBlank(message = "title shouldn't be blank")
    private String title;
    @NotBlank(message = "author shouldn't be blank")
    private String author;
    @NotBlank(message = "isbn shouldn't be blank")
    private String isbn;

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate publicationYear;
    private String publisher;

    @ValidEnum(enumClass = BookCategoryEnum.class)
    private String category;
    @NotBlank(message = "language shouldn't be blank")
    private String language;
    @NotNull(message = "numberOfPages shouldn't be null")
    private Integer numberOfPages;
}
