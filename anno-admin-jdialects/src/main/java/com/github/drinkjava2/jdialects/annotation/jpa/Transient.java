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
 * Specifies that the property or field is not persistent. It is used
 * to annotate a property or field of an entity class, mapped
 * superclass, or embeddable class.
 *
 * <pre>
 *    Example:
 *
 *    &#064;Entity
 *    public class Employee {
 *        &#064;Id int id;
 *        &#064;Transient User currentUser;
 *        ...
 *    }
 * </pre>
 *
 * @since Java Persistence 1.0
 */
@Target({METHOD, FIELD})
@Retention(RUNTIME)

public @interface Transient {}
