package tacos.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import tacos.domain.Ingredient;
import tacos.type.Type;

@RequiredArgsConstructor
@Repository
public class JdbcIngredientRepository implements IngredientRepository {
	private final JdbcTemplate jdbcTemplate;

	@Override
	public List<Ingredient> findAll() {
		return jdbcTemplate.query("SELECT id, name, type FROM ingredient", this::mapRowToIngredient);
	}

	@Override
	public Ingredient findById(String id) {
		return jdbcTemplate.queryForObject("SELECT id, name, type FROM ingredient WHERE id = ?", this::mapRowToIngredient, id);
	}

	@Override
	public Ingredient save(Ingredient ingredient) {
		jdbcTemplate.update("INSERT INTO ingredient (id, name, type) VALUES (?, ?, ?)",
			ingredient.getId(),
			ingredient.getName(),
			ingredient.getType().toString());
		return ingredient;
	}

	private Ingredient mapRowToIngredient(ResultSet resultSet, int rowNumber) throws SQLException {
		return new Ingredient(resultSet.getString("id"),
			resultSet.getString("name"),
			Type.valueOf(resultSet.getString("type")));
	}
}
