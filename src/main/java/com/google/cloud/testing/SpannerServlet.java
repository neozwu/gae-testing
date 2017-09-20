/*
 * Copyright 2017 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.cloud.testing;

import com.google.cloud.spanner.DatabaseClient;
import com.google.cloud.spanner.DatabaseId;
import com.google.cloud.spanner.ResultSet;
import com.google.cloud.spanner.Spanner;
import com.google.cloud.spanner.SpannerOptions;
import com.google.cloud.spanner.Statement;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "spannerServlet", value = "/spanner")
public class SpannerServlet extends HttpServlet {
  @Override
  public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    PrintWriter out = resp.getWriter();
    // Instantiates a client
    SpannerOptions options = SpannerOptions.newBuilder().build();
    Spanner spanner = options.getService();
    String instance = "neowu-spanner-test";
    String database = "neowu-spanner-test-db";
    try {
      // Creates a database client
      DatabaseClient dbClient = spanner.getDatabaseClient(
              DatabaseId.of(options.getProjectId(), instance, database));
      // Queries the database
      ResultSet resultSet = dbClient.singleUse().executeQuery(Statement.of("SELECT * from users"));
      // Prints the results
      while (resultSet.next()) {
        out.append("id: " + resultSet.getLong(0)
                + " username: "
                + resultSet.getString(1)+ "\n\n");
      }
      out.append("End of resultSet\n");
    } finally {
      // Closes the client which will free up the resources used
      spanner.close();
    }
    out.close();
  }
}
