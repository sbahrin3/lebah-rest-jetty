package demo.graphql;

import java.util.List;

import demo.data.PageAttr;
import demo.entity.Role;
import demo.entity.User;
import demo.services.GraphQLDataFetcherProvider;
import demo.services.ServiceUtil;
import graphql.schema.DataFetcher;
import graphql.schema.idl.RuntimeWiring.Builder;
import lebah.db.entity.Persistence;
import lebah.rest.servlets.GraphQL;

@GraphQL
public class UserDataFetcherProvider implements GraphQLDataFetcherProvider {

	@Override
	public void register(Builder wiringBuilder) {

		wiringBuilder
			.type("Query", builder -> builder
				.dataFetcher("users", getUsersFetcher())
				.dataFetcher("user", getUserByIdFetcher())
				.dataFetcher("roles", getRolesFetcher())
				)
		    .type("User", builder -> builder
	            .dataFetcher("roles", getRolesByUserFetcher())
		    	)
		    ;

	}

	private DataFetcher<PagedUsers> getUsersFetcher() {
		return env -> {
			int pageNumber = env.getArgumentOrDefault("pageNumber", 1);
			int pageSize = env.getArgumentOrDefault("pageSize", 10);
			PageAttr page = new PageAttr();
			String query = "pageNumber=" + pageNumber + "&pageSize=" + pageSize + "&orderBy=fullName";
			List<User> users = ServiceUtil.listByQueryParams(User.class, query, page);
			long total = page.getTotal();
			long totalPages = page.getTotalPages();

			return new PagedUsers(total, pageNumber, pageSize, totalPages, users);
		};
	}

	private DataFetcher<User> getUserByIdFetcher() {
		return env -> {
			String id = env.getArgument("id");
			return Persistence.db().get("select u from User u where id = ?", id);
		};
	}

	private DataFetcher<List<Role>> getRolesFetcher() {

		return env -> {
			Persistence db = Persistence.db();
			List<Role> roles = db.list("select r from Role r");
			return roles;
		};
	}

	
	private DataFetcher<List<Role>> getRolesByUserFetcher() {

		return env -> {
			User user = env.getSource();
			List<Role> roles = user.getRoles();
			return roles;
		};
	}
	


}
