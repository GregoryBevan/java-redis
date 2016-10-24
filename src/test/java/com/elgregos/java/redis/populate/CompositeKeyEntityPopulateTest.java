package com.elgregos.java.redis.populate;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import org.junit.Test;

import com.elgregos.java.redis.entities.key.DoubleKey;

public class CompositeKeyEntityPopulateTest {

	@Test
	public void test_double_keys() {
		final List<DoubleKey> doubleKeys = new ArrayList<>();
		char currentFirstCode = 'A';
		int currentSecondCode = 1;
		for (int i = 1; i <= 100000; i++) {
			doubleKeys.add(new DoubleKey(String.valueOf(currentFirstCode), String.valueOf(currentSecondCode)));
			if (currentSecondCode == 10000) {
				currentFirstCode = (char) (currentFirstCode + 1);
				currentSecondCode = 1;
				continue;
			}
			currentSecondCode += 1;
		}
		assertTrue(new HashSet<>(doubleKeys).size() == 100000);
	}

	@Test
	public void test_random_char() {
		System.out.println("Code A : " + (int) 'A' + " Code Z : " + (int) 'Z');
		final Random randomObj = new Random();
		System.out.println(String.valueOf((char) randomObj.ints('A', 'Z').findFirst().getAsInt()));

	}

}
