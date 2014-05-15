## OracleAQ to ActiveMQ XA transactional route with Apache Camel
 This project demonstrates a Camel route that does an XA transaction across OracleAQ and ActiveMQ. It can be deployed to Red Hat JBoss Fuse for demonstration purposes.

### Important Note -
This code is not supported by my employer - Red Hat. I have made this available online to save someone hours of time trying to figure out a way to accomplish this using R&D (Re-run and Debug :)).

 The pre-requisites for this project are wrapped bundles for:

* ojdbc6 (Oracle JDBC Driver)
* aqapi (client jar for OracleAQ)

### Installation and Testing -

 * Use `mvn clean install` to create the bundle for deployment
 * Copy the sample config file from `src/test/resources/etc/com.redhat.gps.aqtoamq.jms.cfg` to the `<JBoss Fuse Install Dir>/etc` folder.
 * Start up JBoss Fuse and install wrapped bundles for `ojdbc6` and `aqapi`
 * Install current bundle using `install -s mvn:com.redhat.gps/aq-to-amq/1.5-SNAPSHOT`
 * Run the `PumpMessagesIntoOracleAQTest.java` class to push messages into OracleAQ, which should show up in ActiveMQ, and the logs should show the transaction start, enlist and commit phases.

#### Note -
If you start seeing some intermittent errors in the log file stating `Recovery error: null` coming from the `Aries TransactionManagerImpl`, it could be a permissions issue for the database user.
Run the following commands to fix this:

```sql
GRANT SELECT ON sys.dba_pending_transactions TO user;
GRANT SELECT ON sys.pending_trans$ TO user;
GRANT SELECT ON sys.dba_2pc_pending TO user;
GRANT EXECUTE ON sys.dbms_system TO user;  
```
Credit for this fix goes to `caarlos0` here: http://stackoverflow.com/questions/14652111/weird-oracle-11g-jdbc-driver-error-under-jboss-as-7-1-1
