package com.redhat.gps.oracleaqtoactivemq;

import org.apache.camel.builder.RouteBuilder;

public class OracleAQToActiveMQMessageBridgeBuilder extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		onException(Exception.class).logStackTrace(true)
				.handled(false);

		//The route ID should be the logger ID
		from("oracleJmsComponent:queue:ORACLE_AQ_QUEUE").tracing().transacted("requiredJta").routeId("oracleaqtoactivemqJmsRoute")
				.to("activeMQJmsComponent:queue:queue.INBOUND_ORACLE_AQ_QUEUE");
	}

}
