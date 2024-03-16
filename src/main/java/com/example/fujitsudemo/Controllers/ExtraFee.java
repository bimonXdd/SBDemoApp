package com.example.fujitsudemo.Controllers;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


/**
 * @Class ExtraFee
 *
 * @description Class to translate info from DB to json
 *
 */
@Data
@Component
@RequiredArgsConstructor
public class ExtraFee {


    private int id;

    private String city;

    private String vehicle;

    private Double RBF;

    private Double WSEF;

    private Double WPEF;

    private Double ATEF;

    private Double extraFee;

    private String error;

    public ExtraFee(int id, String city, String vehicle, Double RBF, Double WSEF, Double WPEF, Double ATEF, Double feeSum, String error) {
        this.id=id;
        this.city = city;
        this.vehicle = vehicle;
        this.RBF = RBF;
        this.WSEF = WSEF;
        this.WPEF = WPEF;
        this.ATEF = ATEF;
        this.extraFee = feeSum;
        this.error = error;
    }
}
