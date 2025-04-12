package demo.graphql;

import demo.services.GraphQLDataFetcherProvider;
import graphql.schema.idl.RuntimeWiring.Builder;
import lebah.rest.servlets.GraphQL;

@GraphQL
public class HelloDataFetcherProvider implements GraphQLDataFetcherProvider {

	@Override
	public void register(Builder wiringBuilder) {
		wiringBuilder.type("Query", builder -> builder
				.dataFetcher("hello", env -> "Hello from Lebah-Jetty GraphQL!")
				);		
	}

}
