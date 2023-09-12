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
    /**
     * 个人中心机器人跳转链接
     */
    public static final String PERSON_CENTER_BOT_URL = "person_center_bot_url";

    /**
     * 唯一专属客服链接
     */
    public static final String EXCLUSION_CUSTOMER_SERVICE = "exclusion_customer_service";

    /**
     * 机器人群邀请链接
     */
    public static final String BOT_GROUP_INVITE_LINK = "bot_group_invite_link";

    /**
     * 机器人前端落地页H5链接
     */
    public static final String BOT_TG_GAME_H5_URL = "tg_game_h5_url";

    /**
     * 机器人开始游戏群链接
     */
    public static final String BOT_BEGIN_GAME_GROUP_LINK = "bot_begin_game_group_link";
    public static final String BOT_BEGIN_GAME_GROUP_CHAT = "bot_begin_game_group_chat";

    /**
     * 发财电子密钥code
     */
    public static final String FC_AGENT_CODE = "fc_agent_code";
    /**
     * fc请求域名
     */
    public static final String FC_HOST = "fc_host";
    /**
     * fc请求渠道key
     */
    public static final String FC_AGENT_KEY = "fc_agent_key";
    /**
     * eg密钥code
     */
    public static final String EG_AGENT_CODE = "eg_agent_code";
    /**
     * eg请求域名
     */
    public static final String EG_HOST = "eg_host";
    /**
     * eg_hmac_sha256_key
     */
    public static final String EG_HASH_KEY = "eg_hash_key";
    /**
     * eg平台value
     */
    public static final String EG_PLATFORM = "eg_platform";
    /**
     * eg_游戏列表
     */
    public static final String EG_GAME_LIST = "eg_game_list";

    /**
     * wl请求域名
     */
    public static final String WL_HOST = "wl_host";
    /**
     * wl参数加密key
     */
    public static final String WL_PARAM_KEY = "wl_param_key";
    /**
     * wl平台value
     */
    public static final String WL_REQ_KEY = "wl_req_key";
    /**
     * wl平台account
     */
    public static final String WL_ACCOUNT = "wl_account";
    /**
     * wl渠道id
     */
    public static final String WL_AGENT_ID = "wl_agent_id";
    /**
     * wl汇率转换
     */
    public static final String WL_GAME_USDT_POINT = "wl_game_usdt_point";

    /**
     * eg/wl/fc返水比例
     */
    public static final String GAME_BACK_WATER_RATE = "game_back_water_rate";



}
