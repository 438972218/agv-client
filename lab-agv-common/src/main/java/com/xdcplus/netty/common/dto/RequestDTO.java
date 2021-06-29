package com.xdcplus.netty.common.dto;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.*;

import java.io.Serializable;

/**
 * @author : Fish Fei
 */
@Builder
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class RequestDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 任务编号
     */
    private Integer taskNum;


    @JSONField(name="Task")
    private TaskDTO Task;
}
