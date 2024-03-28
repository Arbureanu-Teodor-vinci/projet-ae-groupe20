package be.vinci.pae.services.dal;

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
