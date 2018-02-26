package com.loco.api;

import play.libs.Json;
import play.mvc.*;
import play.db.*;

import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Connection;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapListHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.inject.Inject;
import javax.inject.Singleton;

import views.html.*;

import com.loco.api.ApiController;
import com.loco.data.SystemDatabase;
import com.loco.data.Tables;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
@Singleton
public class ApiSystem extends ApiController {
    SystemDatabase sdb;
    @Inject
    public ApiSystem(SystemDatabase sdb)
    {
        this.sdb = sdb;
    }

    /**
     * Find out the active user 
     * based on active column value true in the db
     */
    public String getActiveUser()
    {
        HashMap<String, String> condition = new HashMap<String, String>();
        condition.put(Tables.COLUMN_USERS_ACTIVE,"true");
        ArrayNode activeUserArray = this.sdb.getAllRecords(Tables.TABLE_USERS, condition);
        return activeUserArray.get(0).get("name").asText();
    }

    /**
     * Method for the api end point to get all the comments present in 
     * the db
     */
    public Result getAllComments()
    {

        if (!(request().body().asJson() == null))
        {
            this.sdb.insertRecord(request().body().asJson(),Tables.TABLE_COMMENTS);
        }

        JsonNode retval = new ObjectMapper().createObjectNode();

        HashMap<String, String> parentCommentCondition = new HashMap<String, String>();
        parentCommentCondition.put(Tables.COLUMN_COMMENTS_PARENTCOMMENTID,"0");
		ArrayNode allParentComments = this.sdb.getAllRecords(Tables.TABLE_COMMENTS, parentCommentCondition);

        allParentComments = this.getReplyComments(allParentComments);
        
        ((ObjectNode)retval).put("active_user",this.getActiveUser());
        ((ObjectNode)retval).put("all_comments",allParentComments);

        return ok(retval);
    }

    /**
     * Method deletes the comment itself and 
     * all the replies to that comment from db and
     * return the remaining comments in the db
     */
    public Result deleteComment(String commentId)
    {

        //delete whole hierarchy of replies
		HashMap<String, String> parentCommentCondition = new HashMap<String, String>();
        parentCommentCondition.put(Tables.COLUMN_COMMENTS_PARENTCOMMENTID,commentId);
        ArrayNode allParentComments = this.sdb.getAllRecords(Tables.TABLE_COMMENTS, parentCommentCondition);
        
        if(allParentComments.size() > 0)
        {
            for(int ctr = 0; ctr < allParentComments.size(); ctr++)
            {
                this.deleteComment(allParentComments.get(ctr).get("id").asText());
            }
        }
        HashMap<String, String> deleteCondition = new HashMap<String, String>();
        // deleteCondition.put("parentCommentId",commentId);
        deleteCondition.put("id",commentId);

        this.sdb.deleteAllRecords(Tables.TABLE_COMMENTS,deleteCondition);

        return this.getAllComments();
    }

    /**
     * Method constructs the hierarchy of replies to all the main
     * comments and returns the whole comments tree
     */
    public ArrayNode getReplyComments(ArrayNode allParentComments)
    {

        for(int ctr=0; ctr < allParentComments.size(); ctr++)
        {
            String id = allParentComments.get(ctr).get("id").asText();
            HashMap<String, String> replyCondition = new HashMap<String, String>();
            replyCondition.put("parentCommentId",id);
    
            ArrayNode allRepliesToComment = this.sdb.getAllRecords(Tables.TABLE_COMMENTS, replyCondition);

            ((ObjectNode)allParentComments.get(ctr)).set("replies",allRepliesToComment);

            getReplyComments(allRepliesToComment);
        }

        return allParentComments;
    }
    public void addComment(JsonNode comment)
    {

    }

}