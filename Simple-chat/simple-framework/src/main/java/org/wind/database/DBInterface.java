package org.wind.database;

/**
 * 对数据库操作的接口,使使用着可以自己编写语句创建
 * 
 * @author 作者 :wei.hu
 * @version 创建时间:2015-5-12 下午5:19:41
 */
public interface DBInterface {
	// 获取创建表的sql语句
	StringBuilder getTableSQL();
}
