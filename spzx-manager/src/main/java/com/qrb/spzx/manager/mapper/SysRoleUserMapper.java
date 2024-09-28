package com.qrb.spzx.manager.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SysRoleUserMapper {
    //1.根据userId删除用户之前已经分配过的角色
    void deleteByUserId(Long userId);

    //2.保存新的分配角色数据
    void doAssgin(Long userId, Long roleId);

    //2.根据用户id显示已经分配过的角色
    List<Long> selectRoleIdsByUserId(Long userId);
}
