The project is a simple wallet transaction project.
It contains endpoints to create wallet, view wallet and perform transactions
like credit, debit and transfer.
to run the application add mysql configuration to the application.properties file
spring.application.name=wallet
# Database connection
spring.datasource.url=jdbc:mysql://localhost:3306/fintech
spring.datasource.username
spring.datasource.password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver


spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect

