package com.musalasoft.gateway.service;

import java.util.List;

import com.musalasoft.gateway.entity.Gateway;

public interface IGatewayService {
    public List<Gateway> findAll();
    public Gateway findById(Long id);
    public Gateway save(Gateway gateway);
    public void deleteById(Long id);
    public Gateway update(Gateway gateway);
    public Gateway fetchByIdWithPeripheral(Long id);
}
