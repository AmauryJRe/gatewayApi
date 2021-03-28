package com.musalasoft.gateway.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.musalasoft.gateway.entity.Gateway;
import com.musalasoft.gateway.entity.Peripheral;
import com.musalasoft.gateway.exception.GatewayException;
import com.musalasoft.gateway.exception.GatewayNotFoundExeption;
import com.musalasoft.gateway.exception.PeripheralNotFoundException;
import com.musalasoft.gateway.service.GatewayService;
import com.musalasoft.gateway.service.PeripheralService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/gateway")
public class GatewayController {
    @Autowired
    private GatewayService gatewayService;

    @Autowired
    private PeripheralService peripheralService;

    private final Logger log = LoggerFactory.getLogger(getClass());

    @GetMapping(value = "/")
    public ResponseEntity<List<EntityModel<Gateway>>> getGateways() {
        log.info("GET ALL GATEWAYS");
        List<Gateway> gateways = gatewayService.findAll();
        if (gateways.isEmpty()) {
            throw new GatewayNotFoundExeption("Gateways resources not found");
        }
        Collections.sort(gateways, Gateway::compareTo);
        List<EntityModel<Gateway>> resources = gateways.stream().map(gateway -> {
            try {
                return EntityModel.of(gateway,
                        linkTo(methodOn(GatewayController.class).getGateway(gateway.getId())).withRel("detail"));
            } catch (Exception e) {
                throw new GatewayNotFoundExeption("not found");
            }
        }).collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.OK).body(resources);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<EntityModel<Gateway>> getGateway(@PathVariable(name = "id") Long id) {
        try {
            Gateway gateway = gatewayService.findById(id);
            EntityModel<Gateway> resource = EntityModel.of(gateway,
                    linkTo(methodOn(GatewayController.class).getGateways()).withRel("gatways"));
            return ResponseEntity.status(HttpStatus.OK).body(resource);
        } catch (Exception e) {
            throw new GatewayNotFoundExeption("Not Found");
        }
    }

    @PostMapping(value = "/")
    public ResponseEntity<EntityModel<Gateway>> addGateway(@Valid @RequestBody Gateway gateway, BindingResult result) {

        if (result.hasErrors())
            throw new GatewayException("Error in field " + result.getFieldError().getField() + " it is not valid");
        Gateway toSave = gatewayService.save(gateway);

        EntityModel<Gateway> resource = EntityModel.of(toSave,
                linkTo(methodOn(GatewayController.class).getGateways()).withRel("gatways"));
        return ResponseEntity.status(HttpStatus.CREATED).body(resource);
    }

    @PutMapping(value = "/")
    public ResponseEntity<EntityModel<Gateway>> editGateway(@Valid @RequestBody Gateway gateway, BindingResult result) {
        if (result.hasErrors())
            throw new GatewayException("Error in field " + result.getFieldError().getField() + " it is not valid");
        Gateway toSave = gatewayService.findById(gateway.getId());
        if (!gateway.getName().isBlank())
            toSave.setName(gateway.getName());
        if (!gateway.getIpV4Address().isBlank())
            toSave.setIpV4Address(gateway.getIpV4Address());
        if (!gateway.getPeripherals().isEmpty())
            toSave.setPeripherals(gateway.getPeripherals());
        Gateway savedGatetway = gatewayService.update(toSave);
        EntityModel<Gateway> resource = EntityModel.of(savedGatetway,
                linkTo(methodOn(GatewayController.class).getGateways()).withRel("gatways"));
        return ResponseEntity.status(HttpStatus.CREATED).body(resource);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> deleteGateway(@PathVariable Long id) {
        gatewayService.deleteById(id);
        return new ResponseEntity(HttpStatus.ACCEPTED);
    }

    @PostMapping(value = "/asociate_peripheral")
    public ResponseEntity<EntityModel<Gateway>> addPeripheralToGateway(
            @RequestParam(name = "gateway_id") Long gateway_id,
            @RequestParam(name = "peripheral_id") Long peripheral_id) {
        Gateway gateway = null;
        try {
            gateway = gatewayService.findById(gateway_id);
        } catch (Exception e) {
            throw new GatewayNotFoundExeption("Not Found");
        }
        if (gateway.getPeripherals().size() < 10) {
            Peripheral peripheral = peripheralService.findById(peripheral_id);
            peripheral.setGateway(gateway);
            peripheralService.save(peripheral);
            gateway.getPeripherals().add(peripheral);
            EntityModel<Gateway> resource = EntityModel.of(gateway,
                    linkTo(methodOn(GatewayController.class).getGateways()).withRel("gatways"));
            return ResponseEntity.status(HttpStatus.FOUND).body(resource);
        } else {
            throw new GatewayException("Gateway already have 10 peripherals");
        }
    }

    @PostMapping(value = "/unlink_peripheral")
    public ResponseEntity<EntityModel<Gateway>> unlinkPeripheralFromGateway(
            @RequestParam(name = "gateway_id") Long gateway_id,
            @RequestParam(name = "peripheral_id") Long peripheral_id) {
        try {
            Peripheral peripheral = peripheralService.findById(peripheral_id);
            peripheral.setGateway(null);
            peripheralService.save(peripheral);
        } catch (Exception e) {
            throw new PeripheralNotFoundException("Perpheral not found");
        }

        try {
            Gateway gateway = gatewayService.findById(gateway_id);
            EntityModel<Gateway> resource = EntityModel.of(gateway,
                    linkTo(methodOn(GatewayController.class).getGateways()).withRel("gatways"));
            return ResponseEntity.status(HttpStatus.FOUND).body(resource);
        } catch (Exception e) {
            throw new GatewayNotFoundExeption("Not Found");
        }

    }
}
