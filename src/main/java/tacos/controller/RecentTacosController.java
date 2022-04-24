package tacos.controller;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.RequiredArgsConstructor;
import tacos.api.TacoAssembler;
import tacos.api.TacoEntityModel;
import tacos.domain.Taco;
import tacos.repository.TacoRepository;

@RequiredArgsConstructor
@RepositoryRestController
public class RecentTacosController {
	private final TacoRepository tacoRepository;

	@GetMapping(path = "/tacos/recent", produces = "application/hal+json")
	public ResponseEntity<CollectionModel<TacoEntityModel>> recentTacos() {
		PageRequest pageRequest = PageRequest.of(0, 12, Sort.by("createdAt").descending());
		List<Taco> tacoList = tacoRepository.findAll(pageRequest).getContent();

		CollectionModel<TacoEntityModel> recentResources = new TacoAssembler().toCollectionModel(tacoList);
		recentResources.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(RecentTacosController.class).recentTacos())
			.withRel("recents"));
		return new ResponseEntity<>(recentResources, HttpStatus.OK);
	}
}
