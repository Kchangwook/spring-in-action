package tacos.api;

import java.time.LocalDate;
import java.util.List;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import lombok.Getter;
import tacos.domain.Ingredient;
import tacos.domain.Taco;

@Getter
@Relation(value = "taco", collectionRelation = "tacos")
public class TacoEntityModel extends RepresentationModel<TacoEntityModel> {
	private final String name;
	private final LocalDate createdAt;
	private final List<Ingredient> ingredients;

	public TacoEntityModel(Taco taco) {
		this.name = taco.getName();
		this.createdAt = taco.getCreatedAt();
		this.ingredients = taco.getIngredients();
	}
}
