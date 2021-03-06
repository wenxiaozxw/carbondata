/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.carbondata.core.scan.expression.conditional;

import org.apache.carbondata.core.metadata.datatype.DataType;
import org.apache.carbondata.core.metadata.datatype.DataTypes;
import org.apache.carbondata.core.scan.expression.Expression;
import org.apache.carbondata.core.scan.expression.ExpressionResult;
import org.apache.carbondata.core.scan.expression.exception.FilterIllegalMemberException;
import org.apache.carbondata.core.scan.expression.exception.FilterUnsupportedException;
import org.apache.carbondata.core.scan.filter.intf.ExpressionType;
import org.apache.carbondata.core.scan.filter.intf.RowIntf;

public class LessThanEqualToExpression extends BinaryConditionalExpression {
  private static final long serialVersionUID = 1L;

  public LessThanEqualToExpression(Expression left, Expression right) {
    super(left, right);
  }

  public ExpressionResult evaluate(RowIntf value)
      throws FilterUnsupportedException, FilterIllegalMemberException {
    ExpressionResult elRes = left.evaluate(value);
    ExpressionResult erRes = right.evaluate(value);
    ExpressionResult exprResValue1 = elRes;
    if (elRes.isNull() || erRes.isNull()) {
      elRes.set(DataTypes.BOOLEAN, false);
      return elRes;
    }
    if (elRes.getDataType() != erRes.getDataType()) {
      if (elRes.getDataType().getPrecedenceOrder() < erRes.getDataType().getPrecedenceOrder()) {
        exprResValue1 = erRes;
      }

    }
    boolean result = false;
    DataType dataType = exprResValue1.getDataType();
    if (dataType == DataTypes.BOOLEAN) {
      result = elRes.getBoolean().compareTo(erRes.getBoolean()) <= 0;
    } else if (dataType == DataTypes.STRING) {
      result = elRes.getString().compareTo(erRes.getString()) <= 0;
    } else if (dataType == DataTypes.SHORT) {
      result = elRes.getShort() <= (erRes.getShort());
    } else if (dataType == DataTypes.INT) {
      result = elRes.getInt() <= (erRes.getInt());
    } else if (dataType == DataTypes.DOUBLE) {
      result = elRes.getDouble() <= (erRes.getDouble());
    } else if (dataType == DataTypes.DATE || dataType == DataTypes.TIMESTAMP) {
      result = elRes.getTime() <= (erRes.getTime());
    } else if (dataType == DataTypes.LONG) {
      result = elRes.getLong() <= (erRes.getLong());
    } else if (DataTypes.isDecimal(dataType)) {
      result = elRes.getDecimal().compareTo(erRes.getDecimal()) <= 0;
    } else {
      throw new FilterUnsupportedException("DataType: " + exprResValue1.getDataType()
          + " not supported for the filter expression");
    }
    exprResValue1.set(DataTypes.BOOLEAN, result);
    return exprResValue1;
  }

  @Override public ExpressionType getFilterExpressionType() {
    // TODO Auto-generated method stub
    return ExpressionType.LESSTHAN_EQUALTO;
  }

  @Override public String getString() {
    return "LessThanEqualTo(" + left.getString() + ',' + right.getString() + ')';
  }

}
