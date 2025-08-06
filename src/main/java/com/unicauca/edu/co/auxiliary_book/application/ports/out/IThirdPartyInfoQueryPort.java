package com.unicauca.edu.co.auxiliary_book.application.ports.out;

import com.unicauca.edu.co.auxiliary_book.domain.models.external.ThirdParty;

public interface IThirdPartyInfoQueryPort {
    ThirdParty getThirdPartyById(Long thirdPartyId);
}
