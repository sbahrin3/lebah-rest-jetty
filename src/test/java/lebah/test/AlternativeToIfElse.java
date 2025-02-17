package lebah.test;

import java.util.Map;

public class AlternativeToIfElse {
	
	
	String doA() {
		System.out.println("in do A");;
		return "A";
	}
	
	String doB() {
		System.out.println("in do B");
		return "B";
	}
	
	String doC() {
		System.out.println("in do C");
		return "C";
	}
	
	String doDefault() {
		System.out.println("in do default");
		return "default";
	}
	
	
	@FunctionalInterface
	interface RequestHandler {
		String handle() throws Exception;
	}
	
	String doTest(String action) throws Exception {
		
		Map<String, RequestHandler> actions = Map.of("doA", this::doA, "doB", this::doB, "doC", this::doC);
		return actions.getOrDefault(action, this::doB).handle();
		
		
	}
	
	public static void main(String[] args) throws Exception {
		AlternativeToIfElse test = new AlternativeToIfElse();
		Object result = test.doTest("doA");
		System.out.println(result);
	}

}
