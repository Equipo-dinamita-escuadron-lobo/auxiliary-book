package com.unicauca.edu.co.auxiliary_book.infrastructure.out.clients.accountingInfo;

import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.unicauca.edu.co.auxiliary_book.application.ports.out.IAccountingInfoClient;
import com.unicauca.edu.co.auxiliary_book.domain.models.external.accountingInfo.AccountingInfo;

/**
 * @brief Service for retrieving accounting information from an external service.
 *
 * Implements the contract for obtaining accounting information records
 * using a WebClient to communicate with the external accounting info service.
 */
@Component
public class AccountInfoService implements IAccountingInfoClient {

    private final WebClient webClient;

    public AccountInfoService(
            @Qualifier("externalWebClientBuilder") WebClient.Builder webClientBuilder,
            @Value("${services.account-info-service.base-url}") String url
    ) {
        this.webClient = webClientBuilder.baseUrl(url).build();
    }

    /**
     * @brief Retrieves all accounting information records from the external service.
     * @return List of AccountingInfo objects.
     */
    @Override
    public List<AccountingInfo> getAllAccountInfo() {
        List<AccountingInfo> lst = this.webClient.get().retrieve().bodyToFlux(AccountingInfo.class).collectList().block();
        return lst != null ? lst : List.of();
    }
}
