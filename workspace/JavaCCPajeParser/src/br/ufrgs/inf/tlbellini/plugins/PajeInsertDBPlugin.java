package br.ufrgs.inf.tlbellini.plugins;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import br.ufrgs.inf.tlbellini.PajeGrammar;
import br.ufrgs.inf.tlbellini.lib.*;

public class PajeInsertDBPlugin extends PajePlugin {
	
	String status = "Not connected...";
	private Connection conn;
	private String serverName;
	private String mydatabase;
	private String username;
	private String password;
	public long insertionTime;
	private int fileId; 
	
	PreparedStatement stmtLink;
	PreparedStatement stmtEvent;
	PreparedStatement stmtVariable;
	PreparedStatement stmtState;
	
	
	public PajeInsertDBPlugin(String serverName, String database, String username, String password) throws SQLException {
		this.serverName = serverName;
		this.mydatabase = database;
		this.username = username;
		this.password = password;
		
		connectDB();
		
		stmtLink = conn.prepareStatement(
				"INSERT INTO link (start_time, end_time, link_key, value_alias, type_alias, start_container_alias, end_container_alias, type_file_id) VALUES (?,?,?,?,?,?,?,?)");
		stmtEvent = conn.prepareStatement(
				"INSERT INTO event (time, type_alias, container_alias, value_alias, type_file_id) VALUES (?,?,?,?,?)");
		stmtVariable = conn.prepareStatement(
				"INSERT INTO variable (time, type_alias, container_alias, value, update_time, container_file_id) VALUES (?,?,?,?,?,?)");
		stmtState = conn.prepareStatement(
				"INSERT INTO state (container_alias, type_alias, startTime, endTime, value_alias, imbrication, container_file_id) VALUES (?,?,?,?,?,?,?)");
		
		//create file row
		Date d = new Date();
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		insertFileSQL(PajeGrammar.options.filename, PajeGrammar.options.comment, sdf.format(d));
	}
	
	public void connectDB(){
		Connection connection = null;

		try {
			String driverName = "com.mysql.jdbc.Driver";
			Class.forName(driverName);
			String useSSL = "?verifyServerCertificate=false" + "&useSSL=false";
			String batch = "&rewriteBatchedStatements=true";
			String url = "jdbc:mysql://" + serverName + "/" + mydatabase + useSSL + batch;
			connection = DriverManager.getConnection(url, username, password);
			if (connection != null) {
				status = ("STATUS--->Successfully connected");
			} else {
				status = ("STATUS--->Not connected");
			}
			this.conn = connection;
			System.out.println(status);
		}

		catch (ClassNotFoundException e) {
			System.out.println("The driver specified was not found.");
			
		} catch (SQLException e) {
			System.out.println("The database was not found.");
			
		}
	}
	
	public boolean close() {
		try {
			conn.close();
			return true;
		} catch (SQLException e) {
			return false;
		}
	}
	
	public boolean insert(String sql) {

	    long start = System.currentTimeMillis();
		try {
			java.sql.Statement stmt = conn.createStatement();
			stmt.executeUpdate(sql);
			stmt.close();
			long end = System.currentTimeMillis();
			this.insertionTime += end-start;
			return true;
		} catch (SQLException e) {
			System.out.println("DB ERROR: ");
			e.printStackTrace();
			return false;
		}
	}
	
	public void insertFileSQL(String filename, String comment, String date) throws SQLException{
		String sql = "INSERT INTO file (name, comment, date) " + "VALUES ( " + toString(filename)  + " , " + toString(comment) + ", " + 
				toString(date) + ")";
		insert(sql);
		fileId = getFileId(PajeGrammar.options.filename);
		System.out.println("File Id: " + fileId);
	}
	
	//if filename is the same 
		public int getFileId(String filename) throws SQLException{
			long start = System.currentTimeMillis();
			java.sql.Statement stmt = conn.createStatement();
			ResultSet rs = null;
			try{
				String sql = "SELECT id FROM file WHERE name = " + toString(filename) + " ORDER BY id DESC LIMIT 1";
				rs = stmt.executeQuery(sql);
				rs.next();
				long end = System.currentTimeMillis();
				insertionTime += end-start;
				int result = rs.getInt("id");
				rs.close();
				stmt.close();
				return result;
			}catch (Exception e){
				e.printStackTrace();
				return -1;
			}
			
		}
	
	//getting SQL Exception when string is not double quoted
	public String toString(String str){
		if(str.startsWith("\"") || str == "null")
			return str;
		else
			return "\"" + str + "\"";
	}
	
	protected String getStringColor(PajeColor color){
		if(color != null)
			return "(" + color.r + "," + color.g + "," + color.b + "," + color.a + ")";   
		else
			return "null";
	}
	
	public String generateInsertTypeSQL(String alias, String name, String parent, int depth, int fileId) {
		return "INSERT INTO type (alias, name, parent_type_alias, depth, file_id) " + "VALUES ( " + toString(alias)  + " , " + toString(name) + ", " + 
				toString(parent) + ", " + depth + ", " + fileId + ")";
		
	}
	public String generateInsertVariableTypeSQL(String alias, String name, String parent, int depth, int fileId,
			PajeColor color) {
		return "INSERT INTO type (alias, name, parent_type_alias, depth, file_id, color) " + "VALUES ( " + toString(alias)  + " , " + toString(name) + ", " + 
				toString(parent) + ", " + depth + ", " + fileId + "," + toString(getStringColor(color)) + ")";
	}
	
	public String generateInsertLinkTypeSQL(String alias, String name, String parent, int depth, int fileId,
			String start, String end) {
		return "INSERT INTO type (alias, name, parent_type_alias, depth, file_id, start_link_type, end_link_type) " + "VALUES ( " + toString(alias)  + " , " + toString(name) + ", " + 
				toString(parent) + ", " + depth + ", " + fileId + "," + toString(start) + "," + toString(end) + ")";
	}
	
	public String generateInsertValueSQL(String alias, String name, String type, PajeColor color, int fileId) {
		return "INSERT INTO value (alias, name, type_alias, color, type_file_id) " + "VALUES ( " + toString(alias) + " , "+ toString(name) + ", " + 
				toString(type) + "," + getStringColor(color) +  "," + fileId + ")";
	}
	
	public String generateInsertContainerSQL(String alias, String name, double start, double end, String parent_alias,
			String type_alias, int fileId) {
		StringBuilder sb = new StringBuilder("INSERT INTO container (alias, name, startTime, endTime, parent_container_alias, type_alias, file_id) VALUES (");
		sb.append(toString(alias));
		sb.append(", ");
		sb.append(toString(name));
		sb.append(", ");
		sb.append(start);
		sb.append(", ");
		sb.append(end);
		sb.append(", ");
		sb.append(toString(parent_alias));
		sb.append(", ");
		sb.append(toString(type_alias));
		sb.append(",");
		sb.append(fileId);
		sb.append(")");
		return sb.toString();
		
	}
	
	@Override
	public void addType(PajeType newType) {
		String sql;
		switch(newType.getNature()){
		case ContainerType:
			sql = generateInsertTypeSQL(newType.getAlias(), newType.getName(), newType.getParent() != null? newType.getParent().getAlias() : "null", newType.getDepth(), fileId);
			insert(sql);
			break;
		case EventType:
			sql = generateInsertTypeSQL(newType.getAlias(), newType.getName(), newType.getParent() != null? newType.getParent().getAlias() : "null", newType.getDepth(), fileId);
			insert(sql);
			break;
		case LinkType:
			sql = generateInsertLinkTypeSQL(newType.getAlias(), newType.getName(), newType.getParent() != null? newType.getParent().getAlias() : "null", newType.getDepth(), fileId, (((PajeLinkType) newType).getStartType().getAlias()), (((PajeLinkType) newType).getEndType().getAlias()));
			insert(sql);
			break;
		case StateType:
			sql = generateInsertTypeSQL(newType.getAlias(), newType.getName(), newType.getParent() != null? newType.getParent().getAlias() : "null", newType.getDepth(), fileId);
			insert(sql);
			break;
		case VariableType:
			sql = generateInsertVariableTypeSQL(newType.getAlias(), newType.getName(), newType.getParent() != null? newType.getParent().getAlias() : "null", newType.getDepth(), fileId, ((PajeVariableType) newType).getColor());
			insert(sql);
			break;
		default:
			break;
		
		}
	}

	@Override
	public void addValue(PajeValue value) {
		String sql = generateInsertValueSQL(value.getAlias(), value.getName(), value.getType().getAlias(), value.getColor(), fileId);
		insert(sql);
	}

	@Override
	public void addNewContainer(PajeContainer newContainer) {
		String sql = generateInsertContainerSQL(newContainer.alias, newContainer.getName(), newContainer.getStartTime(), -1,
				newContainer.getContainer() != null ? newContainer.getContainer().alias : "null", newContainer.getType().getAlias(), fileId);
		insert(sql);
	}

	@Override
	public void addDestroyedContainer(PajeContainer pajeContainer) {
		StringBuilder sb = new StringBuilder("UPDATE container SET endTime = ");
		sb.append(pajeContainer.getEndTime());
		sb.append(" WHERE alias = ");
		sb.append(toString(pajeContainer.alias));
		sb.append(" AND file_id = ");
		sb.append(fileId);
		this.insert(sb.toString());
		
		//insertStackStates
		for(Map.Entry<PajeType, ArrayList<PajeUserState>> entry : pajeContainer.getStackStates().entrySet()){
			for(PajeUserState state : entry.getValue()){
				try{
					stmtState.setString(1, state.getContainer().alias);
					stmtState.setString(2, state.getType().getAlias());
					stmtState.setDouble(3, state.getStartTime());
					stmtState.setDouble(4, state.getEndTime());
					stmtState.setString(5, state.getValue().getAlias());
					stmtState.setDouble(6, state.getImbrication());
					stmtState.setInt(7, fileId);
					
					stmtState.addBatch();
				}catch (SQLException e){
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void addState(PajeUserState newState) {
	}

	@Override
	public void pushState(PajeUserState newState) {

	}

	@Override
	public void popState(PajeUserState state) {
		try{
			stmtState.setString(1, state.getContainer().alias);
			stmtState.setString(2, state.getType().getAlias());
			stmtState.setDouble(3, state.getStartTime());
			stmtState.setDouble(4, state.getEndTime());
			stmtState.setString(5, state.getValue().getAlias());
			stmtState.setDouble(6, state.getImbrication());
			stmtState.setInt(7, fileId);
			
			stmtState.addBatch();
		}catch (SQLException e){
			e.printStackTrace();
		}
	}

	@Override
	public void addLink(PajeUserLink link) {
		try{
			stmtLink.setDouble(1, link.getStartTime());
			stmtLink.setDouble(2, link.getEndTime());
			stmtLink.setString(3, link.getKey());
			stmtLink.setString(4, link.getValue().getAlias());
			stmtLink.setString(5, link.getType().getAlias());
			stmtLink.setString(6, link.getStartContainer().alias);
			stmtLink.setString(7, link.getEndContainer().alias);
			stmtLink.setInt(8, fileId);
			stmtLink.addBatch();
		}catch (SQLException e){
			e.printStackTrace();
		}
		

	}

	@Override
	public void addVar(PajeEntity first, PajeUserVariable newValue) {
		try{
			stmtVariable.setDouble(1, ((PajeSingleTimedEntity) first).getStartTime());
			stmtVariable.setString(2, newValue.getType().getAlias());
			stmtVariable.setString(3, newValue.getContainer().alias);
			stmtVariable.setDouble(4, newValue.getValue());
			stmtVariable.setDouble(5, newValue.getStartTime());
			stmtVariable.setInt(6, fileId);
			stmtVariable.addBatch();
		}catch (SQLException e){
			e.printStackTrace();
		}

	}

	@Override
	public void addEvent(PajeUserEvent event) {
		try{
			stmtEvent.setDouble(1, event.getStartTime());
			stmtEvent.setString(2, event.getType().getAlias());
			stmtEvent.setString(3, event.getContainer().alias);
			stmtEvent.setString(4, event.getValue().getAlias());
			stmtEvent.setInt(5, fileId);
			stmtEvent.addBatch();
		}catch (SQLException e){
			e.printStackTrace();
		}

	}

	@Override
	public void finish() {
		try{
			stmtEvent.executeBatch();
			stmtLink.executeBatch();
			stmtState.executeBatch();
			stmtVariable.executeBatch();
			close();
		}catch (SQLException e){
			e.printStackTrace();
		}
		
	}

}
