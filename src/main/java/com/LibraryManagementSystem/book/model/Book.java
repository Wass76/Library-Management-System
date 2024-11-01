package com.LibraryManagementSystem.book.model;

import com.LibraryManagementSystem.book.Enum.BookCategoryEnum;
import com.LibraryManagementSystem.borrowingRecord.model.BorrowingRecord;
import com.LibraryManagementSystem.utils.Model.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
@Entity
@DynamicUpdate
@DynamicInsert
@Data
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String title;
    private String author;

    @Column(unique = true)
    private String isbn;

    private String publisher;
    private LocalDate publicationYear;
    @Enumerated(EnumType.ORDINAL)
    private BookCategoryEnum category;
    private String language;
    private Integer numberOfPages;

    @CreatedDate
    private LocalDate dateAdded;
    @LastModifiedDate
    private LocalDate dateModified;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL)
    private List<BorrowingRecord> borrowingRecords;
}
