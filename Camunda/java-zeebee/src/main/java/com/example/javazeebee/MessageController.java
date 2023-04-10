package com.example.javazeebee;


import com.example.javazeebee.message.dto.PublishRequest;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("api/message")
public class MessageController {

    @Autowired
    MessageService service;
    @PostMapping
    public ResponseEntity<?> publish(@RequestBody PublishRequest request, @RequestHeader(HttpHeaders.AUTHORIZATION) String token) throws Exception {
        System.out.println("Got message request for " + request.getBusiness() + " " + request.getForYearMonth());
        if (!service.isTokenValid(token)) {
            System.out.println("Invalid JWT");
            return ResponseEntity.status(401).body("JWT is not valid");
        }
        System.out.println("Token is valid");

        if (service.updateSeat(request, token)){
            var key = service.publish(request);
        } else {
            ResponseEntity.badRequest().body("Failed to update");
        }

        return ResponseEntity.ok().build();
    }
}