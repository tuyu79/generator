package org.mybatis.generator.internal.types;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.JavaTypeResolver;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.config.Context;

import java.sql.Types;
import java.util.List;
import java.util.Properties;

/**
 * @author gulou
 * @date 2020/8/18 22:19
 * @description
 */
public class MyJavaTypeResolver extends JavaTypeResolverDefaultImpl {
    @Override
    protected FullyQualifiedJavaType overrideDefaultType(IntrospectedColumn column, FullyQualifiedJavaType defaultType) {
        if (column.getJdbcType() == Types.TINYINT && column.getLength() != 1) {
            return new FullyQualifiedJavaType(Integer.class.getName());
        }
        return super.overrideDefaultType(column, defaultType);
    }
}
