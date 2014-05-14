## OracleAQ to ActiveMQ XA transactional route with Apache Camel
 This project demonstrates a Camel route that does an XA transaction across OracleAQ and ActiveMQ. It can be deployed to Red Hat JBoss Fuse for demonstration purposes.

 The pre-requisites for this project are wrapped bundles for:

* ojdbc6 (Oracle JDBC Driver)
* aqapi (client jar for OracleAQ)

### Installation and Testing

 * Use `mvn clean install` to create the bundle for deployment
 * Start up JBoss Fuse and install wrapped bundles for ojdbc6 and aqapi
 * Install current bundle using `install -s mvn:com.redhat.gps/aq-to-amq/1.5-SNAPSHOT`
 * Run the `PumpMessagesIntoOracleAQTest.java` class to push messages into OracleAQ, which should show up in ActiveMQ, and the logs should show the transaction start, enlist and commit phases.
