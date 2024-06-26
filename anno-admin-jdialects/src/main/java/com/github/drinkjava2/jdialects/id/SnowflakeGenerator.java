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
import com.github.drinkjava2.jdialects.DialectException;
import com.github.drinkjava2.jdialects.Type;
import com.github.drinkjava2.jdialects.annotation.jpa.GenerationType;

import java.sql.Connection;

/**
 * SnowflakeGenerator is a special generator, only mark this column is a
 * snowflake type column, but getNextID() method does not work, because
 * snowflake value should generated by outside program, it depends on real
 * machine setting, in jDialects there is a SnowflakeCreator tool for these
 * outside program to use
 */
@SuppressWarnings("all")
public class SnowflakeGenerator implements IdGenerator {
	public final static SnowflakeGenerator INSTANCE = new SnowflakeGenerator();

	@Override
	public Object getNextID(Connection con, Dialect dialect, Type dataType) {
		throw new DialectException("Snowflake type column value should generated by outside program.");
	}

	@Override
	public GenerationType getGenerationType() {
		return GenerationType.SNOWFLAKE;
	}

	@Override
	public String getIdGenName() {
		return "SNOWFLAKE";
	}

	@Override
	public IdGenerator newCopy() {
		return INSTANCE;
	}

	@Override
	public Boolean dependOnAutoIdGenerator() {
		return false;
	}

}