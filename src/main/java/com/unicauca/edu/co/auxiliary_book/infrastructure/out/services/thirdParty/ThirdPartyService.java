package com.unicauca.edu.co.auxiliary_book.infrastructure.out.services.thirdParty;

import com.fasterxml.jackson.databind.JsonNode;
import com.unicauca.edu.co.auxiliary_book.application.ports.out.IThirdPartyInfoQueryPort;
import com.unicauca.edu.co.auxiliary_book.domain.models.external.ThirdParty;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class ThirdPartyService implements IThirdPartyInfoQueryPort {

    private final WebClient webClient;



    public ThirdPartyService(WebClient.Builder webClientBuilder, @Value("${third-party-api.url}") String url) {
        this.webClient = webClientBuilder.baseUrl(url).build();
    }

    @Override
    public ThirdParty getThirdPartyById(Long thirdPartyId) {
        JsonNode thirdPartyResponse = this.webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("third").
                        queryParam("thId", thirdPartyId)
                        .build())
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();
        return (thirdPartyResponse!=null) ? this.convertJsonToThirdParty(thirdPartyResponse) : null;
    }

    private ThirdParty convertJsonToThirdParty(JsonNode jsonNode) {

        ThirdParty objThirdParty = new ThirdParty();

        String name = jsonNode.get("names").asText();
        String LastName = jsonNode.get("lastNames").asText();

        if(name.isEmpty() || LastName.isEmpty()) {
           objThirdParty.setName(jsonNode.get("socialReason").asText());
        }else{
            objThirdParty.setName(name + " " + LastName);
        }

        objThirdParty.setId(jsonNode.get("thId").asLong());
        objThirdParty.setIdentificationNumber(jsonNode.get("identificationNumber").asLong());
        objThirdParty.setTypeId(jsonNode.get("typeId").get("typeId").asText());

        return objThirdParty;
    }
}
