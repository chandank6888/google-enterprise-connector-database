//Copyright 2011 Google Inc.
//
//Licensed under the Apache License, Version 2.0 (the "License");
//you may not use this file except in compliance with the License.
//You may obtain a copy of the License at
//
//http://www.apache.org/licenses/LICENSE-2.0
//
//Unless required by applicable law or agreed to in writing, software
//distributed under the License is distributed on an "AS IS" BASIS,
//WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//See the License for the specific language governing permissions and
//limitations under the License.

package com.google.enterprise.connector.db;

import com.google.enterprise.connector.db.diffing.DBClient;
import com.google.enterprise.connector.db.diffing.DBContext;
import com.google.enterprise.connector.db.diffing.DBException;
import com.google.enterprise.connector.db.diffing.Util;
import com.google.enterprise.connector.spi.RepositoryException;
import com.google.enterprise.connector.traversal.ProductionTraversalContext;

import com.ibatis.common.jdbc.ScriptRunner;
import com.ibatis.common.resources.Resources;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

/**
 * This is a base class for all test classes that requires interaction with
 * database. This provide methods to interact with database.
 */
public abstract class DBTestBase extends TestCase {

    private Map<String, String> configMap = new HashMap<String, String>();

    public static final String CREATE_TEST_DB_TABLE = "com/google/enterprise/connector/db/config/createTable.sql";
    public static final String LOAD_TEST_DATA = "com/google/enterprise/connector/db/config/loadTestData.sql";
    public static final String TRUNCATE_TEST_DB_TABLE = "com/google/enterprise/connector/db/config/truncateTestTable.sql";
    public static final String DROP_TEST_DB_TABLE = "com/google/enterprise/connector/db/config/dropdb.sql";

    public static final String CREATE_USER_DOC_MAP_TABLE = "com/google/enterprise/connector/db/config/createUerDocMapTable.sql";
    public static final String LOAD_USER_DOC_MAP_TEST_DATA = "com/google/enterprise/connector/db/config/loadUerDocMapTable.sql";
    public static final String DROP_USER_DOC_MAP_TABLE = "com/google/enterprise/connector/db/config/dropUserDocMapTable.sql";

    @Override
    protected void setUp() throws Exception {
		TestDirectoryManager testDirManager = new TestDirectoryManager(this);
        configMap.put("login", LanguageResource.getPropertyValue("login"));
        configMap.put("password", LanguageResource.getPropertyValue("password"));
        configMap.put("connectionUrl", LanguageResource.getPropertyValue("connectionUrl"));
        configMap.put("dbName", LanguageResource.getPropertyValue("dbName"));
        configMap.put("hostname", LanguageResource.getPropertyValue("hostname"));
        configMap.put("driverClassName", LanguageResource.getPropertyValue("driverClassName"));
        configMap.put("sqlQuery", LanguageResource.getPropertyValue("sqlQuery"));
        configMap.put("primaryKeysString", LanguageResource.getPropertyValue("primaryKeysString"));
		configMap.put("googleConnectorWorkDir", testDirManager.getTmpDir());
        configMap.put("xslt", "");
        configMap.put("authZQuery", LanguageResource.getPropertyValue("authZQuery"));
        configMap.put("lastModifiedDate", "");
        configMap.put("documentTitle", "");
        configMap.put("externalMetadata", "");
        configMap.put("documentURLField", "");
        configMap.put("documentIdField", "");
        configMap.put("baseURL", "");
        configMap.put("lobField", "");
        configMap.put("fetchURLField", "");
        configMap.put("extMetadataType", "");

    }

	protected ProductionTraversalContext getProductionTraversalContext() {
		ProductionTraversalContext context = new ProductionTraversalContext();
		return context;
	}

    protected String getXslt() {
		return configMap.get("xslt");
	}

    protected DBClient getDbClient()
 throws RepositoryException {

        DBContext dbContext;
        DBClient dbClient;
        try {

            dbContext = new DBContext(configMap.get("connectionUrl"),
                    configMap.get("hostname"),
                    configMap.get("driverClassName"), configMap.get("login"),
                    configMap.get("password"), configMap.get("dbName"),
                    configMap.get("lastModifiedDate"),
                    configMap.get("documentTitle"),
                    configMap.get("documentURLField"),
                    configMap.get("documentIdField"), configMap.get("baseURL"),
                    configMap.get("lobField"), configMap.get("fetchURLField"),
                    configMap.get("extMetadataType"));

            dbClient = new DBClient(
                    dbContext,
                    configMap.get("sqlQuery"),
                    configMap.get("googleConnectorWorkDir"),
                    configMap.get("primaryKeysString").split(Util.PRIMARY_KEYS_SEPARATOR),
					configMap.get("authZQuery"));
            return dbClient;
        } catch (DBException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }

    /* @Override */
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * This method executes the database script.
     *
     * @param scriptPath path of SQL script file
     */
    protected void runDBScript(String scriptPath) {
        Connection connection = null;
        try {
			connection = getDbClient().getSqlMapClient().getDataSource().getConnection();
            ScriptRunner runner = new ScriptRunner(connection, false, true);
            runner.runScript(Resources.getResourceAsReader(scriptPath));
        } catch (SQLException se) {
            fail("SQLException is occured while closing database connection"
                    + se.toString());
        } catch (RepositoryException re) {
            fail("RepositoryException is occured while closing database connection"
                    + re.toString());
        } catch (IOException ioe) {
            fail("IOException is occured while closing database connection"
                    + ioe.toString());
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException se) {
                    fail("SQLException is occured while closing database connection"
                            + se.toString());
                }
            }
        }
    }

}
