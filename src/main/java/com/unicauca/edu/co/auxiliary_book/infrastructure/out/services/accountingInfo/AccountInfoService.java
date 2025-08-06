package com.unicauca.edu.co.auxiliary_book.infrastructure.out.services.accountingInfo;

import com.unicauca.edu.co.auxiliary_book.application.ports.out.IAccountingInfoQueryPort;
import com.unicauca.edu.co.auxiliary_book.domain.models.external.AccountingInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Component
public class AccountInfoService implements IAccountingInfoQueryPort {

    private final WebClient webClient;

    @Autowired
    public AccountInfoService(WebClient.Builder webClientBuilder, @Value("${mock.account-info-url}") String url) {
        this.webClient = webClientBuilder.baseUrl(url).build();
    }

    @Override
    public List<AccountingInfo> getAllAccountInfo() {
        return this.webClient.get().retrieve().bodyToFlux(AccountingInfo.class).collectList().block();
    }
}
