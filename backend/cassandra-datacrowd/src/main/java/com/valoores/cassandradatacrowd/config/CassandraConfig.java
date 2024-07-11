//package com.valoores.cassandradatacrowd.config;
//
//import java.time.Duration;
//import java.util.Arrays;
//import java.util.List;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.cassandra.config.AbstractCassandraConfiguration;
//import org.springframework.data.cassandra.config.SchemaAction;
//import org.springframework.data.cassandra.config.SessionBuilderConfigurer;
//import org.springframework.data.cassandra.core.cql.keyspace.CreateKeyspaceSpecification;
//import org.springframework.data.cassandra.core.cql.keyspace.DropKeyspaceSpecification;
//import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;
//
//import com.datastax.oss.driver.api.core.CqlSessionBuilder;
//import com.datastax.oss.driver.api.core.config.DefaultDriverOption;
//import com.datastax.oss.driver.api.core.config.DriverConfigLoader;
//
//@Configuration
////@EnableCassandraRepositories(basePackages = { "com.valoores.cassandradatacrowd.repository" })
//public class CassandraConfig extends AbstractCassandraConfiguration {
//
//	public static final String KEYSPACE = "datacrowd";
////	@Override
////	protected SessionBuilderConfigurer getSessionBuilderConfigurer() {
////		return new SessionBuilderConfigurer() {
////			@Override
////			public CqlSessionBuilder configure(CqlSessionBuilder cqlSessionBuilder) {
////				return cqlSessionBuilder.withConfigLoader(DriverConfigLoader.programmaticBuilder()
////						.withDuration(DefaultDriverOption.REQUEST_TIMEOUT, Duration.ofMillis(15000)).build());
////			}
////		};
////	}
//
//	 @Override
//	    public SchemaAction getSchemaAction() {
//	        return SchemaAction.NONE;
//	    }
//
//	    @Override
//	    protected List<CreateKeyspaceSpecification> getKeyspaceCreations() {
//	        CreateKeyspaceSpecification specification = CreateKeyspaceSpecification.createKeyspace(KEYSPACE);
//
//	        return Arrays.asList(specification);
//	    }
//
//	    @Override
//	    protected List<DropKeyspaceSpecification> getKeyspaceDrops() {
//	        return Arrays.asList(DropKeyspaceSpecification.dropKeyspace(KEYSPACE));
//	    }
//
//	    @Override
//	    protected String getKeyspaceName() {
//	        return KEYSPACE;
//	    }
//
//	    @Override
//	    public String[] getEntityBasePackages() {
//	        return new String[]{"com.valoores.cassadradatacrowd.model"};
//	    }
//}