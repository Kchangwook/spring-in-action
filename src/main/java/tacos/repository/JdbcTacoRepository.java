package tacos.repository;

import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDate;
import java.util.Arrays;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import tacos.domain.Ingredient;
import tacos.domain.Taco;

@Repository
@RequiredArgsConstructor
public class JdbcTacoRepository implements TacoRepository{
	private final JdbcTemplate jdbcTemplate;

	@Override
	public Taco save(Taco taco) {
		long tacoId = saveTacoInfo(taco);
		taco.setId(tacoId);

		taco.getIngredients().forEach(ingredient -> saveIngredientToTaco(ingredient, tacoId));

		return taco;
	}

	private long saveTacoInfo(Taco taco) {
		taco.setCreatedAt(LocalDate.now());

		PreparedStatementCreator preparedStatementCreator
			= new PreparedStatementCreatorFactory("INSERT INTO taco(name, createdAt) value (?, ?)", Types.VARCHAR, Types.TIMESTAMP)
			.newPreparedStatementCreator(Arrays.asList(taco.getName(), new Timestamp(Timestamp.valueOf(taco.getCreatedAt().atStartOfDay()).getTime())));

		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(preparedStatementCreator, keyHolder);

		return keyHolder.getKey().longValue();
	}

	private void saveIngredientToTaco(Ingredient ingredient, long tacoId) {
		jdbcTemplate.update("INSERT INTO taco_ingredients (taco, ingredient) VALUES (?, ?)", tacoId, ingredient.getId());
	}
}
