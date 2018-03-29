package com.chetan.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapListHandler;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import play.db.Database;
import play.libs.Json;

import com.loco.data.Tables;
import com.loco.utils.Utils;

@Singleton
public class SystemDatabase
{
    protected final Database db;
    protected Connection conn;

    @Inject
    public SystemDatabase(Database db)
    {
	this.db = db;
	this.conn = this.db.getConnection();
    }

    protected ArrayNode getAllRecords(String tablename)
    {
	String selectQuery = "SELECT * FROM " + tablename;
	return this.getQueryAsJSON(selectQuery);
    }


	/**
	 * This method gives all the records form the given table
	 * based on conditions provided
	 */
    public ArrayNode getAllRecords(String tablename, HashMap<String, String> conditions)
    {
	String selectQuery = "SELECT * FROM " + tablename + " WHERE ";
	for (Entry<String, String> entry : conditions.entrySet())
	{
	    selectQuery += entry.getKey() + " = " + entry.getValue() + " and ";
	}
    selectQuery = selectQuery.substring(0, selectQuery.length() - 5);
	return this.getQueryAsJSON(selectQuery);
    }

    // Insert the record into the table specified in the argument
    public void insertRecord(JsonNode recordObj, String tableName)
    {

		// For the apostrophe to be preserved while entering the comment content
		// into the db
        String commentContent = recordObj.get("content").asText();
        String [] contentArray = commentContent.split("'");
        commentContent = "";
        for(String content :contentArray)
        {
            commentContent += content;
            commentContent += "\\'";
        }

		// Remove extra back slash and an apostrophe from the end
        commentContent = commentContent.substring(0,commentContent.length()-2);

		// Insert the comment and the details into the db
        String addCommentQuery = "INSERT INTO "+tableName+" ("+Tables.COLUMN_COMMENTS_PARENTCOMMENTID+","
        +Tables.COLUMN_COMMENTS_AUTHOR+","
        +Tables.COLUMN_COMMENTS_CONTENT
        +") VALUES (";

        addCommentQuery += Utils.quoteMe(recordObj.get("parentCommentId").asText()) + ","
            +Utils.quoteMe(recordObj.get("author").asText()) + ","
            +Utils.quoteMe(commentContent) + ")";

        int respCode = this.executeUpdate(addCommentQuery);
    }
	
	/**
	 * This method deletes the all the records from the table
	 * provided in the argument
	 * which match with the conditions
	 * provided in the HashMap conditions argument 
	 */
    public void deleteAllRecords(String tablename, HashMap<String, String> conditions)
    {

	String deleteQuery = "DELETE FROM " + tablename + " WHERE ";
	for (Entry<String, String> entry : conditions.entrySet())
	{
	    deleteQuery += entry.getKey() + " = " + entry.getValue() + " or ";
	}
    deleteQuery = deleteQuery.substring(0, deleteQuery.length() - 4);

    System.out.println("Delete Query >>> "+deleteQuery);
    int respCode = this.executeUpdate(deleteQuery);
    }

	//To execute the query
    protected int executeUpdate(String query)
    {
	int result = -1;
	try
	{
	    QueryRunner run = new QueryRunner(db.getDataSource());
	    result = run.update(query);
	} catch (SQLException e)
	{
	    e.printStackTrace();
	}
	return result;
    }

     /**
     * Executes a query and returns the result as a ArrayNode
     * 
     * @param query
     * @return
     */
    public ArrayNode getQueryAsJSON(String query)
    {
	return this.getQueryAsJSON(query, new LinkedList<String>());
    }


    /**
     * Executes a query and fetches the specified columns only as an ArrayNode
     * 
     * @param query
     * @param colnames
     * @return
     */
    private ArrayNode getQueryAsJSON(String query, LinkedList<String> colnames)
    {
	ArrayNode retval = Json.newArray();
	try
	{
	    Statement sqlStatement = this.conn.createStatement();
	    ResultSet rs = sqlStatement.executeQuery(query);
	    if (0 == colnames.size())
	    {
		colnames = this.getAllColumns(rs);
	    }
	    while (rs.next())
	    {
		ObjectNode currRec = Json.newObject();
		for (int i = 0; i < colnames.size(); i++)
		{
		    String value = rs.getString(colnames.get(i).toString());
		    if (value == null)
		    {
			value = "";
		    }
		    currRec.put(colnames.get(i).toString(), value);
		}
		retval.add(currRec);
	    }
	    rs.close();
	    sqlStatement.close();
	} catch (Exception e)
	{
	    e.printStackTrace();
	}
	return retval;
    }

    /**
     * Fetches ALL the columns of a query resultset
     * 
     * @param rs
     * @return
     */
    private LinkedList<String> getAllColumns(ResultSet rs)
    {
	LinkedList<String> colList = new LinkedList<String>();
	try
	{
	    ResultSetMetaData rsmd = rs.getMetaData();
	    int columnCount = rsmd.getColumnCount();
	    for (int i = 1; i <= columnCount; i++)
	    {
		String colName = rsmd.getColumnLabel(i);
		colList.add(colName);
	    }
	} catch (SQLException e)
	{
	    e.printStackTrace();
	}

	return colList;
    }


	// Executes the query and returns the records
    public JsonNode getResult(String query)
    {
	try
	{
	    QueryRunner run = new QueryRunner(db.getDataSource());
	    List<Map<String, Object>> maps = run.query(query, new MapListHandler());
	    return Json.toJson(maps);
	} catch (SQLException e)
	{
	    e.printStackTrace();
	}
	return null;
}

    public void closeConnection()
    {
	try
	{
	    this.conn.close();
	} catch (Exception e)
	{
	    e.printStackTrace();
	}
    }
}