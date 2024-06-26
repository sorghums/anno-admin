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
import com.github.drinkjava2.jdialects.Type;
import com.github.drinkjava2.jdialects.annotation.jpa.GenerationType;

import java.sql.Connection;
import java.util.UUID;

/**
 * Generate a JDK 36 letters random UUID generated by
 * UUID.randomUUID().toString(), for example:</br>
 * d3ad36c0-c6c2-495c-a414-b9cc4a0a7a93
 * 
 * @author Yong Zhu
 * @version 1.0.0
 * @since 1.0.0
 */
public class UUID36Generator implements IdGenerator {
	public static final UUID36Generator INSTANCE = new UUID36Generator();

	@Override
	public GenerationType getGenerationType() {
		return GenerationType.UUID36;
	}

	@Override
	public String getIdGenName() {
		return "UUID36";
	}

	@Override
	public Object getNextID(Connection con, Dialect dialect, Type dataType) {
		return UUID.randomUUID().toString();
	}

	@Override
	public Boolean dependOnAutoIdGenerator() {
		return false;
	}

	@Override
	public IdGenerator newCopy() {
		return INSTANCE;
	}
 
}
