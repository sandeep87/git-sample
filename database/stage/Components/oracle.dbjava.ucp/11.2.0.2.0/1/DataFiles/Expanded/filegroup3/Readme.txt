Oracle Universal Connection Pool
Release 11.2.0.1.0

Production README
=======================================================================


Contents Of This Release
------------------------

For all platforms:

  [ORACLE_HOME]/ucp/lib contains:

  - ucp.jar
    Classes for use with JDK 5.0 and JDK 6. It contains the Universal
    Connection Pool classes, as well as the built-in JDBC Pool Adapter
    classes for standalone UCP / JDBC applications.

  - ucpdemos.jar
    Classes for use with JDK 5.0 and JDK 6. It contains the Universal
    Connection Pool demos and code samples, sample logging configurations,
    and a quick-start toolkit for Fast Connection Failover.

  Javadoc / Documentation / Demo:

    All of the above are also available for download on OTN.


Installation
------------

The Oracle Installer puts the Universal Connection Pool files in the
[ORACLE_HOME]/ucp directory.


Setting Up Your Environment
---------------------------

On Windows platforms:
  - Add [ORACLE_HOME]\ucp\lib\ucp.jar to your CLASSPATH.

On all Unix platforms:
  - Add [ORACLE_HOME]/ucp/lib/ucp.jar to your CLASSPATH.


Important Notes
---------------

 * UCP works with older Oracle databases such as 11.1 and 10g databases,
   as well as non-Oracle databases such as DB2, SQLServer. Please refer
   to OTN for complete UCP certification information, including JDBC driver
   and vendor database versions.

 * UCP's connection affinity features (Web-session based affinity and
   Transaction based affinity) are designed to work with Oracle Real
   Application Clusters (RAC) 11.1 or above, since they are designed to
   leverage features of the 11g RAC database. These UCP features may not
   work as expected with older RAC versions.

 * If you are using UCP's Connection Labeling feature, and enabled Fast
   Connection Failover as well as RAC Load Balancing Advisory, note that
   UCP's Runtime Connection Load-Balancing (RCLB) takes precedence over
   connection labeling. In other words, in a RAC enabled environment,
   UCP's RCLB would provide better performance characteristics than
   generic Connection Labeling feature.


Known Problems/Limitations In This Release
------------------------------------------

 * For applications using XA (global transactions) and Fast Connection
   Failover (FCF), the method isValid() on oracle.ucp.jdbc.ValidConnection
   may not be optimal in performance when returning from the method. This
   could happen when using UCP's PoolXADataSource with FCF enabled.
   This is expected, due to Oracle JDBC bug #7314006.

   Workaround: This bug does not affect the correctness of the isValid API.
     The application could choose to check for XAException.XAER_RMERR
     inside the XAException catch-block instead, to determine a connection's
     validity.

 * For applications using the built-in pool adapter PoolDataSource and
   PoolXADataSource, the pool allows dynamic setting of properties on
   the specified connection factory, such as connect-URL, host, port,
   etc., during run-time. However, other than connect-URL, the pool
   does not refresh itself when setting other connection factory properties.
   This applies to both standard connection factory properties (for
   example, those on javax.sql.DataSource including user, password,
   portNumber, databaseName, etc.), and customized properties specific
   to a connection factory.

   Workaround: Application should try to avoid dynamic setting of
     important connection factory properties, after the pool has been
     populated with connections. If this can not be avoided, application
     can explicitly perform pool refresh after all dynamic property set.


Problems/Limitations Fixed in This Release
------------------------------------------

BUG-8489565
    Inconsistent classloaders used in JDBC proxies.

BUG-8356037
    Calling setInitialPoolSize() no longer starts a pool instance.

BUG-7657258
    Incorrect Fast Connection Failover (FCF) outcome recorded after processing
    service-up event.

BUG-7607299
    Minor typos in UCP Development Guide.

BUG-7504160
    UniversalConnectionPoolException: Universal Connection Pool already
    exists.

BUG-7500649
    Added support for connecting directly to a named RAC instance.

BUG-7499744
    Added FCF up-event processing information via getStatistics API.

BUG-7449959
    When remote ONS setup or instantiation fails, subscription to ONS
    is no longer attempted.

BUG-7448113
    Incorrect number of connections processed while tearing down connections
    during FCF.

BUG-7390833
    UCP connection proxies cannot be removed from HashMap while used as key.

BUG-7390692
    JDBC proxies throw exception for methods called on invalid or closed
    connections.

BUG-7355968
    Incorrect factory class check in pool subclasses for connections

