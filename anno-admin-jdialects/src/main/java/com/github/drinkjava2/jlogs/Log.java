/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.github.drinkjava2.jlogs;

/**
 * JLog used for inside of jSqlBox project, if a "jlogs.properties" file if
 * found on class root folder (main/resources), will try load the designated
 * JLog implementation, otherwise use default ConsoleLog as logger<br/>
 * 
 * An example of "jlogs.properties": <br/>
 * log=com.github.drinkjava2.jlogs.SimpleSLF4JLog
 * 
 * Another full example of using SLF4j+LogBack in production environment please
 * see jsqlbox-jbooox demo project
 * 
 * @author Yong Zhu
 * @since 2.0.5
 */
public interface Log {

	public void info(String msg);

	public void warn(String msg);

	public void warn(String msg, Throwable t);

	public void error(String msg);

	public void error(String msg, Throwable t);

	public void debug(String msg);

}
