package com.redhat.gps.test;

import oracle.sql.CLOB;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.slf4j.bridge.SLF4JBridgeHandler;

import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;

public class PumpMessagesIntoOracleAQTest {

	private static final String TEST_MESSAGE = "Four score and seven years ago our fathers brought forth on this continent, a new nation, conceived in Liberty, and dedicated to the proposition that all men are created equal.";

	private static final String DB_SCHEMA_NAME = "esb";

	private String oracleJdbcUrl = "jdbc:oracle:thin:user/password@localhost:1521/poc";

	private java.sql.Connection dbConnection;

	private String oracleQueueName = "ORACLE_AQ_QUEUE";

	private final Logger log = Logger.getLogger(PumpMessagesIntoOracleAQTest.class);

	@Test
	public void pumpMessages() throws Exception {
		java.util.logging.LogManager.getLogManager().getLogger("").setLevel(Level.FINEST);
		SLF4JBridgeHandler.removeHandlersForRootLogger();
		SLF4JBridgeHandler.install();

		log.info("Creating UNIT TEST Class");

		createOracleAQQueue();

		Clob message = CLOB.createTemporary(dbConnection, false, CLOB.DURATION_SESSION);
		message.setString(1, TEST_MESSAGE);

		String plsql = String.format("begin mdb_aq.send_message('" + DB_SCHEMA_NAME + ".%1$s', ?); end;", oracleQueueName);
		CallableStatement statement = dbConnection.prepareCall(plsql);
		statement.setClob(1, message);
		statement.execute();
		statement.close();
		message.free();
	}

	/**
	 * @throws java.sql.SQLException
	 */
	private void createOracleAQQueue() throws SQLException {
		dbConnection = DriverManager.getConnection(oracleJdbcUrl);
		String plsql = String
				.format("BEGIN DBMS_AQADM.CREATE_QUEUE (Queue_name => '" + DB_SCHEMA_NAME + ".%1$s', Queue_table => '" + DB_SCHEMA_NAME + ".jms_qtt_text', max_retries => 5, retry_delay => 5); "
								+ "DBMS_AQADM.START_QUEUE (Queue_name => '" + DB_SCHEMA_NAME + ".%1$s'); END;",
						oracleQueueName
				);

		CallableStatement statement = dbConnection.prepareCall(plsql);
		try {
			statement.execute();
		} catch (SQLException sqlE) {
			if (sqlE.getErrorCode() == 24006) {
				log.info("Queue already exists");
				//removeOracleAQQueue();
				//createOracleAQQueue();
			} else {
				throw sqlE;
			}
		} finally {
			statement.close();
		}

	}

	/**
	 * @throws java.sql.SQLException
	 */

	private void removeOracleAQQueue() throws SQLException {
		String plsql = String
				.format("BEGIN DBMS_AQADM.STOP_QUEUE (Queue_name => '" + DB_SCHEMA_NAME + ".%1$s'); "
								+ "DBMS_AQADM.DROP_QUEUE(Queue_name => '" + DB_SCHEMA_NAME + ".%1$s', auto_commit => TRUE); END;",
						oracleQueueName
				);

		CallableStatement statement = dbConnection.prepareCall(plsql);
		statement.execute();
		statement.close();
	}

}
