/*
 * Copyright 2020 Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.fop.layoutmgr;

import java.util.ArrayList;
import java.util.List;
import org.apache.fop.area.IndexKeyReferenceInfo;
import org.apache.fop.fo.FONode;
import org.apache.fop.fo.flow.IndexRangeBegin;
import org.apache.fop.fo.flow.IndexRangeEnd;
import org.apache.fop.fo.properties.Indexable;

public class IndexKeyReferenceSetter {

    public static void add(PageSequenceLayoutManager pslm, Indexable fObj) {

        String indexKey = fObj.getIndexKey();
        int nameId = ((FONode) fObj).getNameId();

        if (!pslm.getIndexKeyReferenceInfoMap().containsKey(indexKey)) {
            pslm.getIndexKeyReferenceInfoMap().put(indexKey, new ArrayList<IndexKeyReferenceInfo>());
        }

        List<IndexKeyReferenceInfo> indexKeyList = pslm.getIndexKeyReferenceInfoMap().get(indexKey);
        indexKeyList.add(new IndexKeyReferenceInfo(getId(fObj), nameId, fObj.getIndexClass()));
    }

    private static String getId(Indexable fObj) {

        if (fObj instanceof IndexRangeBegin) {
            return ((IndexRangeBegin) fObj).getId();

        } else if (fObj instanceof IndexRangeEnd) {
            return ((IndexRangeEnd) fObj).getRefId();
        }

        return fObj.getIndexKey();
    }

}
