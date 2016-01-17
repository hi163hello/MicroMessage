package com.imooc.dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.imooc.bean.Message;
import com.imooc.db.DBAccess;

public class MessageDao {
	
	public List<Message> queryMessageList(String command, String description) {
		List<Message> messageList = new ArrayList<Message>();
		DBAccess dbAccess = new DBAccess();
		SqlSession sqlSession = null;
		try {
			sqlSession = dbAccess.getSqlSession();
			Message message = new Message();
			message.setCommand(command);
			message.setDescription(description);
			messageList = sqlSession.selectList("Message.queryMessageList", message);
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(sqlSession != null){
				sqlSession.close();
			}
		}
		return messageList;
	}
	
	public static void main(String[] args) {
		MessageDao messageDao = new MessageDao();
		messageDao.queryMessageList("", "");
	}

	public List<Message> queryMessageList_JDBC(String command, String description) {
		List<Message> messageList = new ArrayList<Message>();
		try {

			Class.forName("com.mysql.jdbc.Driver");
			Connection conn = DriverManager
					.getConnection("jdbc:mysql://127.0.0.1:3306/micro_message",
							"root", "root");
			StringBuilder sql = new StringBuilder(
					"select id,command,description,content from message where 1=1");
			List<String> paramList = new ArrayList<String>();
			if (command != null && !"".equals(command.trim())) {
				sql.append(" and command = ? ");
				paramList.add(command);
			}
			if (description != null && !"".equals(description.trim())) {
				sql.append(" and description like '%' ? '%' ");
				paramList.add(description);
			}

			PreparedStatement statement = conn.prepareStatement(sql.toString());
			for (int i = 0; i < paramList.size(); i++) {
				statement.setString(i + 1, paramList.get(i));
			}

			ResultSet rs = statement.executeQuery();

			while (rs.next()) {
				Message message = new Message();
				messageList.add(message);
				message.setId(rs.getString("ID"));
				message.setCommand(rs.getString("COMMAND"));
				message.setDescription(rs.getString("DESCRIPTION"));
				message.setContent(rs.getString("CONTENT"));
			}

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return messageList;
	}

}
