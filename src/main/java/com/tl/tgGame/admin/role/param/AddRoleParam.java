package com.tl.tgGame.admin.role.param;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddRoleParam {
    @NotEmpty
    private String roleName;
    private String remark;
    private List<String> codes;
}
