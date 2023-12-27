package com.artistbooking.BookArtist;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BookArtistApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookArtistApplication.class, args);
	}

	@Bean
	public OpenAPI openApiInformation() {
		Server localServer = new Server()
				.url("http://localhost:8080")
				.description("Localhost Server URL");

		Contact contact = new Contact()
				.email("salamkorede345@gmail.com")
				.name("Salami Korede");

		Info info = new Info()
				.contact(contact)
				.description("BOOK-ARTISTE")
				.summary("AN APP THAT HELPS MANAGE ARTISTES. FANS CAN BE UPDATED AND BOOK THEIR FAVE AT WILL")
				.title("BOOK")
				.version("V1.0.0")
				.license(new License().name("Apache 2.0").url("http://springdoc.org"));

		//return new OpenAPI().info(info).addServersItem(localServer);

		return new OpenAPI();
	}
}
