package com.floyd.ecigmanagement.translators;

import com.floyd.ecigmanagement.models.Arome;
import com.floyd.ecigmanagement.uio.AromeUio;

public class AromeTranslator {

    public static AromeUio translateAromeToAromeUio(Arome arome) {
        AromeUio uio = new AromeUio();
        uio.setBrand(arome.getBrand());
        uio.setCapacity(arome.getCapacity());
        uio.setId(arome.getId());
        uio.setImageUrl("");
        uio.setNote(arome.getNote());
        uio.setQuantity(arome.getQuantity());
        uio.setTaste(arome.getTaste());

        return uio;
    }
}
