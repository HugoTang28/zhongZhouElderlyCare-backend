package com.zzyl.admin.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zzyl.admin.biz.BizModuleRegistry;
import com.zzyl.admin.biz.BizValidator;
import com.zzyl.common.core.R;
import com.zzyl.common.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

/**
 * 通用业务 CRUD 控制器：通过模块 key 路由到对应 {@code BizModule} Service。
 * 权限标识由 {@link BizModuleRegistry#perm(String, String)} 按模块前缀动态拼接。
 */
@RestController
@RequestMapping("/biz")
public class BizController {

    @Autowired
    private BizModuleRegistry registry;
    @Autowired
    private ObjectMapper objectMapper;

    /** 列表（分页）+ 条件查询：非 null 字段作为查询条件（字符串走 like，其余走 =） */
    @PreAuthorize("hasAuthority(@bizAuthz.perm(#module,'list'))")
    @GetMapping("/{module}/list")
    public R<IPage<?>> list(@PathVariable String module,
                            @RequestParam(defaultValue = "1") long pageNum,
                            @RequestParam(defaultValue = "10") long pageSize,
                            @RequestParam Map<String, Object> params) {
        params.remove("pageNum");
        params.remove("pageSize");
        Object entity = objectMapper.convertValue(params, registry.getEntityClass(module));
        QueryWrapper qw = buildWrapper(entity);
        Page page = new Page(pageNum, pageSize);
        return R.ok(registry.getService(module).page(page, qw));
    }

    /** 详情 */
    @PreAuthorize("hasAuthority(@bizAuthz.perm(#module,'query'))")
    @GetMapping("/{module}/{id}")
    public R<?> get(@PathVariable String module, @PathVariable Long id) {
        return R.ok(registry.getService(module).getById(id));
    }

    /** 新增 */
    @PreAuthorize("hasAuthority(@bizAuthz.perm(#module,'add'))")
    @PostMapping("/{module}")
    public R<Void> add(@PathVariable String module, @RequestBody Map<String, Object> body) {
        Object entity = objectMapper.convertValue(body, registry.getEntityClass(module));
        IServiceValidate(module, entity, true);
        registry.getService(module).save(entity);
        return R.ok();
    }

    /** 修改 */
    @PreAuthorize("hasAuthority(@bizAuthz.perm(#module,'edit'))")
    @PutMapping("/{module}")
    public R<Void> edit(@PathVariable String module, @RequestBody Map<String, Object> body) {
        Object entity = objectMapper.convertValue(body, registry.getEntityClass(module));
        IServiceValidate(module, entity, false);
        registry.getService(module).updateById(entity);
        return R.ok();
    }

    /** 删除 */
    @PreAuthorize("hasAuthority(@bizAuthz.perm(#module,'remove'))")
    @DeleteMapping("/{module}/{id}")
    public R<Void> remove(@PathVariable String module, @PathVariable Long id) {
        registry.getService(module).removeById(id);
        return R.ok();
    }

    private void IServiceValidate(String module, Object entity, boolean isAdd) {
        Object svc = registry.getService(module);
        if (svc instanceof BizValidator) {
            if (isAdd) {
                ((BizValidator) svc).validateBeforeSave(entity);
            } else {
                ((BizValidator) svc).validateBeforeUpdate(entity);
            }
        }
    }

    /** 根据实体非 null 字段构建查询条件 */
    @SuppressWarnings("rawtypes")
    private QueryWrapper buildWrapper(Object entity) {
        TableInfo ti = TableInfoHelper.getTableInfo(entity.getClass());
        QueryWrapper qw = Wrappers.query();
        try {
            for (com.baomidou.mybatisplus.core.metadata.TableFieldInfo fi : ti.getFieldList()) {
                Field f = fi.getField();
                f.setAccessible(true);
                Object v = f.get(entity);
                if (v == null) {
                    continue;
                }
                if (v instanceof String) {
                    String s = (String) v;
                    if (!s.isEmpty()) {
                        qw.like(fi.getColumn(), s);
                    }
                } else {
                    qw.eq(fi.getColumn(), v);
                }
            }
        } catch (IllegalAccessException e) {
            throw new ServiceException("查询条件构建失败");
        }
        return qw;
    }
}
