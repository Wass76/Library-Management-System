package com.LibraryManagementSystem.patron.service;

import com.LibraryManagementSystem.patron.model.Patron;
import com.LibraryManagementSystem.patron.repository.PatronRepository;
import com.LibraryManagementSystem.patron.request.PatronRequest;
import com.LibraryManagementSystem.patron.response.PatronInfoResponse;
import com.LibraryManagementSystem.patron.response.PatronInfoResponseWrapper;
import com.LibraryManagementSystem.utils.Mapper.ClassMapper;
import com.LibraryManagementSystem.utils.Validator.ObjectsValidator;
import com.LibraryManagementSystem.utils.exception.RequestNotValidException;
import com.LibraryManagementSystem.utils.request.PaginationRequest;
import com.LibraryManagementSystem.utils.response.PaginationResponse;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PatronService {

    @Autowired
    private PatronRepository patronRepository;
    @Autowired
    ObjectsValidator<PaginationRequest> paginationObjectsValidator;
    @Autowired
    ObjectsValidator<PatronRequest> patronRequestObjectsValidator;

    public PatronInfoResponseWrapper findAllPatrons(PaginationRequest paginationRequest) {
        paginationObjectsValidator.validate(paginationRequest);
        Pageable pageable = PageRequest.of(paginationRequest.getPage()-1, paginationRequest.getSize());
        Page<Patron> patrons = patronRepository.findAll(pageable);
        List<PatronInfoResponse> patronInfoResponses = new ArrayList<>();
        for (Patron patron : patrons) {
            patronInfoResponses.add(ClassMapper.INSTANCE.entityToDto(patron));
        }
        return PatronInfoResponseWrapper.builder()
                .patronsInfo(patronInfoResponses)
                .pagination(PaginationResponse
                        .builder()
                        .page(patrons.getNumber() + 1)
                        .perPage(patrons.getNumberOfElements())
                        .total(patrons.getTotalElements())
                        .build()
                )
                .build();
    }

    public PatronInfoResponse findPatronById(UUID patronId) {
        Patron patron = patronRepository.findById(patronId).orElseThrow(
                () -> new RequestNotValidException("there is no book with patronId: " + patronId)
        );
        return ClassMapper.INSTANCE.entityToDto(patron);
    }

    @Transactional
    public PatronInfoResponse addPatron(PatronRequest request) {
        Patron existedPhoneNumber = patronRepository.findByPhoneNumber(request.getPhoneNumber()).orElse(null);
        if (existedPhoneNumber != null) {
            throw new RequestNotValidException("phone number already taken");
        }
        Patron existedEmail = patronRepository.findByEmail(request.getEmail()).orElse(null);
        if (existedEmail != null) {
            throw new RequestNotValidException("email already taken");
        }
        patronRequestObjectsValidator.validate(request);
        Patron patron = ClassMapper.INSTANCE.patronDtoToEntity(request);
        patronRepository.save(patron);
        return ClassMapper.INSTANCE.entityToDto(patron);
    }

    @Transactional
    public PatronInfoResponse updatePatron(PatronRequest request, UUID id) {
        Patron patron = patronRepository.findById(id).orElseThrow(
                () -> new RequestNotValidException("there is no book with id: " + id)
        );
        patronRequestObjectsValidator.validate(request);
        Patron existedPhoneNumber = patronRepository.findByPhoneNumberAndIdIsNot(request.getPhoneNumber(), id).orElse(null);
        if (existedPhoneNumber != null) {
            throw new RequestNotValidException("phone number already taken");
        }
        Patron existedEmail = patronRepository.findByEmailAndIdIsNot(request.getEmail(), id).orElse(null);
        if (existedEmail != null) {
            throw new RequestNotValidException("email already taken");
        }
        patron.setFirstName(request.getFirstName());
        patron.setLastName(request.getLastName());
        patron.setEmail(request.getEmail());
        patron.setMembershipDate(request.getMembershipDate());
        patron.setAddress(request.getAddress());
        patron.setPhoneNumber(patron.getPhoneNumber());
        patronRepository.saveAndFlush(patron);
        return ClassMapper.INSTANCE.entityToDto(patron);
    }

    public boolean deletePatron(UUID id) {
        Patron patron = patronRepository.findById(id).orElseThrow(
                () -> new RequestNotValidException("there is no patron with id: " + id)
        );
        patronRepository.delete(patron);
        return true;
    }
}
