package com.musalasoft.gateway.service;

import java.util.List;

import com.musalasoft.gateway.dao.GatewayDao;
import com.musalasoft.gateway.entity.Gateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
public class GatewayService implements IGatewayService {

    @Autowired
    private GatewayDao gatewayDao;

    @Override
    public List<Gateway> findAll() {
        return gatewayDao.findAll();
    }

    @Override
    public Gateway findById(Long id) {
        return gatewayDao.findById(id).get();
    }

    @Override
    public Gateway save(Gateway gateway) {
        return gatewayDao.save(gateway);
    }

    @Override
    public void deleteById(Long id) {
        gatewayDao.deleteById(id);
    }

    @Override
    public Gateway update(Gateway gateway) {
        return gatewayDao.save(gateway);
    }

    @Override
    public Gateway fetchByIdWithPeripheral(Long id) {
        return gatewayDao.fetchByIdWithPeripheral(id);
    }

}
