package com.example.rservationclient;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

@EnableZuulProxy  //just blindly forwards requests from edge server to back-end
@SpringBootApplication
@EnableDiscoveryClient
public class RservationClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(RservationClientApplication.class, args);
    }

    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }
}

@RestController
@RequestMapping(name = "/reservations")
class ReservationServiceGatewayController {


    RestTemplate restTemlate;

    Collection<String> getReservationsFallback(){
     return  new ArrayList<>();
    }
    ReservationServiceGatewayController(RestTemplate restTemlate) {
        this.restTemlate = restTemlate;
    }
    @HystrixCommand(fallbackMethod = "getReservationsFallback")
    @RequestMapping(method = RequestMethod.GET, name = "/name")
    Collection<String> getReservations() {
        ParameterizedTypeReference<Resources<Reservation>> parametrizedType = new ParameterizedTypeReference<Resources<Reservation>>() {

        };
        ResponseEntity<Resources<Reservation>> response = restTemlate.exchange("http://localhost:8008/reservations", HttpMethod.GET, null, parametrizedType);
        return response.getBody().getContent().stream().map(Reservation::getReservationName).collect(Collectors.toList());
    }

}

class Reservation {
    private String reservationName;

    public String getReservationName() {
        return reservationName;
    }

    public void setReservationName(String reservationName) {
        this.reservationName = reservationName;
    }
}