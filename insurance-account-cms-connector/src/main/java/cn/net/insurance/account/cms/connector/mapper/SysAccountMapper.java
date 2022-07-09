package cn.net.insurance.account.cms.connector.mapper;

import cn.net.insurance.account.cms.connector.entity.SysAccount;
import cn.net.insurance.account.common.dto.req.AccountQueryPageReqDto;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface SysAccountMapper extends BaseMapper<SysAccount> {

    /**
     * 根据手机号查询账号是否存在
     *
     * @param phoneNumber 用户手机号
     * @param noteqId     需要排除的用户id
     * @return long
     */
    long countByPhoneNumber(@Param("noteqId") String noteqId, @Param("phoneNumber") String phoneNumber);

    /**
     * 根据账号面查询账号是否存在
     *
     * @param username 账号
     * @param noteqId  需要排除的用户id
     * @return long
     */
    long countByUsername(@Param("noteqId") String noteqId, @Param("username") String username);

    /**
     * 根据账号面查询账号是否存在
     *
     * @param username 账号
     * @return SysAccount
     */
    SysAccount queryByUsername(@Param("username") String username);

    @Update("update sys_account set deleted=1 where id=#{accountId} and deleted=0")
    Integer deleteAccountById(@Param("accountId") String accountId);

    /**
     * 修改状态
     *
     * @param id
     * @param state
     * @return
     */
    @Update("update sys_account set `state`=#{state} where id=${id} and deleted=0 ")
    Integer modifyState(@Param("id") String id, @Param("state") Integer state);


    IPage<SysAccount> listPage(IPage page,@Param("queryReqDto") AccountQueryPageReqDto queryReqDto);

    long updatePassword(@Param("param") SysAccount sysAccount);
}
