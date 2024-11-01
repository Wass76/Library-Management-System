package com.LibraryManagementSystem.user.service;

import com.LibraryManagementSystem.user.Enum.Role;
import com.LibraryManagementSystem.user.model.Librarian;
import com.LibraryManagementSystem.user.repository.LibrarianRepository;
import com.LibraryManagementSystem.user.request.AuthenticationRequest;
import com.LibraryManagementSystem.user.response.LibrarianAuthenticationResponse;
import com.LibraryManagementSystem.user.request.LibrarianRegisterRequest;
import com.LibraryManagementSystem.config.JwtService;
import com.LibraryManagementSystem.config.RateLimiterConfig;
import com.LibraryManagementSystem.utils.Mapper.ClassMapper;
import com.LibraryManagementSystem.utils.exception.RequestNotValidException;
import com.LibraryManagementSystem.utils.exception.TooManyRequestException;
import com.LibraryManagementSystem.user.request.ChangePasswordRequest;
import com.LibraryManagementSystem.utils.Validator.ObjectsValidator;
import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class LibrarianService {

    private final PasswordEncoder passwordEncoder;
    private final LibrarianRepository librarianRepository;
    private static final String LOGIN_RATE_LIMITER = "loginRateLimiter";
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final RateLimiterConfig rateLimiterConfig;
    private final RateLimiterRegistry rateLimiterRegistry;

    @Autowired
    private ObjectsValidator<LibrarianRegisterRequest> registerRequestValidator;
    @Autowired
    private ObjectsValidator<AuthenticationRequest> authenticationRequestValidator;

    @Transactional
    public LibrarianAuthenticationResponse librarianRegister(LibrarianRegisterRequest request) {
        registerRequestValidator.validate(request);
        Librarian existedEmail = librarianRepository.findByEmail(request.getEmail()).orElse(null);
        if (existedEmail != null) {
            throw new RequestNotValidException("Email already exists");
        }

        Librarian user = ClassMapper.INSTANCE.librarianDtoToEntity(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        librarianRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        LibrarianAuthenticationResponse response = ClassMapper.INSTANCE.entityToDto(user);
        response.setToken(jwtToken);
        return response;
    }

    @Transactional
    public LibrarianAuthenticationResponse librarianLogin(AuthenticationRequest request, HttpServletRequest httpServletRequest) {

        String userIp = httpServletRequest.getRemoteAddr();
        if (rateLimiterConfig.getBlockedIPs().contains(userIp)) {
            throw new TooManyRequestException("Too many login attempts. Please try again later.");
        }

        String rateLimiterKey = LOGIN_RATE_LIMITER + "-" + userIp;
        RateLimiter rateLimiter = rateLimiterRegistry.rateLimiter(rateLimiterKey);


        if (rateLimiter.acquirePermission()) {
            Authentication authentication;
            try {
                authentication = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.getEmail(),
                                request.getPassword()
                        ));
            } catch (AuthenticationException exception) {
                throw new BadCredentialsException("invalid email or password");
            }
            SecurityContextHolder.getContext().setAuthentication(authentication);

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
            var user = librarianRepository.findByEmail(request.getEmail()).orElseThrow(
                    () -> new RequestNotValidException("email not found")
            );
            var jwtToken = jwtService.generateToken(user);
            LibrarianAuthenticationResponse response = ClassMapper.INSTANCE.entityToDto(user);
            response.setToken(jwtToken);
            return response;
        }
        else
            rateLimiterConfig.blockIP(userIp);
        throw new TooManyRequestException("Too many login attempts, Please try again later.");
    }

    public void changePassword(
            ChangePasswordRequest request, Principal connectedUser){

        var user  = (Librarian) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        if(!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())){
            throw new RequestNotValidException("Wrong password");
        }
        if(!request.getNewPassword().equals(request.getConfirmPassword())){
            throw new RequestNotValidException("Password are not the same");
        }
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        librarianRepository.save(user);
    }
}
