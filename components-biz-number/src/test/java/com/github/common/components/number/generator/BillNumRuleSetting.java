package com.github.common.components.number.generator;

/**
 * 编号生成配置。项目中使用时，需要配置到apollo中
 *
 * @author Michael
 */
public class BillNumRuleSetting {
  public static final String RULES =
      "{'bills':"
          + "[{'bill':'SALE_ORDER',"
          + "'rules':[{'type':'FIXED','length':'2','value':'10'},{'type':'DATE',"
          + "'length':'6'},{'type':'SEQ','length':'9'},{'type':'RANDOM','length':'2'}]},"
          + "{'bill':'PURCHASE_ORDER',"
          + "'rules':[{'type':'FIXED',"
          + "'length':'2','value':'11'},{'type':'DATE','length':'6'},{'type':'SEQ','length':'9'},{'type':'RANDOM','length':'2'}]}"
          + "]}";

  public static void main(String[] args) {
    System.out.println(RULES);
  }
}
