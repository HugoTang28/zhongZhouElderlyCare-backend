package com.zzyl.admin.biz;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zzyl.common.biz.BizModule;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 收集所有带 {@link BizModule} 注解的 Service，建立 模块key -> 元数据 的映射。
 * 同时作为 SpEL 权限Bean（beanName=bizAuthz），供 {@code BizController} 动态校验权限。
 */
@Component("bizAuthz")
public class BizModuleRegistry implements ApplicationContextAware {

    private final Map<String, ModuleMeta> modules = new ConcurrentHashMap<>();

    private static final class ModuleMeta {
        final String perm;
        final Class<?> entityClass;
        final IService service;

        ModuleMeta(String perm, Class<?> entityClass, IService service) {
            this.perm = perm;
            this.entityClass = entityClass;
            this.service = service;
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext ctx) throws BeansException {
        Map<String, Object> beans = ctx.getBeansWithAnnotation(BizModule.class);
        for (Object bean : beans.values()) {
            Class<?> targetClass = AopUtils.getTargetClass(bean);
            BizModule ann = AnnotationUtils.findAnnotation(targetClass, BizModule.class);
            if (ann == null) {
                continue;
            }
            IService service = (IService) bean;
            modules.put(ann.value(), new ModuleMeta(ann.perm(), service.getEntityClass(), service));
        }
    }

    /** 拼接权限标识：prefix + ":" + action（如 visit:appointment:list） */
    public String perm(String module, String action) {
        ModuleMeta m = modules.get(module);
        if (m == null) {
            return "unknown:" + action;
        }
        return m.perm + ":" + action;
    }

    @SuppressWarnings("rawtypes")
    public IService getService(String module) {
        ModuleMeta m = modules.get(module);
        if (m == null) {
            throw new IllegalArgumentException("未找到业务模块：" + module);
        }
        return m.service;
    }

    public Class<?> getEntityClass(String module) {
        ModuleMeta m = modules.get(module);
        if (m == null) {
            throw new IllegalArgumentException("未找到业务模块：" + module);
        }
        return m.entityClass;
    }

    public boolean contains(String module) {
        return modules.containsKey(module);
    }
}
