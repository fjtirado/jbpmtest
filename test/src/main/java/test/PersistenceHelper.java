package test;

import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.sql.XADataSource;
import javax.transaction.TransactionManager;
import javax.transaction.TransactionSynchronizationRegistry;

import com.arjuna.ats.jta.common.jtaPropertyManager;
import com.mysql.cj.jdbc.MysqlXADataSource;
import org.apache.tomcat.dbcp.dbcp2.managed.BasicManagedDataSource;
import org.drools.persistence.jta.JtaTransactionManager;
import org.h2.jdbcx.JdbcDataSource;

public class PersistenceHelper {

    private static final String PERSISTENCE_UNIT_NAME = "org.jbpm.persistence.jpa";

    public static EntityManagerFactory setupPersistence() throws NamingException, SQLException {
        final String userName = "jbpm";
        final String password = "jbpm";
        Context ctx = new InitialContext();
        TransactionManager tm = com.arjuna.ats.jta.TransactionManager.transactionManager();
        TransactionSynchronizationRegistry tsr = jtaPropertyManager.getJTAEnvironmentBean().getTransactionSynchronizationRegistry();
        BasicManagedDataSource mds = new BasicManagedDataSource();
        mds.setUsername(userName);
        mds.setPassword(password);
        mds.setTransactionManager(tm);
        mds.setXaDataSourceInstance(getMySQL(userName, password));
        mds.setTransactionSynchronizationRegistry(tsr);
        ctx.bind("jdbc/jbpm-ds", mds);
        ctx.bind(JtaTransactionManager.DEFAULT_USER_TRANSACTION_NAME, com.arjuna.ats.jta.UserTransaction.userTransaction());
        ctx.bind("java:comp/TransactionManager", tm);
        ctx.bind("java:comp/TransactionSynchronizationRegistry", tsr);
        return Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
    }

    private static XADataSource getH2DS(String userName, String password) {
        JdbcDataSource sds = new JdbcDataSource();
        sds.setURL("jdbc:h2:mem:jbpm-db;MVCC=true");
        sds.setUser(userName);
        sds.setPassword(password);
        return sds;
    }

    private static XADataSource getMySQL(String userName, String password) throws SQLException {
        MysqlXADataSource sds = new MysqlXADataSource();
        sds.setUser(userName);
        sds.setPassword(password);
        sds.setDatabaseName("jbpmdb");
        sds.setServerTimezone("CET");
        return sds;
    }

}
