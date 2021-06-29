package com.xdcplus.netty.common.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.xdcplus.netty.common.model.StationTaskInfo;
import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 
 * </p>
 *
 * @author fish
 * @since 2021-05-06
 */
@Builder
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class StationInfoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Integer stationAsyn;

    /**
     * 站台序号
     */
    private Integer stationIndex;

    /**
     * 站台号
     */
    private Integer stationId;

    @JSONField(name="stationTaskInfo")
    private List<StationTaskInfoDTO> stationTaskInfo;

}
