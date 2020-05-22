/*
 * Copyright 2012 LinkedIn Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package azkaban.executor;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

/**
 * Class to represent events on Azkaban executors
 *
 * @author gaggarwa
 */
@Data
public class ExecutorLogEvent {

  private final int executionId;
  private final int id;
  private final String user;
  private final Date time;
  private final EventType type;
  private final String message;
  
  
  /**
   * Log event type messages. Do not change the numeric representation of each enum. Only represent
   * from 0 to 255 different codes.
   */
  @AllArgsConstructor @Getter
  public enum EventType {
    ERROR(128), HOST_UPDATE(1), PORT_UPDATE(2), ACTIVATION(3), INACTIVATION(4),
    CREATED(5), TRIGGER_COUNT(6), SOURCE_COUNT(7);

    private final int numVal;

    public static EventType fromInteger(final int x)
        throws IllegalArgumentException {
      switch (x) {
        case 1:
          return HOST_UPDATE;
        case 2:
          return PORT_UPDATE;
        case 3:
          return ACTIVATION;
        case 4:
          return INACTIVATION;
        case 5:
          return CREATED;
        case 6:
            return TRIGGER_COUNT;
        case 7:
            return SOURCE_COUNT;
        case 128:
          return ERROR;
        default:
          throw new IllegalArgumentException(String.format(
              "inalid status code %d", x));
      }
    }

  }
}
