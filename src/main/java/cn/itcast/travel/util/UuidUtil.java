package cn.itcast.travel.util;

import java.util.UUID;

/**
 * 产生UUID随机字符串工具类
 * UUID类：可以生成全球唯一的字符串，与当前电脑的硬件信息以及当前时间的毫秒值相关
 */
public final class UuidUtil {
	private UuidUtil(){}
	public static String getUuid(){
		return UUID.randomUUID().toString().replace("-","");
	}
	/**
	 * 测试
	 */
	public static void main(String[] args) {
		System.out.println(UuidUtil.getUuid());
		System.out.println(UuidUtil.getUuid());
		System.out.println(UuidUtil.getUuid());
		System.out.println(UuidUtil.getUuid());
	}
}
