package com.xdcplus.netty.common.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author fish
 * @since 2021-05-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class StationTaskInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long stationInfoId;

    private Integer actionIndex;

    /**
     * 指定轴
     */
    private Integer actionAxis;

    /**
     * 作业类型
     */
    private Integer taskType;

    private Integer ifType;


}
