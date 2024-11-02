package com.LibraryManagementSystem.utils.Mapper;

import com.LibraryManagementSystem.book.model.Book;
import com.LibraryManagementSystem.book.request.BookRequest;
import com.LibraryManagementSystem.book.response.BookInfoResponse;
import com.LibraryManagementSystem.borrowingRecord.model.BorrowingRecord;
import com.LibraryManagementSystem.borrowingRecord.request.NewBorrowingRecordRequest;
import com.LibraryManagementSystem.borrowingRecord.response.BorrowingRecordResponse;
import com.LibraryManagementSystem.patron.model.Patron;
import com.LibraryManagementSystem.patron.request.PatronRequest;
import com.LibraryManagementSystem.patron.response.PatronInfoResponse;
import com.LibraryManagementSystem.user.model.Librarian;
import com.LibraryManagementSystem.user.request.LibrarianRegisterRequest;
import com.LibraryManagementSystem.user.response.LibrarianAuthenticationResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
//@Component
public interface ClassMapper {

    ClassMapper INSTANCE = Mappers.getMapper( ClassMapper.class );


    //entity to Dto
    BookInfoResponse entityToDto(Book book);
    PatronInfoResponse entityToDto(Patron patron);

    @Mapping(source = "firstName" , target = "firstName")
    LibrarianAuthenticationResponse entityToDto(Librarian librarian);

    BorrowingRecordResponse entityToDto(BorrowingRecord borrowingRecord);





    //Dto to entity
    Book bookDtoToEntity(BookRequest request);
    Patron patronDtoToEntity(PatronRequest request);
    @Mapping(source = "firstName" , target = "firstName")
    Librarian librarianDtoToEntity(LibrarianRegisterRequest request);
//    @Mapping(source = "borrowDate" , target = "borrowDate")
    BorrowingRecord borrowingRecordDtoToEntity(NewBorrowingRecordRequest request);

}