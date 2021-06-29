package com.xdcplus.netty.common.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
public class RequestFlow implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
    * 任务id
    */
    private Long requestId;

    /**
    * 上一个状态
    */
    private Integer fromStatus;

    /**
    * 下一个状态
    */
    private Integer toStatus;

    /**
    * 操作时间
    */
    private Date createTime;

    private static final long serialVersionUID = 1L;
}