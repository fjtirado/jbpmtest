package com.company.services.impl;

import java.util.Arrays;
import java.util.Collection;

import com.company.services.BookingOptionsService;
import org.springframework.stereotype.Component;

@Component
public class SpainBookingOptionsService implements BookingOptionsService {

    private final Collection<String> cities = Arrays.asList("Madrid", "Barcelona", "Valencia");

    @Override
    public Collection<String> getCities() {
        return cities;
    }

}
