package com.BaseProject.user.model;

import com.BaseProject.wallet.model.Wallet;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@SuperBuilder
@Table
public class Client extends BaseUser {
    @Column(unique = true)
    private String username;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Wallet wallet;
}
