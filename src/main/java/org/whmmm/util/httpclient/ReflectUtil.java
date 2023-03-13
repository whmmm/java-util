package org.whmmm.util.httpclient;


import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

/**
 * <p><b> ----------------------- </b></p>
 * <p><b> author: whmmm           </b></p>
 * <p><b> date  : 2023/3/10 9:20 </b></p>
 *
 * @author whmmm
 */
public final class ReflectUtil {
    private ReflectUtil() {
    }

    /**
     * 获取全部 fields, 子类如果有同名 field 则排除父类
     * <p> author: whmmm </p>
     * <p> date  : 2023-03-10 09:33 </p>
     *
     * @param type -
     * @return -
     */
    public static List<Field> getAllFields(Class<?> type) {

        return getAllFields(new ArrayList<>(),
                            type,
                            new HashSet<>()
        );
    }

    /**
     * <p> author: whmmm </p>
     * <p> date  : 2023-03-10 09:35 </p>
     *
     * @param fields      存储属性字段的容器
     * @param type        {@link Class}
     * @param existFields 用来排除重名字段用的, null 时查询全部字段
     * @return -
     */
    public static List<Field> getAllFields(List<Field> fields, Class<?> type, @Nullable Set<String> existFields) {
        List<Field> list = new ArrayList<>();
        List<Field> fieldList = Arrays.asList(type.getDeclaredFields());
        if (existFields == null) {
            list = fieldList;
        } else {
            for (Field field : fieldList) {
                String fieldName = field.getName();
                if (existFields.contains(fieldName)) {
                    continue;
                } else {
                    existFields.add(fieldName);
                    list.add(field);
                }
            }
        }

        fields.addAll(list);

        if (type.getSuperclass() != null) {
            getAllFields(fields, type.getSuperclass(), existFields);
        }

        return fields;
    }

    /**
     * 获取第一个泛型的类型
     * <pre>{@code
     * example:
     * Future<String>       =>  string
     * ResponseEntity<Map>  =>  Map
     * }</pre>
     *
     * @param paramType -
     * @return -
     */
    @javax.annotation.Nullable
    public static Type getFirstGenericType(Type paramType) {
        ParameterizedType type = (ParameterizedType) paramType;
        Type[] arguments = type.getActualTypeArguments();
        if (arguments != null && arguments.length > 0) {
            return arguments[0];
        }
        return null;
    }

    /**
     * 判断是否是子泛型
     * <pre>{@code
     * example:
     *
     * isGenericType(ResponseEntity.class, TypeRef<ResponseEntity<String>>  )  => true
     *
     * }</pre>
     *
     * @param destType  要判断的类型
     * @param paramType 参数
     * @return -
     */
    public static boolean isGenericType(Type destType, Type paramType) {
        return paramType.toString().startsWith(destType.getTypeName());
    }
}
