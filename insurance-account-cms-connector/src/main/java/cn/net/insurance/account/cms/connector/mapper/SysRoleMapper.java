package cn.net.insurance.account.cms.connector.mapper;

import cn.net.insurance.account.cms.connector.entity.SysRole;
import cn.net.insurance.account.common.dto.req.RoleQueryPageReqDto;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

import java.util.List;
@Mapper
@Component
public interface SysRoleMapper extends BaseMapper<SysRole> {

    /**
     * 根据角色名称是否存在
     *
     * @param name    角色名称
     * @param noteqId 需要排除的用户id
     * @return long
     */
    long countByName(@Param("noteqId") String noteqId, @Param("name") String name);

    /**
     * 删除角色
     *
     * @param roleId
     * @return
     */
    @Update("update sys_role set deleted=1  where id=#{roleId} and deleted=0")
    Integer deleteRoleById(@Param("roleId") String roleId);

    /**
     * 修改状态
     *
     * @param id
     * @param state
     * @return
     */
    @Update("update sys_role set `state`=#{state} where id=${id} and deleted=0 ")
    Integer modifyState(@Param("id") String id, @Param("state") Integer state);


    IPage<SysRole> listPage(IPage page, @Param("queryReqDto") RoleQueryPageReqDto queryReqDto);

    List<SysRole> listAll();

    List<SysRole> listByAccountId(@Param("accountId") String accountId);
}
