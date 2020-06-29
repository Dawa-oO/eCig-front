package com.floyd.ecigmanagement.translators;

import com.floyd.ecigmanagement.models.Booster;
import com.floyd.ecigmanagement.uio.BoosterUio;

public class BoosterTranslator {

    public static BoosterUio translateBoosterToBoosterUio(Booster booster) {
        BoosterUio uio = new BoosterUio();
        uio.setQuantity(booster.getQuantity());
        uio.setCapacity(booster.getCapacity());
        uio.setId(booster.getId());
        uio.setImageUrl("");
        uio.setPgvg(booster.getPgvg());

        return uio;
    }
}
