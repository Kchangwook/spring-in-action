package tacos.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import tacos.type.Type;

@Data
@AllArgsConstructor
public class Ingredient {
	private String id;
	private String name;
	private Type type;
}
