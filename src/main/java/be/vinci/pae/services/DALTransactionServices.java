package be.vinci.pae.services;

/**
 * Services of database connection to send SQL transactions.
 */
public interface DALTransactionServices {

  /**
   * Start a transaction.
   */
  void startTransaction();

  /**
   * Commit a transaction.
   */
  void commitTransaction();

  /**
   * Rollback a transaction.
   */
  void rollbackTransaction();
}
