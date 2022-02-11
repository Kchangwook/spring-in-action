package tacos.repository;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import tacos.domain.Order;
import tacos.domain.Taco;

@Repository
public class JdbcOrderRepository implements OrderRepository {
	private SimpleJdbcInsert orderInserter;
	private SimpleJdbcInsert orderTacoInserter;
	private ObjectMapper objectMapper;

	@Autowired
	public JdbcOrderRepository(JdbcTemplate jdbcTemplate) {
		orderInserter = new SimpleJdbcInsert(jdbcTemplate)
			.withTableName("taco_order")
			.usingGeneratedKeyColumns("id");

		orderTacoInserter = new SimpleJdbcInsert(jdbcTemplate)
			.withTableName("taco_order_tacos");

		this.objectMapper = new ObjectMapper();
	}

	@Override
	public Order save(Order order) {
		order.setPlacedAt(LocalDate.now());
		long orderId = saveOrderDetails(order);
		order.setId(orderId);
		order.getTacoList().forEach(taco -> saveTacoToOrder(taco, orderId));
		return order;
	}

	private long saveOrderDetails(Order order) {
		Map<String, Object> values = objectMapper.convertValue(order, Map.class);
		values.put("placedAt", order.getPlacedAt());

		return orderInserter.executeAndReturnKey(values)
			.longValue();
	}

	private void saveTacoToOrder(Taco taco, long orderId) {
		Map<String, Object> values = new HashMap<>();
		values.put("taco_order", orderId);
		values.put("taco", taco.getId());
		orderTacoInserter.execute(values);
	}
}
