package com.musalasoft.gateway.dao;

import com.musalasoft.gateway.entity.Peripheral;

import org.springframework.data.repository.CrudRepository;

public interface PeripheralDao extends CrudRepository<Peripheral,Long> {
    
}
