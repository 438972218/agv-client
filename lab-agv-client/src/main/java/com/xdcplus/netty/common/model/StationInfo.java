package com.xdcplus.netty.common.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

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
public class StationInfo implements Serializable {

    private static final long serialVersionUID = 1L;

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
     * 站台号
     */
    private Integer stationId;

    /**
     * 站台序号
     */
    private Integer stationIndex;

    private List<StationTaskInfo> stationTaskInfos;

}
