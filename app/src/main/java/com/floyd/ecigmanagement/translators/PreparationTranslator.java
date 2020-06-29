package com.floyd.ecigmanagement.translators;

import com.floyd.ecigmanagement.models.Preparation;
import com.floyd.ecigmanagement.uio.PreparationUio;

public class PreparationTranslator {

    public static PreparationUio translatePreparationToPreparationUio(Preparation preparation) {
        PreparationUio uio = new PreparationUio();

        uio.setCapacity(preparation.getCapacity());
        uio.setId(preparation.getId());
        uio.setImageUrl("");
        uio.setArome(preparation.getArome().getTaste());
        uio.setQuantity(preparation.getQuantity());
        uio.setAromeId(preparation.getArome().getId());

        return uio;
    }
}
