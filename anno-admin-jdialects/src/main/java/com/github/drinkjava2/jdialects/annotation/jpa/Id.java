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
package com.github.drinkjava2.jdialects.annotation.jpa;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Specifies the primary key of an entity.
 * The field or property to which the <code>Id</code> annotation is applied 
 * should be one of the following types: any Java primitive type; 
 * any primitive wrapper type; 
 * <code>String</code>; 
 * <code>java.util.Date</code>; 
 * <code>java.sql.Date</code>; 
 * <code>java.math.BigDecimal</code>;
 * <code>java.math.BigInteger</code>.
 *
 * <p>The mapped column for the primary key of the entity is assumed 
 * to be the primary key of the primary table. If no <code>Column</code> annotation 
 * is specified, the primary key column name is assumed to be the name 
 * of the primary key property or field.
 *
 * <pre>
 *   Example:
 *
 *   &#064;Id
 *   public Long getId() { return id; }
 * </pre>
 *
 * @see Column
 * @see GeneratedValue
 *
 * @since Java Persistence 1.0
 */
@Target({METHOD, FIELD})
@Retention(RUNTIME)

public @interface Id {}
