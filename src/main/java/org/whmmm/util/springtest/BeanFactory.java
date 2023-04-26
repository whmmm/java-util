package org.whmmm.util.springtest;


import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.whmmm.util.httpclient.ReflectUtil;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p><b> ----------------------- </b></p>
 * <p><b> author: whmmm           </b></p>
 * <p><b> date  : 2023/4/25 17:05 </b></p>
 *
 * @author whmmm
 */
@RequiredArgsConstructor
class BeanFactory {

    private final BeanContext context;

    @SuppressWarnings("rawtypes")
    protected final Map<Class, Object> cache = new ConcurrentHashMap<>();

    protected final List<MethodInvocationAspect> methodInvocationAspectList = new ArrayList<>();


    @SuppressWarnings({"unchecked"})
    @SneakyThrows
    public <T> T getBean(Class<T> interfaceType, Class<? extends T> implType) {
        Object o = cache.get(interfaceType);
        if (o != null) {
            return (T) o;
        }

        T newInstance = implType.getDeclaredConstructor().newInstance();

        this.injectValue(context, newInstance);

        InvocationHandler handler = new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

                // 前置操作
                for (MethodInvocationAspect aspect : methodInvocationAspectList) {
                    aspect.before(method, args);
                }
                Object invokeObject = method.invoke(newInstance, args);
                // 后置操作
                for (MethodInvocationAspect aspect : methodInvocationAspectList) {
                    invokeObject = aspect.after(invokeObject, method, args);
                }

                return invokeObject;
            }
        };

        Object instance = Proxy.newProxyInstance(this.getClass().getClassLoader(),
                                                 new Class[]{interfaceType},
                                                 handler
        );

        cache.put(interfaceType, instance);

        return (T) instance;
    }

    /**
     * 仅作简单处理.
     *
     * @param context -
     * @param target  -
     * @param <T>     -
     */
    private <T> void injectValue(BeanContext context, T target) throws Exception {
        List<Field> fields = ReflectUtil.getAllFields(target.getClass());
        for (Field field : fields) {
            field.setAccessible(true);

            String name = field.getName();

            BeanValue value = context.findByName(name);
            if (value != null && value.getType().equals(field.getType())) {
                field.set(target, value.getValue());
            }
        }
    }

}
