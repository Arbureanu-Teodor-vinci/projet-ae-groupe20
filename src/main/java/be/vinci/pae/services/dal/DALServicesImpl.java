package be.vinci.pae.services.dal;

import be.vinci.pae.api.filters.FatalException;
import be.vinci.pae.utils.Config;
import jakarta.inject.Singleton;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.apache.commons.dbcp2.BasicDataSource;

/**
 * The Class DALServiceImpl.
 */
public class DALServicesImpl implements DALTransactionServices, DALServices {


  // Connection pool to allow multiple conexions
  private static final BasicDataSource dataSourcePool = new BasicDataSource();
  // ThreadLocal is used to store a connection for each thread
  private static final ThreadLocal<Connection> threadConnection = new ThreadLocal<Connection>();
  // ThreadLocal is used to store the number of transactions for each thread to manage nested transactions
  private static final ThreadLocal<Integer> transactionCount = new ThreadLocal<Integer>() {
    @Override
    protected Integer initialValue() {
      return 0;
    }
  };

  // Initialize the connection pool
  static {
    dataSourcePool.setDriverClassName("org.postgresql.Driver");
    dataSourcePool.setUrl(Config.getProperty("DatabaseFilePath"));
    dataSourcePool.setUsername(Config.getProperty("DatabaseUser"));
    dataSourcePool.setPassword(Config.getProperty("DatabasePassword"));
    dataSourcePool.setMinIdle(5);
    dataSourcePool.setMaxIdle(10);
    dataSourcePool.setMaxTotal(5);
  }

  /**
   * Instantiates a new DAL service impl and connect to the database.
   */
  @Singleton
  public DALServicesImpl() {
    try {
      Class.forName("org.postgresql.Driver");
    } catch (ClassNotFoundException e) {
      System.out.println("Driver PostgreSQL manquant !");
      System.exit(1);
    }
  }

  @Override
  public PreparedStatement getPS(String request) {
    try {
      // Create a prepared statement from the request
      return getConnection().prepareStatement(request);
    } catch (SQLException e) {
      throw new FatalException(e);
    }
  }

  @Override
  public Connection getConnection() {
    // Get the connection for the current thread
    Connection connection = threadConnection.get();
    try {
      // If the connection is null or closed, get a new connection from the pool
      if (connection == null || connection.isClosed()) {
        // Get a new connection from the pool
        connection = dataSourcePool.getConnection();
        // Store the connection for the current thread
        threadConnection.set(connection);
      }
    } catch (SQLException e) {
      // If an error occurs, close the connection before throwing the exception
      closeConnection();
      throw new FatalException(e);
    }
    return connection;
  }

  @Override
  public void closeConnection() {
    // Get the connection for the current thread
    Connection connection = threadConnection.get();
    try {
      // If the connection is not null, not closed and in auto-commit mode, close the connection
      if (connection != null && !connection.isClosed() && connection.getAutoCommit()) {
        connection.close();
        // Remove the connection from the thread
        threadConnection.remove();
      }
    } catch (SQLException e) {
      throw new FatalException(e);
    }
  }

  @Override
  public void startTransaction() {
    // If the transaction count is 0, start a new transaction
    if (transactionCount.get() == 0) {
      // Get the connection for the current thread
      Connection connection = getConnection();
      try {
        // set auto-commit to false to start a transaction
        connection.setAutoCommit(false);
      } catch (SQLException e) {
        throw new FatalException(e);
      }
    }
    // Increment the transaction count
    transactionCount.set(transactionCount.get() + 1);
  }

  @Override
  public void commitTransaction() {
    // Decrement the transaction count
    transactionCount.set(transactionCount.get() - 1);
    // If the transaction count is 0, commit the transaction
    if (transactionCount.get() == 0) {
      Connection connection = threadConnection.get();
      try {
        connection.commit();
        // set auto-commit to true to end the transaction
        connection.setAutoCommit(true);
      } catch (SQLException e) {
        throw new FatalException(e);
      } finally {
        // Close the connection
        closeConnection();
      }
    }
  }

  @Override
  public void rollbackTransaction() {
    // Reset the transaction count
    transactionCount.set(0);
    // Get the connection for the current thread
    Connection connection = threadConnection.get();
    if (connection != null) {
      try {
        // Rollback the transaction
        connection.rollback();
        // set auto-commit to true to end the transaction
        connection.setAutoCommit(true);
      } catch (SQLException e) {
        throw new FatalException(e);
      } finally {
        // Close the connection
        closeConnection();
      }
    }
  }
}
