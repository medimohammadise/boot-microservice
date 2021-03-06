package com.example.reservationservice;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Collection;
import java.util.stream.Stream;


@EnableDiscoveryClient
@SpringBootApplication
public class ReservationServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReservationServiceApplication.class, args);
	}
	


	@Bean
	CommandLineRunner  commandLineRunner(ReservationReposirory reservationReposirory) {
		
		
		return strings-> {
			Stream.of("Mehdi","Maryam","avina").forEach(n->
			reservationReposirory.save(new Reservation(n)) );
			
		};
		
	}
	
	

}

@RefreshScope
@RestController
class MessgeRestController{

	@Value("${message}")
	private String message;

	@RequestMapping("/message")
	String getMessage(){
		return message;
	}

}

@RepositoryRestResource
@Repository
interface ReservationReposirory extends JpaRepository<Reservation, Long>{
	@RestResource(path="by-name")
	Collection<Reservation> findByReservationName(@Param("rn") String resevationName);
}
@Entity
class Reservation {
	@Id
	@GeneratedValue
	private Long id;
	private String reservationName;
	public Reservation(){

	}
	public Reservation(String n) {
		//constructor for JPA
		this.reservationName=n;
	}
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getReservationName() {
		return reservationName;
	}

	public void setReservationName(String reservationName) {
		this.reservationName = reservationName;
	}
	@Override
	public String toString() {

		return "Reservation {id: "+id+" ,reservationName:"+reservationName+"}	";
	}
}