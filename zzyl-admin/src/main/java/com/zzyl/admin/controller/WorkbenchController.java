package com.zzyl.admin.controller;

import com.zzyl.common.core.R;
import com.zzyl.system.service.WorkbenchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 工作台（数据看板）统计接口
 */
@RestController
@RequestMapping("/workbench")
public class WorkbenchController {

    @Autowired
    private WorkbenchService workbenchService;

    @GetMapping("/stats")
    public R<Map<String, Object>> stats() {
        return R.ok(workbenchService.stats());
    }
}
