package tacos.converter;

import java.util.Optional;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import tacos.domain.Ingredient;
import tacos.repository.IngredientRepository;

@Component
@RequiredArgsConstructor
public class IngredientByIdConverter implements Converter<String, Ingredient> {
	private final IngredientRepository ingredientRepository;

	@Override
	public Ingredient convert(String source) {
		Optional<Ingredient> optionalIngredient = ingredientRepository.findById(source);
		return optionalIngredient.orElse(null);
	}
}
