package tacos.messaging.jms;

import javax.jms.JMSException;
import javax.jms.Message;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import tacos.domain.Order;

@Component
@RequiredArgsConstructor
public class JmsOrderReceiver implements OrderReceiver {
	private final JmsTemplate jmsTemplate;
	private final MessageConverter messageConverter;

	@Override
	public Order receiveOrder() throws JMSException {
		Message message = jmsTemplate.receive("tacocloud.order.queue");
		return (Order) messageConverter.fromMessage(message);
	}
}
