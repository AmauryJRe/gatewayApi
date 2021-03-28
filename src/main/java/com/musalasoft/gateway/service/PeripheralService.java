package com.musalasoft.gateway.service;

import java.util.List;
import java.util.stream.Collectors;

import com.musalasoft.gateway.dao.PeripheralDao;
import com.musalasoft.gateway.entity.Peripheral;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PeripheralService implements IPeripheralService {

    @Autowired
    private PeripheralDao peripheralDao;

    @Override
    public List<Peripheral> findAll() {

        return (List<Peripheral>) peripheralDao.findAll();
    }

    @Override
    public Peripheral findById(Long id) {

        return peripheralDao.findById(id).get();
    }

    @Override
    public Peripheral save(Peripheral peripheral) {
        return peripheralDao.save(peripheral);
    }

    @Override
    public void deleteById(Long id) {
        peripheralDao.deleteById(id);
    }

    @Override
    public Peripheral update(Peripheral peripheral) {
        Peripheral toSave = peripheralDao.findById(peripheral.getId()).orElseThrow();
        toSave.setVendor(peripheral.getVendor());
        toSave.setStatus(peripheral.getStatus());
        return peripheralDao.save(toSave);
    }

    @Override
    public List<Peripheral> saveList(List<Peripheral> peripherals) {
        return peripherals.stream().map(peripheral -> {
            return peripheralDao.save(peripheral);
        }).collect(Collectors.toList());
    }

}
