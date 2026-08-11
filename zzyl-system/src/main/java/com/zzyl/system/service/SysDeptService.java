package com.zzyl.system.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzyl.common.exception.ServiceException;
import com.zzyl.system.domain.SysDept;
import com.zzyl.system.mapper.SysDeptMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 部门 Service
 */
@Service
public class SysDeptService extends ServiceImpl<SysDeptMapper, SysDept> {

    /**
     * 查询全部部门并组装成树
     */
    public List<SysDept> tree() {
        List<SysDept> depts = list();
        List<SysDept> roots = depts.stream()
                .filter(d -> d.getParentId() == null || d.getParentId() == 0)
                .collect(Collectors.toList());
        roots.forEach(root -> root.setChildren(children(root, depts)));
        return roots;
    }

    /**
     * 是否存在子部门
     */
    public boolean hasChildren(Long deptId) {
        return CollUtil.isNotEmpty(lambdaQuery().eq(SysDept::getParentId, deptId).list());
    }

    private List<SysDept> children(SysDept parent, List<SysDept> all) {
        List<SysDept> children = all.stream()
                .filter(d -> parent.getId().equals(d.getParentId()))
                .collect(Collectors.toList());
        children.forEach(child -> child.setChildren(children(child, all)));
        return children;
    }

    /**
     * 列表查询（条件），返回平铺集合
     */
    public List<SysDept> selectDeptList(SysDept query) {
        return lambdaQuery()
                .like(StrUtil.isNotBlank(query.getDeptName()), SysDept::getDeptName, query.getDeptName())
                .eq(query.getStatus() != null, SysDept::getStatus, query.getStatus())
                .orderByAsc(SysDept::getParentId)
                .orderByAsc(SysDept::getSort)
                .list();
    }

    /**
     * 同父节点下部门名称唯一
     */
    public boolean checkDeptNameUnique(SysDept dept) {
        SysDept info = lambdaQuery()
                .eq(SysDept::getDeptName, dept.getDeptName())
                .eq(SysDept::getParentId, dept.getParentId())
                .one();
        return info == null || info.getId().equals(dept.getId());
    }

    @Transactional(rollbackFor = Exception.class)
    public void insertDept(SysDept dept) {
        if (!checkDeptNameUnique(dept)) {
            throw new ServiceException("新增部门'" + dept.getDeptName() + "'失败，同目录下部门名称已存在");
        }
        baseMapper.insert(dept);
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateDept(SysDept dept) {
        if (!checkDeptNameUnique(dept)) {
            throw new ServiceException("修改部门'" + dept.getDeptName() + "'失败，同目录下部门名称已存在");
        }
        baseMapper.updateById(dept);
    }

    /**
     * 删除部门（有子部门不允许删）
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteDeptById(Long deptId) {
        if (hasChildren(deptId)) {
            throw new ServiceException("存在下级部门，不允许删除");
        }
        removeById(deptId);
    }
}
