package com.tl.tgGame.admin.role.param;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateRoleParam {
    @NonNull
    private Long id;
    @NotEmpty
    private String roleName;
    private String remark;
    private List<String> codes;
}
