package com.zzyl.system.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzyl.common.biz.BizModule;
import com.zzyl.system.domain.Post;
import com.zzyl.system.mapper.PostMapper;
import org.springframework.stereotype.Service;

@BizModule(value = "post", perm = "system:post")
@Service
public class PostService extends ServiceImpl<PostMapper, Post> {
}
