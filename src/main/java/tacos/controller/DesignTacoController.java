package tacos.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import tacos.api.TacoAssembler;
import tacos.api.TacoEntityModel;
import tacos.domain.Ingredient;
import tacos.domain.Order;
import tacos.domain.Taco;
import tacos.domain.User;
import tacos.repository.IngredientRepository;
import tacos.repository.TacoRepository;
import tacos.repository.UserRepository;
import tacos.type.Type;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/design")
@SessionAttributes("order")
public class DesignTacoController {
	private final IngredientRepository ingredientRepository;
	private final TacoRepository tacoRepository;
	private final UserRepository userRepository;

	@ModelAttribute(name = "order")
	public Order order() {
		return new Order();
	}

	@ModelAttribute(name = "taco")
	public Taco taco() {
		return new Taco();
	}

	@GetMapping
	public String showDesignForm(Model model, Principal principal) {
		List<Ingredient> ingredients = new ArrayList<>();
		ingredientRepository.findAll().forEach(ingredients::add);

		Type[] types = Type.values();
		for (Type type : types) {
			model.addAttribute(type.name().toLowerCase(), filterByType(ingredients, type));
		}

		String username = principal.getName();
		User user = userRepository.findByUsername(username);
		model.addAttribute("user", user);

		return "design";
	}

	private List<Ingredient> filterByType(List<Ingredient> ingredients, Type type) {
		return ingredients
			.stream()
			.filter(x -> x.getType().equals(type))
			.collect(Collectors.toList());
	}

	@PostMapping
	public String processDesign(@Valid Taco design, Errors errors, @ModelAttribute Order order) {
		if (errors.hasErrors()) {
			return "design";
		}

		Taco saved = tacoRepository.save(design);
		order.addDesign(saved);

		return "redirect:/orders/current";
	}

	@GetMapping("/recent")
	public CollectionModel<TacoEntityModel> recentTacos() {
		PageRequest pageRequest = PageRequest.of(0, 12, Sort.by("createdAt").descending());
		List<Taco> tacoList = tacoRepository.findAll(pageRequest).getContent();

		CollectionModel<TacoEntityModel> recentResources = new TacoAssembler().toCollectionModel(tacoList);
		recentResources.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(DesignTacoController.class).recentTacos())
			.withRel("recents"));
		return recentResources;
	}
}
