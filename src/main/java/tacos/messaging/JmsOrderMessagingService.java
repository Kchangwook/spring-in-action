package tacos.messaging;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import tacos.domain.Order;

@Service
@RequiredArgsConstructor
public class JmsOrderMessagingService implements OrderMessagingService {
	private final JmsTemplate jmsTemplate;

	@Override
	public void sendOrder(Order order) {
		jmsTemplate.send(session -> session.createObjectMessage(order));
	}
}
