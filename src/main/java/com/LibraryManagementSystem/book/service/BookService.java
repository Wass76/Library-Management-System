package com.LibraryManagementSystem.book.service;

import com.LibraryManagementSystem.book.Enum.BookCategoryEnum;
import com.LibraryManagementSystem.book.model.Book;
import com.LibraryManagementSystem.book.repository.BookRepository;
import com.LibraryManagementSystem.book.request.BookRequest;
import com.LibraryManagementSystem.book.response.BookInfoResponse;
import com.LibraryManagementSystem.book.response.BookInfoResponseWrapper;
import com.LibraryManagementSystem.utils.Mapper.ClassMapper;
import com.LibraryManagementSystem.utils.Validator.ObjectsValidator;
import com.LibraryManagementSystem.utils.exception.RequestNotValidException;
import com.LibraryManagementSystem.utils.request.PaginationRequest;
import com.LibraryManagementSystem.utils.response.PaginationResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    ObjectsValidator<PaginationRequest> paginationObjectsValidator;

    @Autowired
    ObjectsValidator<BookRequest> bookRequestValidator;

    public BookInfoResponseWrapper getAllBooks(PaginationRequest paginationRequest) {
        paginationObjectsValidator.validate(paginationRequest);
        Pageable pageable = PageRequest.of(paginationRequest.getPage()-1, paginationRequest.getSize());
        Page<Book> books = bookRepository.findAll(pageable);

        List<BookInfoResponse> bookInfoResponseList = new ArrayList<>();
        for (Book book : books) {
            bookInfoResponseList.add(ClassMapper.INSTANCE.entityToDto(book));
        }

        PaginationResponse paginationResponse = PaginationResponse.builder()
                .page(books.getNumber() + 1)
                .perPage(books.getNumberOfElements())
                .total(books.getTotalElements())
                .build();

        return BookInfoResponseWrapper.builder()
                .bookInfo(bookInfoResponseList)
                .pagination(paginationResponse)
                .build();
    }

    public BookInfoResponse getBookInfoById(UUID bookId) {
        Book book = bookRepository.findById(bookId).orElseThrow(
                () -> new RequestNotValidException("there is no book with id: " + bookId)
        );
        return ClassMapper.INSTANCE.entityToDto(book);
    }

    @Transactional
    public BookInfoResponse addBook(BookRequest request) {
        bookRequestValidator.validate(request);

        Book existedIsbnBook = bookRepository.findByIsbn(request.getIsbn()).orElse(null);
        if (existedIsbnBook != null) {
            throw new RequestNotValidException("isbn already exists");
        }

        Book book = ClassMapper.INSTANCE.bookDtoToEntity(request);
        bookRepository.save(book);

        return ClassMapper.INSTANCE.entityToDto(book);
    }

    @Transactional
    public BookInfoResponse updateBook(BookRequest request, UUID bookId) {
        if (!bookRepository.existsById(bookId)) {
            throw new RequestNotValidException("there is no book with id: " + bookId);
        }

        bookRequestValidator.validate(request);

        Book existedIsbnBook = bookRepository.findByIsbnAndIdIsNot(request.getIsbn(), bookId).orElse(null);
        if (existedIsbnBook != null) {
            throw new RequestNotValidException("isbn already exists");
        }

        Book book = bookRepository.findById(bookId).get();
        book.setIsbn(request.getIsbn());
        book.setAuthor(request.getAuthor());
        book.setTitle(request.getTitle());
        book.setCategory(BookCategoryEnum.valueOf(request.getCategory()));
        book.setLanguage(request.getLanguage());
        book.setPublisher(request.getPublisher());
        book.setNumberOfPages(request.getNumberOfPages());
        book.setPublicationYear(request.getPublicationYear());

        bookRepository.saveAndFlush(book);

        return ClassMapper.INSTANCE.entityToDto(book);
    }

    public boolean deleteBook(UUID bookId) {
        if (!bookRepository.existsById(bookId)) {
            throw new RequestNotValidException("there is no book with id: " + bookId);
        }

        bookRepository.deleteById(bookId);
        return true;
    }
}