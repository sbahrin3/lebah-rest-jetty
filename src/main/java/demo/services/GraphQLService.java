package demo.services;

import java.io.InputStreamReader;
import java.util.List;

import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;

/**
 * 
 * @author shamsulbahrin
 * @since 11 Apr 2025
 */

public class GraphQLService {
	
	private final GraphQL graphQL;

    public GraphQLService(List<GraphQLDataFetcherProvider> dataFetcherProviders) {
        InputStreamReader reader = new InputStreamReader(
            getClass().getResourceAsStream("/schema.graphqls")
        );

        TypeDefinitionRegistry typeRegistry = new SchemaParser().parse(reader);
        RuntimeWiring.Builder wiringBuilder = RuntimeWiring.newRuntimeWiring();
        
        for (GraphQLDataFetcherProvider provider : dataFetcherProviders) {
            provider.register(wiringBuilder);
        }
		
        RuntimeWiring wiring = wiringBuilder.build();
        GraphQLSchema schema = new SchemaGenerator().makeExecutableSchema(typeRegistry, wiring);
        this.graphQL = GraphQL.newGraphQL(schema).build();
    }

    public GraphQL getGraphQL() {
        return graphQL;
    }
    

}
