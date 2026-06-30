package com.test.web.dto.input;

import com.test.web.dto.PageInfoReq;
import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Date;

@Getter
@Setter
public class GetPbocDataListReqDto extends PageInfoReq {

    @NotBlank(message = "名字不能为空")
    private String name;

    @NotNull(message = "年龄不能为空")
    private Integer age;

    private Date day = new Date();

}
