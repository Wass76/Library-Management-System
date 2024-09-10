package com.BaseProject.user.service;

import com.BaseProject.user.request.AuthenticationRequest;
import com.BaseProject.user.response.ClientAuthenticationResponse;
import com.BaseProject.user.request.ClientRegisterRequest;
import com.BaseProject.config.JwtService;
import com.BaseProject.config.RateLimiterConfig;
import com.BaseProject.utils.exception.RequestNotValidException;
import com.BaseProject.utils.exception.TooManyRequestException;
import com.BaseProject.user.request.ChangePasswordRequest;
import com.BaseProject.user.model.Client;
import com.BaseProject.user.repository.ClientRepository;
import com.BaseProject.utils.Validator.ObjectsValidator;
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

@Service
@RequiredArgsConstructor
public class ClientService {

    private final PasswordEncoder passwordEncoder;
    private final ClientRepository clientRepository;
    private static final String LOGIN_RATE_LIMITER = "loginRateLimiter";
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final RateLimiterConfig rateLimiterConfig;
    private final RateLimiterRegistry rateLimiterRegistry;

    @Autowired
    private ObjectsValidator<ClientRegisterRequest> registerRequestValidator;
    @Autowired
    private ObjectsValidator<AuthenticationRequest> authenticationRequestValidator;

    @Transactional
    public ClientAuthenticationResponse ClientRegister(ClientRegisterRequest request) {
        registerRequestValidator.validate(request);
        var user = Client.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();
        clientRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return ClientAuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    @Transactional
    public ClientAuthenticationResponse ClientLogin(AuthenticationRequest request, HttpServletRequest httpServletRequest) {

        String userIp = httpServletRequest.getRemoteAddr();
        if (rateLimiterConfig.getBlockedIPs().contains(userIp)) {
            throw new TooManyRequestException("Too many login attempts. Please try again later.");
        }

        String rateLimiterKey = LOGIN_RATE_LIMITER + "-" + userIp;
        RateLimiter rateLimiter = rateLimiterRegistry.rateLimiter(rateLimiterKey);


//        Optional<Client> theclient = clientRepository.findByEmail(request.getEmail());


//        if (theclient.isEmpty())
//            throw new UsernameNotFoundException("The email not found, please register");
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
            var user = clientRepository.findByEmail(request.getEmail()).orElseThrow();
            var jwtToken = jwtService.generateToken(user);
            return ClientAuthenticationResponse.builder()
                    .token(jwtToken)
                    .build();
        }
        else
            rateLimiterConfig.blockIP(userIp);
        throw new TooManyRequestException("Too many login attempts, Please try again later.");
    }

    public void changePassword(
            ChangePasswordRequest request, Principal connectedUser){

        var user  = (Client) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        //check if currentPassword is correct
        if(!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())){
            throw new RequestNotValidException("Wrong password");
//          throw new IllegalStateException("Wrong password");
        }
        if(!request.getNewPassword().equals(request.getConfirmPassword())){
            throw new RequestNotValidException("Password are not the same");
//            throw new IllegalStateException("Password are not the same");
        }

        // Update the password
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        clientRepository.save(user);

    }

}
