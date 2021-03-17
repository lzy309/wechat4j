package com.wechat4j.constant;

/**
 * 微信公众号API接口
 *
 * @author lizhaoyang
 */
public interface OfficialAccountApi {

    String API_HOST = "https://api.weixin.qq.com/cgi-bin/";

    /**
     * 全局唯一接口调用凭据
     */
    String GET_ACCESS_TOKEN_API = API_HOST + "token";

    /**
     * 创建自定义菜单
     */
    String CREATE_MENU_API = API_HOST + "menu/create";

    /**
     * 查询自定义菜单
     */
    String GET_MENU_API = API_HOST + "get_current_selfmenu_info";

    /**
     * 删除自定义菜单
     */
    String DELETE_MENU_API = API_HOST + "delete";

    /**
     * 创建个性化菜单
     */
    String CREATE_CONDITIONAL_MENU_API =  API_HOST + "menu/addconditional";

    /**
     * 删除个性化菜单
     */
    String DELETE_CONDITIONAL_MENU_API =  API_HOST + "menu/delconditional";
}
