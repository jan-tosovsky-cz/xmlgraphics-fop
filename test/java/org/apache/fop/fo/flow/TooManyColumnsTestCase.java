/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/* $Id$ */

package org.apache.fop.fo.flow;

import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.fo.FOEventHandler;
import org.apache.fop.fo.ValidationException;
import org.apache.fop.fotreetest.FOTreeUnitTester;

public class TooManyColumnsTestCase extends FOTreeUnitTester {

    private FOTreeUnitTester.FOEventHandlerFactory tableHandlerFactory;

    public TooManyColumnsTestCase() throws Exception {
        super();
        tableHandlerFactory = new FOTreeUnitTester.FOEventHandlerFactory() {
            public FOEventHandler createFOEventHandler(FOUserAgent ua) {
                return new TableHandler(ua);
            }
        };
    }

    private void launchTest(String filename) throws Exception {
        try {
            setUp(filename, tableHandlerFactory);
            fail();
        } catch (ValidationException e) {
            // TODO check location
        }
    }

    public void testBody1() throws Exception {
        launchTest("table/too-many-columns_body_1.fo");
    }

    public void testBody2() throws Exception {
        launchTest("table/too-many-columns_body_2.fo");
    }

    public void testBody3() throws Exception {
        launchTest("table/too-many-columns_body_3.fo");
    }

    public void testHeader() throws Exception {
        launchTest("table/too-many-columns_header.fo");
    }

    public void testFooter() throws Exception {
        launchTest("table/too-many-columns_footer.fo");
    }
}