package com.tl.tgGame.admin.role.param;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SetActiveRoleParam {
    @NotNull
    private Long id;
    @NotNull
    private Boolean active;
}
