// Copyright 2013 Google Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.enterprise.connector.db;

import com.google.enterprise.connector.db.diffing.DigestContentHolder;

import java.sql.CallableStatement;
import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

public class BlobTypeHandler extends LobTypeHandler<Blob> {
  private static final Logger LOGGER =
      Logger.getLogger(BlobTypeHandler.class.getName());

  @Override
  public DigestContentHolder getNullableResult(ResultSet rs, String columnName)
      throws SQLException {
    return getContentHolder(rs.getBlob(columnName));
  }

  @Override
  public DigestContentHolder getNullableResult(ResultSet rs, int columnIndex)
      throws SQLException {
    return getContentHolder(rs.getBlob(columnIndex));
  }

  @Override
  public DigestContentHolder getNullableResult(CallableStatement cs, 
      int columnIndex) throws SQLException {
    return getContentHolder(cs.getBlob(columnIndex));
  }
  
  @Override
  protected byte[] getBytes(Blob blob) throws SQLException {
    LOGGER.finest("BLOB handler called with BLOB of length " + blob.length());
    return blob.getBytes(1, (int) blob.length());
  }
}
