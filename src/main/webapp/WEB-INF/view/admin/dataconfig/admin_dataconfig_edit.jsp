<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%><!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title><la:message key="labels.admin_brand_title" /> | <la:message
		key="labels.data_crawling_configuration" /></title>
<jsp:include page="/WEB-INF/view/common/admin/head.jsp"></jsp:include>
</head>
<body class="skin-blue sidebar-mini">
	<div class="wrapper">
		<jsp:include page="/WEB-INF/view/common/admin/header.jsp"></jsp:include>
		<jsp:include page="/WEB-INF/view/common/admin/sidebar.jsp">
			<jsp:param name="menuCategoryType" value="crawl" />
			<jsp:param name="menuType" value="dataConfig" />
		</jsp:include>
		<div class="content-wrapper">
			<section class="content-header">
				<h1>
					<la:message key="labels.data_crawling_title_details" />
				</h1>
				<jsp:include page="/WEB-INF/view/common/admin/crud/breadcrumb.jsp"></jsp:include>
			</section>
			<section class="content">
				<la:form action="/admin/dataconfig/" styleClass="form-horizontal">
					<la:hidden property="crudMode" />
					<c:if test="${crudMode==2}">
						<la:hidden property="id" />
						<la:hidden property="versionNo" />
					</c:if>
					<la:hidden property="createdBy" />
					<la:hidden property="createdTime" />
					<la:hidden property="sortOrder" />
					<div class="row">
						<div class="col-md-12">
							<div
								class="box <c:if test="${crudMode == 1}">box-success</c:if><c:if test="${crudMode == 2}">box-warning</c:if>">
								<div class="box-header with-border">
									<jsp:include page="/WEB-INF/view/common/admin/crud/header.jsp"></jsp:include>
								</div>
								<!-- /.box-header -->
								<div class="box-body">
									<div>
										<la:info id="msg" message="true">
											<div class="alert alert-info">${msg}</div>
										</la:info>
										<la:errors property="_global" />
									</div>
									<div class="form-group">
										<label for="name" class="col-sm-3 control-label"><la:message
												key="labels.name" /></label>
										<div class="col-sm-9">
											<la:errors property="name" />
											<la:text property="name" styleClass="form-control" />
										</div>
									</div>
									<div class="form-group">
										<label for="handlerName" class="col-sm-3 control-label"><la:message
												key="labels.handler_name" /></label>
										<div class="col-sm-9">
											<la:errors property="handlerName" />
											<la:select property="handlerName" size="1"
												styleClass="form-control">
												<c:forEach var="hn" varStatus="s"
													items="${handlerNameItems}">
													<la:option value="${f:u(hn.value)}">${f:h(hn.label)}</la:option>
												</c:forEach>
											</la:select>
										</div>
									</div>
									<div class="form-group">
										<label for="handlerParameter" class="col-sm-3 control-label"><la:message
												key="labels.handler_parameter" /></label>
										<div class="col-sm-9">
											<la:errors property="handlerParameter" />
											<la:textarea property="handlerParameter"
												styleClass="form-control" rows="5" />
										</div>
									</div>
									<div class="form-group">
										<label for="handlerScript" class="col-sm-3 control-label"><la:message
												key="labels.handler_script" /></label>
										<div class="col-sm-9">
											<la:errors property="handlerScript" />
											<la:textarea property="handlerScript"
												styleClass="form-control" rows="5" />
										</div>
									</div>
									<div class="form-group">
										<label for="boost" class="col-sm-3 control-label"><la:message
												key="labels.boost" /></label>
										<div class="col-sm-9">
											<la:errors property="boost" />
											<la:text property="boost" styleClass="form-control" />
										</div>
									</div>
									<div class="form-group">
										<label for="permissions" class="col-sm-3 control-label"><la:message
												key="labels.permission_type" /></label>
										<div class="col-sm-9">
											<la:errors property="permissions" />
											<la:textarea property="permissions" styleClass="form-control"
												rows="5" />
										</div>
									</div>
									<div class="form-group">
										<label for="labelTypeIds" class="col-sm-3 control-label"><la:message
												key="labels.label_type" /></label>
										<div class="col-sm-9">
											<la:errors property="labelTypeIds" />
											<la:select property="labelTypeIds" multiple="true"
												styleClass="form-control">
												<c:forEach var="l" varStatus="s" items="${labelTypeItems}">
													<la:option value="${f:u(l.id)}">${f:h(l.name)}</la:option>
												</c:forEach>
											</la:select>
										</div>
									</div>
									<div class="form-group">
										<label for="available" class="col-sm-3 control-label"><la:message
												key="labels.available" /></label>
										<div class="col-sm-9">
											<la:errors property="available" />
											<la:select property="available" styleClass="form-control">
												<la:option value="true">
													<la:message key="labels.enabled" />
												</la:option>
												<la:option value="false">
													<la:message key="labels.disabled" />
												</la:option>
											</la:select>
										</div>
									</div>
								</div>
								<!-- /.box-body -->
								<div class="box-footer">
									<jsp:include page="/WEB-INF/view/common/admin/crud/buttons.jsp"></jsp:include>
								</div>
								<!-- /.box-footer -->
							</div>
							<!-- /.box -->
						</div>
					</div>
				</la:form>
			</section>
		</div>
		<jsp:include page="/WEB-INF/view/common/admin/footer.jsp"></jsp:include>
	</div>
	<jsp:include page="/WEB-INF/view/common/admin/foot.jsp"></jsp:include>
</body>
</html>
