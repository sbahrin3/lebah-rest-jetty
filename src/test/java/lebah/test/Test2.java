package lebah.test;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import lebah.db.entity.Persistence;

public class Test2 {
	
	public static void main(String...strings) throws Exception {
		
		String query = "select b from Borrower b";
		
		Persistence db = Persistence.db();
		CompletableFuture<List<?>> listFuture = CompletableFuture.supplyAsync(() -> {
		    return db.list(query);
		});
		CompletableFuture<Long> countFuture = CompletableFuture.supplyAsync(() -> {
		    return db.getTotalRecords(query);
		});
		CompletableFuture<List<?>> combinedFuture = listFuture.thenCombine(countFuture, (list, totalRecords) -> {
		    System.out.println("List of records: " + list);
		    System.out.println("Total records: " + totalRecords);
		    return list;
		});
		
		List<?> list = combinedFuture.join();
		
		//System.out.println(list.size());
	}

}
