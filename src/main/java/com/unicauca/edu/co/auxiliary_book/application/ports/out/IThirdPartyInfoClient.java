package com.unicauca.edu.co.auxiliary_book.application.ports.out;

import com.unicauca.edu.co.auxiliary_book.domain.models.external.thirdParty.ThirdParty;

public interface IThirdPartyInfoClient {
    ThirdParty getThirdPartyById(Long thirdPartyId);
}
