package com.example.backend.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class CustomerLoyalty {
    private long customer;
    private Date optInDate;
    private Date lastEventDate;
    private long loyalMilliseconds;
}
