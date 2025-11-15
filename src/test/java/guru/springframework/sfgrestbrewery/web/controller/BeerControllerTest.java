package guru.springframework.sfgrestbrewery.web.controller;

import guru.springframework.sfgrestbrewery.bootstrap.BeerLoader;
import guru.springframework.sfgrestbrewery.domain.Beer;
import guru.springframework.sfgrestbrewery.services.BeerService;
import guru.springframework.sfgrestbrewery.web.model.BeerDto;
import guru.springframework.sfgrestbrewery.web.model.BeerPagedList;
import guru.springframework.sfgrestbrewery.web.model.BeerStyleEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@WebFluxTest(BeerController.class)
class BeerControllerTest {

    @Autowired
    WebTestClient webTestClient;

    @MockBean
    BeerService beerService;

    BeerDto validBeer1,validBeer2, testListBeer1, testListBeer2;

    @BeforeEach
    void setUp() {
        validBeer1 = BeerDto.builder()
                .beerName("Test beer")
                .beerStyle("PALE_ALE")
                .upc(BeerLoader.BEER_1_UPC)
                .build();
        validBeer2 = BeerDto.builder()
                .beerName("Test beer")
                .beerStyle("PALE_ALE")
                .upc(BeerLoader.BEER_1_UPC)
                .build();

        testListBeer1 = BeerDto.builder()
                .beerName("List beer1")
                .beerStyle("PALE_ALE")
                .upc(BeerLoader.BEER_1_UPC)
                .build();

        testListBeer2 = BeerDto.builder()
                .beerName("List beer2")
                .beerStyle("PALE_ALE")
                .upc(BeerLoader.BEER_2_UPC)
                .build();

    }

    @Test
    void getBeerById() {
        UUID beerId = UUID.randomUUID();
        given(beerService.getById(any(), any())).willReturn(validBeer1);

        webTestClient.get()
                .uri("/api/v1/beer/" + beerId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Beer.class)
                .value(Beer::getBeerName, equalTo(validBeer1.getBeerName()));
    }

    @Test
    void listBeers() {
        List<BeerDto> beerList = Arrays.asList(testListBeer1, testListBeer2);

        BeerPagedList beerPagedList = new BeerPagedList(beerList, PageRequest.of(1,1), beerList.size());

        given(beerService.listBeers(any(),any(),any(),any())).willReturn(beerPagedList);

        webTestClient.get()
                .uri("/api/v1/beer/")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(BeerPagedList.class);
    }

    @Test
    void getBeerByUpc() {
        String testUpc = BeerLoader.BEER_2_UPC;
        given(beerService.getByUpc(any())).willReturn(validBeer2);

        webTestClient.get()
                .uri("/api/v1/beerUpc/" + testUpc)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Beer.class)
                .value(Beer::getUpc, equalTo(validBeer2.getUpc()));
    }
}