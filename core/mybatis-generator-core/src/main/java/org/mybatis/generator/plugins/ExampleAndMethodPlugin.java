package org.mybatis.generator.plugins;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.TopLevelClass;

import java.util.List;

/**
 * 给 example 添加 and 方法
 *
 * @author gulou
 * @date 2020/8/22 11:39
 * @description
 */
public class ExampleAndMethodPlugin extends PluginAdapter {
    @Override
    public boolean validate(List<String> warnings) {
        return true;
    }

    @Override
    public boolean modelExampleClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {

        new FullyQualifiedJavaType("com.example.trial.po.ProductExample.Criteria");

        Method exampleAnd = new Method("and");
        exampleAnd.setVisibility(JavaVisibility.PUBLIC);
        exampleAnd.setReturnType(FullyQualifiedJavaType.getCriteriaInstance());

        exampleAnd.addBodyLine("if (oredCriteria.size() == 0) {");
        exampleAnd.addBodyLine("Criteria criteria = createCriteriaInternal();");
        exampleAnd.addBodyLine("oredCriteria.add(criteria);");
        exampleAnd.addBodyLine("return criteria;");
        exampleAnd.addBodyLine("}");
        exampleAnd.addBodyLine("if (oredCriteria.size() > 1) {");
        exampleAnd.addBodyLine("throw new IllegalStateException(\"There is more than 1 criteria,can't use and!\");");
        exampleAnd.addBodyLine("}");
        exampleAnd.addBodyLine("return oredCriteria.get(0);");

        List<Method> methods = topLevelClass.getMethods();
        if (methods == null) {
            return super.modelExampleClassGenerated(topLevelClass, introspectedTable);
        }

        int index = 0;
        for (int i = 0; i < methods.size(); i++) {
            if (methods.get(i).getName().equalsIgnoreCase("or")) {
                index = i;
                break;
            }
        }
        methods.add(index, exampleAnd);

        return super.modelExampleClassGenerated(topLevelClass, introspectedTable);
    }
}
