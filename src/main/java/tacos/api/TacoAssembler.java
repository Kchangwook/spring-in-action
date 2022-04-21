package tacos.api;

import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;

import tacos.controller.DesignTacoController;
import tacos.domain.Taco;

public class TacoAssembler extends RepresentationModelAssemblerSupport<Taco, TacoEntityModel> {
	public TacoAssembler() {
		super(DesignTacoController.class, TacoEntityModel.class);
	}

	@Override
	protected TacoEntityModel instantiateModel(Taco entity) {
		return new TacoEntityModel(entity);
	}

	@Override
	public TacoEntityModel toModel(Taco entity) {
		return createModelWithId(entity.getId(), entity);
	}
}
