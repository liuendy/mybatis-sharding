package cc.iliz.mybatis.shading.sqltable;

import java.util.Locale;

import org.apache.ibatis.mapping.SqlCommandType;

import cc.iliz.mybatis.shading.db.ShardingEntry;

public class RouteSqlTableParser implements SqlTableParser {
	private BaseSqlTableParser sqlTableParser;

	@Override
	public ShardingEntry markShardingTable(String sql, Object param) {
		SqlCommandType commandType = getSqlCommandType(sql);
		switch(commandType){
		case SELECT:
			sqlTableParser = new SelectSqlTableParser();
			sqlTableParser.setSqlCommandType(SqlCommandType.SELECT);
			break;
		case UPDATE:
			sqlTableParser = new UpdateSqlTableParser();
			sqlTableParser.setSqlCommandType(SqlCommandType.UPDATE);
			break;
		case DELETE:
			sqlTableParser = new DeleteSqlTableParser();
			sqlTableParser.setSqlCommandType(SqlCommandType.DELETE);
			break;
		case INSERT:
			sqlTableParser = new InsertSqlTableParser();
			sqlTableParser.setSqlCommandType(SqlCommandType.INSERT);
			break;
		case UNKNOWN:
		case FLUSH:
		}
		if(sqlTableParser != null){
			return sqlTableParser.markShardingTable(sql, param);
		}
		
		return null;
	}
	
	
	private SqlCommandType getSqlCommandType(String sql){
		String usql = sql.trim().toUpperCase(Locale.ENGLISH);
		if(usql.startsWith("SELECT")){
			return SqlCommandType.SELECT;
		}else if(usql.startsWith("UPDATE")){
			return SqlCommandType.UPDATE;
		}else if(usql.startsWith("DELETE")){
			return SqlCommandType.DELETE;
		}else if(usql.startsWith("INSERT")){
			return SqlCommandType.INSERT;
		}else{
			return SqlCommandType.UNKNOWN;
		}
	}


	@Override
	public SqlCommandType getSqlCommandType() {
		return sqlTableParser.getSqlCommandType();
	}

}
