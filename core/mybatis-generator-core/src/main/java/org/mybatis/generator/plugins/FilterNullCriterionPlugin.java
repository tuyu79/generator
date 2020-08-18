/**
 * Copyright 2006-2019 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.mybatis.generator.plugins;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * This plugin demonstrates adding methods to the example class to enable
 * case-insensitive LIKE searches. It shows hows to construct new methods and
 * add them to an existing class.
 *
 * <p>This plugin only adds methods for String fields mapped to a JDBC character
 * type (CHAR, VARCHAR, etc.)
 *
 * @author Jeff Butler
 */
public class FilterNullCriterionPlugin extends PluginAdapter {

    public FilterNullCriterionPlugin() {
        super();
    }

    @Override
    public boolean validate(List<String> warnings) {
        return true;
    }

    @Override
    public boolean modelExampleClassGenerated(TopLevelClass topLevelClass,
                                              IntrospectedTable introspectedTable) {

        topLevelClass.getInnerClasses().stream()
                .filter(this::isGeneratedCriteria)
                .findFirst()
                .ifPresent(this::modifyAddCriterion);

        return true;
    }

    private boolean isGeneratedCriteria(InnerClass innerClass) {
        return "GeneratedCriteria".equals(innerClass.getType().getShortName()); //$NON-NLS-1$
    }

    private void modifyAddCriterion(InnerClass criteria) {

        List<Method> methods = criteria.getMethods().stream()
                .filter(this::isAddCriterion)
                .collect(Collectors.toList());

        if (methods.size() != 0) {
            methods.forEach(method -> {
                List<String> bodyLines = method.getBodyLines();
                bodyLines.remove(1);
                bodyLines.add(1, "return;");
            });
        }
    }

    private boolean isAddCriterion(Method item) {
        return item.getName().equalsIgnoreCase("addCriterion");
    }
}
