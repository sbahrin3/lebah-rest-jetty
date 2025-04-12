package demo.services;

import graphql.schema.idl.RuntimeWiring;

public interface GraphQLDataFetcherProvider {
	
	void register(RuntimeWiring.Builder wiringBuilder);

}
