
Oracle JVM 11.2 Readme
======================


What's New in 11g (11.1 and 11.2) Oracle JVM?
---------------------------------------------

- Added support for Internet Protocol Version 6 (IPv6) addresses in
  the URL and machine names.  (New in 11.2.  Not available in 11.1.)

- The system classes in Oracle JVM were upgraded from JDK 1.4.2 level
  to JDK 1.5.

- Introduced an all new Just-In-Time compiler (JIT) that enabled
  significant improvement over the old JAccelerator (ncomp) in terms
  of performance and ease of use.  The old JAccelerator was deprecated
  since 11.1.

- Revised Java2 permissions system to provide better security and
  performance.

- Java Management Extensions (JMX) support, which allows you to
  monitor and control the Oracle JVM using a standard JMX enabled
  console such as JConsole.

- Added support for Two-Tier Duration for Java Session.

- Added support for Database Resident JARs, which allows you to create
  database objects representing JARs you loaded into the database.

- Allow you to share private class metadata using the new User
  Classloaded Metadata feature.

- Added support to allow output redirection.

- Added a set of functions in the PL/SQL DBMS_JAVA package to set,
  retrieve, remove and display system properties.

- New runjava function (that can be invoked via the PL/SQL DBMS_JAVA
  package, or the ojvmjava tool) to provide a command line interface
  that is similar to JDK/JRE Java shell command.

- Better utilities
  * New ojvmtc tool to help you to resolve external references.
  * The loadjava tool was re-written to allow the use of URLs to load
    classes and jars from a remote server.
  * The dropjava tool was enhanced to support list-based operations.


Java Compatibility
------------------

This release has been thoroughly tested with Sun's Java Compatibility
Kit for the JDK 1.5.  Oracle is committed to Oracle JVM keeping pace
with Java and other Internet standards.


Getting Started
---------------

The Oracle 11g Java Developer's Guide (the documentation of the
Oracle JVM product) is available on line.

If you are not familiar with the Oracle JVM, it is important to first
follow the instructions in the documentation to install and configure
the product properly.  It is recommended that you follow the examples
presented in the documentation to begin.  The demonstrations and
examples in ORACLE_HOME/javavm/demo include a HelloWorld example.
Execution of the HelloWorld example and others will ensure that your
installation is complete and that OracleJVM is properly enabled in
your database.

Memory resources being used for Java application development and
deployment can vary widely.  To ensure proper development-time and
run-time operations, it is a must to understand how to configure the
memory parameters to satisfy your needs.  The product documentation
has an extensive discussion on Java Memory Usage.


Useful Tips
-----------

- Troubleshooting Tips

  * Errors are often reported in a trace file.  Please examine the
    trace file as your first step in isolating problems.

- Java Stored Procedure

  * Mismatches between call-spec definition and the actual Java
    method are only detected at runtime.

  * DESCRIBE of package.procedure gives "object does not exist" when
    the method has a Java rather than PL/SQL implementation.
    DESCRIBE of package lists only PL/SQL procedures and excludes
    Java Stored Procedures.

- Loadjava and Dropjava

  * The recommended workflow is to first compile Java source with
    a client-side Java compiler, and then load the resulting class
    binaries into the database.  This workflow will save overall
    compiling and loading time.

  * It is not recommended to load source (.java) and binaries (.class)
    together in a jar file.  You cannot load source and binary files
    for the same class in the same jar file.
 
  * You must drop Java schema objects in the same way that you
    first loaded them.  For example, individual classes can not be
    dropped from a JAR object.  The entire jar must be dropped with
    the -jarsasresource command option.

  * If you experience problem loading large jar files, you should
    consider increasing the value of SHARED_POOL_SIZE.  The creation
    of java schema objects consumes shared pool memory.

  * If you suspect classes are not properly loaded and resolved, you
    can examine the state of loaded classes by connecting as the user
    who loaded the classes and executing:

      SELECT * FROM user_objects;
      SELECT * FROM user_errors;

    Properly loaded and resolved classes should show up with status
    of VALID in the user_objects view.

- runjava

  * runjava is designed to be used in application development and
    prototyping workflows.  It is not efficient to be used for
    deployment.

  * Client side JDBC usage will not in general work transparently
    under runjava, particularly the JDBC-OCI driver which isn't
    supported in the server. 


Known Problems
--------------


