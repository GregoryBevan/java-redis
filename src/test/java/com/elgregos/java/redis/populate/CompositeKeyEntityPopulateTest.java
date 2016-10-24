package com.elgregos.java.redis.populate;

import java.util.Random;

import org.junit.Test;

public class CompositeKeyEntityPopulateTest {

	@Test
	public void test_random_char() {
		System.out.println("Code A : " + (int) 'A' + " Code Z : " + (int) 'Z');
		final Random randomObj = new Random();
		System.out.println(String.valueOf((char) randomObj.ints('A', 'Z').findFirst().getAsInt()));

	}

}
