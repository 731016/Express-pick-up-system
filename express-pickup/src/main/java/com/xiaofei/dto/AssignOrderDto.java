package com.xiaofei.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author tuaofei
 * @description TODO
 * @date 2024/8/1
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssignOrderDto {
    private String deliveryId;
    private List<String> orderIds;
}
