package tacos.repository;

import java.util.List;

import tacos.domain.Ingredient;

public interface IngredientRepository {
	List<Ingredient> findAll();
	Ingredient findById(String id);
	Ingredient save(Ingredient ingredient);
}
