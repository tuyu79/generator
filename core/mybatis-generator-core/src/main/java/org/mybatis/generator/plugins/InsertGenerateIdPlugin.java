package org.mybatis.generator.plugins;

import org.mybatis.generator.api.GeneratedXmlFile;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.VisitableElement;
import org.mybatis.generator.api.dom.xml.XmlElement;

import java.lang.reflect.Field;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 给 example 添加 and 方法
 *
 * @author gulou
 * @date 2020/8/22 11:39
 * @description
 */
public class InsertGenerateIdPlugin extends PluginAdapter {
    @Override
    public boolean validate(List<String> warnings) {
        return true;
    }

    @Override
    public boolean sqlMapGenerated(GeneratedXmlFile sqlMap, IntrospectedTable introspectedTable) {
        try {
            Field documentField = sqlMap.getClass().getDeclaredField("document");
            documentField.setAccessible(true);
            Document document = (Document) documentField.get(sqlMap);
            List<VisitableElement> elements = document.getRootElement().getElements();
            if (elements == null) {
                return super.sqlMapGenerated(sqlMap, introspectedTable);
            }

            List<XmlElement> insertElements = elements.stream().filter(isInsertElement())
                    .map(item -> (XmlElement) item)
                    .collect(Collectors.toList());

            insertElements.forEach(item -> {
                item.addAttribute(new Attribute("useGeneratedKeys", "true"));
                item.addAttribute(new Attribute("keyProperty", "id"));
            });
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return super.sqlMapGenerated(sqlMap, introspectedTable);
    }

    private Predicate<VisitableElement> isInsertElement() {
        return item -> item instanceof XmlElement && ((XmlElement) item).getName().startsWith("insert");
    }
}
