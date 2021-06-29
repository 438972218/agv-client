package com.xdcplus.netty.common.dto;

import lombok.*;

import java.io.Serializable;

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
public class StationTaskInfoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

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
