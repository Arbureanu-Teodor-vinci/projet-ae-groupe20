package be.vinci.pae.services;

import java.sql.PreparedStatement;

/**
 * Services to manage the connexion to the database a single time.
 */
public interface DALTransactionServices {

  /**
   * Start a transaction.
   *
   * @return a prepared statement with the query to start a transaction
   */
  PreparedStatement startTransaction();

  /**
   * Commit a transaction.
   *
   * @return a prepared statement with the query to commit a transaction
   */
  PreparedStatement commitTransaction();

  /**
   * Rollback a transaction.
   *
   * @return a prepared statement with the query to rollback a transaction
   */
  PreparedStatement rollbackTransaction();
}
