package com.example.aiylbank.mapper;

import com.example.aiylbank.dto.request.AccountDtoRequest;
import com.example.aiylbank.dto.response.AccountDtoResponse;
import com.example.aiylbank.entity.Account;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    Account toEntity(AccountDtoRequest accountDtoRequest);
    AccountDtoResponse toDtoResponse(Account account);
    List<AccountDtoResponse> toDtoResponseList(List<Account> accounts);
}
