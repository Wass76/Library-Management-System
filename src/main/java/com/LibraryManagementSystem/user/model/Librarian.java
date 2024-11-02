package com.LibraryManagementSystem.user.model;

import com.LibraryManagementSystem.user.Enum.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Librarian extends BaseUser{

    @Id
    @GeneratedValue
    private UUID id;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role.getAuthorities();
    }
}
