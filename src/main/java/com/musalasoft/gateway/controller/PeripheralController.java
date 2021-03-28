package com.musalasoft.gateway.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.musalasoft.gateway.entity.Peripheral;
import com.musalasoft.gateway.exception.PeripheralNotFoundException;
import com.musalasoft.gateway.service.PeripheralService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/peripheral")
public class PeripheralController {
    
    @Autowired
    private PeripheralService peripheralService;

    @GetMapping(value = "/")
    public ResponseEntity<List<EntityModel<Peripheral>>> getPeripherals(){
        List<Peripheral> peripherals = peripheralService.findAll();
        Collections.sort(peripherals,Peripheral::compareTo);
        
        List<EntityModel<Peripheral>> resources = peripherals.stream().map(peripheral -> {
            try {
                return EntityModel.of(peripheral,
                        linkTo(methodOn(PeripheralController.class).getPeripheral(peripheral.getId())).withRel("detail"));
            } catch (Exception e) {
                throw new PeripheralNotFoundException("not found");
            }
        }).collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.FOUND).body(resources);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<EntityModel<Peripheral>> getPeripheral(@PathVariable(name = "id") Long id) {
        try {
            Peripheral gateway = peripheralService.findById(id);
            EntityModel<Peripheral> resource = EntityModel.of(gateway,
                    linkTo(methodOn(GatewayController.class).getGateways()).withRel("gatways"));
            return ResponseEntity.status(HttpStatus.FOUND).body(resource);
        } catch (Exception e) {
            throw new PeripheralNotFoundException("Not Found");
        }
    }

    @PostMapping(value = "/")
    public ResponseEntity<EntityModel<Peripheral>> addPeripheral(@RequestBody Peripheral peripheral) {;
        Peripheral savedP = peripheralService.save(peripheral);

        EntityModel<Peripheral> resource = EntityModel.of(savedP,
                linkTo(methodOn(PeripheralController.class).getPeripherals()).withRel("gatways"));
        return ResponseEntity.status(HttpStatus.CREATED).body(resource);
    }

    @PutMapping(value = "/")
    public ResponseEntity<EntityModel<Peripheral>> editPeripheral(@RequestBody Peripheral peripheral){
        Peripheral savedP= peripheralService.update(peripheral);
        EntityModel<Peripheral> resource = EntityModel.of(savedP,
                linkTo(methodOn(GatewayController.class).getGateways()).withRel("gatways"));
        return ResponseEntity.status(HttpStatus.CREATED).body(resource);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> deletePeripheral(@PathVariable(name = "id") Long id){
        peripheralService.deleteById(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}
