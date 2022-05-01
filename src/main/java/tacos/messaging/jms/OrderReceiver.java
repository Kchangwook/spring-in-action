package tacos.messaging.jms;

import javax.jms.JMSException;

import tacos.domain.Order;

public interface OrderReceiver {
	Order receiveOrder() throws JMSException;
}
