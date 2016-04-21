package br.ufrgs.inf.tlbellini.plugins;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import com.ibatis.common.jdbc.ScriptRunner;

import br.ufrgs.inf.tlbellini.PajeGrammar;
import br.ufrgs.inf.tlbellini.lib.*;

public class PajeInsertDBPlugin extends PajePlugin {

	String status = "Not connected...";
	private Connection conn;
	private String serverName;
	private String mydatabase;
	private String username;
	private String password;
	public long insertionTime = 0;
	private int fileId;
	private boolean batch;
	private int batchSize = Integer.MAX_VALUE;
	private int batchCount = 0;
	public long maxMem = 0;
	/*
	 * Key: startTime Value: [0] endTime [1] duration [2] size in number of
	 * operations
	 */
	private Map<Long, Long[]> batchInfo = new HashMap<Long, Long[]>();

	PreparedStatement stmtLink;
	PreparedStatement stmtEvent;
	PreparedStatement stmtVariable;
	PreparedStatement stmtState;
	FileWriter batchInfoFile;
	int rowCount = 1;
	long sizeInBytes = 518;

	public PajeInsertDBPlugin(String serverName, String database, String username, String password, boolean batch, int batchSize) throws SQLException {
		this.serverName = serverName;
		this.mydatabase = database;
		this.username = username;
		this.password = password;
		this.batch = batch;
		this.batchSize = batchSize;
		connectDB();
		
		stmtLink = conn.prepareStatement(
				"INSERT INTO link (start_time, end_time, link_key, value_alias, type_alias, start_container_alias, end_container_alias, type_file_id) VALUES (?,?,?,?,?,?,?,?)");
		stmtEvent = conn.prepareStatement(
				"INSERT INTO event (time, type_alias, container_alias, value_alias, type_file_id) VALUES (?,?,?,?,?)");
		stmtVariable = conn.prepareStatement(
				"INSERT INTO variable (time, type_alias, container_alias, value, start_time, container_file_id, end_time) VALUES (?,?,?,?,?,?,?)");
		stmtState = conn.prepareStatement(
				"INSERT INTO state (container_alias, type_alias, startTime, endTime, value_alias, imbrication, container_file_id) VALUES (?,?,?,?,?,?,?)");
		
		//create file row
		Date d = new Date();
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		insertFileSQL(PajeGrammar.options.filename, PajeGrammar.options.comment, sdf.format(d));
		openFileBatchInfo();
	}
	
	public void connectMySQL(){
		Connection connection = null;

		try {
			String driverName = "com.mysql.jdbc.Driver";
			Class.forName(driverName);
			String useSSL = "?verifyServerCertificate=false" + "&useSSL=false";
			String batch = "&rewriteBatchedStatements=true";
			String url = "jdbc:mysql://" + serverName + "/" + useSSL + batch;
			connection = DriverManager.getConnection(url, username, password);
			if (connection != null) {
				status = ("STATUS--->Successfully connected");
			} else {
				status = ("STATUS--->Not connected");
			}
			this.conn = connection;
			System.out.println(status);
		}catch (ClassNotFoundException e) {
			System.out.println("The driver specified was not found.");
		}catch(SQLException e){
			e.printStackTrace();
		}
		
	}

	public void connectDB() {
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

		long start = System.nanoTime()/1000;
		try {
			java.sql.Statement stmt = conn.createStatement();
			stmt.executeUpdate(sql);
			stmt.close();
			long end = System.nanoTime()/1000;
			this.insertionTime += end - start;
			return true;
		} catch (SQLException e) {
			System.out.println("DB ERROR: ");
			e.printStackTrace();
			return false;
		}
	}

	public void insertFileSQL(String filename, String comment, String date) throws SQLException {
		String sql = "INSERT INTO file (name, comment, date) " + "VALUES ( " + toString(filename) + " , "
				+ toString(comment) + ", " + toString(date) + ")";
		insert(sql);
		fileId = getFileId(PajeGrammar.options.filename);
		System.out.println("File Id: " + fileId);
	}

	// if filename is the same
	public int getFileId(String filename) throws SQLException {
		long start = System.nanoTime()/1000;
		java.sql.Statement stmt = conn.createStatement();
		ResultSet rs = null;
		try {
			String sql = "SELECT id FROM file WHERE name = " + toString(filename) + " ORDER BY id DESC LIMIT 1";
			rs = stmt.executeQuery(sql);
			rs.next();
			long end = System.nanoTime()/1000;
			insertionTime += end - start;
			int result = rs.getInt("id");
			rs.close();
			stmt.close();
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}

	}

	// getting SQL Exception when string is not double quoted
	public String toString(String str) {
		if (str.startsWith("\"") || str == "null")
			return str;
		else
			return "\"" + str + "\"";
	}

	protected String getStringColor(PajeColor color) {
		if (color != null)
			return "(" + color.r + "," + color.g + "," + color.b + "," + color.a + ")";
		else
			return "null";
	}

	public String generateInsertTypeSQL(String alias, String name, String parent, int depth, int fileId) {
		return "INSERT INTO type (alias, name, parent_type_alias, depth, file_id) " + "VALUES ( " + toString(alias)
				+ " , " + toString(name) + ", " + toString(parent) + ", " + depth + ", " + fileId + ")";

	}

	public String generateInsertVariableTypeSQL(String alias, String name, String parent, int depth, int fileId,
			PajeColor color) {
		return "INSERT INTO type (alias, name, parent_type_alias, depth, file_id, color) " + "VALUES ( "
				+ toString(alias) + " , " + toString(name) + ", " + toString(parent) + ", " + depth + ", " + fileId
				+ "," + toString(getStringColor(color)) + ")";
	}

	public String generateInsertLinkTypeSQL(String alias, String name, String parent, int depth, int fileId,
			String start, String end) {
		return "INSERT INTO type (alias, name, parent_type_alias, depth, file_id, start_link_type, end_link_type) "
				+ "VALUES ( " + toString(alias) + " , " + toString(name) + ", " + toString(parent) + ", " + depth + ", "
				+ fileId + "," + toString(start) + "," + toString(end) + ")";
	}

	public String generateInsertValueSQL(String alias, String name, String type, PajeColor color, int fileId) {
		return "INSERT INTO value (alias, name, type_alias, color, type_file_id) " + "VALUES ( " + toString(alias)
				+ " , " + toString(name) + ", " + toString(type) + "," + getStringColor(color) + "," + fileId + ")";
	}

	public String generateInsertContainerSQL(String alias, String name, double start, double end, String parent_alias,
			String type_alias, int depth, int fileId) {
		StringBuilder sb = new StringBuilder(
				"INSERT INTO container (alias, name, startTime, endTime, parent_container_alias, type_alias, depth, file_id) VALUES (");
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
		sb.append(depth);
		sb.append(",");
		sb.append(fileId);
		sb.append(")");
		return sb.toString();

	}

	@Override
	public void addType(PajeType newType) {
		String sql;
		switch (newType.getNature()) {
		case ContainerType:
			sql = generateInsertTypeSQL(newType.getAlias(), newType.getName(),
					newType.getParent() != null ? newType.getParent().getAlias() : "null", newType.getDepth(), fileId);
			insert(sql);
			break;
		case EventType:
			sql = generateInsertTypeSQL(newType.getAlias(), newType.getName(),
					newType.getParent() != null ? newType.getParent().getAlias() : "null", newType.getDepth(), fileId);
			insert(sql);
			break;
		case LinkType:
			sql = generateInsertLinkTypeSQL(newType.getAlias(), newType.getName(),
					newType.getParent() != null ? newType.getParent().getAlias() : "null", newType.getDepth(), fileId,
					(((PajeLinkType) newType).getStartType().getAlias()),
					(((PajeLinkType) newType).getEndType().getAlias()));
			insert(sql);
			break;
		case StateType:
			sql = generateInsertTypeSQL(newType.getAlias(), newType.getName(),
					newType.getParent() != null ? newType.getParent().getAlias() : "null", newType.getDepth(), fileId);
			insert(sql);
			break;
		case VariableType:
			sql = generateInsertVariableTypeSQL(newType.getAlias(), newType.getName(),
					newType.getParent() != null ? newType.getParent().getAlias() : "null", newType.getDepth(), fileId,
					((PajeVariableType) newType).getColor());
			insert(sql);
			break;
		default:
			break;

		}
	}

	@Override
	public void addValue(PajeValue value) {
		String sql = generateInsertValueSQL(value.getAlias(), value.getName(), value.getType().getAlias(),
				value.getColor(), fileId);
		insert(sql);
	}

	@Override
	public void addNewContainer(PajeContainer newContainer) {
		String sql = generateInsertContainerSQL(newContainer.alias, newContainer.getName(), newContainer.getStartTime(),
				-1, newContainer.getContainer() != null ? newContainer.getContainer().alias : "null",
				newContainer.getType().getAlias(), newContainer.depth, fileId);
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

		// insertStackStates
		for (Map.Entry<PajeType, ArrayList<PajeUserState>> entry : pajeContainer.getStackStates().entrySet()) {
			for (PajeUserState state : entry.getValue()) {
				try {
					stmtState.setString(1, state.getContainer().alias);
					stmtState.setString(2, state.getType().getAlias());
					stmtState.setDouble(3, state.getStartTime());
					stmtState.setDouble(4, state.getEndTime());
					stmtState.setString(5, state.getValue().getAlias());
					stmtState.setDouble(6, state.getImbrication());
					stmtState.setInt(7, fileId);

					stmtState.addBatch();
					
					//get size in bytes based on number of chars in string
					sizeInBytes += Double.toString(state.getStartTime()).length();
					sizeInBytes += Double.toString(state.getEndTime()).length();
					sizeInBytes += state.getValue().getAlias().length();
					sizeInBytes += state.getType().getAlias().length();
					sizeInBytes += state.getContainer().alias.length();
					sizeInBytes += Double.toString(state.getImbrication()).length();
					sizeInBytes += Integer.toString(fileId).length();
					
					verifyBatchCount();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

		// insert last variables (the last entity that has left in memory)
		for (Map.Entry<PajeType, ArrayList<PajeEntity>> entry : pajeContainer.getEntities().entrySet()) {
			if (entry.getValue().size() > 0) {
				PajeEntity last = entry.getValue().get(entry.getValue().size() - 1);
				PajeEntity first = entry.getValue().get(0);
				try {
					if (last.getType().getNature() == PajeTypeNature.VariableType) {
						stmtVariable.setDouble(1, ((PajeSingleTimedEntity) first).getStartTime());
						stmtVariable.setString(2, last.getType().getAlias());
						stmtVariable.setString(3, pajeContainer.alias);
						stmtVariable.setDouble(4, ((PajeUserVariable) last).getValue());
						stmtVariable.setDouble(5, ((PajeSingleTimedEntity) last).getStartTime());
						stmtVariable.setInt(6, fileId);
						stmtVariable.setDouble(7, ((PajeDoubleTimedEntity) last).getEndTime());
						stmtVariable.addBatch();
						
						//get size in bytes based on number of chars in string
						sizeInBytes += Double.toString(((PajeSingleTimedEntity) first).getStartTime()).length();
						sizeInBytes += last.getType().getAlias().length();
						sizeInBytes += last.getContainer().alias.length();
						sizeInBytes += Double.toString(((PajeUserVariable) last).getValue()).length();
						sizeInBytes += Double.toString(((PajeSingleTimedEntity) last).getStartTime()).length();
						sizeInBytes += Double.toString(((PajeSingleTimedEntity) last).getEndTime()).length();
						sizeInBytes += Integer.toString(fileId).length();
						
						verifyBatchCount();
					}

				} catch (SQLException e) {
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
		try {
			stmtState.setString(1, state.getContainer().alias);
			stmtState.setString(2, state.getType().getAlias());
			stmtState.setDouble(3, state.getStartTime());
			stmtState.setDouble(4, state.getEndTime());
			stmtState.setString(5, state.getValue().getAlias());
			stmtState.setDouble(6, state.getImbrication());
			stmtState.setInt(7, fileId);

			stmtState.addBatch();
			
			//get size in bytes based on number of chars in string
			sizeInBytes += Double.toString(state.getStartTime()).length();
			sizeInBytes += Double.toString(state.getEndTime()).length();
			sizeInBytes += state.getValue().getAlias().length();
			sizeInBytes += state.getType().getAlias().length();
			sizeInBytes += state.getContainer().alias.length();
			sizeInBytes += Double.toString(state.getImbrication()).length();
			sizeInBytes += Integer.toString(fileId).length();
			
			
			verifyBatchCount();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void addLink(PajeUserLink link) {
		try {
			stmtLink.setDouble(1, link.getStartTime());
			stmtLink.setDouble(2, link.getEndTime());
			stmtLink.setString(3, link.getKey());
			stmtLink.setString(4, link.getValue().getAlias());
			stmtLink.setString(5, link.getType().getAlias());
			stmtLink.setString(6, link.getStartContainer().alias);
			stmtLink.setString(7, link.getEndContainer().alias);
			stmtLink.setInt(8, fileId);
			stmtLink.addBatch();
			
			//get size in bytes based on number of chars in string
			sizeInBytes += Double.toString(link.getStartTime()).length();
			sizeInBytes += Double.toString(link.getEndTime()).length();
			sizeInBytes += link.getKey().length();
			sizeInBytes += link.getValue().getAlias().length();
			sizeInBytes += link.getType().getAlias().length();
			sizeInBytes += link.getStartContainer().alias.length();
			sizeInBytes += link.getEndContainer().alias.length();
			sizeInBytes += Integer.toString(fileId).length();
			
			verifyBatchCount();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void setVar(PajeUserVariable newVar) {
		// add only when it`s complete (with end time)
	}

	@Override
	public void addVar(PajeEntity first, PajeEntity last, PajeUserVariable newValue) {
		// adds the last one to db, since it is complete with start and end
		try {
			if (last != null && ((PajeDoubleTimedEntity) last).getEndTime() > 0) {
				stmtVariable.setDouble(1, ((PajeSingleTimedEntity) first).getStartTime());
				stmtVariable.setString(2, last.getType().getAlias());
				stmtVariable.setString(3, last.getContainer().alias);
				stmtVariable.setDouble(4, ((PajeUserVariable) last).getValue());
				stmtVariable.setDouble(5, ((PajeSingleTimedEntity) last).getStartTime());
				stmtVariable.setInt(6, fileId);
				stmtVariable.setDouble(7, ((PajeDoubleTimedEntity) last).getEndTime());
				stmtVariable.addBatch();
				
				//get size in bytes based on number of chars in string
				sizeInBytes += Double.toString(((PajeSingleTimedEntity) first).getStartTime()).length();
				sizeInBytes += last.getType().getAlias().length();
				sizeInBytes += last.getContainer().alias.length();
				sizeInBytes += Double.toString(((PajeUserVariable) last).getValue()).length();
				sizeInBytes += Double.toString(((PajeSingleTimedEntity) last).getStartTime()).length();
				sizeInBytes += Double.toString(((PajeSingleTimedEntity) last).getEndTime()).length();
				sizeInBytes += Integer.toString(fileId).length();
				
				verifyBatchCount();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void addEvent(PajeUserEvent event) {
		try {
			stmtEvent.setDouble(1, event.getStartTime());
			stmtEvent.setString(2, event.getType().getAlias());
			stmtEvent.setString(3, event.getContainer().alias);
			stmtEvent.setString(4, event.getValue().getAlias());
			stmtEvent.setInt(5, fileId);
			stmtEvent.addBatch();
			
			//get size in bytes based on number of chars in string
			sizeInBytes += Double.toString(event.getStartTime()).length();
			sizeInBytes += event.getType().getAlias().length();
			sizeInBytes += event.getContainer().alias.length();
			sizeInBytes += event.getValue().getAlias().length();
			sizeInBytes += Integer.toString(fileId).length();
			
			verifyBatchCount();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		

	}

	@Override
	public void finish() {
		execBatches();
		System.out.println("Insertion Time: " + this.insertionTime);
		System.out.println("Mem: " + maxMem);
		System.out.println("Batch Size: " + this.batchSize);
		System.out.println("Batch Count: " + this.batchCount);
		try {
			batchInfoFile.flush();
			batchInfoFile.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		close();
	}

	public void execBatches() {
		try {
			// - start time to get the time starting at zero
			if (PajeGrammar.evaluateMemoryRuntime() > maxMem)
				maxMem = PajeGrammar.evaluateMemoryRuntime();
			long startTime = System.nanoTime()/1000 - PajeGrammar.startTime;
			stmtEvent.executeBatch();
			stmtLink.executeBatch();
			stmtState.executeBatch();
			stmtVariable.executeBatch();
			long endTime = System.nanoTime()/1000 - PajeGrammar.startTime;
			// get the seconds
			insertRowCSV(startTime, endTime, this.batchCount, sizeInBytes);
			insertionTime+= endTime - startTime;
			sizeInBytes = 518;
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void deleteSimulation(){
		if(PajeGrammar.options.test){
			connectMySQL();
			String sql = "DROP DATABASE `paje`;";
			insert(sql);
			ScriptRunner runner = new ScriptRunner(conn, false, false);
			Reader reader;
			try {
				reader = new FileReader("../pajeDB.sql");
				runner.runScript(reader);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			close();
		}
		
	}

	private void verifyBatchCount() {
		if (batch || true) {
			this.batchCount++;
			if (this.batchCount == batchSize) {
				execBatches();
				this.batchCount = 0;
			}
		}
	}

	public void openFileBatchInfo() {
		String filename = String.format("%s_%d_%s.csv", PajeGrammar.options.filename, batchSize,
				PajeGrammar.options.platform);
		try {
			batchInfoFile = new FileWriter(filename);
			batchInfoFile.append("BatchNumber,StartTime(microseconds), EndTime(microseconds), Duration(microseconds), Size (Operations), Size (Bytes)\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void insertRowCSV(long startTime, long endTime, long sizeOp, long sizeBytes) {

		try {
			batchInfoFile.append(Integer.toString(rowCount));
			batchInfoFile.append(",");
			batchInfoFile.append(Long.toString(startTime));
			batchInfoFile.append(",");
			batchInfoFile.append(Long.toString(endTime));
			batchInfoFile.append(",");
			batchInfoFile.append(Long.toString(endTime - startTime));
			batchInfoFile.append(",");
			batchInfoFile.append(Long.toString(sizeOp));
			batchInfoFile.append(",");
			batchInfoFile.append(Long.toString(sizeBytes));
			batchInfoFile.append("\n");
			rowCount++;

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void endSimulation() {
		deleteSimulation();
	}

}
