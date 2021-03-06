/*
 * Copyright 2012-2016 CodeLibs Project and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.codelibs.fess.app.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.codelibs.core.beans.util.BeanUtil;
import org.codelibs.fess.Constants;
import org.codelibs.fess.app.pager.WebConfigPager;
import org.codelibs.fess.es.config.cbean.WebConfigCB;
import org.codelibs.fess.es.config.exbhv.RequestHeaderBhv;
import org.codelibs.fess.es.config.exbhv.WebAuthenticationBhv;
import org.codelibs.fess.es.config.exbhv.WebConfigBhv;
import org.codelibs.fess.es.config.exbhv.WebConfigToLabelBhv;
import org.codelibs.fess.es.config.exentity.WebConfig;
import org.codelibs.fess.es.config.exentity.WebConfigToLabel;
import org.codelibs.fess.mylasta.direction.FessConfig;
import org.dbflute.cbean.result.PagingResultBean;
import org.dbflute.optional.OptionalEntity;

public class WebConfigService implements Serializable {

    private static final long serialVersionUID = 1L;

    @Resource
    protected WebConfigToLabelBhv webConfigToLabelBhv;

    @Resource
    protected WebConfigBhv webConfigBhv;

    @Resource
    protected WebAuthenticationBhv webAuthenticationBhv;

    @Resource
    protected RequestHeaderBhv requestHeaderBhv;

    @Resource
    protected FessConfig fessConfig;

    public List<WebConfig> getWebConfigList(final WebConfigPager webConfigPager) {

        final PagingResultBean<WebConfig> webConfigList = webConfigBhv.selectPage(cb -> {
            cb.paging(webConfigPager.getPageSize(), webConfigPager.getCurrentPageNumber());
            setupListCondition(cb, webConfigPager);
        });

        // update pager
        BeanUtil.copyBeanToBean(webConfigList, webConfigPager, option -> option.include(Constants.PAGER_CONVERSION_RULE));
        webConfigPager.setPageNumberList(webConfigList.pageRange(op -> {
            op.rangeSize(5);
        }).createPageNumberList());

        return webConfigList;
    }

    public void delete(final WebConfig webConfig) {

        final String webConfigId = webConfig.getId();

        webConfigBhv.delete(webConfig, op -> {
            op.setRefresh(true);
        });

        webConfigToLabelBhv.queryDelete(cb -> {
            cb.query().setWebConfigId_Equal(webConfigId);
        });

        webAuthenticationBhv.queryDelete(cb -> {
            cb.query().setWebConfigId_Equal(webConfigId);
        });

        requestHeaderBhv.queryDelete(cb -> {
            cb.query().setWebConfigId_Equal(webConfigId);
        });
    }

    public List<WebConfig> getAllWebConfigList() {
        return getAllWebConfigList(true, true, true, null);
    }

    public List<WebConfig> getWebConfigListByIds(final List<String> idList) {
        if (idList == null) {
            return getAllWebConfigList();
        } else {
            return getAllWebConfigList(true, true, false, idList);
        }
    }

    public List<WebConfig> getAllWebConfigList(final boolean withLabelType, final boolean withRoleType, final boolean available,
            final List<String> idList) {
        final List<WebConfig> list = webConfigBhv.selectList(cb -> {
            if (available) {
                cb.query().setAvailable_Equal(Constants.T);
            }
            if (idList != null) {
                cb.query().setId_InScope(idList);
            }
            cb.fetchFirst(fessConfig.getPageWebConfigMaxFetchSizeAsInteger());
        });

        return list;
    }

    public OptionalEntity<WebConfig> getWebConfig(final String id) {
        return webConfigBhv.selectByPK(id).map(entity -> {

            final List<WebConfigToLabel> wctltmList = webConfigToLabelBhv.selectList(wctltmCb -> {
                wctltmCb.query().setWebConfigId_Equal(entity.getId());
                wctltmCb.fetchFirst(fessConfig.getPageLabeltypeMaxFetchSizeAsInteger());
            });
            if (!wctltmList.isEmpty()) {
                final List<String> labelTypeIds = new ArrayList<>(wctltmList.size());
                for (final WebConfigToLabel mapping : wctltmList) {
                    labelTypeIds.add(mapping.getLabelTypeId());
                }
                entity.setLabelTypeIds(labelTypeIds.toArray(new String[labelTypeIds.size()]));
            }
            return entity;
        });
    }

    public void store(final WebConfig webConfig) {
        final boolean isNew = webConfig.getId() == null;
        final String[] labelTypeIds = webConfig.getLabelTypeIds();

        webConfigBhv.insertOrUpdate(webConfig, op -> {
            op.setRefresh(true);
        });
        final String webConfigId = webConfig.getId();
        if (isNew) {
            // Insert
            if (labelTypeIds != null) {
                final List<WebConfigToLabel> wctltmList = new ArrayList<>();
                for (final String id : labelTypeIds) {
                    final WebConfigToLabel mapping = new WebConfigToLabel();
                    mapping.setWebConfigId(webConfigId);
                    mapping.setLabelTypeId(id);
                    wctltmList.add(mapping);
                }
                webConfigToLabelBhv.batchInsert(wctltmList, op -> {
                    op.setRefresh(true);
                });
            }
        } else {
            // Update
            if (labelTypeIds != null) {
                final List<WebConfigToLabel> list = webConfigToLabelBhv.selectList(wctltmCb -> {
                    wctltmCb.query().setWebConfigId_Equal(webConfigId);
                    wctltmCb.fetchFirst(fessConfig.getPageLabeltypeMaxFetchSizeAsInteger());
                });
                final List<WebConfigToLabel> newList = new ArrayList<>();
                final List<WebConfigToLabel> matchedList = new ArrayList<>();
                for (final String id : labelTypeIds) {
                    boolean exist = false;
                    for (final WebConfigToLabel mapping : list) {
                        if (mapping.getLabelTypeId().equals(id)) {
                            exist = true;
                            matchedList.add(mapping);
                            break;
                        }
                    }
                    if (!exist) {
                        // new
                        final WebConfigToLabel mapping = new WebConfigToLabel();
                        mapping.setWebConfigId(webConfigId);
                        mapping.setLabelTypeId(id);
                        newList.add(mapping);
                    }
                }
                list.removeAll(matchedList);
                webConfigToLabelBhv.batchInsert(newList, op -> {
                    op.setRefresh(true);
                });
                webConfigToLabelBhv.batchDelete(list, op -> {
                    op.setRefresh(true);
                });
            }
        }
    }

    protected void setupListCondition(final WebConfigCB cb, final WebConfigPager webConfigPager) {
        if (webConfigPager.id != null) {
            cb.query().docMeta().setId_Equal(webConfigPager.id);
        }
        // TODO Long, Integer, String supported only.

        // setup condition
        cb.query().addOrderBy_SortOrder_Asc();
        cb.query().addOrderBy_Name_Asc();

        // search

    }

}
