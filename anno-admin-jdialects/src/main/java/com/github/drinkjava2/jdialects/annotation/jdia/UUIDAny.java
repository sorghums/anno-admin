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
package com.github.drinkjava2.jdialects.annotation.jdia;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.github.drinkjava2.jdialects.annotation.jpa.GeneratedValue;

/**
 * Defines a primary key generator that may be referenced by name when
 * a generator element is specified for the {@link GeneratedValue}
 * annotation. A UUIDAny generator may be specified on the entity
 * class or on the primary key field or property. The scope of the
 * generator name is global to the persistence unit (across all
 * generator types).
 *
 * <pre>
 *   Example:
 *
 *   &#064;UUIDAny(name="uuid_100", length=100)
 * </pre>
 *
 * @since Java Persistence 1.0
 */
@Target({TYPE, FIELD}) 
@Retention(RUNTIME)
public @interface UUIDAny {
	/**
	 * (Required) A unique generator name that can be referenced by one or more
	 * classes to be the generator for primary key values.
	 */
	String name();

	/**
	 * The length of the UUID
	 */
	int length() default 20;
}
