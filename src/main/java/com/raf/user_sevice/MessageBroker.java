package com.raf.user_sevice;

import org.apache.activemq.broker.BrokerService;

public class MessageBroker {

    public static void main(String[] args) throws Exception {
        BrokerService broker = new BrokerService();
        // configure the broker t
        broker.addConnector("tcp://localhost:61616");
        broker.start();
    }

}
