package com.allancordeiro.heroesapi.controller;

import com.allancordeiro.heroesapi.document.Heroes;
import com.allancordeiro.heroesapi.repository.HeroesRepository;
import com.allancordeiro.heroesapi.service.HeroesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import com.allancordeiro.heroesapi.constants.HeroesConstant;

@RestController
@Slf4j
public class HeroesController {

    HeroesService heroesService;
    HeroesRepository heroesRepository;

    private static final org.slf4j.Logger logger=
            org.slf4j.LoggerFactory.getLogger(HeroesController.class);

    public HeroesController(HeroesService heroesService, HeroesRepository heroesRepository) {
        this.heroesService = heroesService;
        this.heroesRepository = heroesRepository;
    }

    @GetMapping(HeroesConstant.HEROES_ENDPOINT_LOCAL)
    public Flux<Heroes> getAllItems() {
        logger.info("getting the full heroes list");
        return heroesService.findAll();
    }

    @GetMapping(HeroesConstant.HEROES_ENDPOINT_LOCAL + "/{id}")
    public Mono<ResponseEntity<Heroes>> findById(@PathVariable String id) {
        logger.info("getting hero id {}", id) ;
        return heroesService.findById(id)
                .map((item) -> new ResponseEntity<>(item, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping(HeroesConstant.HEROES_ENDPOINT_LOCAL)
    @ResponseStatus(code=HttpStatus.CREATED)
    public Mono<Heroes> createHero(@RequestBody Heroes hero) {
        logger.info("a new hero has been created {}", hero);
        return heroesService.save(hero);
    }

    @DeleteMapping(HeroesConstant.HEROES_ENDPOINT_LOCAL + "/{id}")
    @ResponseStatus(code=HttpStatus.ACCEPTED)
    public Mono<HttpStatus> deleteById(@PathVariable String id) {
        logger.info("deleting hero id {}", id);
        heroesService.deleteById(id);
        return Mono.just(HttpStatus.ACCEPTED);
    }

}
