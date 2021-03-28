package com.musalasoft.gateway.dao;

import com.musalasoft.gateway.entity.Gateway;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface GatewayDao extends JpaRepository<Gateway,Long> {

    @Query("select g from Gateway g left join fetch g.peripherals p where g.id=?1")
    Gateway fetchByIdWithPeripheral(Long id);
    
}
