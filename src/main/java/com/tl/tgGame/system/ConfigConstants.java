package com.tl.tgGame.system;

public class ConfigConstants {

    /**
     * BSC链钱包地址
     */
    public static final String BSC_CHAIN_URL = "bsc_chain_url";
    public static final String BSC_CHAIN_ID = "bsc_chain_id";
    public static final String BSC_CONTRACT_ADDRESS = "bsc_contract_address";
    public static final String BSC_GAS_PRICE = "bsc_gas_price";
    public static final String BSC_GAS_LIMIT = "bsc_gas_limit";

    /**
     * ETH链钱包地址
     */
    public static final String ETH_CHAIN_URL = "eth_chain_url";
    public static final String ETH_CHAIN_ID = "eth_chain_id";
    public static final String ETH_CONTRACT_ADDRESS = "eth_contract_address";
    public static final String ETH_GAS_PRICE = "eth_gas_price";
    public static final String ETH_GAS_LIMIT = "eth_gas_limit";

    /**
     * TRON合约地址
     */
    public static final String TRON_CONTRACT_ADDRESS = "tron_contract_address";
    public static final String TRON_CHAIN_URL = "tron_chain_url";

    /**
     * 数据中心回调
     */
    public static final String TX_CALLBACK = "tx_callback";
    public static final String REGISTER_ADDRESS = "register_address";
    public static final String CONDITION_ADDRESS = "condition_address";
    public static final String TX_DETAIL_ADDRESS = "tx_detail_address";

    /**
     * 管理归集的钱包(加密存储)
     * {"password":"助记词","ethPriKey":"eth私钥","ethAddress":"eth地址","tronPriKey":"tron私钥","tronAddress":"tron地址"}
     */
    public static final String MANAGER_WALLET = "manager_wallet";

    /**
     * 提现的钱包(加密存储)
     * {"password":"助记词","ethPriKey":"eth私钥","ethAddress":"eth地址","tronPriKey":"tron私钥","tronAddress":"tron地址"}
     */
    public static final String NORMAL_WALLET = "normal_wallet";

    /**
     * bsc usdt 合约地址
     */
    public static final String USDT_BEP20_CONTRACT = "usdt_bep20_contract";
    public static final String USDT_BEP20_DECIMAL = "usdt_bep20_decimals";
    /**
     * tron usdt 合约地址
     */
    public static final String USDT_TRC20_CONTRACT = "usdt_trc20_contract";
    public static final String USDT_TRC20_DECIMAL = "usdt_trc20_decimals";
    /**
     * eth usdt 合约地址
     */
    public static final String USDT_ERC20_CONTRACT = "usdt_erc20_contract";
    public static final String USDT_ERC20_DECIMAL = "usdt_erc20_decimals";

    /**
     * 用户usdt提现固定手续费
     */
    public static final String BEP20_USDT_WITHDRAWAL_FIXED_FEE = "bep20_usdt_withdrawal_fixed_fee";
    public static final String ERC20_USDT_WITHDRAWAL_FIXED_FEE = "erc20_usdt_withdrawal_fixed_fee";
    public static final String TRC20_USDT_WITHDRAWAL_FIXED_FEE = "trc20_usdt_withdrawal_fixed_fee";

    /**
     * 机器人token
     */
    public static final String TG_BOT_TOKEN = "tg_bot_token";

    /**
     * 机器人名称
     */
    public static final String TG_BOT_NAME = "tg_bot_name";

    /**
     * 个人中心机器人token
     */
    public static final String TG_BOT_TOKEN_TWO = "tg_bot_token_two";

    /**
     * 个人中心机器人名称
     */
    public static final String TG_BOT_NAME_TWO = "tg_bot_name_two";

}
