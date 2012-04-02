package test.generics;

import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

public class GenericTest {
	
	@Test
	public void testFondCreation(){
		GenericMain<MyClass> myGeneric = new GenericMain<MyClass>();
		//assertTrue(fond != null);
	}
		

}
