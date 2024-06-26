/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at http://www.apache.org/licenses/LICENSE-2.0 Unless required by
 * applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS
 * OF ANY KIND, either express or implied. See the License for the specific
 * language governing permissions and limitations under the License.
 */
package com.github.drinkjava2.jdialects.id;

import com.github.drinkjava2.jdialects.Dialect;
import com.github.drinkjava2.jdialects.StrUtils;
import com.github.drinkjava2.jdialects.Type;
import com.github.drinkjava2.jdialects.annotation.jpa.GenerationType;

import java.math.BigInteger;
import java.sql.Connection;
import java.util.UUID;

/**
 * Compress JDK UUID to 26 letters based on radix 36, use 0-9 a-z characters,
 * example: pbicz3grgu0zk3ipe1yur03h7a
 * 
 * @author Yong Zhu
 * @version 1.0.0
 * @since 1.0.0
 */
public class UUID26Generator implements IdGenerator {
	public static final UUID26Generator INSTANCE = new UUID26Generator();

	@Override
	public GenerationType getGenerationType() {
		return GenerationType.UUID26;
	}

	@Override
	public String getIdGenName() {
		return "UUID26";
	}

	@Override
	public Object getNextID(Connection con, Dialect dialect, Type dataType) {
		return getUUID26();
	}

	@Override
	public Boolean dependOnAutoIdGenerator() {
		return false;
	}

	@Override
	public IdGenerator newCopy() {
		return INSTANCE;
	}

	public static String getUUID26() {
		String uuidHex = UUID.randomUUID().toString().replace("-", "");
		BigInteger b = new BigInteger(uuidHex, 16);
		String s = b.toString(36);
		while (s.length() < 26)
			s = s + StrUtils.getRandomChar(); //NOSONAR
		return s;
	}

}
