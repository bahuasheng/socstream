/*
  The MIT License (MIT)

  Copyright (c) 2017 Giacomo Marciani and Michele Porretta

  Permission is hereby granted, free of charge, to any person obtaining a copy
  of this software and associated documentation files (the "Software"), to deal
  in the Software without restriction, including without limitation the rights
  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
  copies of the Software, and to permit persons to whom the Software is
  furnished to do so, subject to the following conditions:


  The above copyright notice and this permission notice shall be included in
  all copies or substantial portions of the Software.


  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.  IN NO EVENT SHALL THE
  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
  THE SOFTWARE.
 */
package com.acmutv.socstream.common.meta;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import org.apache.avro.generic.GenericData;

import java.util.ArrayList;
import java.util.List;

/**
 * A monitored person.
 *
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Michele Porretta {@literal <mporretta@acm.org>}
 * @since 1.0
 */
@Data
@AllArgsConstructor
public class Person {

  /**
   * Person's name.
   */
  @NonNull
  private String name;

  /**
   * The id of the sensor placed on the left leg.
   */
  private Long legLeft;

  /**
   * The id of the sensor placed on the right leg.
   */
  private Long legRight;

  /**
   * The id of the sensor placed on the left arm.
   */
  private Long armLeft;

  /**
   * The id of the sensor placed on the right arm.
   */
  private Long armRight;

  public Person(String name) {
    this.name = name;
  }

  /**
   * Returns the array of all sensors id.
   * @return the array of all sensors id.
   */
  public List<Long> getAllSensors() {
    List<Long> sensors = new ArrayList<>();

    sensors.add(this.getLegLeft());
    sensors.add(this.getLegRight());
    sensors.add(this.getArmLeft());
    sensors.add(this.getArmRight());

    return sensors;
  }

}