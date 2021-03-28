package com.musalasoft.gateway.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Data;

@Data
@Entity
@Table(name = "peripherals")
public class Peripheral implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String vendor;

    @Temporal(TemporalType.DATE)
    private Date created;

    private PeripheralStatus status;

    @ManyToOne
    @JoinColumn(name = "gateway_id")
    @JsonBackReference
    private Gateway gateway;

    @PrePersist
    private void prePersist() {
        created = new Date();
    }

    public Long getGateway(){
        return gateway.getId();
    }

    public int compareTo(Peripheral peripheral){
        if (getId() == null || peripheral.getId() == null)
            return 0;
        return getId().compareTo(peripheral.getId());
    }
    
}
