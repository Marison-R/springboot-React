package com.amigoscode.journey;

import com.amigoscode.people.People;
import com.amigoscode.people.PeopleRegistration;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment=SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PeopleIntegrationTest {
    @Autowired
    private WebTestClient webTestClient;

    @Test
    void insertPeople() {
        Faker faker=new Faker();
        String name = faker.name().firstName();
        String email = faker.internet().emailAddress()+'-'+UUID.randomUUID();
        PeopleRegistration req=new PeopleRegistration(name,email,20);
        webTestClient.post()
                .uri("/api/v1/peoples")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(req), PeopleRegistration.class)
                .exchange()
                .expectStatus().isOk();
        List<People> res=webTestClient.get()
                .uri("/api/v1/peoples")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(new ParameterizedTypeReference<People>(){})
                .returnResult()
                .getResponseBody();
        People expected = new People(name, email, 20);
        assertThat(res).usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                .contains(expected);


        int id= Math.toIntExact(res.stream().filter(c -> c.getEmail().equals(email)).findFirst().map(People::getId).orElseThrow());
        expected.setId((long) id);
        webTestClient.get()
                .uri("/api/v1/peoples/{id}",id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<People>(){})
                .isEqualTo(expected);

    }

    @Test
    void deletePeople() {
        Faker faker=new Faker();
        String name = faker.name().firstName();
        String email = faker.internet().emailAddress()+'-'+UUID.randomUUID();
        PeopleRegistration req=new PeopleRegistration(name,email,20);
        webTestClient.post()
                .uri("/api/v1/peoples")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(req), PeopleRegistration.class)
                .exchange()
                .expectStatus().isOk();
        List<People> res=webTestClient.get()
                .uri("/api/v1/peoples")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(new ParameterizedTypeReference<People>(){})
                .returnResult()
                .getResponseBody();
        People expected = new People(name, email, 20);
        assertThat(res).usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                .contains(expected);


        int id= Math.toIntExact(res.stream().filter(c -> c.getEmail().equals(email)).findFirst().map(People::getId).orElseThrow());
        expected.setId((long) id);
        webTestClient.delete()
                .uri("/api/v1/peoples/{id}",expected.getId())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk();
        webTestClient.get()
                .uri("/api/v1/peoples/{id}",id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isNotFound();

    }

    @Test
    void updatePeople() {
        Faker faker=new Faker();
        String name = faker.name().firstName();
        String email = faker.internet().emailAddress()+'-'+UUID.randomUUID();
        PeopleRegistration req=new PeopleRegistration(name,email,20);
        webTestClient.post()
                .uri("/api/v1/peoples")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(req), PeopleRegistration.class)
                .exchange()
                .expectStatus().isOk();
        List<People> res=webTestClient.get()
                .uri("/api/v1/peoples")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(new ParameterizedTypeReference<People>(){})
                .returnResult()
                .getResponseBody();
        People expected = new People(name, email, 20);
        assertThat(res).usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                .contains(expected);
        String name2=faker.name().firstName();
        String email2=faker.internet().emailAddress()+'-'+UUID.randomUUID();

        PeopleRegistration newReg=new PeopleRegistration(name2,email2,20);
        int id= Math.toIntExact(res.stream().filter(c -> c.getEmail().equals(email)).findFirst().map(People::getId).orElseThrow());
        expected.setId((long) id);
        webTestClient.put()
                .uri("/api/v1/peoples/{id}",id)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(newReg), PeopleRegistration.class)
                .exchange()
                .expectStatus().isOk();
        res=webTestClient.get()
                .uri("/api/v1/peoples")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(new ParameterizedTypeReference<People>(){})
                .returnResult()
                .getResponseBody();
        People expected2=new People((long) id,name2,email2, 20);
        assertThat(res).contains(expected2);
    }
}
