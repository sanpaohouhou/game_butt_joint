package com.tl.tgGame.project.enums;

public enum WithdrawStatus {
    created, // 新创建/待审核
    review_fail, // 审核失败
    review_success, // 审核成功
    withdrawing, // 提现中
    withdraw_fail, // 提现失败
    withdraw_success; // 提现成功
}
