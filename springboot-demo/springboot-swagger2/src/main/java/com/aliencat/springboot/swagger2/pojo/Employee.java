package com.aliencat.springboot.swagger2.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * 员工实体类
 **/
@Data
@ApiModel(value = "员工信息", description = "员工信息")
public class Employee {

    @NotNull(message = "userId is empty")
    @ApiModelProperty(value = "员工ID")
    private Integer id;

    @NotBlank(message = "员工姓名不能为空")
    @Length(min = 1, max = 100, message = "员工姓名长度不能超过50字")
    @ApiModelProperty(value = "员工姓名")
    private String name;

    @Range(min = 1, max = 200, message = "年龄范围[1,200]")
    @ApiModelProperty(value = "年龄")
    private Integer age;

    @Pattern(regexp = "^[0-1]$", message = "员工类型范围[0,1]")
    @ApiModelProperty(value = "员工类型（0行政岗;1基础岗")
    private String type;

    @Pattern(regexp = "^[1][^0^1^2][0-9]{9}$", message = "员工手机号码不合法")
    @ApiModelProperty(value = "员工手机号码")
    private String phone;

    @Email(message = "员工邮箱不合法")
    @Length(min = 1, max = 50, message = "员工邮箱长度")
    @ApiModelProperty(value = "员工邮箱")
    private String email;

    @Length(min = 0, max = 200, message = "备注长度不能超过100字")
    @ApiModelProperty(value = "备注")
    private String remarks;

}
