package com.xdcplus.netty.common.dto;

import com.alibaba.fastjson.annotation.JSONField;
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
public class TaskDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 小车编号
     */
    private Integer agvId;

    /**
     * 交互类型
     */
    private Integer actionType;

    @JSONField(name="stationInfo")
    private List<StationInfoDTO> stationInfo;

}
