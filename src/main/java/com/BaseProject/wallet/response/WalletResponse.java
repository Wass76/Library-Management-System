package com.BaseProject.wallet.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WalletResponse {
    private Double balance;
}
