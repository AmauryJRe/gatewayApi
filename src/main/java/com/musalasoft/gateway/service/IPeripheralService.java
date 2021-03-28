package com.musalasoft.gateway.service;

import java.util.List;

import com.musalasoft.gateway.entity.Peripheral;

public interface IPeripheralService {
    public List<Peripheral> findAll();
    public Peripheral findById(Long id);
    public Peripheral save(Peripheral peripheral);
    public List<Peripheral> saveList(List<Peripheral> peripherals);
    public void deleteById(Long id);
    public Peripheral update(Peripheral peripheral);
}
