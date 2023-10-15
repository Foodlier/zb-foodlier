package com.zerobase.foodlier.common.jpa.dialect;

import org.hibernate.dialect.MySQL57Dialect;
import org.hibernate.dialect.function.StandardSQLFunction;
import org.hibernate.type.StandardBasicTypes;

public class MySQLCustomDialect extends MySQL57Dialect {
    public MySQLCustomDialect(){
        super();
        this.registerFunction("ST_Distance_Sphere",
                new StandardSQLFunction("ST_Distance_Sphere", StandardBasicTypes.DOUBLE));
    }
}
