package org.mybatis.generator.plugins;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.TopLevelClass;

import java.util.List;
import java.util.Set;

/**
 * @author gulou
 * @date 2020/8/18 21:54
 * @description
 */
public class LombokPlugin extends PluginAdapter {
    @Override
    public boolean validate(List<String> warnings) {
        return true;
    }

    @Override
    public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        Set<FullyQualifiedJavaType> importedTypes = topLevelClass.getImportedTypes();
        if (importedTypes == null) {
            return true;
        }

        FullyQualifiedJavaType lombokAnnotation = new FullyQualifiedJavaType("lombok.Data");
        importedTypes.add(lombokAnnotation);

        topLevelClass.getMethods().clear();
        topLevelClass.getAnnotations().add("@Data");
        return true;
    }
}
