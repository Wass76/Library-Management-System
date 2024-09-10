package com.BaseProject.wallet.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MoneyCodeResponse {

    private Integer id;
    private String code;
    private Double amount;

}
