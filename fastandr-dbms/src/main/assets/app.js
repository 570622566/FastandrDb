var dbname = '';
var columndata = [];
var rowData;
$(document).ready(function() {
	getDBList()
	$(document).on("click", ".list-group-item", function() {
		$(this).addClass('active').siblings().removeClass('active');
	});
	$(document).on("click", "#query_bt", function() {
		querydb();
	});
	$(function() {
		$("[data-toggle='tooltip']").tooltip();
	});

	toastr.options = {
		"closeButton": false,
		"debug": false,
		"newestOnTop": false,
		"progressBar": false,
		"positionClass": "toast-top-center",
		"preventDuplicates": false,
		"onclick": null,
		"showDuration": "300",
		"hideDuration": "1000",
		"timeOut": "2000",
		"extendedTimeOut": "1000",
		"showEasing": "swing",
		"hideEasing": "linear",
		"showMethod": "fadeIn",
		"hideMethod": "fadeOut"
	}

});

/**
 * 获取数据库列表
 */
function getDBList() {
	$.ajax({
		url: "/getDbList",
		success: function(result) {
			result = JSON.parse(result);
			var dbList = result.rows;
			$('#db-list').empty();
			var appendls = '';
			for(var count = 0; count < dbList.length; count++) {
				appendls += "<a class='list-group-item' onClick=\"getTables('" + dbList[count] + "');\">" + dbList[count] + "</a>";
			}
			$("#db-list").append(appendls);
		}
	});
}

/**
 * 根据数据库 获取table列表
 * @param {Object} dbname
 */
function getTables(dbname) {
	this.dbname = dbname;
	$("#selected-db-info").text("当前选择的数据库是" + dbname);
	$("#selected-db-info").removeClass("disabled");
	$.ajax({
		url: "/getTables?dbname=" + dbname,
		success: function(result) {
			result = JSON.parse(result);
			var tableList = result.rows;
			$('#table-list').empty();
			var appendtablels = '';
			for(var count = 0; count < tableList.length; count++) {
				appendtablels += "<a class='list-group-item' onClick=\"getTablesDatas('" + tableList[count] + "');\">" + tableList[count] + "</a>";
			}
			$("#table-list").append(appendtablels)
		}
	});
}

/**
 * 根据数据库和table获取表数据
 * @param {Object} tablename
 */
function getTablesDatas(tablename) {
	$.ajax({
		url: "/getTableDatas?dbname=" + dbname + "&tableName=" + tablename,
		success: function(result) {
			result = JSON.parse(result);
			console.log(result);
			var tableList = result.allTablefield;
			columndata = [];
			for(var count = 0; count < tableList.length; count++) {
				var obj = {};
				obj.field = tableList[count].title;
				obj.title = tableList[count].title;
				obj.pk = tableList[count].isPrimary;
				columndata.push(obj);
			}
			$('#toolbar').removeClass("hidden");
			$('#table_datas').bootstrapTable('destroy').bootstrapTable({ //'destroy' 是必须要加的==作用是加载服务器//  //数据，初始化表格的内容Destroy the bootstrap table.
				data: result.datas, //datalist 即为需要的数据
				dataType: 'json',
				toolbar: '#toolbar',
				data_locale: "zh-US", //转换中文 但是没有什么用处
				uniqueId: "id",
				pagination: true,
				pageList: [],
				pageNumber: 1,
				height: 580,
				search: true,
				pageSize: 10, //每页显示的数量
				paginationPreText: "上一页",
				paginationNextText: "下一页",
				paginationLoop: false,
				//这里也可以将TABLE样式中的<tr>标签里的内容挪到这里面：
				columns: columndata
			});
		}
	});

	$("#table_datas").on('click-row.bs.table', function(e, row, element) {
		$('.success').removeClass('success'); //去除之前选中的行的，选中样式
		$(element).addClass('success'); //添加当前选中的 success样式用于区别
		rowData = row;
		console.log(rowData)
	});

}


/**
 * 创造添加数据的表单
 */
function addDataform() {
	console.log(columndata);
	var body = '';
	$("#add-body").empty();
		var pk='';
	for(var i = 0; i < columndata.length; i++) {

	if(columndata[i].field.toLowerCase() =="id"||columndata[i].field.toLowerCase()=="_id"){
	pk ="disabled";
	}else{
		pk ="";
	}
		body += "<div class=\"form-group\">" + " <label class=\" control-label\">" + columndata[i].field + "</label><div ><input type=\"text\" class=\"form-control\" " +
			"name='" + columndata[i].field + "' "+pk +">  </div></div>";
	}
	$("#add-body").append(body);
	$("#add_modal").modal('show');

}

/**
 * 添加数据
 */
function addData() {
		var addinput = $("#add-body").find("input");
    	var addData = "";
    	for(var i = 0; i < addinput.length; i++) {

    		if($(addinput[i]).val() == ""&&!(columndata[i].field.toLowerCase() =="id"||columndata[i].field.toLowerCase()=="_id")) {
    			toastr.error("添加的数据不能为空");
    			return;
    		}
    		console.log($(addinput[i]).val());
    		addData += addinput[i].name + "=" + $(addinput[i]).val() + "&";
    }
    addData += "fdbname=" + dbname + "&ftablename=" +$("#table-list .active").text();
	$.ajax({
		url: "/addData",
		data: addData,
		success: function(result) {
		console.log(result);
			if(result == "success") {
				$("#add_modal").modal('hide');
				getTablesDatas($("#table-list .active").text());
				toastr.success("添加数据成功");
			} else {
				alert("添加数据失败")
			}
		}
	});

}

/**
 * 创建修改数据表单
 */
function editDataform() {

	if(!$("table tr").hasClass("success")) {
		toastr.error("请选择表数据");
		return;
	}
	var body = '';
	$("#edit_body").empty();
	var pk='';
	for(var i = 0; i < columndata.length; i++) {

	if(columndata[i].field.toLowerCase() =="id"||columndata[i].field.toLowerCase()=="_id"){
	pk ="disabled";
	}else{
		pk ="";
	}
	console.log("pk="+pk)
		body += "<div class=\"form-group\">" + " <label class=\" control-label\">" + columndata[i].field + "</label><div ><input type='text' class='form-control' " +
		"name='" + columndata[i].field + "' value='" + rowData[columndata[i].field] + "' "+pk +">  </div></div>";
	}
	$("#edit_body").append(body);
	$("#edit_modal").modal('show');

}

/**
 * 修改数据
 */
function editData() {

		var editinput = $("#edit_body").find("input");
    	var editData = "";
    	for(var i = 0; i < editinput.length; i++) {
    		if($(editinput[i]).val() == "") {
    			toastr.error("添加的数据不能为空");
    			return;
    		}
    		console.log($(editinput[i]).val());
    		editData += editinput[i].name + "=" + $(editinput[i]).val() + "&";
    }
    editData += "fdbname=" + dbname + "&ftablename=" +$("#table-list .active").text();
	$.ajax({
		url: "/editData",
		data: editData,
		success: function(result) {
			if(result == "success") {
				$("#edit_modal").modal('hide');
				getTablesDatas($("#table-list .active").text());
				toastr.success("修改数据成功");
			} else {
				alert("修改数据失败")
			}
		}
	});
}

/**
 * 创建删除数据表单
 */
function delDataFrom() {
	if(!$("table tr").hasClass("success")) {
		toastr.error("请选择表数据");
		return;
	}
	$("#del_modal").modal('show');
}

/**
 * 删除数据
 */
function delData() {

var delDatastr = columndata[0].field + "=" + rowData[columndata[0].field] +"&fdbname=" + dbname + "&ftablename=" + $("#table-list .active").text();

	$.ajax({
		url: "/delData",
		data: delDatastr,
		success: function(result) {
			if(result == "success") {
				$("#del_modal").modal('hide');
				getTablesDatas($("#table-list .active").text());
				toastr.success("删除数据成功");
			} else {
				alert("修改数据失败")
			}
		}
	});
}

/**
 * 下载数据库
 */
function downloaddb() {
	if(!$("#db-list .list-group-item").hasClass("active")) {
		toastr.error("请选择数据库");
		return;
	}
	if($("#db-list .active").text()=="SHAREDPREFS_XML"){
    		toastr.error("不支持sharedprefs下载");
    		return;
    	}
	const a = document.createElement('a');
	a.setAttribute('href', "/downloaddb?dbname="+dbname);
	a.setAttribute('download', dbname);
	a.click();
}

function querydb() {
toastr.error("暂不支持自定义查询操作");
//	if(!$("#db-list .list-group-item").hasClass("active")) {
//		toastr.error("请选择数据库");
//		return;
//	}
//	$.ajax({
//		url: "/queryDb?dbname=" + dbname,
//		success: function(result) {
//			if(result == "success") {
//				getTablesDatas($("#table-list .active").text());
//			} else {
//				alert("查询失败")
//			}
//		}
//	});
}