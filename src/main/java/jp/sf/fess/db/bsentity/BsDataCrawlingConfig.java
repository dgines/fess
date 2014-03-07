/*
 * Copyright 2009-2014 the CodeLibs Project and the Others.
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

package jp.sf.fess.db.bsentity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import jp.sf.fess.db.allcommon.DBMetaInstanceHandler;
import jp.sf.fess.db.exentity.DataConfigToLabelTypeMapping;
import jp.sf.fess.db.exentity.DataConfigToRoleTypeMapping;
import jp.sf.fess.db.exentity.DataCrawlingConfig;

import org.seasar.dbflute.Entity;
import org.seasar.dbflute.dbmeta.DBMeta;

/**
 * The entity of DATA_CRAWLING_CONFIG as TABLE. <br />
 * <pre>
 * [primary-key]
 *     ID
 * 
 * [column]
 *     ID, NAME, HANDLER_NAME, HANDLER_PARAMETER, HANDLER_SCRIPT, BOOST, AVAILABLE, SORT_ORDER, CREATED_BY, CREATED_TIME, UPDATED_BY, UPDATED_TIME, DELETED_BY, DELETED_TIME, VERSION_NO
 * 
 * [sequence]
 *     
 * 
 * [identity]
 *     ID
 * 
 * [version-no]
 *     VERSION_NO
 * 
 * [foreign table]
 *     
 * 
 * [referrer table]
 *     DATA_CONFIG_TO_LABEL_TYPE_MAPPING, DATA_CONFIG_TO_ROLE_TYPE_MAPPING
 * 
 * [foreign property]
 *     
 * 
 * [referrer property]
 *     dataConfigToLabelTypeMappingList, dataConfigToRoleTypeMappingList
 * 
 * [get/set template]
 * /= = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = =
 * Long id = entity.getId();
 * String name = entity.getName();
 * String handlerName = entity.getHandlerName();
 * String handlerParameter = entity.getHandlerParameter();
 * String handlerScript = entity.getHandlerScript();
 * java.math.BigDecimal boost = entity.getBoost();
 * String available = entity.getAvailable();
 * Integer sortOrder = entity.getSortOrder();
 * String createdBy = entity.getCreatedBy();
 * java.sql.Timestamp createdTime = entity.getCreatedTime();
 * String updatedBy = entity.getUpdatedBy();
 * java.sql.Timestamp updatedTime = entity.getUpdatedTime();
 * String deletedBy = entity.getDeletedBy();
 * java.sql.Timestamp deletedTime = entity.getDeletedTime();
 * Integer versionNo = entity.getVersionNo();
 * entity.setId(id);
 * entity.setName(name);
 * entity.setHandlerName(handlerName);
 * entity.setHandlerParameter(handlerParameter);
 * entity.setHandlerScript(handlerScript);
 * entity.setBoost(boost);
 * entity.setAvailable(available);
 * entity.setSortOrder(sortOrder);
 * entity.setCreatedBy(createdBy);
 * entity.setCreatedTime(createdTime);
 * entity.setUpdatedBy(updatedBy);
 * entity.setUpdatedTime(updatedTime);
 * entity.setDeletedBy(deletedBy);
 * entity.setDeletedTime(deletedTime);
 * entity.setVersionNo(versionNo);
 * = = = = = = = = = =/
 * </pre>
 * @author DBFlute(AutoGenerator)
 */
public abstract class BsDataCrawlingConfig implements Entity, Serializable,
        Cloneable {

    // ===================================================================================
    //                                                                          Definition
    //                                                                          ==========
    /** Serial version UID. (Default) */
    private static final long serialVersionUID = 1L;

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    // -----------------------------------------------------
    //                                                Column
    //                                                ------
    /** ID: {PK, ID, NotNull, BIGINT(19)} */
    protected Long _id;

    /** NAME: {NotNull, VARCHAR(200)} */
    protected String _name;

    /** HANDLER_NAME: {NotNull, VARCHAR(200)} */
    protected String _handlerName;

    /** HANDLER_PARAMETER: {VARCHAR(4000)} */
    protected String _handlerParameter;

    /** HANDLER_SCRIPT: {VARCHAR(4000)} */
    protected String _handlerScript;

    /** BOOST: {NotNull, DOUBLE(17)} */
    protected java.math.BigDecimal _boost;

    /** AVAILABLE: {NotNull, VARCHAR(1)} */
    protected String _available;

    /** SORT_ORDER: {NotNull, INTEGER(10)} */
    protected Integer _sortOrder;

    /** CREATED_BY: {NotNull, VARCHAR(255)} */
    protected String _createdBy;

    /** CREATED_TIME: {NotNull, TIMESTAMP(23, 10)} */
    protected java.sql.Timestamp _createdTime;

    /** UPDATED_BY: {VARCHAR(255)} */
    protected String _updatedBy;

    /** UPDATED_TIME: {TIMESTAMP(23, 10)} */
    protected java.sql.Timestamp _updatedTime;

    /** DELETED_BY: {VARCHAR(255)} */
    protected String _deletedBy;

    /** DELETED_TIME: {TIMESTAMP(23, 10)} */
    protected java.sql.Timestamp _deletedTime;

    /** VERSION_NO: {NotNull, INTEGER(10)} */
    protected Integer _versionNo;

    // -----------------------------------------------------
    //                                              Internal
    //                                              --------
    /** The modified properties for this entity. (NotNull) */
    protected final EntityModifiedProperties __modifiedProperties = newModifiedProperties();

    // ===================================================================================
    //                                                                          Table Name
    //                                                                          ==========
    /**
     * {@inheritDoc}
     */
    @Override
    public String getTableDbName() {
        return "DATA_CRAWLING_CONFIG";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTablePropertyName() { // according to Java Beans rule
        return "dataCrawlingConfig";
    }

    // ===================================================================================
    //                                                                              DBMeta
    //                                                                              ======
    /**
     * {@inheritDoc}
     */
    @Override
    public DBMeta getDBMeta() {
        return DBMetaInstanceHandler.findDBMeta(getTableDbName());
    }

    // ===================================================================================
    //                                                                         Primary Key
    //                                                                         ===========
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasPrimaryKeyValue() {
        if (getId() == null) {
            return false;
        }
        return true;
    }

    // ===================================================================================
    //                                                                    Foreign Property
    //                                                                    ================
    // ===================================================================================
    //                                                                   Referrer Property
    //                                                                   =================
    /** DATA_CONFIG_TO_LABEL_TYPE_MAPPING by DATA_CONFIG_ID, named 'dataConfigToLabelTypeMappingList'. */
    protected List<DataConfigToLabelTypeMapping> _dataConfigToLabelTypeMappingList;

    /**
     * DATA_CONFIG_TO_LABEL_TYPE_MAPPING by DATA_CONFIG_ID, named 'dataConfigToLabelTypeMappingList'.
     * @return The entity list of referrer property 'dataConfigToLabelTypeMappingList'. (NotNull: even if no loading, returns empty list)
     */
    public List<DataConfigToLabelTypeMapping> getDataConfigToLabelTypeMappingList() {
        if (_dataConfigToLabelTypeMappingList == null) {
            _dataConfigToLabelTypeMappingList = newReferrerList();
        }
        return _dataConfigToLabelTypeMappingList;
    }

    /**
     * DATA_CONFIG_TO_LABEL_TYPE_MAPPING by DATA_CONFIG_ID, named 'dataConfigToLabelTypeMappingList'.
     * @param dataConfigToLabelTypeMappingList The entity list of referrer property 'dataConfigToLabelTypeMappingList'. (NullAllowed)
     */
    public void setDataConfigToLabelTypeMappingList(
            final List<DataConfigToLabelTypeMapping> dataConfigToLabelTypeMappingList) {
        _dataConfigToLabelTypeMappingList = dataConfigToLabelTypeMappingList;
    }

    /** DATA_CONFIG_TO_ROLE_TYPE_MAPPING by DATA_CONFIG_ID, named 'dataConfigToRoleTypeMappingList'. */
    protected List<DataConfigToRoleTypeMapping> _dataConfigToRoleTypeMappingList;

    /**
     * DATA_CONFIG_TO_ROLE_TYPE_MAPPING by DATA_CONFIG_ID, named 'dataConfigToRoleTypeMappingList'.
     * @return The entity list of referrer property 'dataConfigToRoleTypeMappingList'. (NotNull: even if no loading, returns empty list)
     */
    public List<DataConfigToRoleTypeMapping> getDataConfigToRoleTypeMappingList() {
        if (_dataConfigToRoleTypeMappingList == null) {
            _dataConfigToRoleTypeMappingList = newReferrerList();
        }
        return _dataConfigToRoleTypeMappingList;
    }

    /**
     * DATA_CONFIG_TO_ROLE_TYPE_MAPPING by DATA_CONFIG_ID, named 'dataConfigToRoleTypeMappingList'.
     * @param dataConfigToRoleTypeMappingList The entity list of referrer property 'dataConfigToRoleTypeMappingList'. (NullAllowed)
     */
    public void setDataConfigToRoleTypeMappingList(
            final List<DataConfigToRoleTypeMapping> dataConfigToRoleTypeMappingList) {
        _dataConfigToRoleTypeMappingList = dataConfigToRoleTypeMappingList;
    }

    protected <ELEMENT> List<ELEMENT> newReferrerList() {
        return new ArrayList<ELEMENT>();
    }

    // ===================================================================================
    //                                                                 Modified Properties
    //                                                                 ===================
    /**
     * {@inheritDoc}
     */
    @Override
    public Set<String> modifiedProperties() {
        return __modifiedProperties.getPropertyNames();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clearModifiedInfo() {
        __modifiedProperties.clear();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasModification() {
        return !__modifiedProperties.isEmpty();
    }

    protected EntityModifiedProperties newModifiedProperties() {
        return new EntityModifiedProperties();
    }

    // ===================================================================================
    //                                                                      Basic Override
    //                                                                      ==============
    /**
     * Determine the object is equal with this. <br />
     * If primary-keys or columns of the other are same as this one, returns true.
     * @param other The other entity. (NullAllowed: if null, returns false fixedly)
     * @return Comparing result.
     */
    @Override
    public boolean equals(final Object other) {
        if (other == null || !(other instanceof BsDataCrawlingConfig)) {
            return false;
        }
        final BsDataCrawlingConfig otherEntity = (BsDataCrawlingConfig) other;
        if (!xSV(getId(), otherEntity.getId())) {
            return false;
        }
        return true;
    }

    protected boolean xSV(final Object value1, final Object value2) { // isSameValue()
        return InternalUtil.isSameValue(value1, value2);
    }

    /**
     * Calculate the hash-code from primary-keys or columns.
     * @return The hash-code from primary-key or columns.
     */
    @Override
    public int hashCode() {
        int result = 17;
        result = xCH(result, getTableDbName());
        result = xCH(result, getId());
        return result;
    }

    protected int xCH(final int result, final Object value) { // calculateHashcode()
        return InternalUtil.calculateHashcode(result, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int instanceHash() {
        return super.hashCode();
    }

    /**
     * Convert to display string of entity's data. (no relation data)
     * @return The display string of all columns and relation existences. (NotNull)
     */
    @Override
    public String toString() {
        return buildDisplayString(InternalUtil.toClassTitle(this), true, true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toStringWithRelation() {
        final StringBuilder sb = new StringBuilder();
        sb.append(toString());
        final String l = "\n  ";
        if (_dataConfigToLabelTypeMappingList != null) {
            for (final Entity e : _dataConfigToLabelTypeMappingList) {
                if (e != null) {
                    sb.append(l).append(
                            xbRDS(e, "dataConfigToLabelTypeMappingList"));
                }
            }
        }
        if (_dataConfigToRoleTypeMappingList != null) {
            for (final Entity e : _dataConfigToRoleTypeMappingList) {
                if (e != null) {
                    sb.append(l).append(
                            xbRDS(e, "dataConfigToRoleTypeMappingList"));
                }
            }
        }
        return sb.toString();
    }

    protected String xbRDS(final Entity e, final String name) { // buildRelationDisplayString()
        return e.buildDisplayString(name, true, true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String buildDisplayString(final String name, final boolean column,
            final boolean relation) {
        final StringBuilder sb = new StringBuilder();
        if (name != null) {
            sb.append(name).append(column || relation ? ":" : "");
        }
        if (column) {
            sb.append(buildColumnString());
        }
        if (relation) {
            sb.append(buildRelationString());
        }
        sb.append("@").append(Integer.toHexString(hashCode()));
        return sb.toString();
    }

    protected String buildColumnString() {
        final StringBuilder sb = new StringBuilder();
        final String delimiter = ", ";
        sb.append(delimiter).append(getId());
        sb.append(delimiter).append(getName());
        sb.append(delimiter).append(getHandlerName());
        sb.append(delimiter).append(getHandlerParameter());
        sb.append(delimiter).append(getHandlerScript());
        sb.append(delimiter).append(getBoost());
        sb.append(delimiter).append(getAvailable());
        sb.append(delimiter).append(getSortOrder());
        sb.append(delimiter).append(getCreatedBy());
        sb.append(delimiter).append(getCreatedTime());
        sb.append(delimiter).append(getUpdatedBy());
        sb.append(delimiter).append(getUpdatedTime());
        sb.append(delimiter).append(getDeletedBy());
        sb.append(delimiter).append(getDeletedTime());
        sb.append(delimiter).append(getVersionNo());
        if (sb.length() > delimiter.length()) {
            sb.delete(0, delimiter.length());
        }
        sb.insert(0, "{").append("}");
        return sb.toString();
    }

    protected String buildRelationString() {
        final StringBuilder sb = new StringBuilder();
        final String c = ",";
        if (_dataConfigToLabelTypeMappingList != null
                && !_dataConfigToLabelTypeMappingList.isEmpty()) {
            sb.append(c).append("dataConfigToLabelTypeMappingList");
        }
        if (_dataConfigToRoleTypeMappingList != null
                && !_dataConfigToRoleTypeMappingList.isEmpty()) {
            sb.append(c).append("dataConfigToRoleTypeMappingList");
        }
        if (sb.length() > c.length()) {
            sb.delete(0, c.length()).insert(0, "(").append(")");
        }
        return sb.toString();
    }

    /**
     * Clone entity instance using super.clone(). (shallow copy) 
     * @return The cloned instance of this entity. (NotNull)
     */
    @Override
    public DataCrawlingConfig clone() {
        try {
            return (DataCrawlingConfig) super.clone();
        } catch (final CloneNotSupportedException e) {
            throw new IllegalStateException("Failed to clone the entity: "
                    + toString(), e);
        }
    }

    // ===================================================================================
    //                                                                            Accessor
    //                                                                            ========
    /**
     * [get] ID: {PK, ID, NotNull, BIGINT(19)} <br />
     * @return The value of the column 'ID'. (basically NotNull if selected: for the constraint)
     */
    public Long getId() {
        return _id;
    }

    /**
     * [set] ID: {PK, ID, NotNull, BIGINT(19)} <br />
     * @param id The value of the column 'ID'. (basically NotNull if update: for the constraint)
     */
    public void setId(final Long id) {
        __modifiedProperties.addPropertyName("id");
        _id = id;
    }

    /**
     * [get] NAME: {NotNull, VARCHAR(200)} <br />
     * @return The value of the column 'NAME'. (basically NotNull if selected: for the constraint)
     */
    public String getName() {
        return _name;
    }

    /**
     * [set] NAME: {NotNull, VARCHAR(200)} <br />
     * @param name The value of the column 'NAME'. (basically NotNull if update: for the constraint)
     */
    public void setName(final String name) {
        __modifiedProperties.addPropertyName("name");
        _name = name;
    }

    /**
     * [get] HANDLER_NAME: {NotNull, VARCHAR(200)} <br />
     * @return The value of the column 'HANDLER_NAME'. (basically NotNull if selected: for the constraint)
     */
    public String getHandlerName() {
        return _handlerName;
    }

    /**
     * [set] HANDLER_NAME: {NotNull, VARCHAR(200)} <br />
     * @param handlerName The value of the column 'HANDLER_NAME'. (basically NotNull if update: for the constraint)
     */
    public void setHandlerName(final String handlerName) {
        __modifiedProperties.addPropertyName("handlerName");
        _handlerName = handlerName;
    }

    /**
     * [get] HANDLER_PARAMETER: {VARCHAR(4000)} <br />
     * @return The value of the column 'HANDLER_PARAMETER'. (NullAllowed even if selected: for no constraint)
     */
    public String getHandlerParameter() {
        return _handlerParameter;
    }

    /**
     * [set] HANDLER_PARAMETER: {VARCHAR(4000)} <br />
     * @param handlerParameter The value of the column 'HANDLER_PARAMETER'. (NullAllowed: null update allowed for no constraint)
     */
    public void setHandlerParameter(final String handlerParameter) {
        __modifiedProperties.addPropertyName("handlerParameter");
        _handlerParameter = handlerParameter;
    }

    /**
     * [get] HANDLER_SCRIPT: {VARCHAR(4000)} <br />
     * @return The value of the column 'HANDLER_SCRIPT'. (NullAllowed even if selected: for no constraint)
     */
    public String getHandlerScript() {
        return _handlerScript;
    }

    /**
     * [set] HANDLER_SCRIPT: {VARCHAR(4000)} <br />
     * @param handlerScript The value of the column 'HANDLER_SCRIPT'. (NullAllowed: null update allowed for no constraint)
     */
    public void setHandlerScript(final String handlerScript) {
        __modifiedProperties.addPropertyName("handlerScript");
        _handlerScript = handlerScript;
    }

    /**
     * [get] BOOST: {NotNull, DOUBLE(17)} <br />
     * @return The value of the column 'BOOST'. (basically NotNull if selected: for the constraint)
     */
    public java.math.BigDecimal getBoost() {
        return _boost;
    }

    /**
     * [set] BOOST: {NotNull, DOUBLE(17)} <br />
     * @param boost The value of the column 'BOOST'. (basically NotNull if update: for the constraint)
     */
    public void setBoost(final java.math.BigDecimal boost) {
        __modifiedProperties.addPropertyName("boost");
        _boost = boost;
    }

    /**
     * [get] AVAILABLE: {NotNull, VARCHAR(1)} <br />
     * @return The value of the column 'AVAILABLE'. (basically NotNull if selected: for the constraint)
     */
    public String getAvailable() {
        return _available;
    }

    /**
     * [set] AVAILABLE: {NotNull, VARCHAR(1)} <br />
     * @param available The value of the column 'AVAILABLE'. (basically NotNull if update: for the constraint)
     */
    public void setAvailable(final String available) {
        __modifiedProperties.addPropertyName("available");
        _available = available;
    }

    /**
     * [get] SORT_ORDER: {NotNull, INTEGER(10)} <br />
     * @return The value of the column 'SORT_ORDER'. (basically NotNull if selected: for the constraint)
     */
    public Integer getSortOrder() {
        return _sortOrder;
    }

    /**
     * [set] SORT_ORDER: {NotNull, INTEGER(10)} <br />
     * @param sortOrder The value of the column 'SORT_ORDER'. (basically NotNull if update: for the constraint)
     */
    public void setSortOrder(final Integer sortOrder) {
        __modifiedProperties.addPropertyName("sortOrder");
        _sortOrder = sortOrder;
    }

    /**
     * [get] CREATED_BY: {NotNull, VARCHAR(255)} <br />
     * @return The value of the column 'CREATED_BY'. (basically NotNull if selected: for the constraint)
     */
    public String getCreatedBy() {
        return _createdBy;
    }

    /**
     * [set] CREATED_BY: {NotNull, VARCHAR(255)} <br />
     * @param createdBy The value of the column 'CREATED_BY'. (basically NotNull if update: for the constraint)
     */
    public void setCreatedBy(final String createdBy) {
        __modifiedProperties.addPropertyName("createdBy");
        _createdBy = createdBy;
    }

    /**
     * [get] CREATED_TIME: {NotNull, TIMESTAMP(23, 10)} <br />
     * @return The value of the column 'CREATED_TIME'. (basically NotNull if selected: for the constraint)
     */
    public java.sql.Timestamp getCreatedTime() {
        return _createdTime;
    }

    /**
     * [set] CREATED_TIME: {NotNull, TIMESTAMP(23, 10)} <br />
     * @param createdTime The value of the column 'CREATED_TIME'. (basically NotNull if update: for the constraint)
     */
    public void setCreatedTime(final java.sql.Timestamp createdTime) {
        __modifiedProperties.addPropertyName("createdTime");
        _createdTime = createdTime;
    }

    /**
     * [get] UPDATED_BY: {VARCHAR(255)} <br />
     * @return The value of the column 'UPDATED_BY'. (NullAllowed even if selected: for no constraint)
     */
    public String getUpdatedBy() {
        return _updatedBy;
    }

    /**
     * [set] UPDATED_BY: {VARCHAR(255)} <br />
     * @param updatedBy The value of the column 'UPDATED_BY'. (NullAllowed: null update allowed for no constraint)
     */
    public void setUpdatedBy(final String updatedBy) {
        __modifiedProperties.addPropertyName("updatedBy");
        _updatedBy = updatedBy;
    }

    /**
     * [get] UPDATED_TIME: {TIMESTAMP(23, 10)} <br />
     * @return The value of the column 'UPDATED_TIME'. (NullAllowed even if selected: for no constraint)
     */
    public java.sql.Timestamp getUpdatedTime() {
        return _updatedTime;
    }

    /**
     * [set] UPDATED_TIME: {TIMESTAMP(23, 10)} <br />
     * @param updatedTime The value of the column 'UPDATED_TIME'. (NullAllowed: null update allowed for no constraint)
     */
    public void setUpdatedTime(final java.sql.Timestamp updatedTime) {
        __modifiedProperties.addPropertyName("updatedTime");
        _updatedTime = updatedTime;
    }

    /**
     * [get] DELETED_BY: {VARCHAR(255)} <br />
     * @return The value of the column 'DELETED_BY'. (NullAllowed even if selected: for no constraint)
     */
    public String getDeletedBy() {
        return _deletedBy;
    }

    /**
     * [set] DELETED_BY: {VARCHAR(255)} <br />
     * @param deletedBy The value of the column 'DELETED_BY'. (NullAllowed: null update allowed for no constraint)
     */
    public void setDeletedBy(final String deletedBy) {
        __modifiedProperties.addPropertyName("deletedBy");
        _deletedBy = deletedBy;
    }

    /**
     * [get] DELETED_TIME: {TIMESTAMP(23, 10)} <br />
     * @return The value of the column 'DELETED_TIME'. (NullAllowed even if selected: for no constraint)
     */
    public java.sql.Timestamp getDeletedTime() {
        return _deletedTime;
    }

    /**
     * [set] DELETED_TIME: {TIMESTAMP(23, 10)} <br />
     * @param deletedTime The value of the column 'DELETED_TIME'. (NullAllowed: null update allowed for no constraint)
     */
    public void setDeletedTime(final java.sql.Timestamp deletedTime) {
        __modifiedProperties.addPropertyName("deletedTime");
        _deletedTime = deletedTime;
    }

    /**
     * [get] VERSION_NO: {NotNull, INTEGER(10)} <br />
     * @return The value of the column 'VERSION_NO'. (basically NotNull if selected: for the constraint)
     */
    public Integer getVersionNo() {
        return _versionNo;
    }

    /**
     * [set] VERSION_NO: {NotNull, INTEGER(10)} <br />
     * @param versionNo The value of the column 'VERSION_NO'. (basically NotNull if update: for the constraint)
     */
    public void setVersionNo(final Integer versionNo) {
        __modifiedProperties.addPropertyName("versionNo");
        _versionNo = versionNo;
    }
}
