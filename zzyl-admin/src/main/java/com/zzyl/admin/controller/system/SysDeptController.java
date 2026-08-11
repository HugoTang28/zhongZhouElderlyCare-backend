package com.zzyl.admin.controller.system;

import com.zzyl.common.core.R;
import com.zzyl.system.domain.SysDept;
import com.zzyl.system.service.SysDeptService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 部门管理
 */
@Tag(name = "部门管理")
@RestController
@RequestMapping("/system/dept")
@RequiredArgsConstructor
public class SysDeptController {

    private final SysDeptService deptService;

    @Operation(summary = "部门列表（树）")
    @PreAuthorize("hasAuthority('system:dept:list')")
    @GetMapping("/list")
    public R<List<SysDept>> list() {
        return R.ok(deptService.tree());
    }

    @Operation(summary = "部门详情")
    @PreAuthorize("hasAuthority('system:dept:query')")
    @GetMapping("/{id}")
    public R<SysDept> getInfo(@PathVariable Long id) {
        return R.ok(deptService.getById(id));
    }

    @Operation(summary = "新增部门")
    @PreAuthorize("hasAuthority('system:dept:add')")
    @PostMapping
    public R<Void> add(@Validated @RequestBody SysDept dept) {
        deptService.insertDept(dept);
        return R.ok();
    }

    @Operation(summary = "修改部门")
    @PreAuthorize("hasAuthority('system:dept:edit')")
    @PutMapping
    public R<Void> edit(@Validated @RequestBody SysDept dept) {
        deptService.updateDept(dept);
        return R.ok();
    }

    @Operation(summary = "删除部门")
    @PreAuthorize("hasAuthority('system:dept:remove')")
    @DeleteMapping("/{id}")
    public R<Void> remove(@PathVariable Long id) {
        deptService.deleteDeptById(id);
        return R.ok();
    }
}
